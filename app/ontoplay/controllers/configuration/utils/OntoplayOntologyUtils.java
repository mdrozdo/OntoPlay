package ontoplay.controllers.configuration.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ontoplay.OntologyHelper;
import ontoplay.Pathes;
import ontoplay.jobs.JenaOwlReaderConfiguration;
import ontoplay.models.ontologyReading.jena.JenaOwlReaderConfig;

/**
 * Read and set ontology configuration.
 * @author Motasem Alwazir
 *
 */
public class OntoplayOntologyUtils {

	public static void setOntologyInTheXml(String newOntologyName, String iri) {
			
		   try {
				XMLUtils xmlUtils = new XMLUtils(Pathes.ONTOLOGY_XML_FILE_PATH);
				NodeList nodeList = xmlUtils.getElementsbyName("ontology");
				Element element = (Element)nodeList.item(0);
				element.setAttribute("name", newOntologyName);
				element.setAttribute("iri", iri);
				xmlUtils.prettyPrint();
		   }catch(Exception e){
			   System.out.println("Error "+e.toString());
			   e.printStackTrace(System.out);
		   }

	}

	public static void setOntologyCF() {
		//we need to read the ontology name from the XML CF
		try {

			XMLUtils xmlUtils = new XMLUtils(Pathes.ONTOLOGY_XML_FILE_PATH);
			NodeList nodeList = xmlUtils.getElementsbyName("ontology");
			Element element = (Element)nodeList.item(0);
			String ontologyName=element.getAttribute("name");
			String ontologyIri=element.getAttribute("iri");
			OntologyHelper.ontologyName=ontologyName;
			OntologyHelper.fileName = Pathes.UPLOADS_PATH+ontologyName;
			OntologyHelper.file = "file:"+Pathes.UPLOADS_PATH+ontologyName;
			OntologyHelper.iriString=ontologyIri;
			OntologyHelper.nameSpace=ontologyIri+"#";
		}catch(Exception e){
			
		}
	}
	/**
	 * To replace the current annotation configuration with the original (empty and structured) one
	 */
	public static void resetAnnotationCF() {
		File original=new File(Pathes.Annotation_xml_original);
		File currentFile =new File(Pathes.Annotation_XML_FILE_PATH);
		 try {
			 currentFile.delete();
				FileUtils.copyFile(original, currentFile);
			} catch (IOException e) {
				System.out.println("Error replacing Annotation file "+e.toString());
			}
	}

}
