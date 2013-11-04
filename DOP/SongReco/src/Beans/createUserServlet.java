package Beans;

import java.io.IOException;
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
 * Servlet implementation class createUserServlet
 */
@WebServlet("/createUserServlet")
public class createUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public createUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		User u=new User();
		String user_name=null;
		if(request.getParameter("user_name")!=null) {
			user_name=request.getParameter("user_name");
			//Users.user.setLoginName(user_name);
		}
		/*if(request.getParameter("password")!=null) {
				String password=request.getParameter("password");
				u.setPassword(password);
		}*/
		//Users.users.add(u);
		testConnection1 con=new testConnection1();
		con.initializeParams();
		//String query_string="from UserData where loginName='rskinne27'";
		List list=con.testQuery(user_name);
		//UserData user=null;

		RequestDispatcher rd=request.getRequestDispatcher("Second.jsp");

		rd.forward(request, response);
	}

}
