package todo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {
	static protected String url = "";
	Connection conn = null;
	Statement stmt = null;

	public void open(String db_file) throws SQLException {
		url = "jdbc:sqlite:" + db_file;
		
		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
			    	conn.close();
			    }
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	
	public void close() throws SQLException {
		conn.close();
	}
	
	public abstract void load();
	public abstract void store();
	public abstract void create();
}
