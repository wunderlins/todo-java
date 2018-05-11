package todo;

import java.sql.SQLException;
import java.util.ArrayList;

public class Node extends Database {
	protected int id = -1;
	protected String name = "";
	protected int parent = 0;
	protected ArrayList<Integer> children = new ArrayList<>();
	protected boolean loaded = false;
	protected boolean dirty = false;

	public Node() {}
	public Node(int id) {
		this.id = id;
		load();
	}

	@Override
	public void load() {
		
	}

	@Override
	public void store() {
		
	}
	
	@Override
	public void create() {
		String sql = "CREATE table if not exists node (id int PRIMARY KEY, name VARCHAR(255) NOT NULL);";
		System.out.println(sql);
		System.out.println(conn);
		System.out.println(stmt);
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
