import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;
import ontoplay.models.ClassCondition;
import ontoplay.models.InvalidConfigurationException;
import ontoplay.models.PropertyValueCondition;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlElement;
import ontoplay.models.ontologyModel.OwlIndividual;
import ontoplay.models.ontologyReading.OntologyReader;
import ontoplay.models.ontologyReading.jena.JenaOwlReader;
import ontoplay.models.ontologyReading.owlApi.OwlApiReader;
import ontoplay.models.properties.IntegerProperty;
import ontoplay.models.properties.OwlObjectProperty;
import ontoplay.models.propertyConditions.ClassValueCondition;
import ontoplay.models.propertyConditions.DatatypePropertyCondition;
import ontoplay.models.propertyConditions.IndividualValueCondition;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

@Ignore("OWL API reader is no longer maintained")
public class OwlApiReaderGridTest {
	private OwlApiReader kb;

	@BeforeClass
	public static void classSetup(){
		XMLUnit.setControlParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		XMLUnit.setTestParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		XMLUnit.setSAXParserFactory("org.apache.xerces.jaxp.SAXParserFactoryImpl");
		//XMLUnit.setTransformerFactory("org.apache.xalan.processor.TransformerFactoryImpl");
		XMLUnit.setIgnoreWhitespace(true);
	}

	@Before
	public void setup() {
		try {
			new OwlApiReaderConfiguration().doJob();
			new PropertyTypeConfiguration().doJob();
			kb = (OwlApiReader) OntologyReader.getGlobalInstance();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
		//TODO: Fix test
		assertThat(selectLocalNames(memoryClass.getProperties())).containsOnly(
				"hasName", "hasID", "belongToVO", "hasTotalSize", "hasAvailableSize");
	}

	@Test
	public void getClass_ReturnsClassWithIntegerProperty() throws Exception {
		OntoClass memoryClass = kb.getOwlClass("http://gridagents.sourceforge.net/AiGGridOntology#PhysicalMemory");

		assertThat(memoryClass).isNotNull();

		String localName = "hasAvailableSize";
		OntoProperty integerProperty = memoryClass.findProperty(localName);

		assertThat(integerProperty).as("integerProperty").isNotNull();
		assertThat(integerProperty).isInstanceOf(IntegerProperty.class);
	}

	@Test
	public void getClass_ReturnsClassWithObjectProperty() throws Exception {
		OntoClass classFromOntology = kb.getOwlClass("http://purl.org/NET/cgo#WorkerNode");

		assertThat(classFromOntology).isNotNull();

		String localName = "hasMemory";
		OntoProperty property = classFromOntology.findProperty(localName);

		assertThat(property).as("objectProperty").isNotNull();
		assertThat(property).isInstanceOf(OwlObjectProperty.class);
	}

	@Test
	public void forComputingElement_getClass_ReturnsClassWithStorageSpaceProperty() throws Exception {
		OntoClass computingElement = kb.getOwlClass("http://purl.org/NET/cgo#ComputingElement");

		assertThat(computingElement).isNotNull();
		assertThat(selectLocalNames(computingElement.getProperties())).contains(
				"hasStorageSpace");
	}

	@Test
	public void forWorkerNode_hasMemory_getClassesInRange_ReturnsClassAndSubClasses() throws Exception{
		OntoClass workerNodeClass = kb.getOwlClass("http://purl.org/NET/cgo#WorkerNode");

		assertThat(workerNodeClass).isNotNull();

		String localName = "hasMemory";
		OntoProperty hasMemoryProperty = workerNodeClass.findProperty(localName);

		 List<OntoClass> actualClasses = kb.getClassesInRange(workerNodeClass, hasMemoryProperty);

		 assertThat(selectLocalNames(actualClasses)).containsOnly(
					"Memory", "PhysicalMemory", "VirtualMemory");
	}

	@Test
	public void forInstalledSoftware_getIndividualsInRange_ReturnsCorrectResults() throws Exception {
		OntoClass owlClass = new OntoClass("http://purl.org/NET/cgo#", "WorkerNode", new ArrayList<OntoProperty>());
		OwlObjectProperty property = new OwlObjectProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasInstalledSoftware");

		assertThat(selectLocalNames(kb.getIndividualsInRange(owlClass, property))).containsExactly("vista_sp2", "debian_5.0", "Scientific_Linux_303");
	}

	@Test
	public void forHasNameProperty_getProperty_works(){
		OntoProperty prop = kb.getProperty("http://purl.org/NET/cgo#hasName");
		assertThat(prop).isNotNull();
	}

	private List<String> selectLocalNames(List<? extends OwlElement> properties) {
		if(properties == null){
			return new ArrayList<String>();
		}

		List<String> names = new ArrayList<String>(properties.size());

		for (OwlElement prop : properties) {
			names.add(prop.getLocalName());
		}

		return names;
	}

	private OwlIndividual getByUri(String uri, List<OwlIndividual> individuals) {
		for (OwlIndividual i : individuals) {
			if(i.getUri().equals(uri)){
				return i;
			}
		}
		return null;
	}

	private DatatypePropertyCondition createEqualToDatatypeCondition(String propertyUri, String propertyValue) {
		return new DatatypePropertyCondition(propertyUri, "equalTo", propertyValue);
	}
}
