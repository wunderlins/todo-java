package todo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {
	static protected String url = "";
	static Connection conn = null;
	static Statement stmt = null;

	protected boolean loaded = false;
	protected boolean dirty = false;
	protected ResultSet rs = null;
	
	public static void open(String db_file) throws SQLException {
		url = "jdbc:sqlite:" + db_file;
		
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void close() throws SQLException {
		conn.close();
	}
	
	public void load() throws SQLException, Exception {
		rs = loadStmt().executeQuery();
	}
	
	public void store() throws SQLException {}
	
	public void insert() throws SQLException {
		insertStmt().execute();
	}
	
	public void update() throws SQLException {
		updateStmt().execute();
	}
	
	public void delete() throws SQLException {
		deleteStmt().execute();
	}
	
	public void createTable() throws SQLException {
		createStmt().execute();
	}
	
	public abstract PreparedStatement deleteStmt();
	public abstract PreparedStatement loadStmt();
	public abstract PreparedStatement insertStmt();
	public abstract PreparedStatement updateStmt();
	public abstract PreparedStatement createStmt();

}
