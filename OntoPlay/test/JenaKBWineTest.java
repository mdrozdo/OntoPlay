import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jobs.PropertyTypeConfiguration;
import junit.framework.Assert;
import ontoplay.models.InvalidConfigurationException;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyReading.OntologyReader;
import ontoplay.models.ontologyReading.jena.JenaOwlReader;
import ontoplay.models.ontologyReading.jena.OwlPropertyFactory;
import ontoplay.models.ontologyReading.jena.propertyFactories.DateTimePropertyFactory;
import ontoplay.models.ontologyReading.jena.propertyFactories.FloatPropertyFactory;
import ontoplay.models.ontologyReading.jena.propertyFactories.IntegerPropertyFactory;
import ontoplay.models.ontologyReading.jena.propertyFactories.ObjectPropertyFactory;
import ontoplay.models.ontologyReading.jena.propertyFactories.StringPropertyFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class JenaKBWineTest {
	private OntologyReader kb;

	@Before
	public void setupJena() throws Exception {
		kb = JenaOwlReader.loadFromFile("file:test/wine.owl");	//TODO: Map imported ontology redirections - fails without internet connection.

		OwlPropertyFactory.registerPropertyFactory(new IntegerPropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new FloatPropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new DateTimePropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new StringPropertyFactory());
		OwlPropertyFactory.registerPropertyFactory(new ObjectPropertyFactory());
		
	}

	@Test
	public void getClass_ReturnsClassForClassName() throws Exception {
		OntoClass grapeClass = kb.getOwlClass("WineGrape");
		Assert.assertNotNull(grapeClass);
		Assert.assertEquals("WineGrape", grapeClass.getLocalName());
	}

	@Test
	public void getClass_ReturnsClassWithProperties() throws Exception {
		OntoClass wineClass = kb.getOwlClass("Wine");
				
		assertThat(selectLocalNames(wineClass.getProperties())).contains(
				"madeFromFruit", "hasWineDescriptor",
				"madeFromGrape", "hasSugar", "hasFlavor", "hasBody",
				"hasColor", "locatedIn");
	}

	private List<String> selectLocalNames(List<OntoProperty> properties) {
		List<String> names = new ArrayList<String>(properties.size());

		for (OntoProperty prop : properties) {
			names.add(prop.getLocalName());
		}

		return names;
	}

}
