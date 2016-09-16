package ontoplay.models;

import org.semanticweb.owlapi.model.IRI;

public class OntologyUtils {
	public static String getNamespace(IRI iri){
		String namespace = iri.getStart();
		if(namespace.endsWith("#") || namespace.endsWith("/")){
			namespace = namespace.substring(0, namespace.length()-1);
		}
		return namespace;
	}
}
