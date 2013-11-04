package am.app.ontology.ontologyParser;

import am.app.ontology.Ontology.DatasetType;
import am.app.ontology.instance.endpoint.EndpointRegistry;

import com.hp.hpl.jena.util.LocationMapper;

/**
 * This data structure holds all the information required
 * for loading an ontology.
 *  
 * @author Cosmin Stroe - Sep 8, 2011
 *
 */
public class OntologyDefinition {
	
	public boolean loadOntology = true;
	
	/**
	 * 
	 */
	public String ontologyURI;
	
	/**
	 * @see OntologyLanguage
	 */
	public OntologyLanguage ontologyLanguage;
	
	/**
	 * @see OntologySyntax
	 */
	public OntologySyntax ontologySyntax;
	
	/**
	 * Allows the ontology to be stored into a Jena TDB on disk triple store.
	 */
	public boolean onDiskStorage = false;
	public boolean onDiskPersistent = false;
	public String  onDiskDirectory;
	
	/** <p>
	 * When instances are stored in a separate file from the schema, this
	 * allows us to load the instance file.
	 * </p> <p>
	 * It can also be the case that there is no schema or ontology, and in that case
	 * we just load the instances. </p>
	 */
	public boolean loadInstances = false;
	public DatasetType instanceSourceType;
	
	public String instanceSourceFile;
	public int instanceSourceFormat;  // 0 = RDF
	public EndpointRegistry instanceEndpointType;
	
	public boolean loadSchemaAlignment = false;
	public String  schemaAlignmentURI;
	public int  schemaAlignmentFormat;
	
	public LocationMapper locationMapper;
	
	/**
	 * <p>
	 * If the ontology is the source ontology, set to {@link #SOURCE_ONTOLOGY}.
	 * If the ontology is the target ontology, set to {@link #TARGET_ONTOLOGY}.
	 * </p>
	 * <p>
	 * TODO: Get rid of this variable, it does not make sense in a multi-ontology framework.
	 * </p>
	 * 
	 * @see #SOURCE_ONTOLOGY
	 * @see #TARGET_ONTOLOGY
	 */
	public int sourceOrTarget;
	
	@Override
	public boolean equals(Object obj) {
		if( obj instanceof OntologyDefinition ) {
			OntologyDefinition ontDef = (OntologyDefinition) obj;
			
			if( loadOntology != ontDef.loadOntology ) return false;
			if( loadOntology == true ) {
				if( !ontologyURI.equals(ontDef.ontologyURI) ) return false;
				if( ontologyLanguage != ontDef.ontologyLanguage ) return false;
				if( ontologySyntax != ontDef.ontologySyntax ) return false;
			}
			
			if( onDiskStorage != ontDef.onDiskStorage ) return false;
			if( onDiskStorage == true ) {
				if( !onDiskDirectory.equals(ontDef.onDiskDirectory) ) return false;
				if( onDiskPersistent != ontDef.onDiskPersistent ) return false;
			}
			
			if( loadInstances != ontDef.loadInstances ) return false;
			if( loadInstances == true ) {
				if( !instanceSourceFile.equals(ontDef.instanceSourceFile) ) return false;
				if( instanceSourceFormat != ontDef.instanceSourceFormat ) return false;
				if( instanceSourceType != ontDef.instanceSourceType ) return false;
				if( instanceEndpointType != ontDef.instanceEndpointType ) return false;
				
				if( loadSchemaAlignment != ontDef.loadSchemaAlignment ) return false;
				
				if( loadSchemaAlignment == true ) {
					if( !schemaAlignmentURI.equals(ontDef.schemaAlignmentURI) ) return false;
					if( schemaAlignmentFormat != ontDef.schemaAlignmentFormat ) return false;
				}
			}
			
			// the definitions are equal.
			return true;
		}
		return false;
	}
	
	
	public final static int SOURCE_ONTOLOGY = 0;
	public final static int TARGET_ONTOLOGY = 1;
	
	/**
	 * Use this to reference an ontology language.
	 */
	public enum OntologyLanguage {
		RDFS("RDFS", 0),
		OWL("OWL", 1),
		XML("XML", 2),
		TABBEDTEXT("Tabbed Text", 3);
		
		String name;
		int ID;
		
		private OntologyLanguage(String name, int ID) {
			this.name = name;
			this.ID = ID;
		}
		
		public int getID() { return ID; }
		
		@Override public String toString() { return name; }
		
		public static OntologyLanguage getLanguage(String langString) {
			for( OntologyLanguage lang : OntologyLanguage.values() ) {
				if( lang.name.equals(langString) ) return lang;
			}
			return null;
		}
		
		public static OntologyLanguage getLanguage(int langID) {
			for( OntologyLanguage lang : OntologyLanguage.values() ) {
				if( lang.ID == langID ) return lang;
			}
			return null;
		}
	}
	
	/**
	 * Use this to reference an ontology syntax.
	 */
	public enum OntologySyntax {
		RDFXML("RDF/XML", 0),
		RDFXMLABBREV("RDF/XML-ABBREV", 1),
		NTRIPLE("N-TRIPLE", 2),
		N3("N3", 3),
		TURTLE("TURTLE", 4);
		
		String name;
		int ID;
		
		private OntologySyntax(String name, int ID) {
			this.name = name;
			this.ID = ID;
		}
		
		public int getID() { return ID; }
		
		@Override public String toString() { return name; }
		
		public static OntologySyntax getSyntax(String synString) {
			for( OntologySyntax syn : OntologySyntax.values() ) {
				if( syn.name.equals(synString) ) return syn;
			}
			return null;
		}
		
		public static OntologySyntax getSyntax(int synID) {
			for( OntologySyntax syn : OntologySyntax.values() ) {
				if( syn.ID == synID ) return syn;
			}
			return null;
		}
	}
}
