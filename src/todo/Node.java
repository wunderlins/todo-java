package todo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Node extends Database {
	
	// item properties
	protected int id;
	protected String name;
	protected int parent;
	protected ArrayList<Integer> children;
	
	/**
	 * create a node object
	 * <p>
	 * default to root node. id -1 means the db object does not yet exist
	 */
	public Node() {
		id = -1;
		name = "";
		parent = 0;
		children = new ArrayList<>();
	}
	
	/**
	 * fetch a node object
	 * <p>
	 * default to root node. The root nodes id is always 0.
	 */
	public Node(int id) {
		id = 0;
		name = "Root";
		parent = -1;
		children = new ArrayList<>();
		
		if (id > 0) {
			this.id = id;
			load();
		}
	}
	
	@Override
	public void store() {
		String[] sqls = insertSql();
		for (String sql: sqls) {
			try {
				stmt.execute(sql);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public String[] loadSql() {
		String[] sqls = {"SELECT * FROM node WHERE id=?;"};
		return sqls;
	}

	@Override
	public String[] insertSql() {
		String[] sqls = {
			"INSERT INTO node (name) VALUES ('"+name+"')"
		};
		return sqls;
	}
	
	@Override
	public String[] updateSql() {
		String[] sqls = {};
		return sqls;
	}
	
	@Override
	public String[] createSql() {
		String[] sqls = {"CREATE TABLE IF NOT EXISTS node (id INTEGER PRIMARY KEY, name VARCHAR(255) NOT NULL);",
		                 "CREATE TABLE IF NOT EXISTS node2node (id INTEGER PRIMARY KEY, parent INTEGER NOT NULL, child INTEGER NOT NULL);"};
		return sqls;
	}

	// getters and setters
	public int getId() {
		return id;
	}
	
	/* user should not be able to create ids
	public void setId(int id) {
		if (id != this.id)
			dirty = true;
		this.id = id;
	}
	*/
	
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
	
	public ArrayList<Integer> getChildren() {
		return children;
	}
	
	public void setChildren(ArrayList<Integer> children) {
		this.children = children;
	}

	
}
