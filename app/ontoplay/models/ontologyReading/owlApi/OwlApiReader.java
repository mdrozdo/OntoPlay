package ontoplay.models.ontologyReading.owlApi;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ontoplay.models.PropertyValueCondition;
import ontoplay.models.angular.AnnotationDTO;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlIndividual;
import ontoplay.models.ontologyReading.OntologyReader;
import ontoplay.models.ontologyReading.jena.FolderMapping;
import ontoplay.models.ontologyReading.owlApi.propertyFactories.DateTimePropertyFactory;
import ontoplay.models.ontologyReading.owlApi.propertyFactories.FloatPropertyFactory;
import ontoplay.models.ontologyReading.owlApi.propertyFactories.IntegerPropertyFactory;
import ontoplay.models.ontologyReading.owlApi.propertyFactories.ObjectPropertyFactory;
import ontoplay.models.ontologyReading.owlApi.propertyFactories.StringPropertyFactory;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import com.hp.hpl.jena.shared.ConfigException;
import uk.ac.manchester.cs.owl.owlapi.OWLOntologyIRIMapperImpl;

import javax.inject.Singleton;

@Singleton
public class OwlApiReader implements OntologyReader {
	private final OWLOntology ontology;
	private final OWLReasoner reasoner;
	private final OWLOntologyManager manager;
	private final OWLDataFactory factory;
	private String uri;
	private boolean ignorePropsWithNoDomain;

	public OwlApiReader(String uri, List<FolderMapping> localMappings, boolean ignorePropsWithNoDomain) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntologyIRIMapperImpl iriMapper = new OWLOntologyIRIMapperImpl();
		for (FolderMapping mapping : localMappings) {
			iriMapper.addMapping(IRI.create(mapping.getUri()), IRI.create(mapping.getFolderPath()));
		}
		manager.addIRIMapper(iriMapper);
		OWLOntology ontology = null;
		try {
			ontology = manager.loadOntologyFromOntologyDocument(new File(uri));
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		Configuration hermitConfig = new Configuration();
		hermitConfig.ignoreUnsupportedDatatypes = true;
		Reasoner hermitReasoner = new Reasoner(hermitConfig, ontology);

		this.manager = manager;
		this.ontology = ontology;
		this.reasoner = hermitReasoner;
		this.ignorePropsWithNoDomain = ignorePropsWithNoDomain;
		this.factory = manager.getOWLDataFactory();
		this.uri = ontology.getOntologyID().getOntologyIRI().toString();

		//TODO:Make this non static and move initialization to module.
		OwlPropertyFactory.registerPropertyFactory(new IntegerPropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new FloatPropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new DateTimePropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new StringPropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new ObjectPropertyFactory());		
	}

	@Override
	public List<OntoClass> getClassesInRange( OntoProperty property) {
		List<OntoClass> classes = new ArrayList<OntoClass>();

		for (OWLClass ontClass : getAllClassesFromRange(property)) {
			classes.add(new OntoClass(ontClass.getIRI(), new ArrayList()));
		}
		return classes;
	}

	private List<OWLClass> getAllClassesFromRange(OntoProperty property) {
		List<OWLClass> classes = new ArrayList<OWLClass>();

		OWLObjectProperty prop = factory.getOWLObjectProperty(IRI.create(property.getUri()));
		
		NodeSet<OWLClass> propertyRanges = reasoner.getObjectPropertyRanges(prop, true);		
		
		for (Node<OWLClass> rangeNode : propertyRanges) {
			
			OWLClass rangeClass = rangeNode.getRepresentativeElement();
			classes.add(rangeClass);
			NodeSet<OWLClass> subClasses = reasoner.getSubClasses(rangeClass, false);
			for(Node<OWLClass> subClassNode : subClasses){
				OWLClass subClass = subClassNode.getRepresentativeElement();
				if (!subClass.getIRI().toString().equals("http://www.w3.org/2002/07/owl#Nothing"))
					classes.add(subClass);
			}
		}		

		return classes;
	}

	@Override
	public List<OwlIndividual> getIndividualsInRange(OntoClass owlClass, OntoProperty property) {
		List<OwlIndividual> individuals = new ArrayList<OwlIndividual>();

		for (OWLClass rangeClass : getAllClassesFromRange(property)) {
			NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(rangeClass, false);
			for (Node<OWLNamedIndividual> instance : instances) {
				OWLNamedIndividual individual = instance.getRepresentativeElement();
				
				OwlIndividual owlIndividual = new OwlIndividual(individual, new ArrayList<PropertyValueCondition>());
				if (!individuals.contains(owlIndividual))
					individuals.add(owlIndividual);
			}			
		}
		return individuals;
	}

