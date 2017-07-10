import ontoplay.models.ClassCondition;
import ontoplay.models.ConditionDeserializer;
import ontoplay.models.PropertyProvider;
import ontoplay.models.PropertyValueCondition;
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
        String json = "{'classUri':'http://purl.org/NET/cgo#WorkerNode','propertyConditions':[{'propertyUri':'http://purl.org/NET/cgo#belongToVO','operator':'equalToIndividual','individualValue':'http://purl.org/NET/cgo#Biomed'}]}";
        ClassCondition condition = ConditionDeserializer.deserializeCondition(propertyProvider, json);

        assertThat(condition.getPropertyConditions()).hasSize(1);
        PropertyValueCondition actualPropertyCondition = condition.getPropertyConditions().get(0);
        assertIsIndividualValueCondition(actualPropertyCondition,
                "http://purl.org/NET/cgo#belongToVO",
                "http://purl.org/NET/cgo#Biomed");
    }

    @Test
    public void deserializeCondition_createsDatatypeEqualToValueConditions() {
        String json = "{'classUri':'http://purl.org/NET/cgo#WorkerNode','propertyConditions':[{'propertyUri':'http://gridagents.sourceforge.net/AiGGridOntology#isVirtualized','operator':'equalTo', 'datatypeValue':'true'}]}";

        ClassCondition condition = ConditionDeserializer.deserializeCondition(propertyProvider, json);

        assertContainsOnlyDatatypePropertyCondition(condition, "http://gridagents.sourceforge.net/AiGGridOntology#isVirtualized", "equalTo", "true");
    }

    @Test
    public void deserializeCondition_fillsPropertyDataInCondition() {
        String json = "{'classUri':'http://purl.org/NET/cgo#WorkerNode','propertyConditions':[{'propertyUri':'http://gridagents.sourceforge.net/AiGGridOntology#isVirtualized','operator':'equalTo', 'datatypeValue':'true'}]}";

        ClassCondition condition = ConditionDeserializer.deserializeCondition(propertyProvider, json);

        OntoProperty property = condition.getPropertyConditions().get(0).getProperty();
        assertThat(property).isNotNull();
        assertThat(property.getUri()).isEqualTo("http://gridagents.sourceforge.net/AiGGridOntology#isVirtualized");
    }

    @Test
    public void deserializeCondition_createsDatatypeGreaterThanValueConditions() {
        String json = "{'classUri':'http://purl.org/NET/cgo#StorageSpace','propertyConditions':[{'propertyUri':'http://gridagents.sourceforge.net/AiGGridOntology#hasTotalSize','operator':'greaterThan', 'datatypeValue':'123'}]}";

        ClassCondition condition = ConditionDeserializer.deserializeCondition(propertyProvider, json);

        assertContainsOnlyDatatypePropertyCondition(condition,
                "http://gridagents.sourceforge.net/AiGGridOntology#hasTotalSize", "greaterThan", "123");
    }

    @Test
    public void deserializeCondition_createsClassValueConditions() {
        String json = "{'classUri':'http://purl.org/NET/cgo#WorkerNode','propertyConditions':[{'propertyUri':'http://gridagents.sourceforge.net/AiGGridOntology#hasMemory','operator':'constrainedBy','classConstraintValue':{'classUri':'http://purl.org/NET/cgo#Memory','propertyConditions':[{'propertyUri':'http://gridagents.sourceforge.net/AiGGridOntology#hasTotalSize','propertyValue':'1234'}]}}]}";
        ClassCondition condition = ConditionDeserializer.deserializeCondition(propertyProvider, json);
        assertThat(condition.getPropertyConditions()).hasSize(1);
        assertThat(condition.getPropertyConditions().get(0).getClass().equals(ClassValueCondition.class)).isTrue();
        ClassValueCondition propCond = (ClassValueCondition) condition.getPropertyConditions().get(0);
        assertThat(propCond.getPropertyUri()).isEqualTo("http://gridagents.sourceforge.net/AiGGridOntology#hasMemory");
        assertThat(propCond.getClassConstraintValue().getPropertyConditions()).hasSize(1);
    }

    @Test
    public void deserializeCondition_given_description_of_individual_createsClassValueConditions() {
        String json = "{'classUri':'http://purl.org/NET/cgo#WorkerNode','propertyConditions':[{'propertyUri':'http://gridagents.sourceforge.net/AiGGridOntology#hasMemory','operator':'describedWith','classConstraintValue':{'classUri':'http://purl.org/NET/cgo#Memory','propertyConditions':[{'propertyUri':'http://gridagents.sourceforge.net/AiGGridOntology#hasTotalSize','propertyValue':'1234'}]}}]}";
        ClassCondition condition = ConditionDeserializer.deserializeCondition(propertyProvider, json);
        assertThat(condition.getPropertyConditions()).hasSize(1);
        assertThat(condition.getPropertyConditions().get(0).getClass().equals(ClassValueCondition.class)).isTrue();
        ClassValueCondition propCond = (ClassValueCondition) condition.getPropertyConditions().get(0);
        assertThat(propCond.getPropertyUri()).isEqualTo("http://gridagents.sourceforge.net/AiGGridOntology#hasMemory");
        assertThat(propCond.getClassConstraintValue().getPropertyConditions()).hasSize(1);
    }

    private void assertIsIndividualValueCondition(PropertyValueCondition actualPropertyCondition, String expectedPropertyUri, String expectedIndividualValue) {
        assertThat(actualPropertyCondition.getClass().equals(IndividualValueCondition.class)).isTrue();
        IndividualValueCondition propCond = (IndividualValueCondition) actualPropertyCondition;
        assertThat(propCond.getPropertyUri()).isEqualTo(expectedPropertyUri);
        assertThat(propCond.getIndividualValue()).isEqualTo(expectedIndividualValue);
    }

    private void assertContainsOnlyDatatypePropertyCondition(ClassCondition condition, String expectedPropertyUri, String expectedOperator, String expectedValue) {
        assertThat(condition.getPropertyConditions()).hasSize(1);
        PropertyValueCondition propertyValueCondition = condition.getPropertyConditions().get(0);
        assertThat(propertyValueCondition.getClass().equals(DatatypePropertyCondition.class)).isTrue();
        DatatypePropertyCondition propCond = (DatatypePropertyCondition) propertyValueCondition;
        assertThat(propCond.getPropertyUri()).isEqualTo(expectedPropertyUri);
        assertThat(propCond.getOperator()).isEqualTo(expectedOperator);
        assertThat(propCond.getDatatypeValue()).isEqualTo(expectedValue);
    }

    private class FakeProperty implements OntoProperty {

        private String uri;

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
