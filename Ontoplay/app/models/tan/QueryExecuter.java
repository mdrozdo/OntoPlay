package models.tan;

import controllers.org.TANHelper;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

import java.io.InputStream;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import play.Logger.ALogger;

public class QueryExecuter {
	
	static QueryExecuter connection = new QueryExecuter();
	
	OntModel connect() {
		OntModel mode = null;
		mode = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		InputStream in = FileManager.get().open(TANHelper.file); 
		if (in == null) {
			throw new IllegalArgumentException("File not found"); 
		}
		return (OntModel) mode.read(in, "");
	}

	ResultSet executeQuery(String queryString) {
		ALogger log = play.Logger.of("application");
		log.info( "\n" + queryString + "\n");
		Query query = QueryFactory.create(queryString);
		QueryExecution queryExcution = QueryExecutionFactory.create(query,connect());
		ResultSet resultSet = queryExcution.execSelect();
		return resultSet;
	}

}
