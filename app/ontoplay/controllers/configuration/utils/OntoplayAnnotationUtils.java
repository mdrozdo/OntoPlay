package ontoplay.controllers.configuration.utils;

import ontoplay.models.angular.AnnotationCFDTO;
import ontoplay.models.angular.AnnotationDTO;
import ontoplay.models.angular.ComponentCF;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Help manipulating and reading the configuration in the Annotation CF XML file
 * <p>
 * Note 1: Possible to use to JAXB to marshall  and unmarshall  objects with elements.
 * Note 2: IrI is used as an identifier and not id because the Ontology knows nothing about the ids here
 * </p>
 *
 * @author Motasem Alwazir
 */
public class OntoplayAnnotationUtils {
    public final String ANNOTATION_ELEMENT_NAME = "Annotation";
    public final String ANNOTATION_ID_ATTRIBUTE = "id";
    public final String ANNOTATION_LOCAL_NAME_ATTRIBUTE = "name";
    public final String ANNOTATION_IRI_ATTRIBUTE = "iri";
    public final String RELATION_ELEMENT_NAME = "Relation";
    public final String RELATION_COMPONENT_ID_ATTRIBUTE = "componentId";
    public final String RELATION_ANNOTATION_ID_ATTRIBUTE = "annotationId";
    public final String RELATION_ANNOTATION_INPUT_TYPE = "type";
    public final String COMPONENT_ELEMENT_NAME = "Component";
    public final String COMPONENT_ID_ATTRIBUTE = "id";
    public final String COMPONENT_TYPE_ATTRIBUTE = "type";
    public final String COMPONENT_IRI_ATTRIBUTE = "iri";
    private final String COMPONENT_NAME_ATTRIBUTE = "name";
    public XMLUtils utils;

    public OntoplayAnnotationUtils(String filePath) {
        try {
            utils = new XMLUtils(filePath);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out
                    .println(OntoplayAnnotationUtils.class.toString() + " : Error creating Ontoply XMLUtils " + e.toString());
            utils = null;
        }

    }


    public void createRelation(String annotationName, String annotationIri, String componentType, String componentName,
                               String copmonentIri, String relationType) throws Exception {
        Element annotationElement, componentElement, relationElement;
        annotationElement = getOrCreateAnnotationElement(annotationName, annotationIri);
        componentElement = getOrCreateComponentElement(componentType, componentName, copmonentIri);
        relationElement = getRelationByAnnotationIdAndComponentId(
                annotationElement.getAttribute(ANNOTATION_ID_ATTRIBUTE),
                componentElement.getAttribute(COMPONENT_ID_ATTRIBUTE));

        //delete relation if it exists
        if (relationElement != null)
            relationElement.getParentNode().removeChild(relationElement);
        createRelationElement(annotationElement.getAttribute(ANNOTATION_ID_ATTRIBUTE),
                componentElement.getAttribute(COMPONENT_ID_ATTRIBUTE), relationType);

        utils.prettyPrint();

    }

    private void createRelationElement(String annotationId, String componentId, String relationType) {
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(RELATION_ANNOTATION_ID_ATTRIBUTE, annotationId);
        attributes.put(RELATION_COMPONENT_ID_ATTRIBUTE, componentId);
        attributes.put(RELATION_ANNOTATION_INPUT_TYPE, relationType);
        utils.createElementWithAttributes(RELATION_ELEMENT_NAME, attributes);
    }

    /**
     * Get or create annotation element in the Annotations XML configuration
     *
     * @return The annotation element
     * @throws XPathExpressionException
     */
    private Element getOrCreateAnnotationElement(String annotationName, String annotationIri)
            throws XPathExpressionException {

        Element annotationElement = getAnnotationElementByIri(annotationIri);
        //System.out.println(annotationElement.getAttribute(ANNOTATION_ID_ATTRIBUTE));
        if (annotationElement == null)
            annotationElement = createAnnotationElement(annotationName, annotationIri);

        return annotationElement;
    }

