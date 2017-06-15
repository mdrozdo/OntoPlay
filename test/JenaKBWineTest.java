import fakes.FakeOntoplayConfig;
import junit.framework.Assert;
import ontoplay.Module;
import ontoplay.OntoplayConfig;
import ontoplay.models.ConfigurationException;
import ontoplay.models.ontologyModel.OntoClass;
import ontoplay.models.ontologyModel.OntoProperty;
import ontoplay.models.ontologyReading.OntologyReader;
import ontoplay.models.ontologyReading.OntologyReaderFactory;
import ontoplay.models.ontologyReading.jena.FolderMapping;
import org.junit.Before;
import org.junit.Test;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class JenaKBWineTest {
	private OntologyReader kb;

	@Before
	public void setup() throws ConfigurationException {
		OntoplayConfig config = new FakeOntoplayConfig();

		Injector injector = new GuiceInjectorBuilder()
				.bindings(new Module(config))
				.injector();

		OntologyReaderFactory kbFactory = injector.instanceOf(OntologyReaderFactory.class);
		kb = kbFactory.create("./wine.owl", Arrays.asList(new FolderMapping("http://www.w3.org/TR/2003/PR-owl-guide-20031209/food", "./food.owl")), true);
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
