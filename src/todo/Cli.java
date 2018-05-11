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
		
		int parent = 0;
		for(int i=0; i<10; i++) {
			Node n1 = new Node();
			n1.setName(String.valueOf(i) + " element");
			n1.setParent(parent);
			n1.store();
			System.out.println(n1);
			parent = n1.getId();
		}
		
	}

}
