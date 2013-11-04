package Beans;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Beans.testConnection1;

import newEntities.UserData;
import Beans.User;
import Beans.Users;

/**
 * Servlet implementation class DisplaySngsServlet
 */
@WebServlet("/DisplaySongsServlet")
public class DisplaySongsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DisplaySongsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Welcome servlet");
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		String username=request.getParameter("id");
		out.println(username);
		testConnection1 con=new testConnection1();
		con.initializeParams();
		try {
			con.testQuerySongs();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RequestDispatcher rd=request.getRequestDispatcher("Songs.jsp");

		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*User u=new User();
		String user_name=null;
		if(request.getParameter("user_name")!=null) {
			user_name=request.getParameter("user_name");
		}
		testConnection1 con=new testConnection1();
		con.initializeParams();
		String query_string="rskinne27";
		List list=con.testQuery(query_string);

		RequestDispatcher rd=request.getRequestDispatcher("Second.jsp");

		rd.forward(request, response);*/
		/*System.out.println("Welcome servlet");
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		//String username=request.getParameter("username");
		out.println("hii tejas");*/
	}

}
