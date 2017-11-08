package ontoplay.controllers.configuration;

import ontoplay.OntoplayConfig;
import ontoplay.controllers.MainTemplate;
import org.apache.commons.io.FileUtils;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class UploadController extends Controller {

    private final OntoplayConfig config;
    private final MainTemplate mainTemplate;

    @Inject
    public UploadController(OntoplayConfig config, MainTemplate mainTemplate) {
        this.config = config;
        this.mainTemplate = mainTemplate;
    }

    /**
     * To replace the current annotation configuration with the original (empty and structured) one
     */

    public Result showUploadPage() {

        return ok(ontoplay.views.html.configuration.upload.render(config.getOntologyFileName(), config.getOntologyNamespace(),
                mainTemplate.getRenderFunction()));
    }

    public Result upload() {
        MultipartFormData body = request().body().asMultipartFormData();
        FilePart<File> ontologyFile = body.getFile("ontologyFile");
        String result = "";
        if (ontologyFile != null) {
            result += "file Name " + ontologyFile.getFilename() + "\n";
            result += "contentType " + ontologyFile.getContentType() + "\n";

            File file = ontologyFile.getFile();
            File destination = new File(config.getUploadsPath(), ontologyFile.getFilename());
            destination.delete();
            try {
                FileUtils.moveFile(file, destination);
            } catch (IOException e) {
                flash("error", "Missing file");

                // return redirect(routes.Application.index());
                return ok("Error");
            }
            Map<String, String[]> dataPart = request().body().asMultipartFormData().asFormUrlEncoded();
            String uri = dataPart.get("ontologyIRI")[0];

            try {
                config.updateOntologyConfig(ontologyFile.getFilename(), uri);
            } catch (IOException e) {
                e.printStackTrace();
                return internalServerError("Error updating configuration.");
            }

            resetAnnotationCF();
            //TODO: What to do with the below code? Check if works as it is.
            //new JenaOwlReaderConfiguration().initialize(OntologyUtils.file,new JenaOwlReaderConfig().useLocalMapping(OntologyUtils.iriString,OntologyUtils.fileName));
            //OntologyController.setObjects();
            return ok("File uploaded \n " + result + "\n URI " + uri);

        } else {
            flash("error", "Missing file");
            return ok("Error");
        }
    }

    private void resetAnnotationCF() {
        File original = new File(config.getOriginalAnnotationsFilePath());
        File currentFile = new File(config.getAnnotationsFilePath());
        try {
            currentFile.delete();
            FileUtils.copyFile(original, currentFile);
        } catch (IOException e) {
            System.out.println("Error replacing Annotation file " + e.toString());
        }
    }
}
