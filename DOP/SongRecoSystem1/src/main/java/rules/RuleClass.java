package rules;


import java.util.Iterator;
import java.util.List;

import newEntities.SongArtists;

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

import com.sun.rowset.internal.Row;


public class RuleClass {
	
	Configuration cfg = null;
	SessionFactory factory = null;
	ServiceRegistry registry = null;
	
	public void initializeParams(){
		cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		registry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
		factory = cfg.buildSessionFactory(registry);
	}
	
	@SuppressWarnings("rawtypes")
	public void readData() throws Exception{
		
		KnowledgeBase kbase = readKnowledgeBase();
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "rule");
        
		Session session = factory.openSession();
		
		/*Query query = session.createQuery("from SongArtists");
		List res = query.list();
		SongArtists artist = null;
		
		for (Iterator it = res.iterator(); it.hasNext();) {
			artist = (SongArtists) it.next();
			ksession.insert(artist);
		}*/
		
		ksession.setGlobal("hbSession", session);
		
		Message newMesage = new Message();
//		newMesage.setMessage("AlbumsByYear");
//		ksession.insert(newMesage);
		newMesage.setMessage("DanceableByLocation");
		ksession.insert(newMesage);
		ksession.fireAllRules();
		
		logger.close();
		
		
		/*select s.song_id, s.song_title,a.artist_name 
		from song_artists a, song_data s 
		where s.song_artist_id = a.artist_id 
		and a.artist_location like '%India%' 
		order by s.song_danceability desc limit 20;*/
		
		/*String location = "India";
		
		Session session = factory.openSession();
		String sql_query = "select s.songTitle, a.artistName " +
				"from SongData s " +
				"inner join s.songArtists a " +
				"where a.artistLocation like '%" + location + 
				"%' order by s.songDanceability desc limit 100";
		
		Query query = session.createQuery(sql_query);
		Iterator iter = query.list().iterator();
		
		while(iter.hasNext()){
			Object[] retrieved = (Object[]) iter.next();
			System.out.print("Song: " + retrieved[0] + " Artist:" + retrieved[1]);
			System.out.println("");
		}*/
	}
	
	private static KnowledgeBase readKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("danceable.drl"), ResourceType.DRL);
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
	
	public static class Message {

        private String message;

        private int status;

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return this.status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

    }
	
	
	public static void main(String[] args) {
		RuleClass rule = new RuleClass();
		rule.initializeParams();
		try {
			rule.readData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
