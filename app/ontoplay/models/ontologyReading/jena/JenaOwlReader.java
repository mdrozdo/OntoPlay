package ontoplay.models.ontologyReading.jena;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.*;

import com.google.inject.assistedinject.Assisted;
import org.apache.jena.util.FileManager;
import ontoplay.OntoplayConfig;
import openllet.jena.PelletReasonerFactory;

import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;

import ontoplay.models.ConfigurationException;
import ontoplay.models.InvalidConfigurationException;
import ontoplay.models.PropertyValueCondition;
import ontoplay.models.angular.AnnotationDTO;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlIndividual;
import ontoplay.models.ontologyReading.OntologyReader;
import ontoplay.models.ontologyReading.jena.propertyFactories.AnnotationDataPropertyFactory;
import ontoplay.models.ontologyReading.jena.propertyFactories.DateTimePropertyFactory;
import ontoplay.models.ontologyReading.jena.propertyFactories.FloatPropertyFactory;
import ontoplay.models.ontologyReading.jena.propertyFactories.IntegerPropertyFactory;
import ontoplay.models.ontologyReading.jena.propertyFactories.ObjectPropertyFactory;
import ontoplay.models.ontologyReading.jena.propertyFactories.StringPropertyFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

public class JenaOwlReader implements OntologyReader{
	private final OntoplayConfig config;
	private OntModel model;
	private String ontologyNamespace;
	private boolean ignorePropsWithNoDomain;
	private OwlPropertyFactory owlPropertyFactory;

