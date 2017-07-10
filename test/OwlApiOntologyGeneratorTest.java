import fakes.FakeOntoplayConfig;
import ontoplay.Module;
import ontoplay.OntoplayConfig;
import ontoplay.models.ClassCondition;
import ontoplay.models.owlGeneration.OntologyGenerator;
import ontoplay.models.properties.*;
import ontoplay.models.propertyConditions.ClassValueCondition;
import ontoplay.models.propertyConditions.DatatypePropertyCondition;
import ontoplay.models.propertyConditions.IndividualValueCondition;
import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.NamespaceContext;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

public class OwlApiOntologyGeneratorTest {
    private OntologyGenerator ontologyWriter;
    private String classXpath;
    private String individualXpath;

    @BeforeClass
    public static void classSetup() {
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
        individualXpath = "//owl:NamedIndividual[@rdf:about='http://bla.org#testIndividual']/..";
        HashMap m = new HashMap();
        m.put("owl", "http://www.w3.org/2002/07/owl#");
        m.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");

        NamespaceContext ctx = new SimpleNamespaceContext(m);
        XMLUnit.setXpathNamespaceContext(ctx);
        initializeOntologyGenerator();

    }

    private void initializeOntologyGenerator() {
        OntoplayConfig config = new FakeOntoplayConfig() {
            @Override
            public String getOntologyNamespace() {
                return "./AiGGridOntology.owl";
            }

            @Override
            public String getOntologyFilePath() {
                return "http://gridagents.sourceforge.net/AiGGridOntology#";
            }
        };

        Injector injector = new GuiceInjectorBuilder()
                .bindings(new Module(config))
                .injector();

        ontologyWriter = injector.instanceOf(OntologyGenerator.class);
        //.create("./AiGGridOntology.owl", Arrays.asList(new FolderMapping("http://purl.org/NET/cgo", "./cgo.owl")), true);
    }

    @Test
    public void forDatatypePropertyEqualsCondition_convertToOwlClass_ReturnsCreatedOwlClassDescription() throws Exception {
        ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
        OwlDatatypeProperty property = new IntegerProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasClockSpeed", "");
        condition.addProperty(createEqualToDatatypeCondition(property, "12345"));

        String expectedOwl = readTestFile("datatypeConditionOwlApi.xml");
        String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);