    private Element getAnnotationElementByIri(String annotationIri) throws XPathExpressionException {
        NodeList nodes = utils.getElementByNameAndAttribute(ANNOTATION_ELEMENT_NAME, ANNOTATION_IRI_ATTRIBUTE,
                annotationIri);
        if (nodes.getLength() == 1)
            return (Element) nodes.item(0);
        return null;
    }

    private Element createComponentyeElement(String componentType, String componentName, String componentIRI) {
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(ANNOTATION_ID_ATTRIBUTE, new UID() + "");
        attributes.put(COMPONENT_TYPE_ATTRIBUTE, componentType);
        attributes.put(COMPONENT_NAME_ATTRIBUTE, componentName);
        attributes.put(COMPONENT_IRI_ATTRIBUTE, componentIRI);
        return utils.createElementWithAttributes(COMPONENT_ELEMENT_NAME, attributes);
    }

    private Element createAnnotationElement(String annotationName, String annotationIRI) {
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(ANNOTATION_ID_ATTRIBUTE, new UID() + "");
        attributes.put(ANNOTATION_LOCAL_NAME_ATTRIBUTE, annotationName);
        attributes.put(ANNOTATION_IRI_ATTRIBUTE, annotationIRI);
        return utils.createElementWithAttributes(ANNOTATION_ELEMENT_NAME, attributes);
    }

    /**
     * Get or create Component element in the Annotations XML configuration
     *
     * @return The component element
     * @throws XPathExpressionException
     */

    private Element getOrCreateComponentElement(String type, String name, String componentUri)
            throws XPathExpressionException {
        Element componentElement;
        componentElement = getComponentElementByUri(componentUri);
        if (componentElement == null)
            componentElement = createComponentyeElement(type, name, componentUri);
        return componentElement;
    }

    private Element getComponentElementByUri(String componentUri) throws XPathExpressionException {
        NodeList nodes = utils.getElementByNameAndAttribute(COMPONENT_ELEMENT_NAME, COMPONENT_IRI_ATTRIBUTE,
                componentUri);
        if (nodes.getLength() == 1)
            return (Element) nodes.item(0);
        return null;
    }

    public void deleteRelationByAnnotationIdAndComponentId(String annotationId, String componentId) throws Exception {
        Element relationElement = getRelationByAnnotationIdAndComponentId(annotationId, componentId);
        if (relationElement != null) {
            relationElement.getParentNode().removeChild(relationElement);
        }
        utils.prettyPrint();

    }

    private Element getRelationByAnnotationIdAndComponentId(String annotationId, String componentId)
            throws XPathExpressionException {
        Map<String, String> att = new HashMap<String, String>();
        att.put(RELATION_ANNOTATION_ID_ATTRIBUTE, annotationId);
        att.put(RELATION_COMPONENT_ID_ATTRIBUTE, componentId);
        NodeList nodes = utils.getElementByNameAndAttributes(RELATION_ELEMENT_NAME, att);
        if (nodes.getLength() == 1) {
            return (Element) nodes.item(0);
        }
        return null;
    }

