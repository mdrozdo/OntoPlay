import fakes.FakeOntoplayConfig;
import junit.framework.Assert;
import ontoplay.Module;
import ontoplay.OntoplayConfig;
import ontoplay.models.ConfigurationException;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlElement;
import ontoplay.models.ontologyReading.OntologyReader;
import ontoplay.models.ontologyReading.OntologyReaderFactory;
import ontoplay.models.ontologyReading.jena.FolderMapping;
import ontoplay.models.properties.OwlObjectProperty;
import org.apache.jena.ontology.Ontology;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class JenaKBGridTest {
    private OntologyReader kb;
    private OntologyReaderFactory kbFactory;

    @BeforeClass
    public static void classSetup() {
        XMLUnit.setControlParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        XMLUnit.setTestParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        XMLUnit.setSAXParserFactory("org.apache.xerces.jaxp.SAXParserFactoryImpl");
        //XMLUnit.setTransformerFactory("org.apache.xalan.processor.TransformerFactoryImpl");
        XMLUnit.setIgnoreWhitespace(true);
    }

    @Before
    public void setup() throws ConfigurationException {
        OntoplayConfig config = new FakeOntoplayConfig();

        Injector injector = new GuiceInjectorBuilder()
                .bindings(new Module(config))
                .injector();

        kbFactory = injector.instanceOf(OntologyReaderFactory.class);
        kb = kbFactory.create("./AiGGridOntology.owl", Arrays.asList(new FolderMapping("http://purl.org/NET/cgo", "./cgo.owl")), true);
    }

    @Test
    public void getClass_ReturnsClassFromImportedOntology() {
        OntoClass wnClass = kb.getOwlClass("http://purl.org/NET/cgo#WorkerNode");
        assertThat(wnClass).isNotNull();
        Assert.assertEquals("WorkerNode", wnClass.getLocalName());
        Assert.assertEquals("http://purl.org/NET/cgo#", wnClass.getNamespace());
    }

    @Test
    public void getClass_ReturnsClassWithPropertiesFromImportedOntology() throws Exception {
        OntoClass memoryClass = kb.getOwlClass("http://gridagents.sourceforge.net/AiGGridOntology#PhysicalMemory");

        assertThat(memoryClass).isNotNull();
        assertThat(selectLocalNames(memoryClass.getProperties())).containsOnly(
                "hasModelName", "hasName", "hasID", "belongToVO", "hasTotalSize", "hasAvailableSize");
    }

    @Test
    public void forComputingElement_getClass_ReturnsClassWithStorageSpaceProperty() throws Exception {
        OntoClass memoryClass = kb.getOwlClass("http://purl.org/NET/cgo#ComputingElement");

        assertThat(selectLocalNames(memoryClass.getProperties())).contains(
                "hasStorageSpace");
    }

    @Test
    public void forInstalledSoftware_getIndividualsInRange_ReturnsCorrectResults() throws Exception {
        OntoClass owlClass = new OntoClass("http://purl.org/NET/cgo#", "WorkerNode", new ArrayList<OntoProperty>(), null);
        OwlObjectProperty property = new OwlObjectProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasInstalledSoftware", null);

        assertThat(selectLocalNames(kb.getIndividualsInRange(owlClass, property))).containsExactly("debian_5.0", "MPI16", "Scientific_Linux_303", "vista_sp2");
    }


    @Test
    public void forHasNameProperty_getProperty_works() throws ConfigurationException {
        OntoProperty prop = kb.getProperty("http://purl.org/NET/cgo#hasName");
        assertThat(prop).isNotNull();
    }

    @Test
    public void for_Unknown_Property_getProperty_Throws() {
        try {
            OntoProperty prop = kb.getProperty("http://purl.org/NET/cgo#notExistantProperty");
            Assert.fail("Expected exception to be thrown");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(ConfigurationException.class);
        }
    }

    @Test
    public void withoutIgnoreEmptyDomains_getClass_includesPropertyWithNoDomain() throws Exception {

        OntologyReader specialReader = kbFactory.create("./AiGGridOntology.owl", Arrays.asList(new FolderMapping("http://purl.org/NET/cgo", "./cgo.owl")), false);

        OntoClass memoryClass = specialReader.getOwlClass("http://purl.org/NET/cgo#CPU");

        assertThat(memoryClass).isNotNull();
        assertThat(selectLocalNames(memoryClass.getProperties())).contains(
                "model");
    }

    @Test
    public void withoutIgnoreEmptyDomains_getClass_includesPropertiesWithCorrectDomain() throws Exception {
        OntologyReader specialReader = kbFactory.create("./AiGGridOntology.owl", Arrays.asList(new FolderMapping("http://purl.org/NET/cgo", "./cgo.owl")), false);

        OntoClass memoryClass = specialReader.getOwlClass("http://purl.org/NET/cgo#CPU");

        assertThat(memoryClass).isNotNull();
        assertThat(selectLocalNames(memoryClass.getProperties())).contains(
                "availableNum");
    }

    @Test
    public void withoutIgnoreEmptyDomains_getClass_excludesPropertyWithIncorrectDomain() throws Exception {
        OntologyReader specialReader = kbFactory.create("./AiGGridOntology.owl", Arrays.asList(new FolderMapping("http://purl.org/NET/cgo", "./cgo.owl")), false);

        OntoClass memoryClass = specialReader.getOwlClass("http://purl.org/NET/cgo#CPU");

        assertThat(memoryClass).isNotNull();
        assertThat(selectLocalNames(memoryClass.getProperties())).excludes(
                "descriptor");
    }


    private List<String> selectLocalNames(List<? extends OwlElement> properties) {
        List<String> names = new ArrayList<String>(properties.size());

        for (OwlElement prop : properties) {
            System.out.println(prop.getLocalName());
            names.add(prop.getLocalName());
        }

        return names;
    }


}
