package ontoplay.models.ontologyReading.jena;

import java.util.HashMap;

public class JenaOwlReaderConfig {
	private HashMap<String, String> localMappings = new HashMap<String, String>();
	private boolean ignorePropsWithNoDomain;
	
	public JenaOwlReaderConfig useLocalMapping(String uri, String filePath) {
		localMappings.put(uri, filePath);
		return this;
	}
	
	public JenaOwlReaderConfig ignorePropertiesWithUnspecifiedDomain(){
		ignorePropsWithNoDomain = true;
		return this;
	}

	public boolean isIgnorePropsWithNoDomain(){
		return ignorePropsWithNoDomain;
	}
	
	public HashMap<String, String> getLocalMappings() {
		return localMappings;
	}

}
