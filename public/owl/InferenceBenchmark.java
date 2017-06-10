package ibspan.aig.benchmark;

import java.io.FileNotFoundException;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.PrintUtil;
import org.apache.jena.vocabulary.RDF;
import de.fau.cs.www8.jadeowlcodec.GenericIndividual;
import de.fau.cs.www8.jadeowlcodec.KnowledgeBase;
import de.fau.cs.www8.jadeowlcodec.TBoxMerger.MergeException;
import de.fau.cs.www8.jadeowlcodec.test.dogOntology.w.Dog;
import de.fau.cs.www8.jadeowlcodec.tool.PlatformServices;
import de.fau.cs.www8.jadeowlcodec.triplestore.Uri;

public class InferenceBenchmark {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		InferenceBenchmark benchmark = new InferenceBenchmark();
		benchmark.run();
	}

	private void run() {
		//runPelletDemo();
		//runXsdConstraintDemo();
		runJADEOWLDemo();
	}
	
	private void runJADEOWLDemo() {
		PlatformServices.setReasoner("localhost", 8080); //the address of your running RACER instance 
		KnowledgeBase kb = new KnowledgeBase(Uri.Other.instanceNotPretyped("http://test/KB"));
		try {
			kb.expandTBox("data/dogOntology.owl");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MergeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GenericIndividual dog = new GenericIndividual(kb.getABox(), null, Dog.Factory.instance.getUri()); // if the second argument is null a new URI will be created

		Dog wrappedDog = Dog.Factory.wrap(dog);
		wrappedDog.setWHasName("Claire");
	}

	private void runPelletDemo(){
		Model schema = FileManager.get().loadModel("file:data/testOntology.owl");
		
	    Reasoner reasoner = PelletReasonerFactory.theInstance().create();
	    reasoner = reasoner.bindSchema(schema);
	    InfModel infmodel = ModelFactory.createInfModel(reasoner, schema);
	    
	    Resource matchingCandidate = infmodel.getResource("http://gridagents.sourceforge.net/testOntology.owl#MatchingCandidate");
	    
	    System.out.println("PELLET:");
	    System.out.println("MatchingCandidate *:");
	    printStatements(infmodel, matchingCandidate, null, null);		
	}
	
	private void runXsdConstraintDemo(){
		Model schema = FileManager.get().loadModel("file:data/testOntology.owl");
		
	    Model data = FileManager.get().loadModel("file:data/owlDemoData.xml");
	    Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
	    reasoner = reasoner.bindSchema(schema);
	    InfModel infmodel = ModelFactory.createInfModel(reasoner, schema);
	    
	    Resource matchingCandidate = infmodel.getResource("http://gridagents.sourceforge.net/testOntology.owl#MatchingCandidate");
	    
	    System.out.println("JENA OWL:");
	    System.out.println("MatchingCandidate *:");
	    printStatements(infmodel, matchingCandidate, null, null);
	}
	
	private void runOwlDemo(){
		Model schema = FileManager.get().loadModel("file:data/owlDemoSchema.xml");
	    Model data = FileManager.get().loadModel("file:data/owlDemoData.xml");
	    Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
	    reasoner = reasoner.bindSchema(schema);
	    InfModel infmodel = ModelFactory.createInfModel(reasoner, data);
	    
	    Resource nForce = infmodel.getResource("urn:x-hp:eg/nForce");
	    System.out.println("nForce *:");
	    printStatements(infmodel, nForce, null, null);
	}
	
	private void runRdfsDemo() {
		Model schema = FileManager.get().loadModel("file:data/simple_Schema.rdf");
		Model data = FileManager.get().loadModel("file:data/simple_Data.rdf");
		InfModel infmodel = ModelFactory.createRDFSModel(schema, data);
		        
		Resource colin = infmodel.getResource("urn:x-hp:eg/colin");
		System.out.println("colin has types:");
		printStatements(infmodel, colin, RDF.type, null);
		        
		Resource Person = infmodel.getResource("urn:x-hp:eg/Person");
		System.out.println("\nPerson has types:");
		printStatements(infmodel, Person, RDF.type, null);
	}
	
	
	
	private void printStatements(Model m, Resource s, Property p, Resource o) {
        for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) {
            Statement stmt = i.nextStatement();
            System.out.println(" - " + PrintUtil.print(stmt));
        }
    }

}
