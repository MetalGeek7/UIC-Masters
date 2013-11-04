package testConnection;


import java.util.List;


import newEntities.SongArtists;
import newEntities.UserData;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.hibernate.Query;
import org.hibernate.Session;

public class InvokeRules {
	public  List getDanceabilty(Session session,int start,int end)
	{
		//List l=null;
		
		Query query = session.createQuery("from newEntities.SongData s");
		query.setFirstResult(start);
		query.setMaxResults(1000);
		List res = query.list();
		return res;
	}
	public  List getSongRating(Session session,int start,int end)
	{
		//List l=null;
		
		Query query = session.createQuery("from newEntities.SongRating s");
		query.setFirstResult(start);
		query.setMaxResults(end);
		List res = query.list();
		return res;
	}
	
	public void showDanceabilty(Session session,StatefulKnowledgeSession ksession,KnowledgeBase kbase)
	{
	UserData current =new UserData();
				current.setUserLocation("China");
			current.setUserPreferredGenre("genre_4");
				current.setUserId("user_120283");
				ksession.setGlobal("current", current);	
				List ratings=getSongRating(session, 0, 10000);
				insert(ksession, ratings);
				ksession.fireAllRules();
				
		
	}
	
	public void performActions(Session session,StatefulKnowledgeSession ksession,KnowledgeBase kbase)
	{
		int i=0;
		Connectivity c=new Connectivity();
		//List artist=c.getList("SongArtists", session);
		List songData;//=getDanceabilty(session);
		List location=getLocations(session, 0, 48);
		SongArtists s=new SongArtists();
		
		while(i<location.size())
		{
for(int start=0;start<500000;start=start+10000)
{
	 songData=getDanceabilty(session,start,10000);
	 ksession = kbase.newStatefulKnowledgeSession();
		s.setArtistLocation((String)location.get(i));
		ksession.setGlobal("tempGlobal", s);
	//	insert(ksession,artist);
		insert(ksession,songData);
		
		ksession.fireAllRules();
	
}				
			
		}
		
		
		
	}
	public static List getLocations(Session session,int start,int end)
	{
		Query query=session.createQuery("select s.artistLocation from newEntities.SongArtists s group by s.artistLocation"); //group by u.userLocation");
		query.setFirstResult(start);
		query.setMaxResults(end);
		List results=query.list();
		System.out.println(results.get(0)+" "+results.get(1));
		return results;
	}
	public void locationBasedFilter()
	{
		
	}
	public static void insert(StatefulKnowledgeSession ksession,List res)
	{
		for(Object o:res)
		{
			ksession.insert(o);
		}
		//System.out.println("inserted");
	}

}