	@Override
	public OntoClass getOwlClass(String className) {
		if (!(className.contains("#"))) {
			className = String.format("%s#%s", uri, className);
		}
		
		OWLClass owlClass = factory.getOWLClass(IRI.create(className));
		
		List<OntoProperty> classProperties = getClassProperties(owlClass); 
		
		return new OntoClass(owlClass.getIRI().getStart(), owlClass.getIRI().getFragment(), classProperties, null);		
	}

	private List<OntoProperty> getClassProperties(OWLClass owlClass) {
		List<OntoProperty> classProperties = new ArrayList<OntoProperty>();
		Set<OWLProperty> allProperties = new HashSet<OWLProperty>();
		allProperties.addAll(ontology.getDataPropertiesInSignature(true));
		allProperties.addAll(ontology.getObjectPropertiesInSignature(true));
		
		for (Iterator iterator = allProperties.iterator(); iterator.hasNext();) {
			OWLProperty owlProperty = (OWLProperty) iterator.next();
			if(classInRangeOfProperty(owlClass, owlProperty)){
				OntoProperty ontoProperty = OwlPropertyFactory.createOwlProperty(ontology, owlProperty);
				classProperties.add(ontoProperty);
			}
		}
		return classProperties;
	}

	private boolean classInRangeOfProperty(OWLClass owlClass, OWLProperty owlProperty) {
		Set<OWLClassExpression> topLevelDomains = new HashSet<OWLClassExpression>();
		
		// OWL doesn't specify that operands of a unionOf domain should be regarded as property domains, 
		// so additionally we have to also parse the domain manually
		Set domains = owlProperty.getDomains(ontology.getImportsClosure());

		for (Iterator iterator = domains.iterator(); iterator.hasNext();) {
			OWLClassExpression domainExpression = (OWLClassExpression) iterator.next();
			
			Set<OWLClassExpression> domainDisjuncts = domainExpression.asDisjunctSet();
			topLevelDomains.addAll(domainDisjuncts);
		}
		
		
		//
//		if(owlProperty.isDataPropertyExpression()){
//			OWLDataProperty dataProperty = (OWLDataProperty) owlProperty;
//			NodeSet<OWLClass> dataDomains = reasoner.getDataPropertyDomains(dataProperty, true);
//			
//			for (Node<OWLClass> domainNode : dataDomains) {
//				topLevelDomains.add(domainNode.getRepresentativeElement());
//			}
//		}
//		else if(owlProperty.isObjectPropertyExpression()){
//			OWLObjectPropertyExpression objectProperty = (OWLObjectPropertyExpression) owlProperty;
//			NodeSet<OWLClass> objectDomains = reasoner.getObjectPropertyDomains(objectProperty, true);
//			
//			for (Node<OWLClass> domainNode : objectDomains) {
//				topLevelDomains.add(domainNode.getRepresentativeElement());
//			}
//		}
		
		//remove owl:Thing so that we ignore all properties that don't have domains specified 
//		OWLClass topLevelClass = factory.getOWLClass(IRI.create("http://www.w3.org/2002/07/owl#Thing"));			
//		topLevelDomains.remove(topLevelClass);
		
		for (Iterator iterator2 = topLevelDomains.iterator(); iterator2.hasNext();) {
			OWLClassExpression topLevelDomain = (OWLClassExpression) iterator2.next();
			
			if(topLevelDomain.equals(owlClass)){
				return true;
			}
			if (reasoner.getSubClasses(topLevelDomain, false).containsEntity(owlClass)) {
				return true;
			}
		}
	
		return false;
	}

	@Override
	public OntoProperty getProperty(String propertyUri) {
		OWLProperty property = factory.getOWLObjectProperty(IRI.create(propertyUri));
		if(property == null){
			property = factory.getOWLDataProperty(IRI.create(propertyUri));
		}		
		return OwlPropertyFactory.createOwlProperty(ontology, property);
	}



	@Override
	public List<OwlIndividual> getIndividuals(OntoClass owlClass) {
		List<OwlIndividual> individuals = new ArrayList<OwlIndividual>();
		OWLClass temp = factory.getOWLClass(IRI.create(owlClass.getUri()));
		
			NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(temp, false);
			for (Node<OWLNamedIndividual> instance : instances) {
				OWLNamedIndividual individual = instance.getRepresentativeElement();
				
				OwlIndividual owlIndividual = new OwlIndividual(individual, new ArrayList<PropertyValueCondition>());
				if (!individuals.contains(owlIndividual))
					individuals.add(owlIndividual);
			}			
		
		return individuals;
	}

	@Override
	public OwlIndividual getIndividual(String name) {
		OWLNamedIndividual individual=factory.getOWLNamedIndividual(IRI.create(name));
		OwlIndividual owlIndividual = new OwlIndividual(individual, new ArrayList<PropertyValueCondition>());
		return owlIndividual;
	}

	@Override
	public Set<AnnotationDTO> getAnnotations(boolean isFromNameSpace) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
