package ontoplay.models.angular.update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;

import org.apache.commons.lang.NullArgumentException;

import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.iterator.ExtendedIterator;

import ontoplay.controllers.utils.OntologyUtils;
import ontoplay.models.angular.IndividualDTO;
import ontoplay.models.ontologyReading.OntologyReader;

public class IndividualUpdateModel {

	private Individual individual;
	private OntClass ontClass;
	private UpdateIndividualDTO updateIndividual;
	private OntologyReader ontoReader;
	private Set<AnnotationProperty> annotationProperties;
	public List<AnnotationAxiom> annotationAxiomsList;
	
	public String getClassUri() {
		return ontClass.getURI();
	}

	public String getUri() {
		return individual.getURI();
	}

	public String getLocalName() {
		return individual.getLocalName();
	}

	public UpdateIndividualDTO getUpdateIndividual() {
		return updateIndividual;
	}

	public void setUpdateIndividual(UpdateIndividualDTO updateIndividual) {
		this.updateIndividual = updateIndividual;
	}

	public IndividualUpdateModel(Individual individual, OntologyReader ontoReader, Set<AnnotationProperty> annotationProperties, List<AnnotationAxiom> annotationAxiomsList) {
		this.ontoReader = ontoReader;
		if (individual == null) {
			throw new NullArgumentException("inidividual");
		}
		this.individual = individual;
		this.ontClass = individual.getOntClass(true);
		this.updateIndividual = new UpdateIndividualDTO();
		this.updateIndividual.setIndividualName(individual.getLocalName());
		this.annotationProperties = annotationProperties;
		this.annotationAxiomsList=annotationAxiomsList;
		Initialize();
	}

	private void Initialize() {

		ArrayList<OntProperty> dataProperties = new ArrayList<OntProperty>();
		ArrayList<OntProperty> objectProperties = new ArrayList<OntProperty>();

		ExtendedIterator<OntProperty> properties = ontClass.listDeclaredProperties();
		while (properties.hasNext()) {
			OntProperty property = properties.next();
			if (property.isDatatypeProperty()) {
				dataProperties.add(property);
			} else {
				if (!property.isAnnotationProperty())
					objectProperties.add(property);
			}
		}

		addDataProperties(dataProperties);
		addObjectProperties(objectProperties);
		addAnnotationsFortheObject();
	}

	private void addAnnotationsFortheObject() {
		for (AnnotationProperty annotationProperty : annotationProperties) {
			NodeIterator it = individual.listPropertyValues(annotationProperty);
			while (it.hasNext()) {
				updateIndividual.addAnnotation(annotationProperty.getLocalName(), annotationProperty.getURI(),
						it.next().asLiteral().getString());
			}
		}

	}

	private void addObjectProperties(ArrayList<OntProperty> functionalObjectProperties) {
		PropertyNode propertyNode = null;
		for (OntProperty property : functionalObjectProperties) {
			NodeIterator it = individual.listPropertyValues(property);
			Resource res = null;
			while (it.hasNext()) {
				propertyNode = new PropertyNode();
				propertyNode.setProperty(property.getURI());
				propertyNode.setInputType("object");
				propertyNode.setClassName(individual.getOntClass(true).getLocalName());

				RDFNode node = it.next();
				if (node.isURIResource()) {
					res = getResourceFromURI(node);
					propertyNode.setOperator("equalToIndividual");
					if (res != null && res.getURI() != null && res.getURI().length() != 0) {
						propertyNode.setObjectValue(res.getURI());
						propertyNode.setPropertyClass(getIndividualClass(res.getURI()));
					addAnnotationForTheProperty(propertyNode, property.getURI(), individual.getURI(), res);
					}
				} else {
					res = node.asResource();
					Individual tempInd = res.as(Individual.class);

					propertyNode.setOperator("describedWith");
					propertyNode.setInputType("object");
					propertyNode.setObjectValue("off");
					IndividualUpdateModel temp = new IndividualUpdateModel(tempInd, ontoReader, annotationProperties,this.annotationAxiomsList);
					List<PropertyNode> nodes = temp.getUpdateIndividual().getProperties();
					if (nodes != null && nodes.size() > 0) {
						propertyNode.setPropertyClass(nodes.get(0).getClassName());
						propertyNode.setNodes(nodes);
					}
					propertyNode.setObjectAnnotations(temp.getUpdateIndividual().getAnnotations());
					addAnnotationForTheProperty(propertyNode, property.getURI(), individual.getURI(), tempInd);
				}
				updateIndividual.addProperty(propertyNode);
			}
		}
	}


	private void addDataProperties(ArrayList<OntProperty> dataProperties) {
		PropertyNode propertyNode = null;

		for (OntProperty property : dataProperties) {
			NodeIterator it = individual.listPropertyValues(property);

	
			while (it.hasNext()) {
				propertyNode = new PropertyNode();
				propertyNode.setProperty(property.getURI());
				propertyNode.setOperator("equalTo");
				RDFNode currentNodes=it.next();
				propertyNode.setDataValue(currentNodes.asLiteral().getString());
				propertyNode.setClassName(individual.getOntClass(true).getLocalName());
				addAnnotationForTheProperty(propertyNode, property.getURI(), individual.getURI(), currentNodes);
				updateIndividual.addProperty(propertyNode);
			}
		}

	}

	private Resource getResourceFromURI(RDFNode node) {
		return ontoReader.getIndividual(node.asResource().getURI()).getIndividual();
	}

	private String getIndividualClass(String individualUri) {
		// check if it's Uri or name
		if (individualUri.indexOf("#") == -1)
			individualUri = ontoReader.getOntologyNamespace() + individualUri;

		try {
			return ontoReader.getIndividual(individualUri).getIndividual().getOntClass(true)
					.getURI();

		} catch (Exception e) {
			System.out.println("Error getting class for an individual");
			return "";
		}
	}
	
	private void addAnnotationForTheProperty(PropertyNode propertyNode,String propertyUri,String source,RDFNode target){
		AnnotationAxiom tempAnnotationAxiom=new AnnotationAxiom(propertyUri,source,target);
		for (AnnotationAxiom annotationAxiom : annotationAxiomsList) {
			if(annotationAxiom.equals(tempAnnotationAxiom)){
				propertyNode.addAnnotation(annotationAxiom.getAnnotationProperty(), annotationAxiom.getAnnotationPropertyValue());
			}
		}
	}
}
