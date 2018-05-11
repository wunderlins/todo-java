package todo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {
	static protected String url = "";
	static Connection conn = null;
	static Statement stmt = null;

	protected boolean loaded = false;
	protected boolean dirty = false;
	
	public static void open(String db_file) throws SQLException {
		url = "jdbc:sqlite:" + db_file;
		
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
	
	public void load() throws SQLException, Exception {}
	public void store() throws SQLException {}
	public void delete() throws SQLException {}
	
	public void createTable() {
		execute(createSql());
	}
	
	protected void execute(String sql) {
		System.out.println(sql);
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected void execute(String[] sqls) {
		for(String sql:sqls) {
			execute(sql);
		}
	}
	
	public abstract String[] loadSql();
	public abstract String[] insertSql();
	public abstract String[] updateSql();
	public abstract String[] createSql();
	public abstract String[] deleteSql();

}
