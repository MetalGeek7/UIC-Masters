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


public class FirstRule {
	
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
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
        
		Session session = factory.openSession();
		//Transaction tx = session.beginTransaction();
		
		Query query = session.createQuery("from SongArtists");
		List res = query.list();
		SongArtists artist = null;
		
		for (Iterator it = res.iterator(); it.hasNext();) {
			artist = (SongArtists) it.next();
			ksession.insert(artist);
			ksession.fireAllRules();
			//System.out.println("Name: " + artist.getArtistName());
			//System.out.println("Artist: " + artist.getArtistLocation());
		}
		logger.close();
	}
	
	private static KnowledgeBase readKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("artist.drl"), ResourceType.DRL);
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
		FirstRule rule = new FirstRule();
		rule.initializeParams();
		try {
			rule.readData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
