package ontoplay.controllers.configuration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import ontoplay.controllers.OntologyController;
import ontoplay.controllers.configuration.utils.OntoplayOntologyUtils;
import ontoplay.controllers.utils.OntologyUtils;
import ontoplay.controllers.utils.PathesUtils;
import ontoplay.jobs.JenaOwlReaderConfiguration;
import ontoplay.jobs.OntologyGeneratorConfiguration;
import ontoplay.jobs.PropertyTypeConfiguration;
import ontoplay.models.ontologyReading.jena.JenaOwlReaderConfig;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

public class UploadController extends Controller {
	public static Result showUploadPage() {
		return ok(ontoplay.views.html.configuration.upload.render(OntologyUtils.ontologyName,OntologyUtils.iriString));
	}

	public static Result upload() {
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart<File> ontologyFile = body.getFile("ontologyFile");
		String result = "";
		if (ontologyFile != null) {
			result += "file Name " + ontologyFile.getFilename()+"\n";
			result += "contentType " + ontologyFile.getContentType()+"\n";
		
			//File file = ontologyFile.getFile();
			File file = ontologyFile.getFile();
		    File destination = new File(PathesUtils.UPLOADS_PATH, ontologyFile.getFilename());
		    destination.delete();
		    try {
				FileUtils.moveFile(file, destination);
			} catch (IOException e) {
				flash("error", "Missing file");

				// return redirect(routes.Application.index());
				return ok("Error");
			}
		      Map<String,String[]> dataPart = request().body().asMultipartFormData().asFormUrlEncoded();
	          String uri = dataPart.get("ontologyIRI")[0];

			OntoplayOntologyUtils.setOntologyInTheXml(ontologyFile.getFilename(),uri);
			OntoplayOntologyUtils.setOntologyCF();
			OntoplayOntologyUtils.resetAnnotationCF();
				new JenaOwlReaderConfiguration().initialize(OntologyUtils.file,new JenaOwlReaderConfig().useLocalMapping(OntologyUtils.iriString,OntologyUtils.fileName));
				OntologyController.setObjects();
			return ok("File uploaded \n "+result+"\n URI "+uri);
			
		} else {
			flash("error", "Missing file");
			return ok("Error");
		}
	}
}
