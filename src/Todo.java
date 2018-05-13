

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String f = System.getProperty("user.dir") + "/nodes.db";
		String absoluteDiskPath = getServletContext().getRealPath(".");
		f = "/home/wus/Projects/todo-java/nodes.db";
		System.out.println(absoluteDiskPath);
		try {
			Database.open(f);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		Node.setRootNodeName("Nodes");
		Node n = Node.getRootNode();
		
		PrintWriter writer = response.getWriter();
		writer.append("Served at: ").append(request.getContextPath());
		writer.append("<br><br>");
		writer.append(String.valueOf(n.getParentId()) + " | " + n.getName() + " | " + String.valueOf(n.getNumChildren()));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
