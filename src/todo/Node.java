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
		parent = 0;
		numChildren = 0;
		children = new ArrayList<>();
	}
	
	/**
	 * fetch a node object
	 * <p>
	 * default to root node. The root nodes id is always 0.
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
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void load() throws SQLException {
		super.load();
		String[] loadSql = loadSql();
		PreparedStatement loadStmt;
		ResultSet rs = null;
		//System.out.println(id);

		loadStmt = conn.prepareStatement(loadSql[0]);
		loadStmt.setInt(1, this.id);
		loadStmt.setInt(2, this.id);
		rs  = loadStmt.executeQuery();
		
		this.name        = rs.getString("name");
		this.parent      = rs.getInt("parent");
		this.numChildren = rs.getInt("numChildren");
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
			System.out.println(sqls[0]);
			System.out.println(this);
			loadStmt.setString(1, this.name);
			loadStmt.setInt(2, this.parent);
			loadStmt.setInt(3, this.id);
			loadStmt.execute();
		}
		
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
		String[] sqls = {"CREATE TABLE IF NOT EXISTS node (id INTEGER PRIMARY KEY, name VARCHAR(255) NOT NULL, parent INTEGER DEFAULT 0);",
		                 "CREATE TABLE IF NOT EXISTS node2node (id INTEGER PRIMARY KEY, parent INTEGER NOT NULL, child INTEGER NOT NULL);"};
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
	
	public int getParent() {
		return parent;
	}
	
	public void setParent(int parent) {
		if (parent != this.parent)
			dirty = true;
		this.parent = parent;
	}
	
	public ArrayList<Node> getChildren() {
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
