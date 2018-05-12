package todo;

import java.sql.SQLException;
import java.util.ArrayList;
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

	public static void main(String[] args) throws Exception {
		
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
			n1.store();
			System.out.println(n1);
			parent = n1.getId();
		}
		
		// modify 9
		n = new Node(9);
		n.setName("i am a new name");
		n.setParent(0);
		n.store();
		
		n = new Node(9);
		System.out.println(n);
		
		// delete 10
		n = new Node(10);
		n.delete();
		System.out.println(n);
		
		n = new Node(10);
		System.out.println(n);
		
		// move 7/8/9 to be a child of 1
		n = new Node(7);
		n.setParent(1);
		n.store();

		n = new Node(8);
		n.setParent(1);
		n.store();

		n = new Node(9);
		n.setParent(1);
		n.store();

		n = new Node(2);
		n.setParent(8);
		n.store();
		
		/*
		// create new node
		n = new Node();
		n.setNode(12, "12th", 1);
		*/

		// get all children of root node
		n = new Node(1);
		System.out.println(n.getChildren());
		
		
		ArrayList<Node> c = new ArrayList<>();
		c = n.getChildren();
		
		for (Node nr: c) {
			nr.setName(nr.getName() + " ------");
			nr.store();
			System.out.println(nr);
		}
		
		// traverse to root from a node via getParent();
		System.out.println("=================");
		n = new Node(2);
		do {
			System.out.println(n);
			n = n.getParent();
		} while (n.getParentId() != -1);
		System.out.println(n);
	}

}
