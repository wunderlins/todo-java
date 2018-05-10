package data;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class Database {
	protected static ConnectionSource connectionSource;
	protected static Dao<todoItem, Integer> itemDao;

	public ConnectionSource connect() throws SQLException {
        // this uses h2 by default but change to match your database
        String databaseUrl = "jdbc:h2:~/Todo";
        // create a connection source to our database
        connectionSource = new JdbcConnectionSource(databaseUrl);
        
        createDatabase();
        
        // instantiate the dao
        itemDao = DaoManager.createDao(connectionSource, todoItem.class);
        
        return connectionSource;
	}
	
	public static void main(String[] args) throws SQLException {
		Database d = new Database();
		d.connect();
	}
	
	protected static void createDatabase() throws SQLException {
        // if you need to create the 'accounts' table make this call
        TableUtils.createTable(connectionSource, todoItem.class);
	}

}
