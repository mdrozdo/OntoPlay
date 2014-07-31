package models.ontologyModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import models.PropertyValueCondition;
import models.propertyConditions.DatatypePropertyCondition;

import com.hp.hpl.jena.ontology.Individual;


public class OwlIndividual implements OwlElement{
	private String uri;
	private String localName;
	private String classUri;
	private List<PropertyValueCondition> properties;
	
	public OwlIndividual(Individual individual, List<PropertyValueCondition> properties) {
		if(properties == null){
			throw new NullArgumentException("properties");
		}
		this.uri = individual.getURI();
		this.localName = individual.getLocalName();
		this.properties = properties;
	}
	
	public OwlIndividual(OWLNamedIndividual individual, List<PropertyValueCondition> properties) {
		if(properties == null){
			throw new NullArgumentException("properties");
		}
		this.uri = individual.getIRI().toString();
		this.localName = individual.getIRI().getFragment();
		this.properties = properties;
	}
	
	public String getUri(){
		return uri;
	}
	
	public String getLocalName(){
		return localName;
	}
	
	public String getClassUri(){
		return uri;
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
	
	
}
