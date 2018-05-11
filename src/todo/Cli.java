package todo;

import java.sql.SQLException;

public class Cli {

	public Cli() {}

	public static void main(String[] args) {
		Node n = new Node();
		try {
			n.open("nodes.db");
			n.create();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}

}
