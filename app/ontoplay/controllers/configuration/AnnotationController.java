package ontoplay.controllers.configuration;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import ontoplay.OntoplayConfig;
import ontoplay.controllers.OntologyController;
import ontoplay.controllers.configuration.utils.OntoplayAnnotationUtils;
import ontoplay.controllers.utils.OntologyUtils;
import ontoplay.models.angular.AnnotationDTO;
import ontoplay.models.angular.OwlElementDTO;
import ontoplay.models.ontologyReading.OntologyReader;
import openllet.jena.PelletReasonerFactory;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Handle HTTP requests related to annotation configuration
 *
 * @author Motasem Alwazir
 */
public class AnnotationController extends OntologyController {
    private OntoplayAnnotationUtils ontoplayAnnotationUtils;
    private OntoplayConfig config;
    private FormFactory formFactory;
    private OntologyReader ontologyReader;

    @Inject
    public AnnotationController(OntologyUtils ontologyUtils, OntologyReader ontologyReader, OntoplayAnnotationUtils ontoplayAnnotationUtils, OntoplayConfig config, FormFactory formFactory) {
        super(ontologyUtils);
        this.ontologyReader = ontologyReader;
        this.ontoplayAnnotationUtils = ontoplayAnnotationUtils;
        this.config = config;
        this.formFactory = formFactory;
    }

    private static JsonObject getSuccessJsonObject() {
        JsonObject object = new JsonObject();
        object.addProperty("success", true);
        return object;
    }

    public Result showAnnotationCFPage() {
        return ok(ontoplay.views.html.configuration.annotations.render());
    }

    public Result getAnnotationForCFPage() {

        Set<AnnotationDTO> annotations = ontologyReader.getAnnotations(true);
        return ok(new GsonBuilder().create().toJson(annotations));
    }

    /**
     * @return classes, object properties and data properties as JSON object
     */
    public Result getOntologyComponents() {
        OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
        FileManager.get().readModel(model, new File(config.getOntologyFileName()).toURI().toString());
        Map<String, List<OwlElementDTO>> results = new HashMap<String, List<OwlElementDTO>>();

        ExtendedIterator<OntClass> classes = model.listClasses();
        List<OwlElementDTO> tempDTOs = new ArrayList<OwlElementDTO>();

        while (classes.hasNext()) {
            OntClass temp = classes.next();
            if (isFromTheOntologyNameSpace(temp.getURI()))
                tempDTOs.add(new OwlElementDTO(temp.getURI(), temp.getLocalName()));
        }
        results.put("Classes", tempDTOs);


        ExtendedIterator<DatatypeProperty> dataProperties = model.listDatatypeProperties();
        tempDTOs = new ArrayList<OwlElementDTO>();
        while (dataProperties.hasNext()) {
            DatatypeProperty temp = dataProperties.next();
            if (isFromTheOntologyNameSpace(temp.getURI()))
                tempDTOs.add(new OwlElementDTO(temp.getURI(), temp.getLocalName()));
        }
        results.put("Data Properties", tempDTOs);

        ExtendedIterator<ObjectProperty> objectProperties = model.listObjectProperties();
        tempDTOs = new ArrayList<OwlElementDTO>();
        while (objectProperties.hasNext()) {
            ObjectProperty temp = objectProperties.next();
            if (isFromTheOntologyNameSpace(temp.getURI()))
                tempDTOs.add(new OwlElementDTO(temp.getURI(), temp.getLocalName()));
        }
        results.put("Object Properties", tempDTOs);


        return ok(new GsonBuilder().create().toJson(results));
    }

    private boolean isFromTheOntologyNameSpace(String uri) {
        return uri != null && uri.indexOf(config.getOntologyNamespace()) != -1;
    }

    public Result getRelationsByAnnotationIri(String annotationIri) {
        try {
            annotationIri = java.net.URLDecoder.decode(annotationIri, "UTF-8");
            return ok(new GsonBuilder().create().toJson(ontoplayAnnotationUtils.getComponentsByAnnotation(annotationIri)));
        } catch (XPathExpressionException | UnsupportedEncodingException e) {
            System.out.println("error in getting components by annotation iri " + e.toString());
            return badRequest();
        }
    }

    public Result AddRelation() {
        DynamicForm dynamicForm = formFactory.form().bindFromRequest();
        String annotationIri = dynamicForm.get("annotationIri");
        String annotationName = dynamicForm.get("annotationName");
        String componentIri = dynamicForm.get("componentIri");
        String componentName = dynamicForm.get("componentName");
        String componentType = dynamicForm.get("componentType");
        String relationType = dynamicForm.get("relationType");
        try {
            ontoplayAnnotationUtils.createRelation(annotationName, annotationIri, componentType, componentName, componentIri,
                    relationType);

            return ok(new GsonBuilder().create().toJson(getSuccessJsonObject()));
        } catch (Exception e) {
            return badRequest();
        }

    }

    public Result deleteRelation() {
        DynamicForm dynamicForm = formFactory.form().bindFromRequest();
        String annotationId = dynamicForm.get("annotationId");
        String componentId = dynamicForm.get("componentId");
        try {
            ontoplayAnnotationUtils.deleteRelationByAnnotationIdAndComponentId(annotationId, componentId);
            return ok(new GsonBuilder().create().toJson(getSuccessJsonObject()));
        } catch (Exception e) {
            return badRequest();
        }
    }

    public Result deleteAllRelations() {
        DynamicForm dynamicForm = formFactory.form().bindFromRequest();
        String annotationId = dynamicForm.get("annotationId");
        try {
            ontoplayAnnotationUtils.deleteAllRelationForAnnotaionByAnnotationId(annotationId);
            return ok(new GsonBuilder().create().toJson(getSuccessJsonObject()));
        } catch (Exception e) {
            return badRequest();
        }
    }

}
