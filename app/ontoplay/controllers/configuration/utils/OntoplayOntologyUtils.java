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
