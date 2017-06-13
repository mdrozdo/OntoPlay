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
import java.util.HashMap;
import java.util.List;

import jobs.PropertyTypeConfiguration;
import jobs.PropertyTypeConfiguration;
import junit.framework.Assert;

import ontoplay.models.ClassCondition;
import ontoplay.models.InvalidConfigurationException;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlElement;
import ontoplay.models.ontologyModel.XsdType;
import ontoplay.models.ontologyReading.jena.JenaOwlReader;
import ontoplay.models.ontologyReading.jena.JenaOwlReaderConfig;
import ontoplay.models.ontologyReading.jena.propertyFactories.IntegerPropertyFactory;
import ontoplay.models.owlGeneration.OntologyGenerator;
import ontoplay.models.properties.DateTimeProperty;
import ontoplay.models.properties.FloatProperty;
import ontoplay.models.properties.IntegerProperty;
import ontoplay.models.properties.OwlDatatypeProperty;
import ontoplay.models.properties.OwlObjectProperty;
import ontoplay.models.properties.StringProperty;
import ontoplay.models.propertyConditions.ClassValueCondition;
import ontoplay.models.propertyConditions.DatatypePropertyCondition;
import ontoplay.models.propertyConditions.IndividualValueCondition;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.NamespaceContext;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class OwlApiOntologyGeneratorTest {
	private OntologyGenerator ontologyWriter;
	private String classXpath;
	private String individualXpath;

	@BeforeClass
	public static void classSetup(){
		XMLUnit.setControlParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		XMLUnit.setTestParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		XMLUnit.setSAXParserFactory("org.apache.xerces.jaxp.SAXParserFactoryImpl");
		//XMLUnit.setTransformerFactory("org.apache.xalan.processor.TransformerFactoryImpl");
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreComments(true);
	}
	
	@Before
	public void setup() {
		classXpath = "//owl:Class[@rdf:about='http://bla.org/testCondition']";		
		individualXpath = "//owl:NamedIndividual[@IRI='#testIndividual']";
		HashMap m = new HashMap();
	    m.put("owl", "http://www.w3.org/2002/07/owl#");
	    m.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
	    
	    NamespaceContext ctx = new SimpleNamespaceContext(m);
	    XMLUnit.setXpathNamespaceContext(ctx);
		initializeOntologyGenerator();
		
	}

	private void initializeOntologyGenerator() {
		ontologyWriter = OntologyGenerator.loadFromFile("file:../Ontology/AiGGridOntology.owl", "../Ontology");
		try {
			new PropertyTypeConfiguration().doJob();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void forDatatypePropertyEqualsCondition_convertToOwlClass_ReturnsCreatedOwlClassDescription() throws Exception {
		ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
		OwlDatatypeProperty property = new IntegerProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasClockSpeed");
		condition.addProperty(createEqualToDatatypeCondition(property, "12345"));
		
		String expectedOwl = FileUtils.readFileToString(new File("test/datatypeConditionOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);
		
	//	System.out.println(actualOwl);
		XMLAssert.assertXpathExists(classXpath, actualOwl);
		XMLAssert.assertXpathsEqual(classXpath, expectedOwl, classXpath, actualOwl);		
	}
	
	@Test
	public void forEmptyCondition_convertToOwlIndividual_ReturnsCreatedOwlIndividual() throws Exception {
		ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
		
		String expectedOwl = FileUtils.readFileToString(new File("test/emptyConditionIndividualOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlIndividual("http://bla.org#testIndividual", condition);
		
	//	System.out.println(actualOwl);
		XMLAssert.assertXpathExists(individualXpath, actualOwl);
		XMLAssert.assertXpathsEqual(individualXpath, expectedOwl, individualXpath, actualOwl);		
	}
	
	@Test
	public void forDatatypePropertyEqualsCondition_convertToOwlIndividual_ReturnsCreatedOwlIndividual() throws Exception {
		ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
		OwlDatatypeProperty property = new IntegerProperty("http://purl.org/NET/cgo#", "hasClockSpeed");
		condition.addProperty(createEqualToDatatypeCondition(property, "12345"));
		
		String expectedOwl = FileUtils.readFileToString(new File("test/datatypeConditionIndividualOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlIndividual("http://bla.org#testIndividual", condition);
		individualXpath = "//owl:NamedIndividual[@IRI='#testIndividual']/..";
		System.out.println(actualOwl);
		XMLAssert.assertXpathExists(individualXpath, actualOwl);
		XMLAssert.assertXpathsEqual(individualXpath, expectedOwl, individualXpath, actualOwl);		
	}
	
	@Test
	public void forAnyURIDatatypePropertyEqualsCondition_convertToOwlIndividual_ReturnsProperXsdType() throws Exception {
		ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#URL");
		OwlDatatypeProperty property = new StringProperty("http://purl.org/NET/cgo#", "hasURIAddress", "http://www.w3.org/2001/XMLSchema#anyURI");
		condition.addProperty(createEqualToDatatypeCondition(property, "http://localhost/file.txt"));
		
		String expectedOwl = FileUtils.readFileToString(new File("test/anyURIdatatypeConditionIndividualOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlIndividual("http://bla.org#testIndividual", condition);
		individualXpath = "//owl:NamedIndividual[@IRI='#testIndividual']/..";
		System.out.println(actualOwl);
		XMLAssert.assertXpathExists(individualXpath, actualOwl);
		XMLAssert.assertXpathsEqual(individualXpath, expectedOwl, individualXpath, actualOwl);		
	}
	
	@Test
	public void forDatetimePropertyEqualsCondition_convertToOwlClass_ReturnsCreatedOwlClassDescription() throws Exception {
		ClassCondition condition = new ClassCondition("http://www.w3.org/2006/time#Instant");
		OwlDatatypeProperty property = new DateTimeProperty("http://www.w3.org/2006/time#", "inXSDDateTime");
		condition.addProperty(createEqualToDatatypeCondition(property, "10/26/2001 00:00 "));
		
		String expectedOwl = FileUtils.readFileToString(new File("test/datetimeConditionOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);
		
		System.out.println(actualOwl);
		XMLAssert.assertXpathExists(classXpath, actualOwl);
		XMLAssert.assertXpathsEqual(classXpath, expectedOwl, classXpath, actualOwl);		
	}
	
	@Test
	public void forDecimalPropertyEqualsCondition_convertToOwlClass_ReturnsCreatedOwlClassDescription() throws Exception {
		ClassCondition condition = new ClassCondition("http://gridagents.sourceforge.net/MOSTOntology#FaultPlaneInfo");
		OwlDatatypeProperty property = new FloatProperty("http://gridagents.sourceforge.net/MOSTOntology#", "hasDEPTH", "http://www.w3.org/2001/XMLSchema#decimal");
		condition.addProperty(createEqualToDatatypeCondition(property, "10.5"));
		
		String expectedOwl = FileUtils.readFileToString(new File("test/decimalConditionOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);
		
		System.out.println(actualOwl);
		XMLAssert.assertXpathExists(classXpath, actualOwl);
		XMLAssert.assertXpathsEqual(classXpath, expectedOwl, classXpath, actualOwl);		
	}
	
	@Test
	public void forIndividualEqualsCondition_convertToOwlClass_ReturnsCreatedOwlClassDescription() throws Exception {
		ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#WorkerNode");
		condition.addProperty(new IndividualValueCondition("http://gridagents.sourceforge.net/AiGGridOntology#hasMemory", 
				"http://gridagents.sourceforge.net/AiGGridOntology#condorWorkerNode2RAM"));
		
		String expectedOwl = FileUtils.readFileToString(new File("test/individualConditionOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);
		XMLAssert.assertXpathExists(classXpath, actualOwl);
		XMLAssert.assertXpathsEqual(classXpath, expectedOwl, classXpath, actualOwl);		
	}
	
	@Test
	public void forIndividualEqualsCondition_convertToOwlIndividual_ReturnsCreatedOwlIndividual() throws Exception {
		ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#WorkerNode");
		condition.addProperty(new IndividualValueCondition("http://gridagents.sourceforge.net/AiGGridOntology#hasMemory", 
				"http://gridagents.sourceforge.net/AiGGridInstances#condorWorkerNode2RAM"));
		
		individualXpath = "//owl:NamedIndividual[@IRI='#testIndividual']/..";
		
		String expectedOwl = FileUtils.readFileToString(new File("test/individualConditionIndividualOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlIndividual("http://bla.org#testIndividual", condition);
		System.out.println(actualOwl);
		XMLAssert.assertXpathExists(individualXpath, actualOwl);
		XMLAssert.assertXpathsEqual(individualXpath, expectedOwl, individualXpath, actualOwl);		
	}
	
	@Test
	public void forNestedEqualsCondition_convertToOwlClass_ReturnsCreatedOwlClassDescription() throws Exception {
		ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#WorkerNode");
		
		ClassCondition nestedCondition = new  ClassCondition("http://purl.org/NET/cgo#Memory");
		
		OwlDatatypeProperty property = new FloatProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasTotalSize");
		nestedCondition.addProperty(createEqualToDatatypeCondition(property, "123"));
		ClassValueCondition valueCondition = new ClassValueCondition("http://gridagents.sourceforge.net/AiGGridOntology#hasMemory", 
				nestedCondition); 
		condition.addProperty(valueCondition);
		
		String expectedOwl = FileUtils.readFileToString(new File("test/classConditionOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);
		//System.out.print(actualOwl);
		XMLAssert.assertXpathExists(classXpath, actualOwl);
		XMLAssert.assertXpathsEqual(classXpath, expectedOwl, classXpath, actualOwl);		
	}
	
	@Test
	public void forNestedEqualsCondition_convertToOwlIndividual_ReturnsCreatedOwlIndividual() throws Exception {
		//TODO: This is a really crappy test - output is used as "expected". Anonymous individuals can't be created in Protege.
		// Would be good to make the assert base on actually parsing the result.
		ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#WorkerNode");
		
		ClassCondition nestedCondition = new  ClassCondition("http://purl.org/NET/cgo#Memory");
		
		OwlDatatypeProperty property = new FloatProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasTotalSize");
		nestedCondition.addProperty(createEqualToDatatypeCondition(property, "123"));
		ClassValueCondition valueCondition = new ClassValueCondition("http://gridagents.sourceforge.net/AiGGridOntology#hasMemory", 
				nestedCondition); 
		condition.addProperty(valueCondition);
		
		String expectedOwl = FileUtils.readFileToString(new File("test/objectConditionIndividualOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlIndividual("http://bla.org#testIndividual", condition);
		System.out.print(actualOwl);
		
		individualXpath = "//owl:NamedIndividual[@IRI='#testIndividual']/..";			
		String nestedXpath = "//owl:AnonymousIndividual/..";
		XMLAssert.assertXpathExists(individualXpath, actualOwl);
		XMLAssert.assertXpathsEqual(individualXpath, expectedOwl, individualXpath, actualOwl);
		XMLAssert.assertXpathExists(nestedXpath, actualOwl);
		XMLAssert.assertXpathsEqual(nestedXpath, expectedOwl, nestedXpath, actualOwl);
	}
	
	@Test
	public void forListOfPropertyConditions_convertToOwlClass_ReturnsCreatedOwlClassDescription() throws Exception {
		ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
		OwlDatatypeProperty intProperty = new IntegerProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasClockSpeed");
		OwlDatatypeProperty stringProperty = new StringProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasName");
		
		condition.addProperty(createEqualToDatatypeCondition(intProperty, "12345"));
		condition.addProperty(createEqualToDatatypeCondition(stringProperty, "abc"));
		
		String expectedOwl = FileUtils.readFileToString(new File("test/multipleConditionsOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);
		XMLAssert.assertXpathExists(classXpath, actualOwl);
		XMLAssert.assertXpathsEqual(classXpath, expectedOwl, classXpath, actualOwl);		
	}
	
	@Test
	public void forGreaterThanCondition_convertToOwlClass_ReturnsCorrectXsdRestriction() throws Exception {
		OwlDatatypeProperty intProperty = new IntegerProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasClockSpeed");
		DatatypePropertyCondition propertyCondition = new DatatypePropertyCondition("http://gridagents.sourceforge.net/AiGGridOntology#hasClockSpeed", "greaterThan", "12345");
		propertyCondition.setProperty(intProperty);
		ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
		condition.addProperty(propertyCondition);
		
		String expectedOwl = FileUtils.readFileToString(new File("test/greaterThanConditionOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);
		System.out.println(actualOwl);
		XMLAssert.assertXpathExists(classXpath, actualOwl);
		XMLAssert.assertXpathsEqual(classXpath, expectedOwl, classXpath, actualOwl);		
	}
	
	@Test
	public void forLessThanCondition_convertToOwlClass_ReturnsCorrectXsdRestriction() throws Exception {
		OwlDatatypeProperty intProperty = new IntegerProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasClockSpeed");
		DatatypePropertyCondition propertyCondition = new DatatypePropertyCondition("http://gridagents.sourceforge.net/AiGGridOntology#hasClockSpeed", "lessThan", "12345");
		propertyCondition.setProperty(intProperty);
		ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
		condition.addProperty(propertyCondition);
		
		String expectedOwl = FileUtils.readFileToString(new File("test/lessThanConditionOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);
		System.out.println(actualOwl);
		XMLAssert.assertXpathExists(classXpath, actualOwl);
		XMLAssert.assertXpathsEqual(classXpath, expectedOwl, classXpath, actualOwl);		
	}
	
	@Test
	public void convertToOwlIndividual_AddsNecessaryImportStatements() throws Exception{
		ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
		
		String expectedOwl = FileUtils.readFileToString(new File("test/emptyConditionIndividualOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlIndividual("http://bla.org#testIndividual", condition);
		
		System.out.println(actualOwl);
		individualXpath = "//owl:Import";
		XMLAssert.assertXpathExists(individualXpath, actualOwl);
		XMLAssert.assertXpathsEqual(individualXpath, expectedOwl, individualXpath, actualOwl);		
	}
	
	@Test
	public void convertToOwlIndividual_AddsOnlyUniqueImportStatements() throws Exception{
		ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
		OwlDatatypeProperty property = new IntegerProperty("http://purl.org/NET/cgo#", "hasClockSpeed");
		condition.addProperty(createEqualToDatatypeCondition(property, "12345"));
		
		String expectedOwl = FileUtils.readFileToString(new File("test/datatypeConditionIndividualOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlIndividual("http://bla.org#testIndividual", condition);
		
		System.out.println(actualOwl);
		individualXpath = "//owl:Import";
		XMLAssert.assertXpathExists(individualXpath, actualOwl);
		XMLAssert.assertXpathsEqual(individualXpath, expectedOwl, individualXpath, actualOwl);		
	}
	
	@Test
	public void convertToOwlIndividual_SetsOntologyIRI() throws Exception{
		ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
		OwlDatatypeProperty property = new IntegerProperty("http://purl.org/NET/cgo#", "hasClockSpeed");
		condition.addProperty(createEqualToDatatypeCondition(property, "12345"));
		
		String expectedOwl = FileUtils.readFileToString(new File("test/datatypeConditionIndividualOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlIndividual("http://bla.org#testIndividual", condition);
		
		System.out.println(actualOwl);
		String ontologyIRIXpath = "//owl:Ontology/@ontologyIRI";
		XMLAssert.assertXpathExists(ontologyIRIXpath, actualOwl);
		XMLAssert.assertXpathsEqual(ontologyIRIXpath, expectedOwl, ontologyIRIXpath, actualOwl);	
	}
	
	@Test
	public void convertToOwlClass_SetsOntologyIRI() throws Exception{
		ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
		OwlDatatypeProperty property = new IntegerProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasClockSpeed");
		condition.addProperty(createEqualToDatatypeCondition(property, "12345"));
		
		String expectedOwl = FileUtils.readFileToString(new File("test/datatypeConditionOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);
		
		System.out.println(actualOwl);
		String ontologyIRIXpath = "//owl:Ontology/@rdf:about";
		XMLAssert.assertXpathExists(ontologyIRIXpath, actualOwl);
		XMLAssert.assertXpathsEqual(ontologyIRIXpath, expectedOwl, ontologyIRIXpath, actualOwl);		
	}
	
	@Test
	public void convertToOwlClass_AddsNecessaryImportStatements() throws Exception{
		ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
		OwlDatatypeProperty property = new IntegerProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasClockSpeed");
		condition.addProperty(createEqualToDatatypeCondition(property, "12345"));
		
		String expectedOwl = FileUtils.readFileToString(new File("test/datatypeConditionOwlApi.xml"));
		String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);
		
		System.out.println(actualOwl);
		String importsXpath = "//owl:imports";
		XMLAssert.assertXpathExists(importsXpath, actualOwl);
		XMLAssert.assertXpathsEqual(importsXpath, expectedOwl, importsXpath, actualOwl);		
	}
	
	private DatatypePropertyCondition createEqualToDatatypeCondition(OwlDatatypeProperty property, String propertyValue) {
		//TODO: Extract common logic for creating and filling property in condition into a separate factory class.
		DatatypePropertyCondition datatypePropertyCondition = new DatatypePropertyCondition(property.getUri(), "equalTo", propertyValue);
		datatypePropertyCondition.setProperty(property);
		return datatypePropertyCondition;
	}	
}
