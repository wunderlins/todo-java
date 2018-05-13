

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import todo.Database;
import todo.Node;

/**
 * Servlet implementation class Todo
 */
@WebServlet("/Todo")
public class Todo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Todo() {
        super();
    }
    
    private void displayChildren(Node n, HttpServletResponse response) {
		PrintWriter writer = null;
		
		try {
			writer = response.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (n.getNumChildren() > 0) {
			ArrayList<Node> children = null;
			try {
				children = n.getChildren();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			for (Node nc: children) {
				writer.append(String.valueOf(nc.getParentId()) + " | " + nc.getName() + " | " + String.valueOf(nc.getNumChildren()) + "<br>\n");
				if (nc.getNumChildren() > 0) {
					displayChildren(nc, response);
				}
			}
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String f = System.getProperty("user.dir") + "/nodes.db";
		f = "/home/wus/Projects/todo-java/nodes.db";
		
		int id = 0;
		String path = request.getPathInfo();
		if (path == null) { 
			path = "";
		} else {
			path = path.substring(1);
			id = Integer.parseInt(path);
		}
		
		System.out.println("Root: " + path);
		
		System.out.println(f);
		try {
			Database.open(f);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		Node.setRootNodeName("Nodes");
		Node n = new Node(id);
		
		PrintWriter writer = response.getWriter();
		writer.append(String.valueOf(n.getParentId()) + " | " + n.getName() + " | " + String.valueOf(n.getNumChildren()) + "<br>\n");
		
		displayChildren(n, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
