package todo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Node extends Database {
	
	// item properties
	private int id;
	private String name;
	private int parent;
	private int numChildren;
	private ArrayList<Node> children;
	private boolean childrenLoaded = false;
	
	/**
	 * create a node object
	 * <p>
	 * default to root node. id -1 means the db object does not yet exist
	 */
	public Node() {
		id = -1;
		name = "";
		parent = -1;
		numChildren = 0;
		children = new ArrayList<>();
		dirty = true;
	}
	
	/**
	 * fetch a node object
	 * <p>
	 * The root nodes id is always 0.
	 * @throws Exception 
	 */
	public Node(int id) {
		this.id = 0;
		this.name = "Root";
		this.parent = -1;
		numChildren = 0;
		this.children = new ArrayList<>();
		
		if (id > 0) {
			this.id = id;
			try {
				load();
			} catch (Exception e) {
				//System.out.println("Object with id " +String.valueOf(id)+ " not found in the Database");
				this.id = -2;
				this.name = "";
			}
		}
		
		// load root node children count. We need to do this here, because it is never laded 
		// from the database
		if(this.id == 0) {
			ResultSet rs;
			try {
				rs = stmt.executeQuery("SELECT count(id) as numChildren FROM node WHERE parent=0;");
				this.numChildren = rs.getInt("numChildren");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setNode(int id, String name, int parent) {
		this.id = id;
		this.name = name;
		this.parent = parent;
		System.out.println(this);
		try {
			store();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isValis() {
		if (this.id == -2)
			return false;
		return true;
	}
	
	@Override
	public void load() throws Exception {
		super.load();
		String[] loadSql = loadSql();
		PreparedStatement loadStmt;
		ResultSet rs = null;
		//System.out.println(id);

		loadStmt = conn.prepareStatement(loadSql[0]);
		loadStmt.setInt(1, this.id);
		loadStmt.setInt(2, this.id);
		rs  = loadStmt.executeQuery();
		
		try {
			this.name        = rs.getString("name");
			this.parent      = rs.getInt("parent");
			this.numChildren = rs.getInt("numChildren");
		} catch (Exception e) {
			throw new Exception("Node "+String.valueOf(this.id)+" no found");
		}
		
		this.dirty = false;
	}
	
	@Override
	public void store() throws SQLException {
		super.store();
		
		if (!dirty)
			return;
		
		if (id == -1) { // insert
			String[] sqls = insertSql();
			ResultSet res;
			for (String sql: sqls) {
				stmt.execute(sql);
			}
			
			res = stmt.executeQuery("SELECT MAX(ID) as id FROM node;");
			this.id = res.getInt("id");
			this.dirty = false;
		} else { // update
			String[] sqls = updateSql();
			PreparedStatement loadStmt;
			loadStmt = conn.prepareStatement(sqls[0]);
			loadStmt.setString(1, this.name);
			loadStmt.setInt(2, this.parent);
			loadStmt.setInt(3, this.id);
			loadStmt.execute();
		}
		
	}
	
	/**
	 * Delete a node from the Database
	 * <p>
	 * This method is remove a node from the database. If the node still has children, then they will be moved 
	 * to the parent of this node. If you want the children to be deleted or assigned to another node, you must 
	 * make sure to move or delete them before deleting this node.
	 *  
	 * @throws SQLException
	 */
	@Override
	public void delete() throws SQLException {
		
		// make sure this object has not children, otherwise they will be orphaned
		// when we destroy it's parent. Move all children to the parent of the current object.
		// worst case this means the children will be reassigned to the root object.
		ArrayList<Node> c = getChildren(false);
		if (c.size() > 0) {
			int p = getParentId();
			for (Node child: c) {
				child.setParent(p);
				child.store();
			}
		}
		
		String[] sqls = deleteSql();
		PreparedStatement deleteStmt;
		for (String sql: sqls) {
			deleteStmt = conn.prepareStatement(sql);
			deleteStmt.setInt(1, this.id);
			deleteStmt.execute();
		}
		this.id = -1;
		this.dirty = true;
	}

	@Override
	public String[] deleteSql() {
		String[] sqls = {"DELETE FROM node WHERE id=?;"};
		return sqls;
	}

	@Override
	public String[] loadSql() {
		String[] sqls = {"SELECT id, name, parent, (SELECT count(id) FROM node WHERE parent=?) as numChildren "
		               + "FROM node WHERE id=?;"};
		return sqls;
	}

	@Override
	public String[] insertSql() {
		String[] sqls = {
			"INSERT INTO node (name, parent) VALUES ('"+name+"', "+String.valueOf(parent)+");"
		};
		return sqls;
	}
	
	@Override
	public String[] updateSql() {
		String[] sqls = {
			"UPDATE node SET name=?, parent=? WHERE id=?;"
		};
		return sqls;
	}
	
	@Override
	public String[] createSql() {
		String[] sqls = {"CREATE TABLE IF NOT EXISTS node (id INTEGER PRIMARY KEY, name VARCHAR(255) NOT NULL, parent INTEGER DEFAULT 0);"};
		                 //"CREATE TABLE IF NOT EXISTS node2node (id INTEGER PRIMARY KEY, parent INTEGER NOT NULL, child INTEGER NOT NULL);"};
		return sqls;
	}

	// getters and setters
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		if (name != this.name)
			dirty = true;
		this.name = name;
	}
	
	public int getParentId() {
		return this.parent;
	}
	
	public Node getParent() throws Exception {
		if (this.parent == -1) {
			throw new Exception("Root node has no parent.");
		}
		return new Node(this.parent);
	}
	
	public void setParent(int parent) {
		if (parent != this.parent)
			dirty = true;
		this.parent = parent;
	}
	
	public void setParent(Node parent) {
		if (parent.getId() != this.parent)
			dirty = true;
		this.parent = parent.getId();
	}
	
	/**
	 * Fetch all children of a node
	 * <p>
	 * This is a convenience function of {@link Node#getChildren(boolean)}. Use this if you want
	 * to work with cached objects.
	 * 
	 * @return
	 * @throws SQLException
	 * @see Node#getChildren(boolean)
	 */
	public ArrayList<Node> getChildren() throws SQLException {
		return getChildren(true);
	}
		
	/**
	 * Read child nodes from database
	 * <p>
	 * This method fetches all children as Node objects. The method uses a caching mechanism. After the first
	 * read is done, {@link #childrenLoaded} is set to true. This is to prevent a lot of database operations on 
	 * Nodes with many Children.
	 * <p>
	 * To force a reload from the database on every call, set the parameter `useCache` to true.
	 * 
	 * @param useCache if true, force reload from database. Objects are cached in this.children
	 * @return
	 * @throws SQLException
	 * @see #childrenLoaded
	 * @see #children
	 */
	public ArrayList<Node> getChildren(boolean useCache) throws SQLException {
		
		if (childrenLoaded && useCache)
			return children;
		
		String sql = "SELECT no.id, no.name, no.parent, (SELECT count(n.id) FROM node n WHERE parent=no.id) as numChildren "
		           + "FROM node no WHERE no.parent=?;";
		
		PreparedStatement loadStmt;
		loadStmt = conn.prepareStatement(sql);
		loadStmt.setInt(1, this.id);
		ResultSet rs = loadStmt.executeQuery();
		
		while (rs.next()) {
			Node n = new Node();
			n.id = rs.getInt("id");
			n.name = rs.getString("name");
			n.parent = rs.getInt("parent");
			n.numChildren = rs.getInt("numChildren");
			n.dirty = false;
			
			children.add(n);
		}
		
		numChildren = children.size();
		childrenLoaded = true;
		return children;
	}
	
	public void setChildren(ArrayList<Node> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return String.format("Node [{%s} '%s', parent=%s, children=%s, %s]", id, name, parent, numChildren, dirty);
	}

	
}