	@Inject
	public JenaOwlReader(OwlPropertyFactory owlPropertyFactory, OntoplayConfig config, @Assisted boolean ignorePropsWithNoDomain) {
		this.owlPropertyFactory = owlPropertyFactory;
		this.config = config;
		this.ignorePropsWithNoDomain = ignorePropsWithNoDomain;

		config.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				readModel();
			}
		});

		readModel();
	}

	private void readModel() {
		OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		List<FolderMapping> localMappings = config.getMappings();
		if (localMappings != null) {
			OntDocumentManager dm = model.getDocumentManager();

			for (FolderMapping mapping : localMappings) {
				dm.addAltEntry(mapping.getUri(), mapping.getFolderPath());
			}
		}

		String filePath = config.getOntologyFilePath();
		try(InputStream fileStream = FileManager.get().open(filePath)){
			model.read(fileStream, null);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error when reading the file from: "+filePath, e);
		}

		this.model = model;
		String namespace = model.getNsPrefixURI("");
		this.ontologyNamespace = namespace.substring(0, namespace.length() - 1);
	}


	/* (non-Javadoc)
	 * @see models.ontologyReading.jena.OntologyReader#getOwlClass(java.lang.String)
	 */
	@Override
	public OntoClass getOwlClass(String className) {
		if (!(className.contains("#"))&& !(className.contains(":"))) {
			className = String.format("%s#%s", ontologyNamespace, className);
		}
		OntClass ontClass = model.getOntClass(className);
		if (ontClass == null)
			return null;

		OntoClass owlClass = new OntoClass(ontClass.getNameSpace(), ontClass.getLocalName(), getProperties(ontClass), ontClass);
		return owlClass;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see models.PropertyProvider#getProperty(java.lang.String)
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * models.ontologyReading.jena.OntologyReader#getProperty(java.lang.String)
	 */
	@Override
	public OntoProperty getProperty(String propertyUri) throws ConfigurationException {
		OntProperty ontProperty = model.getOntProperty(propertyUri);
		if (ontProperty == null) {
			throw new ConfigurationException(String.format("Property %s not found in ontology", propertyUri));
		}

		return createProperty(ontProperty);
	}

	private List<OntoProperty> getProperties(OntClass ontClass) {

		List<OntoProperty> props = new ArrayList<OntoProperty>();
		System.out.println("get properties for: " + ontClass.getLocalName());
		for (Iterator<OntProperty> i = ontClass.listDeclaredProperties(); i.hasNext();) {
			OntProperty prop = i.next();

			if (prop.getDomain() != null || !ignorePropsWithNoDomain)
				try {
					OntoProperty temp = createProperty(prop);
					if (temp != null)
						props.add(temp);
				} catch (InvalidConfigurationException ex) {
					ex.printStackTrace();
				}
		}

		// The below code should not be needed, but it may depend on the
		// reasoner used (how smart it is).
		// for (Iterator<OntProperty> i = model.listOntProperties();
		// i.hasNext();) {
		// OntProperty prop = i.next();
		//
		// if (isInUnionDomain(ontClass, prop)) {
		// props.add(createProperty(prop));
		// }
		// }
		return props;
	}

	private OntoProperty createProperty(OntProperty prop) {
		return owlPropertyFactory.createProperty(prop);
	}

	/* (non-Javadoc)
	 * @see models.ontologyReading.jena.OntologyReader#getIndividualsInRange(models.ontologyModel.OntoClass, ontoplay.models.ontologyModel.OntoProperty)
	 */
	@Override
	public List<OwlIndividual> getIndividualsInRange(OntoClass owlClass, OntoProperty property) {
		List<OwlIndividual> individuals = new ArrayList<OwlIndividual>();

		for (OntClass rangeClass : getAllClassesFromRange(property)) {
			for (ExtendedIterator<? extends OntResource> i = rangeClass.listInstances(); i.hasNext();) {
				Individual individual = i.next().asIndividual();

				OwlIndividual owlIndividual = new OwlIndividual(individual, new ArrayList<PropertyValueCondition>());
				if (!individuals.contains(owlIndividual))
					individuals.add(owlIndividual);
			}
		}
		return individuals;
	}

	private List<OntClass> getAllClassesFromRange(OntoProperty property) {
		List<OntClass> classes = new ArrayList<OntClass>();

		OntProperty ontProp = model.getOntProperty(property.getUri());
		for (ExtendedIterator<? extends OntResource> r = ontProp.listRange(); r.hasNext();) {
			OntResource res = r.next();
			if (ontProp.hasRange(res)) {
				if (res.isClass()) {
					OntClass rangeClass = res.asClass();

					classes.add(rangeClass);
					for (ExtendedIterator<? extends OntResource> s = rangeClass.listSubClasses(); s.hasNext();) {
						OntClass subclass = s.next().asClass();
						if (!subclass.getURI().equals("http://www.w3.org/2002/07/owl#Nothing"))
							classes.add(subclass);
					}
				}
			}
		}
		return classes;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see models.ontologyReading.jena.OntologyReader#getClassesInRange(models.
	 * ontologyModel.OntoClass, ontoplay.models.ontologyModel.OntoProperty)
	 */
	@Override
	public List<OntoClass> getClassesInRange(OntoProperty property) {
		List<OntoClass> classes = new ArrayList<OntoClass>();

		OntProperty ontProp = model.getOntProperty(property.getUri());
		for (ExtendedIterator<? extends OntResource> r = ontProp.listRange(); r.hasNext();) {
			OntResource res = r.next();
			if (ontProp.hasRange(res)) {
				if (res.isClass()) {
					OntClass rangeClass = res.asClass();

					classes.add(new OntoClass(rangeClass));
					fillWithSubClasses(classes, rangeClass);
				}
			}
		}
		return classes;
	}

	private void fillWithSubClasses(List<OntoClass> classes, OntClass superClass) {
		for (ExtendedIterator<? extends OntResource> s = superClass.listSubClasses(true); s.hasNext();) {
			OntClass subclass = s.next().asClass();
			if (!subclass.getURI().equals("http://www.w3.org/2002/07/owl#Nothing")) {
				classes.add(new OntoClass(subclass, superClass));
				fillWithSubClasses(classes, subclass);
			}
		}
	}

	@Override
	public List<OwlIndividual> getIndividuals(OntoClass owlClass) {
		OntClass ontClass = model.getOntClass(owlClass.getUri());
		List<OwlIndividual> individuals = new ArrayList<OwlIndividual>();

		for (ExtendedIterator<? extends OntResource> i = ontClass.listInstances(); i.hasNext();) {
			Individual individual = i.next().asIndividual();

			if (individual.getURI() == null)
				continue;

			OwlIndividual owlIndividual = new OwlIndividual(individual, new ArrayList<PropertyValueCondition>());
			if (!individuals.contains(owlIndividual))
				individuals.add(owlIndividual);
		}

		return individuals;
	}

	@Override
	public OwlIndividual getIndividual(String name) {
		Individual individual = model.getIndividual(name);
		OwlIndividual owlIndividual = new OwlIndividual(individual, new ArrayList<PropertyValueCondition>());
		return owlIndividual;
	}

	@Override
	public Set<AnnotationDTO> getAnnotations(boolean isOnlyFromNameSpace) {
		ExtendedIterator<AnnotationProperty> ei = model.listAnnotationProperties();
		Set<AnnotationDTO> annotations = new HashSet<AnnotationDTO>();
		while (ei.hasNext()) {
			AnnotationProperty temp = ei.next();
			if (temp.getURI().indexOf(this.ontologyNamespace) > -1 && isOnlyFromNameSpace) {
				annotations.add(new AnnotationDTO(temp.getURI(), temp.getLocalName()));
			}
			if (!isOnlyFromNameSpace) {
				annotations.add(new AnnotationDTO(temp.getURI(), temp.getLocalName()));
			}
		}

		return annotations;
	}

	@Override
	public Set<AnnotationProperty> getAnnotations() {
		ExtendedIterator<AnnotationProperty> ei = model.listAnnotationProperties();
		Set<AnnotationProperty> annotations = new HashSet<AnnotationProperty>();
		while (ei.hasNext()) {
			annotations.add(ei.next());
		}

		return annotations;
	}

	@Override
	public ResIterator getAnnotationsAxioms() {
		return model.listSubjectsWithProperty( RDF.type, OWL2.Axiom );
	}

	@Override
	public String getOntologyNamespace() {
		return ontologyNamespace;
	}

	@Override
	public List<OntoClass> getClasses() {
		List<OntoClass> classes = new ArrayList<>();
		OntClass thing = model.getOntClass("http://www.w3.org/2002/07/owl#Thing");

		fillWithSubClasses(classes, thing);

		return classes;
	}
}
