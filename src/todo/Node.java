package todo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Store hirarchial data in sqlite
 * <p>
 * The Node class implements a hirarchic datastore. A node may have always one parent and zero to many children. 
 * Every node has a parent. There is a special node with id == 0 which is the root node. It is a special node 
 * because it has no representation in the database. You can change the Roots name with 
 * {@link #setRootNodeName(String)}.
 * <p>
 * Example usage. Create a node:
 * {@code
 * Node n = new Node(); 
 * }
 * This will create a new node. It will be empty and have an id that is -1. An id with -1 means the node 
 * has not been committed, there is no representation of it in the database.
 * <p>
 * Create a node, add a child and commit it
 * {@code
 * Node n = new Node();
 * n.setName("my name");
 * n.setPartent(0); // make the root node the parent
 * n.store();
 * Node n1 = new Node();
 * n1.setName("I am a child");
 * n1.setParent(n); // this will fail if n has not been stored. because n will have an -1 id.
 * n1.commit();
 * }
 * <p>
 * Fetch an existing object, get it's parent and children:
 * {@code
 * Node n = new Node(2);
 * Node n1 = new Node(n.getParentId()); // fetch the parent Node of the object
 * if (n1.hasChildren()) &#123;
 *     ArrayList<Node> nl = n1.getChildren(); // fetch a list of child Nodes
 * &#125;
 * }
 * 
 * @author Simon Wunderlin
 */
public class Node extends Database {
	
	// item properties
	/**
	 * The primary key of an item. -1 is uncommitted. -2 means error while storing. 0 is the root node
	 * and every other positive integer is a node loaded from the database.  
	 */
	private int id;
	
	/**
	 * The name property.
	 */
	private String name;
	
	/**
	 * A reference to the parent node. This points to the parents {@link #id}
	 */
	private int parent;
	
	/**
	 * number of children.
	 * <p> 
	 * The object is lazy loaded. Children are not available during load but the numChildren is
	 * populated when a node is fetched.
	 * 
	 * @see #hasChildren() 
	 * @see #children
	 * @see #getChildren()
	 */
	private int numChildren;
	
	/**
	 * List containing all child nodes
	 * <p>
	 * This list is not loaded when the node is created. you have to specifically call 
	 * {@link #getChildren()}. {@link #childrenLoaded} will keep the state if the children have been loaded. 
	 * <p>
	 * Be aware that after loading the children from the database, this list is cached in memory. 
	 * To explicitly reload the list again, you must call getChildren(false) to circumvent the local cache.
	 * 
	 * @see #getChildren()
	 * @see #getChildren(boolean)
	 * @see #hasChildren()
	 * @see #childrenLoaded
	 */
	private ArrayList<Node> children;
	
	/**
	 * state of the children
	 * <p>
	 * children are loaded lazily. This is set to true once {@link #getChildren()} has been called once.
	 * 
	 * @see #getChildren()
	 * @see #getChildren(boolean)
	 * @see #children
	 * @see #numChildren
	 */
	private boolean childrenLoaded = false;
	
	/**
	 * The name of the root node
	 * <p>
	 * This is the name of a special node called root with id 0. it is not represented in the database. This variable 
	 * makes it possible to give it a different name than the default which is "Root".
	 * 
	 * @see #setRootNodeName(String)
	 */
	private String rootNodeName = "Root";
	
	/**
	 * Set the display name of the Root node
	 * 
	 * @param name
	 */
	public void setRootNodeName(String name) {
		this.rootNodeName = name;
	}
	
	/**
	 * create a node object
	 * <p>
	 * default to root node. id -1 means the db object does not yet exist. after the first commit, the object's
	 * id will be updated with the generated database id.
	 * 
	 * @see #store()
	 * @see #insert()
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
	 * fetch a node object by id
	 * <p>
	 * The root nodes id is always 0. If the node can not be found in the database, the returning object will have 
	 * and id of -2.
	 * 
	 * @throws Exception 
	 */
	public Node(int id) {
		this.id = 0;
		this.name = rootNodeName;
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
	
	/**
	 * Update a node
	 * <p>
	 * This is a convince function which will create and store the new node.
	 *  
	 * @param id
	 * @param name
	 * @param parent
	 */
	private void setNode(int id, String name, int parent) {
		this.id = id;
		this.name = name;
		this.parent = parent;
		//System.out.println(this);
		try {
			store();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if a Node has children
	 * 
	 * @return
	 */
	public boolean hasChildren() {
		if (numChildren > 0)
			return true;
		return false;
	}
	
	/**
	 * Check if the node is still valid
	 * <p>
	 * if something went wrong, the node's id will be changed to -2. This method checks if the node is still 
	 * in sync with the database.
	 * 
	 * @return
	 */
	public boolean isValid() {
		if (this.id == -2)
			return false;
		return true;
	}
	
	/**
	 * load metadata for an object from the database
	 * 
	 * @see todo.Database#load()
	 */
	@Override
	public void load() throws Exception {
		super.load();
		
		try {
			this.name        = rs.getString("name");
			this.parent      = rs.getInt("parent");
			this.numChildren = rs.getInt("numChildren");
		} catch (Exception e) {
			throw new Exception("Node "+String.valueOf(this.id)+" no found");
		}
		
		this.dirty = false;
	}
	
	/**
	 * Commit the current state to database
	 * <p>
	 * This function stores the current object. It will decide if the object has to be created 
	 * or if it will be updated. 
	 * <p>
	 * if the database is not dirty, then the operation will not store anything to the database
	 * 
	 * @see todo.Database#dirty
	 * @see todo.Database#store()
	 */
	@Override
	public void store() throws SQLException {
		super.store();
		
		if (!dirty)
			return;
		
		if (id == -1) {
			insert();
		} else {
			update();
		}
		
	}

	/**
	 * store a new object to the database
	 * <p>
	 * the {@link #id} is always auto generated, no matter what it has been set to. New objects are supposed to 
	 * have an id == -1 before committing to the database. 
	 * <p>
	 * after an successful commit, the newly generated id will be fetched and stored in the objects {@link #id}
	 * property.
	 * <p>
	 * After a successful commit the {@link todo.Database#dirty} flag will be set to false.
	 * 
	 * @see todo.Database#insert()
	 */
	@Override
	public void insert() throws SQLException {
		super.insert();
		
		ResultSet res;
		res = stmt.executeQuery("SELECT MAX(ID) as id FROM node;");
		this.id = res.getInt("id");
		
		this.dirty = false;
	}

	@Override
	public void update() throws SQLException {
		super.update();
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
				try {
					child.setParent(p);
				} catch (Exception e) {
					e.printStackTrace();
				}
				child.store();
			}
		}
		
		super.delete();
		this.id = -1;
		this.dirty = true;
	}

	/**
	 * create a prepared statement for fetching a record 
	 * 
	 * @see todo.Database#loadStmt()
	 */
	@Override
	public PreparedStatement loadStmt() {
		PreparedStatement loadStmt = null;
		String sql = "SELECT id, name, parent, (SELECT count(id) FROM node WHERE parent=?) as numChildren "
	               + "FROM node WHERE id=?;";
		try {
			loadStmt = conn.prepareStatement(sql);
			loadStmt.setInt(1, this.id);
			loadStmt.setInt(2, this.id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return loadStmt;
	}
	
	/**
	 * create a prepared statement for updating a record
	 * 
	 * @see todo.Database#updateStmt()
	 */
	@Override
	public PreparedStatement updateStmt() {
		PreparedStatement updateStmt = null;
		try {
			updateStmt = conn.prepareStatement("UPDATE node SET name=?, parent=? WHERE id=?;");
			updateStmt.setString(1, this.name);
			updateStmt.setInt(2, this.parent);
			updateStmt.setInt(3, this.id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return updateStmt;
	}
	
	/**
	 * create a prepared statement for inserting a record
	 * 
	 * @see todo.Database#insertStmt()
	 */
	@Override
	public PreparedStatement insertStmt() {
		PreparedStatement insertStmt = null;
		try {
			insertStmt = conn.prepareStatement("INSERT INTO node (name, parent) VALUES (?, ?);");
			insertStmt.setString(1, this.name);
			insertStmt.setInt(2, this.parent);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return insertStmt;
	}
	
	/**
	 * create a prepared statement for deleting a record
	 * 
	 * @see todo.Database#deleteStmt()
	 */
	@Override
	public PreparedStatement deleteStmt() {
		PreparedStatement deleteStmt = null;
		try {
			deleteStmt = conn.prepareStatement("DELETE FROM node WHERE id=?;");
			deleteStmt.setInt(1, this.id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return deleteStmt;
	}
	
	/**
	 * create a prepared statement for creating the table
	 * 
	 * @see todo.Database#createStmt()
	 */
	@Override
	public PreparedStatement createStmt() {
		PreparedStatement createStmt = null;
		String sql = "CREATE TABLE IF NOT EXISTS node (id INTEGER PRIMARY KEY, name VARCHAR(255) NOT NULL, "
				   + "parent INTEGER DEFAULT 0);";
		try {
			createStmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return createStmt;
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
	
	/**
	 * Set the parent by Id
	 * <p>
	 * This operation will fail if the parent's id is -1 or -2
	 * 
	 * @param parent
	 * @throws Exception
	 */
	public void setParent(int parent) throws Exception {
		if (parent == -1) {
			throw new Exception("You are trying to set a parent that has not been committed (parent id -1).");
		}
		
		if (parent == -2) {
			throw new Exception("You are trying to set a parent that is Invalid (parent id -2).");
		}
		
		if (parent != this.parent)
			dirty = true;
		this.parent = parent;
	}
	
	/**
	 * Set the parent by Node
	 * <p>
	 * This operation will fail if the parent's id is -1 or -2
	 * 
	 * @param parent
	 * @throws Exception
	 */
	public void setParent(Node parent) throws Exception {
		if (parent.getId() == -1) {
			throw new Exception("You are trying to set a parent that has not been committed (parent id -1).");
		}
		
		if (parent.getId() == -2) {
			throw new Exception("You are trying to set a parent that is Invalid (parent id -2).");
		}
		
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
	
	/**
	 * update children
	 * 
	 * @param children
	 */
	public void setChildren(ArrayList<Node> children) {
		// FIXME: this should update all children's parents
		// FIXME: new objects added to this list need to be committed
		this.children = children;
	}

	@Override
	public String toString() {
		return String.format("Node [{%s} '%s', parent=%s, children=%s, %s]", id, name, parent, numChildren, dirty);
	}

}
