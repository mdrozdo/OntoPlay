package ontoplay.controllers.configuration.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ontoplay.controllers.utils.OntologyUtils;
import ontoplay.controllers.utils.PathesUtils;

/**
 * Read and set ontology configuration.
 * @author Motasem Alwazir
 *
 */
public class OntoplayOntologyUtils {

	public static void setOntologyInTheXml(String newOntologyName, String iri) {
			
		   try {
				XMLUtils xmlUtils = new XMLUtils(PathesUtils.ONTOLOGY_XML_FILE_PATH);
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

			XMLUtils xmlUtils = new XMLUtils(PathesUtils.ONTOLOGY_XML_FILE_PATH);
			NodeList nodeList = xmlUtils.getElementsbyName("ontology");
			Element element = (Element)nodeList.item(0);
			String ontologyName=element.getAttribute("name");
			String ontologyIri=element.getAttribute("iri");
			OntologyUtils.ontologyName=ontologyName;
			OntologyUtils.fileName = PathesUtils.UPLOADS_PATH+ontologyName;
			OntologyUtils.file = "file:"+PathesUtils.UPLOADS_PATH+ontologyName;
			OntologyUtils.iriString=ontologyIri;
			OntologyUtils.nameSpace=ontologyIri+"#";
		}catch(Exception e){
			
		}
	}
	/**
	 * To replace the current annotation configuration with the original (empty and structured) one
	 */
	public static void resetAnnotationCF() {
		File original=new File(PathesUtils.Annotation_xml_original);
		File currentFile =new File(PathesUtils.Annotation_XML_FILE_PATH);
		 try {
			 currentFile.delete();
				FileUtils.copyFile(original, currentFile);
			} catch (IOException e) {
				System.out.println("Error replacing Annotation file "+e.toString());
			}
	}

}
