package Beans;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.drools.runtime.StatefulKnowledgeSession;

import com.sample.Connectivity;
import com.sample.InvokeRules;


/**
 * Servlet implementation class DisplaySngsServlet
 */
@WebServlet("/UpdateSongLikeServlet")
public class UpdateSongLikeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateSongLikeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		String song_id=request.getParameter("id");
		Connectivity c=new Connectivity();
		c.initializeParams();
		System.out.println("here");
			try {
				//StatefulKnowledgeSession ksession=c.getKnowledgeSession();
				//InvokeRules ruleInvoker=new InvokeRules();
				//ruleInvoker.fetchSongDataForArtist(ksession, c.getPreferredArtist("user_99999"), "SongFromArtist");
				c.getLikes(Users.user.getUserId(), song_id);
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
		//System.out.println("Welcome servlet");

		//out.println(username);
	}

}
