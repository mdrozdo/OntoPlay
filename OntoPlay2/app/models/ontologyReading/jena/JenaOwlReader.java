package models.ontologyReading.jena;

import java.io.Console;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import models.InvalidConfigurationException;
import models.PropertyProvider;
import models.PropertyValueCondition;
import models.ontologyModel.OntoClass;
import models.ontologyModel.OntoProperty;
import models.ontologyModel.OwlIndividual;
import models.ontologyReading.OntologyReader;
import models.propertyConditions.ClassValueCondition;
import models.propertyConditions.DatatypePropertyCondition;
import models.propertyConditions.IndividualValueCondition;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import play.db.DB;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.HasValueRestriction;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.IntersectionClass;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;
import com.hp.hpl.jena.ontology.UnionClass;
import com.hp.hpl.jena.ontology.OntDocumentManager.ReadFailureHandler;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

public class JenaOwlReader extends OntologyReader{
	private OntModel model;
	private String uri;
	private boolean ignorePropsWithNoDomain;

	public static void initialize(String uri, JenaOwlReaderConfig config) {
		setGlobalInstance(loadFromFile(uri, config));
	}

	public static JenaOwlReader loadFromFile(String uri, JenaOwlReaderConfig config) {
		OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		if (config != null) {
			OntDocumentManager dm = model.getDocumentManager();
			for (Map.Entry<String, String> mapping : config.getLocalMappings().entrySet()) {
				dm.addAltEntry(mapping.getKey(), mapping.getValue());
			}
		}

		model.read(uri);

		return new JenaOwlReader(model, config);
	}

	public static OntologyReader loadFromFile(String uri) {
		return loadFromFile(uri, null);
	}

	private JenaOwlReader(OntModel model) {
		this.model = model;
		String namespace = model.getNsPrefixURI("");
		this.uri = namespace.substring(0, namespace.length() - 1);
	}

	public JenaOwlReader(OntModel model, JenaOwlReaderConfig config) {
		this(model);
		if (config != null) {
			ignorePropsWithNoDomain = config.isIgnorePropsWithNoDomain();
		}
	}

	/* (non-Javadoc)
	 * @see models.ontologyReading.jena.OntologyReader#getOwlClass(java.lang.String)
	 */
	public OntoClass getOwlClass(String className) {
		if (!(className.contains("#"))) {
			className = String.format("%s#%s", uri, className);
		}
		OntClass ontClass = model.getOntClass(className);
		if (ontClass == null)
			return null;

		OntoClass owlClass = new OntoClass(ontClass.getNameSpace(), ontClass.getLocalName(), getProperties(ontClass));
		return owlClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see models.PropertyProvider#getProperty(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see models.ontologyReading.jena.OntologyReader#getProperty(java.lang.String)
	 */
	public OntoProperty getProperty(String propertyUri) {
		return createProperty(model.getOntProperty(propertyUri));
	}

	private List<OntoProperty> getProperties(OntClass ontClass) {
		List<OntoProperty> props = new ArrayList<OntoProperty>();
		System.out.println("get properties for: " + ontClass.getLocalName());
		for (Iterator<OntProperty> i = ontClass.listDeclaredProperties(); i.hasNext();) {
			OntProperty prop = i.next();
			if (prop.getDomain() != null || !ignorePropsWithNoDomain)
				try {
					props.add(createProperty(prop));
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
		return OwlPropertyFactory.createOwlProperty(prop);
	}

	/* (non-Javadoc)
	 * @see models.ontologyReading.jena.OntologyReader#getIndividualsInRange(models.ontologyModel.OntoClass, models.ontologyModel.OntoProperty)
	 */
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

	/* (non-Javadoc)
	 * @see models.ontologyReading.jena.OntologyReader#getClassesInRange(models.ontologyModel.OntoClass, models.ontologyModel.OntoProperty)
	 */
	public List<OntoClass> getClassesInRange(OntoClass owlClass, OntoProperty property) {
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

	private void fillWithSubClasses(List<OntoClass> classes, OntClass superClass){
		for (ExtendedIterator<? extends OntResource> s = superClass.listSubClasses(true); s.hasNext();) {
			OntClass subclass = s.next().asClass();
			if (!subclass.getURI().equals("http://www.w3.org/2002/07/owl#Nothing")){
				classes.add(new OntoClass(subclass, superClass));
				fillWithSubClasses(classes, subclass);
			}
		}
	}
}
