package todo;

import java.sql.SQLException;

public class Cli {

	public Cli() {}

	public static void main(String[] args) {
		Node n = new Node();
		try {
			Database.open("nodes.db");
			n.createTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(int i=0; i<10; i++) {
			Node n1 = new Node();
			n1.name = String.valueOf(i) + " element";
			n1.store();
		}
		
	}

}
