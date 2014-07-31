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

import junit.framework.Assert;

import models.InvalidConfigurationException;
import models.ontologyModel.OntoClass;
import models.ontologyModel.OntoProperty;
import models.ontologyReading.OntologyReader;
import models.ontologyReading.jena.JenaOwlReader;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class JenaKBWineTest {
	private OntologyReader kb;

	@Before
	public void setupJena() {
		kb = JenaOwlReader.loadFromFile("file:test/wine.owl");	//TODO: Map imported ontology redirections - fails without internet connection. 
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
