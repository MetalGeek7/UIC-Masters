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

import org.drools.runtime.StatefulKnowledgeSession;

import MongoEntities.ArtistDataBean;
import MongoEntities.SongBean;

import com.sample.Connectivity;
import com.sample.InvokeRules;

/**
 * Servlet implementation class FetchArtistDataByArtistServlet
 */
@WebServlet("/FetchArtistDataByArtistServlet")
public class FetchArtistDataByArtistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FetchArtistDataByArtistServlet() {
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
		String artist_id=request.getParameter("id");
		Connectivity c=new Connectivity();
		c.initializeParams();
		//System.out.println("here");
			try {
				StatefulKnowledgeSession ksession=c.getKnowledgeSession();
				InvokeRules ruleInvoker=new InvokeRules();
				List<ArtistDataBean> list=ruleInvoker.fetchArtistData(ksession, artist_id, "ArtistDetails");
				request.setAttribute("list_artist", list);
				RequestDispatcher rd=request.getRequestDispatcher("ArtistByArtist.jsp");
				rd.forward(request, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
