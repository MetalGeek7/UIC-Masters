package testConnection;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import newEntities.SongArtists;
import newEntities.SongRating;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;



public class Connectivity {
	
	Configuration cfg = null;
	SessionFactory factory = null;
	ServiceRegistry registry = null;
	
	public void initializeParams(){
		cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		registry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
		factory = cfg.buildSessionFactory(registry);
	}
	public static void retrieve(StatefulKnowledgeSession ksession,List res)
	{
		for(Object o:res)
		{
			ksession.insert(o);
		}
		//System.out.println("inserted");
	}
	public static List getList(String className, Session session)
	{
		//List l=null;
		Query query = session.createQuery("from newEntities."+className);
		query.setFirstResult(0);
		query.setMaxResults(500000);
		List res = query.list();
		return res;
	}
	public static List getSongRating(Session session)
	{
		//List l=null;
		Query query = session.createQuery("select s.id.songId,avg(s.id.songRating)  from newEntities.SongRating s order by s.id.songId ");
		query.setFirstResult(0);
		query.setMaxResults(10000);
		List res = query.list();
		SongRating s;//=(SongRating) res.get(0);
		//s.setId(id)
		System.out.println(res.get(0));
		return res;
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
	public static List getUsers(Session session,int start,int end)
	{
		Query query=session.createQuery(" from newEntities.SongData s order by s.songId "); //group by u.userLocation");
		query.setFirstResult(start);
		query.setMaxResults(end);
		List results=query.list();
		return results;
	}
	public static List getArtists(Session session,int start,int end)
	{
		Query query=session.createQuery(" from newEntities.SongArtists a order by a.artistLocation"); //group by u.userLocation");
		query.setFirstResult(start);
		query.setMaxResults(end);
		List results=query.list();
		return results;
		
	}
	public static void  passUsers(Session session, StatefulKnowledgeSession ksession,KnowledgeBase kbase) throws FileNotFoundException
	{
		List results;List artists;List ratings;
		artists=getArtists(session, 0, 1000);
		results=getUsers(session,0, 1000);
		ratings=getSongRating(session);
		System.out.println("size of songdata" +results.size());
		
		System.out.println(results.get(0));
		List locations=getLocations(session, 0, 2000);
		System.out.println("size of location "+locations.size());
		int count=0;
		ksession.setGlobal("count", count);
		SongArtists s=new SongArtists();
		//PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
		//System.setOut(out);
		int i=0;
		while(i<1)
		{
			ksession = kbase.newStatefulKnowledgeSession();
			s.setArtistLocation((String)locations.get(i));
			System.out.println(s.getArtistLocation());
			ksession.setGlobal("location", s);
			retrieve(ksession,results);
			retrieve(ksession,artists);
			retrieve(ksession,ratings);
			
			ksession.fireAllRules();
			//System.out.println("Here "+locations.get(i));
			i++;
			
		}
		
		
		
		
		
		/*int start=0;
		int end=10000;
		SongArtists current;
		List results;List artists;
		int count=0;
		while(end<200000)
		{
			results=getUsers(session,start,end);
			artists=getArtists(session, start, end);
			retrieve(ksession, results);
			retrieve(ksession,artists);
			System.out.println();
			System.out.println("size "+results.size());
			int i=0;
			while(i<results.size())
			{
				//current=(UserData)results.get(i);
				//System.out.println("current "+current.getUserLocation()+"  "+current.getLoginName());
				//Thread t;
				count++;
				System.out.println("count "+count);
				ksession.setGlobal("current", current);
				//System.out.println("hi");
				
				//ksession.getAgenda().getAgendaGroup( "location" ).setFocus();
				//ksession.fireAllRules();
				i++;
				
				
			}
			start=end+1;
			end=end+10000;
			
		}*/
		
		
	}
	@SuppressWarnings("rawtypes")
	public void ruleTest() throws Exception{
		KnowledgeBase kbase = readKnowledgeBase();
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
        
        Session session = factory.openSession();
		//Transaction tx = session.beginTransaction();
        //UserData current =new UserData();
		//current.setUserLocation("India");
		//current.setUserPreferredGenre("genre_4");
		//current.setUserId("user_120283");
		//ksession.setGlobal("current", current);		
		/*List res=getList("Genre",session);
		retrieve(ksession,res);
		res=getList("SongData",session);
		retrieve(ksession,res);		
		 res=getList("SongArtists",session);
		retrieve(ksession,res);		
		res=getList("UserPlaycount",session);
		retrieve(ksession,res);	
		res=getList("SongRating",session);
		retrieve(ksession,res);
		List results=new ArrayList();*/
		
		
		//ksession.fireAllRules();
      // passUsers(session,ksession,kbase);//important one
       // List r=getSongRating(session);
       // System.out.println(r.get(0));
		
    	//List res=getList("SongRating",session);
    	//retrieve(ksession,res);	
    	//res=getList("SongData",session);
		//retrieve(ksession,res);	
        InvokeRules s=new InvokeRules();
        //s.getList(className, session)
        //s.performActions(session, ksession,kbase);
        s.showDanceabilty(session, ksession, kbase);
		logger.close();
		
	}
	
	private static KnowledgeBase readKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
       // kbuilder.add(ResourceFactory.newClassPathResource("student.drl"), ResourceType.DRL);
       // kbuilder.add(ResourceFactory.newClassPathResource("requestHandler.drl"), ResourceType.DRL);
       //kbuilder.add(ResourceFactory.newClassPathResource("preference.drl"), ResourceType.DRL);
       // kbuilder.add(ResourceFactory.newClassPathResource("location.drl"), ResourceType.DRL);
        kbuilder.add(ResourceFactory.newClassPathResource("danceabilty.drl"), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error: errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        return kbase;
    }
	
	public static void main(String[] args) {
		Connectivity c = new Connectivity();
		c.initializeParams();
		try {
			c.ruleTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
