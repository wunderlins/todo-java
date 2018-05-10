package ormtest;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class H2test {
	protected static ConnectionSource connectionSource;
	protected static Dao<Account, String> accountDao;

	public H2test() {;}
	
	protected static void createDatabase() throws SQLException {
        // if you need to create the 'accounts' table make this call
        TableUtils.createTable(connectionSource, Account.class);
	}
	
	protected static void createData() throws SQLException {
        // create an instance of Account
        Account account = new Account();
        account.setName("Jim Coakley");

        // persist the account object to the database
        accountDao.create(account);
	}
	
	protected static void createDataEntry(String name) throws SQLException {
        // create an instance of Account
        Account account = new Account();
        account.setName(name);

        // persist the account object to the database
        accountDao.create(account);
	}

	public static void main(String[] args) throws Exception {
        
        // this uses h2 by default but change to match your database
        String databaseUrl = "jdbc:h2:~/Account";
        // create a connection source to our database
        connectionSource = new JdbcConnectionSource(databaseUrl);
        
        createDatabase();
        
        // instantiate the dao
        accountDao = DaoManager.createDao(connectionSource, Account.class);
        
        //createDataEntry("asdf asdf ");
        //createDataEntry("asd fasdfasdf");
        

        // retrieve the account from the database by its id field (name)
        // Account account2 = accountDao.queryForId("Jim Coakley");
        // System.out.println("Account: " + account2.getName());
        
        List<Account> res = accountDao.queryForAll();
        System.out.println(res);
        
        for(Account a: res) {
        	System.out.println(a);
        }
        
        // close the connection source
        connectionSource.close();
	}

}