    public AnnotationCFDTO getComponentsByAnnotation(String annotationIri) throws XPathExpressionException {

        AnnotationCFDTO annotationCFDTO = new AnnotationCFDTO();

        Element annotationElement;
        NodeList tempCopmonents;
        NodeList nodes = utils.getElementByNameAndAttribute(ANNOTATION_ELEMENT_NAME, ANNOTATION_IRI_ATTRIBUTE,
                annotationIri);
        if (nodes.getLength() == 0)
            return annotationCFDTO;
        //get the element
        annotationElement = (Element) nodes.item(0);

        //initialize the object using the element
        annotationCFDTO = new AnnotationCFDTO(annotationElement.getAttribute(ANNOTATION_ID_ATTRIBUTE),
                annotationElement.getAttribute(ANNOTATION_IRI_ATTRIBUTE),
                annotationElement.getAttribute(ANNOTATION_LOCAL_NAME_ATTRIBUTE));

        nodes = utils.getElementByNameAndAttribute(RELATION_ELEMENT_NAME, RELATION_ANNOTATION_ID_ATTRIBUTE,
                annotationElement.getAttribute(ANNOTATION_ID_ATTRIBUTE));
        Element relationTemp, componentTemp;
        //Temp object to hold the component inforamation before adding it to the list in AnnotationCF object
        ComponentCF tempComponentCF;
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                // This is relation element
                relationTemp = (Element) nodes.item(i);

                tempComponentCF = new ComponentCF();
                //set component id
                tempComponentCF.setComponentId(relationTemp.getAttribute(RELATION_COMPONENT_ID_ATTRIBUTE));
                tempComponentCF.setInputType(relationTemp.getAttribute(RELATION_ANNOTATION_INPUT_TYPE));

                tempCopmonents = utils.getElementByNameAndAttribute(COMPONENT_ELEMENT_NAME, COMPONENT_ID_ATTRIBUTE,
                        relationTemp.getAttribute(RELATION_COMPONENT_ID_ATTRIBUTE));
                componentTemp = (Element) tempCopmonents.item(0);
                //Set component type
                tempComponentCF.setComponentType(componentTemp.getAttribute(COMPONENT_TYPE_ATTRIBUTE));
                //Set component name
                tempComponentCF.setComponentName(componentTemp.getAttribute(COMPONENT_NAME_ATTRIBUTE));
                //Set component Iri
                tempComponentCF.setComponentIri(componentTemp.getAttribute(COMPONENT_IRI_ATTRIBUTE));
                annotationCFDTO.addComponent(tempComponentCF);
            }

        }
        return annotationCFDTO;
    }

    public void deleteAllRelationForAnnotaionByAnnotationId(String annotationId) throws Exception {
        NodeList relationNodes = utils.getElementByNameAndAttribute(RELATION_ELEMENT_NAME,
                RELATION_ANNOTATION_ID_ATTRIBUTE, annotationId);
        Node tempNode;
        Element tempElement;
        for (int i = 0; i < relationNodes.getLength(); i++) {
            tempNode = relationNodes.item(i);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                tempElement = (Element) tempNode;
                tempElement.getParentNode().removeChild(tempElement);
            }
        }
        utils.prettyPrint();
    }

    public String getAnnotationIdByAnnotationIri(String annotationIri) throws XPathExpressionException {
        Element annotationElement = getAnnotationElementByIri(annotationIri);
        return annotationElement.getAttribute(ANNOTATION_ID_ATTRIBUTE);
    }

    public List<AnnotationDTO> getAnnotationForComponentByComponentUri(String componentUri) throws XPathExpressionException {
        List<AnnotationDTO> annotations = new ArrayList<AnnotationDTO>();
        //get the component Element
        Element tempComponent = getComponentElementByUri(componentUri);
        //exit if element doesn't exist
        if (tempComponent == null)
            return annotations;

        NodeList relationNodes = utils.getElementByNameAndAttribute(RELATION_ELEMENT_NAME, RELATION_COMPONENT_ID_ATTRIBUTE, tempComponent.getAttribute(COMPONENT_ID_ATTRIBUTE));
        NodeList tempAnnotaitons;
        Element relationTemp, tempAnnotation;
        AnnotationDTO tempAnnotationDTO;
        for (int i = 0; i < relationNodes.getLength(); i++) {
            if (relationNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                // This is relation element
                relationTemp = (Element) relationNodes.item(i);

                tempAnnotationDTO = new AnnotationDTO();
                //set input type id
                tempAnnotationDTO.setInputType(relationTemp.getAttribute(RELATION_ANNOTATION_INPUT_TYPE));

                //get all annotations
                tempAnnotaitons = utils.getElementByNameAndAttribute(ANNOTATION_ELEMENT_NAME, ANNOTATION_ID_ATTRIBUTE,
                        relationTemp.getAttribute(RELATION_ANNOTATION_ID_ATTRIBUTE));
                tempAnnotation = (Element) tempAnnotaitons.item(0);
                //Set name
                tempAnnotationDTO.setLocalName(tempAnnotation.getAttribute(ANNOTATION_LOCAL_NAME_ATTRIBUTE));
                //Set Annotation Uri
                tempAnnotationDTO.setUri(tempAnnotation.getAttribute(ANNOTATION_IRI_ATTRIBUTE));

                annotations.add(tempAnnotationDTO);
            }

        }

        return annotations;
    }


}
