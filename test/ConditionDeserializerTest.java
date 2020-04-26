import ontoplay.models.*;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.propertyConditions.ClassValueCondition;
import ontoplay.models.propertyConditions.DatatypePropertyCondition;
import ontoplay.models.propertyConditions.IndividualValueCondition;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ConditionDeserializerTest {

    FakePropertyProvider propertyProvider;

    @Before
    public void setUp() {
        propertyProvider = new FakePropertyProvider();
    }

    @Test
    public void deserializeCondition_createsIndividualValueCondition() {
        String json = "{\n" +
                "    \"classUri\": \"http: //purl.org/NET/cgo#WorkerNode\",\n" +
                "    \"propertyConditions\": {\n" +
                "        \"type\": \"condition\",\n" +
                "        \"propertyUri\": \"http://purl.org/NET/cgo#belongToVO\",\n" +
                "        \"operator\": \"equalToIndividual\",\n" +
                "        \"individualValue\": \"http://purl.org/NET/cgo#Biomed\"\n" +
                "    }\n" +
                "}";
        ClassCondition condition = ConditionDeserializer.deserializeCondition(propertyProvider, json);

        PropertyValueCondition actualPropertyCondition = (PropertyValueCondition) condition.getPropertyConditions();
        assertIsIndividualValueCondition(actualPropertyCondition,
                "http://purl.org/NET/cgo#belongToVO",
                "http://purl.org/NET/cgo#Biomed");
    }

    @Test
    public void deserializeCondition_createsDatatypeEqualToValueConditions() {
        String json = "{\n" +
                "    \"classUri\": \"http: //purl.org/NET/cgo#WorkerNode\",\n" +
                "    \"propertyConditions\": {\n" +
                "        \"type\": \"condition\",\n" +
                "        \"propertyUri\": \"http://gridagents.sourceforge.net/AiGGridOntology#isVirtualized\",\n" +
                "        \"operator\": \"equalTo\",\n" +
                "        \"datatypeValue\": \"true\"\n" +
                "    }\n" +
                "}";

        ClassCondition condition = ConditionDeserializer.deserializeCondition(propertyProvider, json);

        assertContainsOnlyDatatypePropertyCondition(condition, "http://gridagents.sourceforge.net/AiGGridOntology#isVirtualized", "equalTo", "true");
    }

    @Test
    public void deserializeCondition_fillsPropertyDataInCondition() {
        String json = "{\n" +
                "    \"classUri\": \"http: //purl.org/NET/cgo#WorkerNode\",\n" +
                "    \"propertyConditions\": {\n" +
                "        \"type\": \"condition\",\n" +
                "        \"propertyUri\": \"http://gridagents.sourceforge.net/AiGGridOntology#isVirtualized\",\n" +
                "        \"operator\": \"equalTo\",\n" +
                "        \"datatypeValue\": \"true\"\n" +
                "    }\n" +
                "}";

        ClassCondition condition = ConditionDeserializer.deserializeCondition(propertyProvider, json);

        PropertyValueCondition propertyCondition = (PropertyValueCondition) condition.getPropertyConditions();
        OntoProperty property = propertyCondition.getProperty();
        assertThat(property).isNotNull();
        assertThat(property.getUri()).isEqualTo("http://gridagents.sourceforge.net/AiGGridOntology#isVirtualized");
    }

    @Test
    public void deserializeCondition_createsDatatypeGreaterThanValueConditions() {
        String json = "{\n" +
                "    \"classUri\": \"http: //purl.org/NET/cgo#StorageSpace\",\n" +
                "    \"propertyConditions\": {\n" +
                "        \"type\": \"condition\",\n" +
                "        \"propertyUri\": \"http://gridagents.sourceforge.net/AiGGridOntology#hasTotalSize\",\n" +
                "        \"operator\": \"greaterThan\",\n" +
                "        \"datatypeValue\": \"123\"\n" +
                "    }\n" +
                "}";

        ClassCondition condition = ConditionDeserializer.deserializeCondition(propertyProvider, json);

        assertContainsOnlyDatatypePropertyCondition(condition,
                "http://gridagents.sourceforge.net/AiGGridOntology#hasTotalSize", "greaterThan", "123");
    }

    @Test
    public void deserializeCondition_createsClassValueConditions() {
        String json = "{\n" +
                "    \"classUri\": \"http: //purl.org/NET/cgo#WorkerNode\",\n" +
                "    \"propertyConditions\": {\n" +
                "        \"type\": \"condition\",\n" +
                "        \"propertyUri\": \"http://gridagents.sourceforge.net/AiGGridOntology#hasMemory\",\n" +
                "        \"operator\": \"constrainedBy\",\n" +
                "        \"classConstraintValue\": {\n" +
                "            \"classUri\": \"http://purl.org/NET/cgo#Memory\",\n" +
                "            \"propertyConditions\": {\n" +
                "                \"type\": \"condition\",\n" +
                "                \"propertyUri\": \"http://gridagents.sourceforge.net/AiGGridOntology#hasTotalSize\",\n" +
                "                \"datatypeValue\": \"1234\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";

        ClassCondition condition = ConditionDeserializer.deserializeCondition(propertyProvider, json);
        assertThat(condition.getPropertyConditions()).isNotNull().isInstanceOf(ClassValueCondition.class);
        ClassValueCondition propCond = (ClassValueCondition) condition.getPropertyConditions();
        assertThat(propCond.getPropertyUri()).isEqualTo("http://gridagents.sourceforge.net/AiGGridOntology#hasMemory");
        assertThat(propCond.getClassConstraintValue().getPropertyConditions()).isNotNull();
    }

    @Test
    public void deserializeCondition_given_description_of_individual_createsClassValueConditions() {
        String json = "{\n" +
                "    \"classUri\": \"http://purl.org/NET/cgo#WorkerNode\",\n" +
                "    \"propertyConditions\": {\n" +
                "        \"type\": \"condition\",\n" +
                "        \"propertyUri\": \"http://gridagents.sourceforge.net/AiGGridOntology#hasMemory\",\n" +
                "        \"operator\": \"describedWith\",\n" +
                "        \"classConstraintValue\": {\n" +
                "            \"classUri\": \"http://purl.org/NET/cgo#Memory\",\n" +
                "            \"propertyConditions\": {\n" +
                "                \"type\": \"condition\",\n" +
                "                \"propertyUri\": \"http://gridagents.sourceforge.net/AiGGridOntology#hasTotalSize\",\n" +
                "                \"datatypeValue\": \"1234\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";

        ClassCondition condition = ConditionDeserializer.deserializeCondition(propertyProvider, json);
        assertThat(condition.getPropertyConditions()).isInstanceOf(ClassValueCondition.class);
        ClassValueCondition propCond = (ClassValueCondition) condition.getPropertyConditions();
        assertThat(propCond.getPropertyUri()).isEqualTo("http://gridagents.sourceforge.net/AiGGridOntology#hasMemory");
        assertThat(propCond.getClassConstraintValue().getClassUri()).isEqualTo("http://purl.org/NET/cgo#Memory");
        assertThat(propCond.getClassConstraintValue().getPropertyConditions()).isNotNull();
    }

    @Test
    public void deserializeCondition_createsIntersectionOfConditions() {
        String json = "{\n" +
                "    \"classUri\": \"http://purl.org/NET/cgo#WorkerNode\",\n" +
                "    \"propertyConditions\": {\n" +
                "      \"type\": \"intersection\",\n" +
                "      \"contents\": [\n" +
                "        {\n" +
                "          \"type\": \"condition\",\n" +
                "          \"propertyUri\": \"http://purl.org/NET/cgo#runningJobs\",\n" +
                "          \"operator\": \"equalTo\",\n" +
                "          \"datatypeValue\": \"0\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"type\": \"condition\",\n" +
                "          \"propertyUri\": \"http://gridagents.sourceforge.net/AiGGridOntology#isVirtualized\",\n" +
                "          \"operator\": \"equalTo\",\n" +
                "          \"datatypeValue\": \"false\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }";
        ClassCondition condition = ConditionDeserializer.deserializeCondition(propertyProvider, json);

        assertThat(condition.getPropertyConditions()).isInstanceOf(PropertyGroupCondition.class);
        PropertyGroupCondition actualPropertyCondition = (PropertyGroupCondition) condition.getPropertyConditions();
        assertThat(actualPropertyCondition.getType()).isEqualTo(PropertyConditionType.INTERSECTION);
        assertThat(actualPropertyCondition.getContents()).hasSize(2);
    }

    private void assertIsIndividualValueCondition(PropertyValueCondition actualPropertyCondition, String expectedPropertyUri, String expectedIndividualValue) {
        assertThat(actualPropertyCondition.getClass().equals(IndividualValueCondition.class)).isTrue();
        IndividualValueCondition propCond = (IndividualValueCondition) actualPropertyCondition;
        assertThat(propCond.getPropertyUri()).isEqualTo(expectedPropertyUri);
        assertThat(propCond.getIndividualValue()).isEqualTo(expectedIndividualValue);
    }

    private void assertContainsOnlyDatatypePropertyCondition(ClassCondition condition, String expectedPropertyUri, String expectedOperator, String expectedValue) {
        assertThat(condition.getPropertyConditions()).isInstanceOf(DatatypePropertyCondition.class);
        DatatypePropertyCondition propCond = (DatatypePropertyCondition) condition.getPropertyConditions();
        assertThat(propCond.getPropertyUri()).isEqualTo(expectedPropertyUri);
        assertThat(propCond.getOperator()).isEqualTo(expectedOperator);
        assertThat(propCond.getDatatypeValue()).isEqualTo(expectedValue);
    }

    private class FakeProperty implements OntoProperty {

        private final String uri;

        public FakeProperty(String uri) {
            this.uri = uri;
        }

        public String getLocalName() {
            return "";
        }

        @Override
        public String getLabel() {
            return "";
        }

        public String getUri() {
            return uri;
        }

    }

    private class FakePropertyProvider implements PropertyProvider {

        public OntoProperty getProperty(String propertyUri) {
            return new FakeProperty(propertyUri);

        }
    }
}
