import static org.junit.Assert.*;
import ontoplay.models.OntologyUtils;

import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;

import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.*;

public class OntologyUtilTest {

	@Test
	public void getNamespace_StripsHash() {
		String ns = OntologyUtils.getNamespace(IRI.create("http://foo.org#bar"));
		
		assertThat(ns, equalTo("http://foo.org"));		
	}
	
	@Test
	public void getNamespace_StripsSlash() {
		String ns = OntologyUtils.getNamespace(IRI.create("http://foo.org/bar"));
		
		assertThat(ns, equalTo("http://foo.org"));		
	}

}
