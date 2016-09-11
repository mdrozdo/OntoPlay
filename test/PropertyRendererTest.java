
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
import java.util.Map;

import jobs.JenaOwlReaderConfiguration;
import jobs.PropertyTypeConfiguration;
import jobs.PropertyTypeConfiguration;
import junit.framework.Assert;
import ontoplay.models.ClassCondition;
import ontoplay.models.ConfigurationException;
import ontoplay.models.InvalidConfigurationException;
import ontoplay.models.PropertyOperator;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyModel.OwlElement;
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
import org.junit.Ignore;
import org.junit.Test;

import conf.Config;
import ontoplay.controllers.Constraints;
import ontoplay.controllers.PropertyConditionRenderer;
import ontoplay.controllers.Renderer;
import static org.fest.assertions.Assertions.assertThat;

//TODO: Create properties manually instead of using kb
public class PropertyRendererTest {
	private OntologyReader kb;

	@Before
	public void setup() {
		try {
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
	public void forGreaterThan_renderOperator_rendersSimpleDatatypeValueTemplate() throws ConfigurationException{
		int conditionId = 1;
		OntoClass owlClass = kb.getOwlClass("http://purl.org/NET/cgo#StorageSpace");
		OntoProperty property = kb.getProperty("http://gridagents.sourceforge.net/AiGGridOntology#hasAvailableSize");
		
		PropertyConditionRenderer conditionRenderer = PropertyConditionRenderer
				.getRenderer(property.getClass());
		
		SpyRenderer renderer = new SpyRenderer();
		conditionRenderer.renderOperator(conditionId, owlClass, property, "greaterThan", renderer);
		
		assertThat(renderer.actualTemplateName).isEqualTo("@Constraints.simpleDatatypeValue");
	}
	
	@Test
	public void forLessThan_renderOperator_rendersSimpleDatatypeValueTemplate() throws ConfigurationException{
		int conditionId = 1;
		OntoClass owlClass = kb.getOwlClass("http://purl.org/NET/cgo#StorageSpace");
		OntoProperty property = kb.getProperty("http://gridagents.sourceforge.net/AiGGridOntology#hasAvailableSize");
		
		PropertyConditionRenderer conditionRenderer = PropertyConditionRenderer
				.getRenderer(property.getClass());
		
		SpyRenderer renderer = new SpyRenderer();
		conditionRenderer.renderOperator(conditionId, owlClass, property, "lessThan", renderer);
		
		assertThat(renderer.actualTemplateName).isEqualTo("@Constraints.simpleDatatypeValue");
	}
	
	@Test
	public void forIntegerProperty_and_class_expression_constraint_renderProperty_rendersAllOperators() throws ConfigurationException{
		int conditionId = 1;
		OntoClass owlClass = kb.getOwlClass("http://purl.org/NET/cgo#StorageSpace");
		OntoProperty property = kb.getProperty("http://gridagents.sourceforge.net/AiGGridOntology#hasAvailableSize");
		
		PropertyConditionRenderer conditionRenderer = PropertyConditionRenderer
				.getRenderer(property.getClass());
		
		SpyRenderer renderer = new SpyRenderer();
		conditionRenderer.renderProperty(conditionId, owlClass, property, false, renderer);
		
		List<PropertyOperator> operators = (List<PropertyOperator>)renderer.actualArgs.get("operators");
		
		assertThat(operators).contains(
				new PropertyOperator("equalTo", "is equal to ", true),
				new PropertyOperator("greaterThan", "is greater than ", false),
				new PropertyOperator("lessThan", "is less than ", false)
				);
	}

	@Test
	public void forStringProperty_and_class_expression_constraint_renderProperty_rendersOnlyEqualToOperator() throws ConfigurationException{
		int conditionId = 1;
		OntoClass owlClass = kb.getOwlClass("http://purl.org/NET/cgo#StorageSpace");
		OntoProperty property = kb.getProperty("http://purl.org/NET/cgo#hasName");
		
		PropertyConditionRenderer conditionRenderer = PropertyConditionRenderer
				.getRenderer(property.getClass());
		
		SpyRenderer renderer = new SpyRenderer();
		conditionRenderer.renderProperty(conditionId, owlClass, property, false, renderer);
		
		List<PropertyOperator> operators = (List<PropertyOperator>)renderer.actualArgs.get("operators");
		
		assertThat(operators).containsOnly(new PropertyOperator("equalTo", "is equal to ", true));
	}
	
	@Test
	public void forFloatProperty_and_individual_description_renderProperty_rendersOnlyEqualToOperator() throws ConfigurationException{
		int conditionId = 1;
		OntoClass owlClass = kb.getOwlClass("http://purl.org/NET/cgo#Memory");
		OntoProperty property = kb.getProperty("http://gridagents.sourceforge.net/AiGGridOntology#hasTotalSize");
		
		PropertyConditionRenderer conditionRenderer = PropertyConditionRenderer
				.getRenderer(property.getClass());
		
		SpyRenderer renderer = new SpyRenderer();
		conditionRenderer.renderProperty(conditionId, owlClass, property, true, renderer);
		
		List<PropertyOperator> operators = (List<PropertyOperator>)renderer.actualArgs.get("operators");
		
		assertThat(operators).containsOnly(new PropertyOperator("equalTo", "is equal to ", true));
	}
	
	public class SpyRenderer implements Renderer{
		public String actualTemplateName;
		public Map<String, Object> actualArgs;
		public void renderTemplate(String templateName,
				Map<String, Object> args) {
			actualTemplateName = templateName;
			actualArgs = args;
		}
	}
}
