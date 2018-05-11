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
		
		/*
		int parent = 0;
		for(int i=0; i<10; i++) {
			Node n1 = new Node();
			n1.setName(String.valueOf(i) + " element");
			n1.setParent(parent);
			try {
				n1.store();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println(n1);
			parent = n1.getId();
		}
		*/
		
		n = new Node(9);
		n.setName("i am a new name");
		n.setParent(0);
		try {
			n.store();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		n = new Node(9);
		System.out.println(n);
	}

}