        //	System.out.println(actualOwl);
        XMLAssert.assertXpathExists(classXpath, actualOwl);
        XMLAssert.assertXpathsEqual(classXpath, expectedOwl, classXpath, actualOwl);
    }

    private String readTestFile(String fileName) throws IOException, URISyntaxException {
        return FileUtils.readFileToString(new File(getClass().getResource(fileName).toURI()));
    }

    @Test
    public void forEmptyCondition_convertToOwlIndividual_ReturnsCreatedOwlIndividual() throws Exception {
        ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");

        String expectedOwl = readTestFile("emptyConditionIndividualOwlApi.xml");
        String actualOwl = ontologyWriter.convertToOwlIndividual("http://bla.org#testIndividual", condition);

        System.out.println(actualOwl);
        XMLAssert.assertXpathExists(individualXpath, actualOwl);
        XMLAssert.assertXpathsEqual(individualXpath, expectedOwl, individualXpath, actualOwl);
    }

    @Test
    public void forDatatypePropertyEqualsCondition_convertToOwlIndividual_ReturnsCreatedOwlIndividual() throws Exception {
        ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
        OwlDatatypeProperty property = new IntegerProperty("http://purl.org/NET/cgo#", "hasClockSpeed", null);
        condition.addProperty(createEqualToDatatypeCondition(property, "12345"));

        String expectedOwl = readTestFile("datatypeConditionIndividualOwlApi.xml");
        String actualOwl = ontologyWriter.convertToOwlIndividual("http://bla.org#testIndividual", condition);
        System.out.println(actualOwl);
        XMLAssert.assertXpathExists(individualXpath, actualOwl);
        XMLAssert.assertXpathsEqual(individualXpath, expectedOwl, individualXpath, actualOwl);
    }

    @Test
    public void forAnyURIDatatypePropertyEqualsCondition_convertToOwlIndividual_ReturnsProperXsdType() throws Exception {
        ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#URL");
        OwlDatatypeProperty property = new StringProperty("http://purl.org/NET/cgo#", "hasURIAddress", "http://www.w3.org/2001/XMLSchema#anyURI", "");
        condition.addProperty(createEqualToDatatypeCondition(property, "http://localhost/file.txt"));

        String expectedOwl = readTestFile("anyURIdatatypeConditionIndividualOwlApi.xml");
        String actualOwl = ontologyWriter.convertToOwlIndividual("http://bla.org#testIndividual", condition);
        System.out.println(actualOwl);
        XMLAssert.assertXpathExists(individualXpath, actualOwl);
        XMLAssert.assertXpathsEqual(individualXpath, expectedOwl, individualXpath, actualOwl);
    }

    @Test
    public void forDatetimePropertyEqualsCondition_convertToOwlClass_ReturnsCreatedOwlClassDescription() throws Exception {
        ClassCondition condition = new ClassCondition("http://www.w3.org/2006/time#Instant");
        OwlDatatypeProperty property = new DateTimeProperty("http://www.w3.org/2006/time#", "inXSDDateTime", null);
        condition.addProperty(createEqualToDatatypeCondition(property, "10/26/2001 00:00 "));

        String expectedOwl = readTestFile("datetimeConditionOwlApi.xml");
        String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);

        System.out.println(actualOwl);
        XMLAssert.assertXpathExists(classXpath, actualOwl);
        XMLAssert.assertXpathsEqual(classXpath, expectedOwl, classXpath, actualOwl);
    }

    @Test
    public void forDecimalPropertyEqualsCondition_convertToOwlClass_ReturnsCreatedOwlClassDescription() throws Exception {
        ClassCondition condition = new ClassCondition("http://gridagents.sourceforge.net/MOSTOntology#FaultPlaneInfo");
        OwlDatatypeProperty property = new FloatProperty("http://gridagents.sourceforge.net/MOSTOntology#", "hasDEPTH", "http://www.w3.org/2001/XMLSchema#decimal", "");
        condition.addProperty(createEqualToDatatypeCondition(property, "10.5"));

        String expectedOwl = readTestFile("decimalConditionOwlApi.xml");
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

        String expectedOwl = readTestFile("individualConditionOwlApi.xml");
        String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);
        XMLAssert.assertXpathExists(classXpath, actualOwl);
        XMLAssert.assertXpathsEqual(classXpath, expectedOwl, classXpath, actualOwl);
    }

    @Test
    public void forIndividualEqualsCondition_convertToOwlIndividual_ReturnsCreatedOwlIndividual() throws Exception {
        ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#WorkerNode");
        condition.addProperty(new IndividualValueCondition("http://gridagents.sourceforge.net/AiGGridOntology#hasMemory",
                "http://gridagents.sourceforge.net/AiGGridInstances#condorWorkerNode2RAM"));

        String expectedOwl = readTestFile("individualConditionIndividualOwlApi.xml");
        String actualOwl = ontologyWriter.convertToOwlIndividual("http://bla.org#testIndividual", condition);
        System.out.println(actualOwl);
        XMLAssert.assertXpathExists(individualXpath, actualOwl);
        XMLAssert.assertXpathsEqual(individualXpath, expectedOwl, individualXpath, actualOwl);
    }

    @Test
    public void forNestedEqualsCondition_convertToOwlClass_ReturnsCreatedOwlClassDescription() throws Exception {
        ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#WorkerNode");

        ClassCondition nestedCondition = new ClassCondition("http://purl.org/NET/cgo#Memory");

        OwlDatatypeProperty property = new FloatProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasTotalSize", null);
        nestedCondition.addProperty(createEqualToDatatypeCondition(property, "123"));
        ClassValueCondition valueCondition = new ClassValueCondition("http://gridagents.sourceforge.net/AiGGridOntology#hasMemory",
                nestedCondition);
        condition.addProperty(valueCondition);

        String expectedOwl = readTestFile("classConditionOwlApi.xml");
        String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);
        System.out.print(actualOwl);
        XMLAssert.assertXpathExists(classXpath, actualOwl);
        XMLAssert.assertXpathsEqual(classXpath, expectedOwl, classXpath, actualOwl);
    }

    @Test
    public void forNestedEqualsCondition_convertToOwlIndividual_ReturnsCreatedOwlIndividual() throws Exception {
        //TODO: This is a really crappy test - output is used as "expected". Anonymous individuals can't be created in Protege.
        // Would be good to make the assert base on actually parsing the result.
        ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#WorkerNode");

        ClassCondition nestedCondition = new ClassCondition("http://purl.org/NET/cgo#Memory");

        OwlDatatypeProperty property = new FloatProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasTotalSize", null);
        nestedCondition.addProperty(createEqualToDatatypeCondition(property, "123"));
        ClassValueCondition valueCondition = new ClassValueCondition("http://gridagents.sourceforge.net/AiGGridOntology#hasMemory",
                nestedCondition);
        condition.addProperty(valueCondition);

        String expectedOwl = readTestFile("objectConditionIndividualOwlApi.xml");
        String actualOwl = ontologyWriter.convertToOwlIndividual("http://bla.org#testIndividual", condition);
        System.out.print(actualOwl);

        XMLAssert.assertXpathExists(individualXpath, actualOwl);
        XMLAssert.assertXpathsEqual(individualXpath, expectedOwl, individualXpath, actualOwl);
    }

    @Test
    public void forListOfPropertyConditions_convertToOwlClass_ReturnsCreatedOwlClassDescription() throws Exception {
        ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
        OwlDatatypeProperty intProperty = new IntegerProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasClockSpeed", null);
        OwlDatatypeProperty stringProperty = new StringProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasName", null);

        condition.addProperty(createEqualToDatatypeCondition(intProperty, "12345"));
        condition.addProperty(createEqualToDatatypeCondition(stringProperty, "abc"));

        String expectedOwl = readTestFile("multipleConditionsOwlApi.xml");
        String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);
        XMLAssert.assertXpathExists(classXpath, actualOwl);
        XMLAssert.assertXpathsEqual(classXpath, expectedOwl, classXpath, actualOwl);
    }

    @Test
    public void forGreaterThanCondition_convertToOwlClass_ReturnsCorrectXsdRestriction() throws Exception {
        OwlDatatypeProperty intProperty = new IntegerProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasClockSpeed", null);
        DatatypePropertyCondition propertyCondition = new DatatypePropertyCondition("http://gridagents.sourceforge.net/AiGGridOntology#hasClockSpeed", "greaterThan", "12345");
        propertyCondition.setProperty(intProperty);
        ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
        condition.addProperty(propertyCondition);

        String expectedOwl = readTestFile("greaterThanConditionOwlApi.xml");
        String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);
        System.out.println(actualOwl);
        XMLAssert.assertXpathExists(classXpath, actualOwl);
        XMLAssert.assertXpathsEqual(classXpath, expectedOwl, classXpath, actualOwl);
    }

    @Test
    public void forLessThanCondition_convertToOwlClass_ReturnsCorrectXsdRestriction() throws Exception {
        OwlDatatypeProperty intProperty = new IntegerProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasClockSpeed", null);
        DatatypePropertyCondition propertyCondition = new DatatypePropertyCondition("http://gridagents.sourceforge.net/AiGGridOntology#hasClockSpeed", "lessThan", "12345");
        propertyCondition.setProperty(intProperty);
        ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
        condition.addProperty(propertyCondition);

        String expectedOwl = readTestFile("lessThanConditionOwlApi.xml");
        String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);
        System.out.println(actualOwl);
        XMLAssert.assertXpathExists(classXpath, actualOwl);
        XMLAssert.assertXpathsEqual(classXpath, expectedOwl, classXpath, actualOwl);
    }

    @Test
    public void convertToOwlIndividual_AddsNecessaryImportStatements() throws Exception {
        ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");

        String expectedOwl = readTestFile("emptyConditionIndividualOwlApi.xml");
        String actualOwl = ontologyWriter.convertToOwlIndividual("http://bla.org#testIndividual", condition);

        System.out.println(actualOwl);
        individualXpath = "//owl:imports";
        XMLAssert.assertXpathExists(individualXpath, actualOwl);
        XMLAssert.assertXpathsEqual(individualXpath, expectedOwl, individualXpath, actualOwl);
    }

    @Test
    public void convertToOwlIndividual_AddsOnlyUniqueImportStatements() throws Exception {
        ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
        OwlDatatypeProperty property = new IntegerProperty("http://purl.org/NET/cgo#", "hasClockSpeed", null);
        condition.addProperty(createEqualToDatatypeCondition(property, "12345"));

        String expectedOwl = readTestFile("datatypeConditionIndividualOwlApi.xml");
        String actualOwl = ontologyWriter.convertToOwlIndividual("http://bla.org#testIndividual", condition);

        System.out.println(actualOwl);
        individualXpath = "//owl:imports";
        XMLAssert.assertXpathExists(individualXpath, actualOwl);
        XMLAssert.assertXpathsEqual(individualXpath, expectedOwl, individualXpath, actualOwl);
    }

    @Test
    public void convertToOwlIndividual_SetsOntologyIRI() throws Exception {
        ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
        OwlDatatypeProperty property = new IntegerProperty("http://purl.org/NET/cgo#", "hasClockSpeed", null);
        condition.addProperty(createEqualToDatatypeCondition(property, "12345"));

        String expectedOwl = readTestFile("datatypeConditionIndividualOwlApi.xml");
        String actualOwl = ontologyWriter.convertToOwlIndividual("http://bla.org#testIndividual", condition);

        System.out.println(actualOwl);
        String ontologyIRIXpath = "//owl:Ontology/@rdf:about";
        XMLAssert.assertXpathExists(ontologyIRIXpath, actualOwl);
        XMLAssert.assertXpathsEqual(ontologyIRIXpath, expectedOwl, ontologyIRIXpath, actualOwl);
    }

    @Test
    public void convertToOwlClass_SetsOntologyIRI() throws Exception {
        ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
        OwlDatatypeProperty property = new IntegerProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasClockSpeed", null);
        condition.addProperty(createEqualToDatatypeCondition(property, "12345"));

        String expectedOwl = readTestFile("datatypeConditionOwlApi.xml");
        String actualOwl = ontologyWriter.convertToOwlClass("http://bla.org/testCondition", condition);

        System.out.println(actualOwl);
        String ontologyIRIXpath = "//owl:Ontology/@rdf:about";
        XMLAssert.assertXpathExists(ontologyIRIXpath, actualOwl);
        XMLAssert.assertXpathsEqual(ontologyIRIXpath, expectedOwl, ontologyIRIXpath, actualOwl);
    }

    @Test
    public void convertToOwlClass_AddsNecessaryImportStatements() throws Exception {
        ClassCondition condition = new ClassCondition("http://purl.org/NET/cgo#CPU");
        OwlDatatypeProperty property = new IntegerProperty("http://gridagents.sourceforge.net/AiGGridOntology#", "hasClockSpeed", null);
        condition.addProperty(createEqualToDatatypeCondition(property, "12345"));

        String expectedOwl = readTestFile("datatypeConditionOwlApi.xml");
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
