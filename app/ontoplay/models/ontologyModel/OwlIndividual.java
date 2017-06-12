package ontoplay.models.ontologyModel;

import java.util.Iterator;
import java.util.List;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

import ontoplay.models.PropertyValueCondition;
import org.apache.jena.ontology.Individual;


public class OwlIndividual implements OwlElement{
	private String uri;
	private String localName;
	private String classUri;
	private Individual individual;
	
	private List<PropertyValueCondition> properties;
	
	public OwlIndividual(Individual individual, List<PropertyValueCondition> properties) {
		if(properties == null){
			throw new IllegalArgumentException("properties");
		}
		
		if(individual == null)
			throw new IllegalArgumentException("Individual");
		this.uri = individual.getURI();
		this.localName = individual.getLocalName();
		this.properties = properties;
		this.individual = individual;
	}
	
	public OwlIndividual(OWLNamedIndividual individual, List<PropertyValueCondition> properties) {
		if(properties == null){
			throw new IllegalArgumentException("properties");
		}
		this.uri = individual.getIRI().toString();
		this.localName = individual.getIRI().getFragment();
		this.properties = properties;
	}
	
	@Override
	public String getUri(){
		return uri;
	}
	
	@Override
	public String getLocalName(){
		return localName;
	}
	
	public String getClassUri(){
		return uri;
	}
	
	@Override
	public Individual getIndividual(){
		return individual;
	}
	
	public List<PropertyValueCondition> getProperties() {
		return properties;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ uri.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OwlIndividual other = (OwlIndividual) obj;
		if(this.uri==null||other.getUri()==null)
			return false;
		return uri.equals(other.getUri());
	}

	public PropertyValueCondition getProperty(String uri) {
		for (Iterator it = properties.iterator(); it.hasNext();) {
			PropertyValueCondition prop = (PropertyValueCondition) it.next();
			
			if(prop.getPropertyUri().equals(uri)){
				return prop;
			}
		}
		return null;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
