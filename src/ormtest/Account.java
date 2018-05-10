package ormtest;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "accounts")
public class Account {
    
    @DatabaseField(id = true, useGetSet = true)
    private String name;
    @DatabaseField(useGetSet = true)
    private String password;
    @DatabaseField(canBeNull = false, foreign = true, useGetSet = true)
    private Account parent;
    
    public Account() {
        // ORMLite needs a no-arg constructor 
    }
    public Account(String name, String password) {
        this.name = name;
        this.password = password;
    }
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Account getParent() {
		return parent;
	}
	public void setParent(Account parent) {
		this.parent = parent;
	}
	@Override
	public String toString() {
		return String.format("Account [name=%s, password=%s]", name, password);
	}
}

