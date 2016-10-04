package ontoplay.models.angular.update;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.NullArgumentException;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.pfunction.library.concat;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import ontoplay.models.ontologyReading.OntologyReader;

public class IndividualUpdateModel {

	private Individual individual;
	private OntClass ontClass;
	private UpdateIndividualDTO updateIndividual;

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

	public HashMap<String, Resource> functionalObjectValues = new HashMap<String, Resource>();
	public HashMap<String, ArrayList<Resource>> nonFunctionalObjectValues = new HashMap<String, ArrayList<Resource>>();
	public HashMap<String, Literal> functionalDataValues = new HashMap<String, Literal>();
	public HashMap<String, ArrayList<Literal>> nonFunctionalDataValues = new HashMap<String, ArrayList<Literal>>();

	public HashMap<String, Literal> getFunctionalData() {
		return functionalDataValues;
	}

	public IndividualUpdateModel(Individual individual) {
		System.out.println("asdqwe");
		if (individual == null)
			throw new NullArgumentException("inidividual");
		this.individual = individual;
		this.ontClass = individual.getOntClass();
		this.updateIndividual = new UpdateIndividualDTO();
		this.updateIndividual.setIndividualName(individual.getLocalName());
		Initialize();
	}

	public boolean getHasFunctionalDataProperties() {
		return functionalDataValues.size() > 0;
	}

	public boolean getHasNonFunctionalDataProperties() {
		return nonFunctionalObjectValues.size() > 0;
	}

	public boolean getHasFunctionalObjectProperties() {
		return functionalObjectValues.size() > 0;
	}

	public boolean getHasNonFunctionalObjectProperties() {
		return nonFunctionalDataValues.size() > 0;
	}

	private void Initialize() {

		ArrayList<OntProperty> dataProperties = new ArrayList<OntProperty>();
		ArrayList<OntProperty> objectProperties = new ArrayList<OntProperty>();

		ExtendedIterator<OntProperty> properties = ontClass.listDeclaredProperties(true);

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
	}

	private void IntializeNonFunctionalObjectProperties(ArrayList<OntProperty> nonFunctionalObjectProperties) {
		for (OntProperty property : nonFunctionalObjectProperties) {
			NodeIterator it = individual.listPropertyValues(property);
			String name = getPropertyName(property);
			while (it.hasNext()) {
				RDFNode node = it.next();
				Resource res = null;
				if (node.isURIResource())
					res = getResourceFromURI(node);
				else
					res = node.asResource();
				if (!nonFunctionalObjectValues.containsKey(name))
					nonFunctionalObjectValues.put(name, new ArrayList<Resource>());
				nonFunctionalObjectValues.get(name).add(res);
			}
		}

	}

	private void addObjectProperties(ArrayList<OntProperty> functionalObjectProperties) {
		boolean isExisted = false;
		PropertyNode propertyNode = null;
		for (OntProperty property : functionalObjectProperties) {
			isExisted = false;

			NodeIterator it = individual.listPropertyValues(property);
			Resource res = null;
			while (it.hasNext()) {
				propertyNode = new PropertyNode();
				propertyNode.setProperty(property.getURI());
				propertyNode.setInputType("object");
				propertyNode.setClassName(individual.getOntClass().getLocalName());

				RDFNode node = it.next();
				isExisted = true;
				if (node.isURIResource()) {
					res = getResourceFromURI(node);
					propertyNode.setOperator("equalToIndividual");
					if (res != null && res.getURI() != null && res.getURI().length() != 0) {
						propertyNode.setObjectValue(res.getURI());
						propertyNode.setPropertyClass(getIndividualClass(res.getURI()));
					}
				} else {
					res = node.asResource();
				}
				updateIndividual.addProperty(propertyNode);
			}
		}

	}

	private void IntializeNonFunctionalDataProperties(ArrayList<OntProperty> nonFunctionalDataProperties) {

		for (OntProperty property : nonFunctionalDataProperties) {
			NodeIterator it = individual.listPropertyValues(property);
			String name = getPropertyName(property);
			while (it.hasNext()) {
				if (!nonFunctionalDataValues.containsKey(name))
					nonFunctionalDataValues.put(name, new ArrayList<Literal>());
				nonFunctionalDataValues.get(name).add(it.next().asLiteral());
			}
		}
	}

	private void addDataProperties(ArrayList<OntProperty> dataProperties) {
		PropertyNode propertyNode = null;

		for (OntProperty property : dataProperties) {
			NodeIterator it = individual.listPropertyValues(property);
			while (it.hasNext()) {

				System.out.println("1");
				propertyNode = new PropertyNode();
				System.out.println("2");
				propertyNode.setProperty(property.getURI());
				System.out.println("3");
				propertyNode.setOperator("equalTo");
				System.out.println("4");
				propertyNode.setDataValue(it.next().asLiteral().getString());
				System.out.println("5");
				propertyNode.setClassName(individual.getOntClass().getLocalName());

				updateIndividual.addProperty(propertyNode);
			}
		}

	}

	private Resource getResourceFromURI(RDFNode node) {
		return OntologyReader.getGlobalInstance().getIndividual(node.asResource().getURI()).getIndividual();
	}

	private String getPropertyName(OntProperty prop) {
		String name = null;
		name = prop.getLabel("");
		if (name != null)
			return name;
		return prop.getLocalName();
	}

	private String getIndividualClass(String individualUri) {
		try {
			return OntologyReader.getGlobalInstance().getIndividual(individualUri).getIndividual().getOntClass()
					.getLocalName();

		} catch (Exception e) {
			System.out.println("Error getting class for an individual");
			return "";
		}

	}
}
