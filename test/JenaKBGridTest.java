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
import java.util.List;

import jobs.JenaOwlReaderConfiguration;
import jobs.PropertyTypeConfiguration;
import jobs.PropertyTypeConfiguration;
import junit.framework.Assert;
import ontoplay.models.ClassCondition;
import ontoplay.models.ConfigurationException;
import ontoplay.models.InvalidConfigurationException;
import ontoplay.models.PropertyValueCondition;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlElement;
import ontoplay.models.ontologyModel.OwlIndividual;
import ontoplay.models.ontologyReading.OntologyReader;
import ontoplay.models.ontologyReading.jena.JenaOwlReader;
import ontoplay.models.ontologyReading.jena.JenaOwlReaderConfig;
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
import org.junit.Test;

import conf.Config;
import static org.fest.assertions.Assertions.assertThat;

public class JenaKBGridTest {
	private OntologyReader kb;

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
			OntologyReader.setGlobalInstance(null);
			Config.setInstancesLocation("../Ontology/AiGInstances/");
			new JenaOwlReaderConfiguration().doJob();
			new PropertyTypeConfiguration().doJob();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kb = OntologyReader.getGlobalInstance();
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
		OntoClass owlClass = new OntoClass("http://purl.org/NET/cgo#", "WorkerNode", new ArrayList<OntoProperty>());
		OwlObjectProperty property = new OwlObjectProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasInstalledSoftware"); 

		assertThat(selectLocalNames(kb.getIndividualsInRange(owlClass, property))).containsExactly("MPI16", "vista_sp2", "debian_5.0", "Scientific_Linux_303");
	}
	
		
	@Test
	public void forHasNameProperty_getProperty_works() throws ConfigurationException{
		OntoProperty prop = kb.getProperty("http://purl.org/NET/cgo#hasName");
		assertThat(prop).isNotNull();
	}
	
	@Test
	public void for_Unknown_Property_getProperty_Throws(){
		try{
			OntoProperty prop = kb.getProperty("http://purl.org/NET/cgo#notExistantProperty");
			Assert.fail("Expected exception to be thrown");
		}
		catch(Exception ex){
			assertThat(ex).isInstanceOf(ConfigurationException.class);
		}
	}
	
	@Test
	public void withoutIgnoreEmptyDomains_getClass_includesPropertyWithNoDomain() throws Exception {
		JenaOwlReader.initialize("file:" + Config.getOntologyLocation() + "cgo.owl",
				new JenaOwlReaderConfig());
		OntologyReader specialReader = JenaOwlReader.getGlobalInstance();
		OntoClass memoryClass = specialReader.getOwlClass("http://purl.org/NET/cgo#CPU");

		assertThat(memoryClass).isNotNull();
		assertThat(selectLocalNames(memoryClass.getProperties())).contains(
				"model");
	}
	
	@Test
	public void withoutIgnoreEmptyDomains_getClass_includesPropertiesWithCorrectDomain() throws Exception {
		JenaOwlReader.initialize("file:" + Config.getOntologyLocation() + "cgo.owl",
				new JenaOwlReaderConfig());
		OntologyReader specialReader = JenaOwlReader.getGlobalInstance();
		OntoClass memoryClass = specialReader.getOwlClass("http://purl.org/NET/cgo#CPU");

		assertThat(memoryClass).isNotNull();
		assertThat(selectLocalNames(memoryClass.getProperties())).contains(
				"availableNum");
	}
	
	@Test
	public void withoutIgnoreEmptyDomains_getClass_excludesPropertyWithIncorrectDomain() throws Exception {
		JenaOwlReader.initialize("file:" + Config.getOntologyLocation() + "cgo.owl",
				new JenaOwlReaderConfig());
		OntologyReader specialReader = JenaOwlReader.getGlobalInstance();
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
