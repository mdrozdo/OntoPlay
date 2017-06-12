package ontoplay.models.ontologyModel;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.IRI;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;

public class OntoClass implements OwlElement {

	private String localName;
	private String namespace;
	private OntoClass superClass;
	private List<OntoProperty> properties;
	private String label;

	public OntoClass(String namespace, String name, List<OntoProperty> properties, OntClass ontClass) {
		this.namespace = namespace;
		this.localName = name;
		this.properties = properties;
		if(ontClass  != null && ontClass.getSuperClass()!=null)
			this.superClass = new OntoClass(ontClass.getSuperClass());
		
	}

	public OntoClass(OntClass ontClass) {
		this(ontClass.getNameSpace(), ontClass.getLocalName(), new ArrayList<OntoProperty>(), ontClass);
		this.label = ontClass.getLabel(null);
		if(label != null && label.isEmpty())
			label = null;
	}
	
	public OntoClass(OntClass ontClass, OntClass ontSuperClass) {
		this(ontClass.getNameSpace(), ontClass.getLocalName(), new ArrayList<OntoProperty>(), ontClass);
		this.superClass = new OntoClass(ontSuperClass);
	}

	public OntoClass(IRI iri, List<OntoProperty> properties) {
		this(iri.getScheme(), iri.getFragment(), properties, null);
	}

	@Override
	public String getLocalName() {
		return localName;
	}

	public String getNamespace() {
		return namespace;
	}

	public List<OntoProperty> getProperties() {
		return properties;
	}

	public OntoProperty findProperty(String propertyLocalName) {
		for (OntoProperty prop : getProperties()) {
			if (prop.getLocalName().equals(propertyLocalName)) {
				return prop;
			}
		}
		return null;
	}

	@Override
	public String getUri() {
		return String.format("%s%s", namespace, localName);
	}
	
	public String getDisplayName() {
		String localDisplayName = label;
		if(localDisplayName == null)
			localDisplayName = localName;
		if(superClass != null)
			return String.format("%s\\%s", superClass.getDisplayName(), localDisplayName);
		else 			
			return localDisplayName; 
	}

	public OntoClass getSuperClass() {
		return superClass;
	}
	
	public String getSuperClassName() {
		if(superClass != null )
			return superClass.getLocalName();	

			return "NotFound";
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public Individual getIndividual() {
		// TODO Auto-generated method stub
		return null;
	}

}
