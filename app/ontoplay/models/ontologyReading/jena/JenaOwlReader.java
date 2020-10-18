package ontoplay.models.ontologyReading.jena;

import com.google.common.collect.Iterators;
import com.google.inject.assistedinject.Assisted;
import ontoplay.OntoplayConfig;
import ontoplay.models.ConfigurationException;
import ontoplay.models.InvalidConfigurationException;
import ontoplay.models.PropertyValueCondition;
import ontoplay.models.dto.AnnotationDTO;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlElement;
import ontoplay.models.ontologyModel.OwlIndividual;
import ontoplay.models.ontologyReading.OntologyReader;
import openllet.core.OpenlletOptions;
import openllet.jena.PelletReasonerFactory;
import org.apache.jena.ext.com.google.common.collect.Streams;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JenaOwlReader implements OntologyReader {
    private final boolean ignorePropsWithNoDomain;
    private final OwlPropertyFactory owlPropertyFactory;
    private OntModel model;
    private String ontologyNamespace;
    private OntoplayConfig config;

    @Inject
    public JenaOwlReader(OwlPropertyFactory owlPropertyFactory, OntoplayConfig config, @Assisted boolean ignorePropsWithNoDomain) {
        this.owlPropertyFactory = owlPropertyFactory;
        this.ignorePropsWithNoDomain = ignorePropsWithNoDomain;

        this.config = config;
        this.config.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                readModel(((OntoplayConfig) o).getOntologyFilePath(), ((OntoplayConfig) o).getOntologyNamespace(), ((OntoplayConfig) o).getMappings());
            }
        });

        readModel(config.getOntologyFilePath(), config.getOntologyNamespace(), config.getMappings());
    }

    public JenaOwlReader(OwlPropertyFactory owlPropertyFactory, String filePath, String ontologyNamespace, List<FolderMapping> localMappings, boolean ignorePropsWithNoDomain) {
        this.owlPropertyFactory = owlPropertyFactory;
        this.ignorePropsWithNoDomain = ignorePropsWithNoDomain;

        readModel(filePath, ontologyNamespace, localMappings);
    }

    private void readModel(String filePath, String ontologyNamespace, List<FolderMapping> localMappings) {
        //OpenlletOptions.USE_TRACING = true;
        OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
        if (localMappings != null) {
            OntDocumentManager dm = model.getDocumentManager();

            for (FolderMapping mapping : localMappings) {
                dm.addAltEntry(mapping.getUri(), mapping.getFolderPath());
            }
        }

        try (InputStream fileStream = FileManager.get().open(filePath)) {
            model.read(fileStream, null);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error when reading the file from: " + filePath, e);
        }

        this.model = model;
        this.ontologyNamespace = ontologyNamespace;
    }


    /* (non-Javadoc)
     * @see models.ontologyReading.jena.OntologyReader#getOwlClass(java.lang.String)
     */
    @Override
    public OntoClass getOwlClass(String className) {
        className = prependNamespaceIfNecessary(className);
        OntClass ontClass = model.getOntClass(className);
        if (ontClass == null)
            return null;

        OntoClass owlClass = new OntoClass(ontClass.getNameSpace(), ontClass.getLocalName(), getProperties(ontClass), ontClass);
        return owlClass;
    }

    private String prependNamespaceIfNecessary(String nameOrUri) {
        if (!(nameOrUri.contains("#")) && !(nameOrUri.contains(":"))) {
            nameOrUri = String.format("%s#%s", ontologyNamespace, nameOrUri);
        }
        return nameOrUri;
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

        return owlPropertyFactory.createProperty(ontProperty, new ArrayList<OwlElement>(0));
    }

    private List<OntoProperty> getProperties(OntClass ontClass) {

        System.out.println("get properties for: " + ontClass.getLocalName());

        var properties = ontClass.listDeclaredProperties()
                //.filterKeep(prop -> prop.getDomain() != null || !ignorePropsWithNoDomain)
                .mapWith(prop -> {
                    try {
                        return createProperty(ontClass, prop);
                    } catch (InvalidConfigurationException ex) {
                        ex.printStackTrace();
                        return null;
                    }
                })
                .filterDrop(prop -> prop == null)
                .toList();

        if (properties.size() > 0) {
            var relevanceRanking = calculateRelevanceRanking(properties);

            for (var prop : properties) {
                prop.setRelevance(relevanceRanking.get(prop.getUri()));
            }
        }

        return properties;

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
    }

    private Map<String, Double> calculateRelevanceRanking(List<OntoProperty> properties) {

//        var domainSizes = properties.stream()
//                .collect( Collectors.toMap(OntoProperty::getUri, p->p.getDomain().size()) );
        var sorted = properties.stream().sorted((p1, p2) -> p1.getDomain().size() - p2.getDomain().size()).collect(Collectors.toList());

        var rankingMap = new HashMap<String, Integer>(properties.size());

        var rank = 0;
        var lastSize = sorted.get(0).getDomain().size();
        for (var prop : sorted) {
            if (prop.getDomain().size() > lastSize) {
                rank++;
                lastSize = prop.getDomain().size();
            }

            rankingMap.put(prop.getUri(), rank);
        }
        var maxRank = rank;

        return rankingMap.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> (maxRank > 0) ? (Double.valueOf(maxRank - e.getValue()) / maxRank) : 1
                        )
                );

    }


    /* (non-Javadoc)
     * @see models.ontologyReading.jena.OntologyReader#getIndividualsInRange(models.ontologyModel.OntoClass, ontoplay.models.ontologyModel.OntoProperty)
     */
    @Override
    public List<OwlIndividual> getIndividualsInRange(OntoClass owlClass, OntoProperty property) {
        List<OwlIndividual> individuals = new ArrayList<OwlIndividual>();

        for (OntClass rangeClass : getAllClassesFromRange(property)) {
            for (ExtendedIterator<? extends OntResource> i = rangeClass.listInstances(); i.hasNext(); ) {
                Individual individual = i.next().asIndividual();

                OwlIndividual owlIndividual = new OwlIndividual(individual, new ArrayList<PropertyValueCondition>());
                if (!individuals.contains(owlIndividual))
                    individuals.add(owlIndividual);
            }
        }
        return individuals;
    }

    private OntoProperty createProperty(OntClass declaringClass, OntProperty prop) {
        List<OwlElement> domainClasses = null;

        if (prop.getDomain() != null) {
            domainClasses = getRdfDomainClasses(declaringClass, prop);

            if (domainClasses == null) {
                // If rdf domain is inapplicable to the declaring class, we shouldn't return the property at all.
                return null;
            }
        }

        if (domainClasses == null) {
            domainClasses = getSchemaOrgDomainClasses(declaringClass, prop);
        }

        if (domainClasses == null) {
            domainClasses = getDomainClassesFromClassRestrictions(declaringClass, prop);
        }

        if (domainClasses == null && !ignorePropsWithNoDomain) {
            //If no domain was specified and schema.org domains not found, grab rdf domain, which should really return
            //all classes.
            domainClasses = getRdfDomainClasses(declaringClass, prop);
        }

        if (domainClasses == null) {
            return null;
        }

        return owlPropertyFactory.createProperty(prop, domainClasses);
    }


    private List<OwlElement> getDomainClassesFromClassRestrictions(OntClass declaringClass, OntProperty property) {
        var classesIterator = property.listReferringRestrictions()
                .mapWith(r -> r.listSubClasses(false)
                        .filterDrop(c -> c.isAnon() || c.getURI().equalsIgnoreCase("http://www.w3.org/2002/07/owl#Nothing")));

        var classes = Streams.stream(Iterators.concat(classesIterator))
                .distinct()
                .collect(Collectors.toList());

        var allClassCount = model.listNamedClasses()
                .filterDrop(c -> c.getURI().equalsIgnoreCase("http://www.w3.org/2002/07/owl#Thing"))
                .filterDrop(c -> c.getURI().equalsIgnoreCase("http://www.w3.org/2002/07/owl#Nothing"))
                .toList()
                .size();

        if (classes.size() < allClassCount && classes.contains(declaringClass)) {
            return classes.stream().map(c -> createOwlClass(c)).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    private OwlElement createOwlClass(OntClass ontClass) {
        return new OntoClass((OntClass) ontClass);
    }


    private List<OwlElement> getSchemaOrgDomainClasses(OntClass declaringClass, OntProperty property) {
        OntModel ontModel = property.getOntModel();
        var schemaDomainProperty = ontModel.getAnnotationProperty("http://schema.org/domainIncludes");
        if (schemaDomainProperty == null) {
            return null;
        }

        List<OntClass> schemaDomainClasses = property.listPropertyValues(schemaDomainProperty)
                .mapWith(res -> ontModel.getOntClass(res.asResource().getURI()))
                .filterKeep(res -> res != null)
                .toList();

        var domainClassDescendants = schemaDomainClasses.stream()
                .flatMap(c -> c.listSubClasses(false).toList().stream())
                .collect(Collectors.toList());

        schemaDomainClasses.addAll(domainClassDescendants);

        if (schemaDomainClasses.contains(declaringClass)) {
            return schemaDomainClasses.stream()
                    .filter(c -> !c.getURI().equalsIgnoreCase("http://www.w3.org/2002/07/owl#Nothing"))
                    .map(c -> createOwlClass(c))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    private List<OwlElement> getRdfDomainClasses(OntClass declaringClass, OntProperty property) {
        List<OwlElement> domainClasses = property.listDeclaringClasses()
                //Only keep named classes. The remaining ones are unions/intersections/class expressions. If these
                // can be reduced to named classes, listDeclaringClasses will return them. Otherwise, we lose some
                // information, but we can still use the domain size as a metric of how generic the property is.
                .filterKeep(res -> !res.isAnon())
                .mapWith(res -> createOwlClass(res))
                .toList();


        // For some reason declaredProperties and listDeclaringClasses sometimes do not match.
        // if the declaring classes is not in the domain of the property, we shouldn't use it.
        if (domainClasses.stream()
                .anyMatch(c -> c.getUri().equalsIgnoreCase(declaringClass.getURI()))) {
            return domainClasses;
        } else {
            return null;
        }
    }

    private List<OntClass> getAllClassesFromRange(OntoProperty property) {
        List<OntClass> classes = new ArrayList<OntClass>();

        OntProperty ontProp = model.getOntProperty(property.getUri());
        for (ExtendedIterator<? extends OntResource> r = ontProp.listRange(); r.hasNext(); ) {
            OntResource res = r.next();
            if (ontProp.hasRange(res)) {
                if (res.isClass()) {
                    OntClass rangeClass = res.asClass();

                    classes.add(rangeClass);
                    for (ExtendedIterator<? extends OntResource> s = rangeClass.listSubClasses(); s.hasNext(); ) {
                        OntClass subclass = s.next().asClass();
                        if (!subclass.getURI().equals("http://www.w3.org/2002/07/owl#Nothing"))
                            classes.add(subclass);
                    }
                }
            }
        }
        return classes;
    }

    private Stream<OntClass> listSubclassesOrSelf(OntClass aClass) {
        return Streams.concat(Stream.of(aClass),
                Streams.stream(aClass.listSubClasses(false))
                        .filter(c -> !c.getURI().equalsIgnoreCase("http://www.w3.org/2002/07/owl#Nothing")));
    }

    /*
     * (non-Javadoc)
     *
     * @see models.ontologyReading.jena.OntologyReader#getClassesInRange(models.
     * ontologyModel.OntoClass, ontoplay.models.ontologyModel.OntoProperty)
     */
    @Override
    public List<OntoClass> getClassesInRange(String domainClassUri, String propertyUri) {
        List<OntClass> classes;

        OntProperty ontProp = model.getOntProperty(propertyUri);

        OntClass ontClass = model.getOntClass(domainClassUri);

        classes = Streams.stream(ontProp.listReferringRestrictions())
                .filter(r -> r.isAllValuesFromRestriction()
                        && r.hasSubClass(ontClass, false)
                        && r.asAllValuesFromRestriction().getAllValuesFrom() instanceof OntClass)
                .flatMap(r -> listSubclassesOrSelf((OntClass) r.asAllValuesFromRestriction().getAllValuesFrom()))
                .filter(r -> !r.isAnon())
                .collect(Collectors.toList());

        if (classes.isEmpty()) {

            if (ontProp.getRange() != null) {
                var range = Streams.stream(ontProp.listRange())
                        .filter(c -> c.isClass())
                        .map(c -> c.asClass())
                        .collect(Collectors.toList());

                var subclasses = range.stream()
                        .flatMap(c -> c
                                .listSubClasses(false)
                                .filterDrop(sc -> sc.getURI().equals("http://www.w3.org/2002/07/owl#Nothing")).toList().stream());

                classes = Streams.concat(range.stream(), subclasses).collect(Collectors.toList());

            } else {
                classes = Streams.stream(model.listNamedClasses()).collect(Collectors.toList());
            }
        }

        return classes.stream().map(c -> new OntoClass((c))).collect(Collectors.toList());
    }


    @Override
    public List<OwlIndividual> getIndividuals(OntoClass owlClass) {
        OntClass ontClass = model.getOntClass(owlClass.getUri());
        List<OwlIndividual> individuals = new ArrayList<OwlIndividual>();

        for (ExtendedIterator<? extends OntResource> i = ontClass.listInstances(); i.hasNext(); ) {
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
        Individual individual = model.getIndividual(prependNamespaceIfNecessary(name));
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
        return model.listSubjectsWithProperty(RDF.type, OWL2.Axiom);
    }

    @Override
    public String getOntologyNamespace() {
        return ontologyNamespace;
    }

    @Override
    public List<OntoClass> getClasses() {
        List<OntoClass> classes = model.listNamedClasses()
                .filterDrop(c -> c.getURI().equals("http://www.w3.org/2002/07/owl#Nothing"))
                .mapWith(c -> new OntoClass(c))
                .toList();

        return classes;
    }

    @Override
    public void refreshModel() {
        readModel(config.getOntologyFilePath(), config.getOntologyNamespace(), config.getMappings());
    }
}
