package ontoplay.controllers.configuration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import controllers.configuration.utils.OntoplayOntologyUtils;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

public class UploadController extends Controller {
	public static Result showUploadPage() {
		return ok(views.html.configuration.upload.render(OntologyHelper.ontologyName,OntologyHelper.iriString));
	}

	public static Result upload() {
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart ontologyFile = body.getFile("ontologyFile");
		String result = "";
		if (ontologyFile != null) {
			result += "file Name " + ontologyFile.getFilename()+"\n";
			result += "contentType " + ontologyFile.getContentType()+"\n";
		
			//File file = ontologyFile.getFile();
			File file = ontologyFile.getFile();
		    File destination = new File(Pathes.UPLOADS_PATH, ontologyFile.getFilename());
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
			return ok("File uploaded \n "+result+"\n URI "+uri);
		} else {
			flash("error", "Missing file");

			// return redirect(routes.Application.index());
			return ok("Error");
		}
	}
}
