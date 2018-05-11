package todo;

import java.sql.SQLException;
import java.io.File;

public class Cli {

	public Cli() {}
	
	private static void reset() {
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		String f = System.getProperty("user.dir") + "/nodes.db";
		File file = new File(f);
		file.delete();
	}
	
	private static void open() {
		try {
			Database.open("nodes.db");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
		// remove Database file
		reset();
		
		// open database connection
		open();
		
		// create tables
		Node n = new Node();
		n.createTable();
		
		// generate 10 dummy objects
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
		
		// modify 9
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
		
		// delete 10
		n = new Node(10);
		try {
			n.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(n);
		
		n = new Node(10);
		System.out.println(n);
		
		// move 7/8/9 to be a child of 1
		try {
			n = new Node(7);
			n.setParent(1);
			n.store();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			n = new Node(8);
			n.setParent(1);
			n.store();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			n = new Node(9);
			n.setParent(1);
			n.store();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			n = new Node(2);
			n.setParent(8);
			n.store();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// get all children of root node
		n = new Node(1);
		try {
			System.out.println(n.getChildren());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
