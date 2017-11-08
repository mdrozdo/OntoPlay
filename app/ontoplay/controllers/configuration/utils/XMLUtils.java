package ontoplay.controllers.configuration.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * General XML utils . Can be used in any project
 *
 * @author Motasem Alwazir
 */
public class XMLUtils {
    private Document xmlDocument;
    private final File xmlFile;

    public XMLUtils(String filePath) throws ParserConfigurationException, SAXException, IOException {
        this.xmlFile = new File(filePath);
        this.xmlDocument = createXmlDocument();
    }

    private Document createXmlDocument() throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document xmlDocument = dBuilder.parse(xmlFile);

        // optional, but recommended
        // read this -
        // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        xmlDocument.getDocumentElement().normalize();
        return xmlDocument;
    }

    public String getRootElement() {
        return xmlDocument.getDocumentElement().getNodeName();
    }

    public final NodeList getElementsbyName(String name) {
        NodeList nodeList = xmlDocument.getElementsByTagName(name);
        return nodeList;
    }

    public final NodeList getElementByNameAndAttribute(String name, String attribute, String attributeValue)
            throws XPathExpressionException {
        String expression = "//" + name + "[@" + attribute + "='" + attributeValue + "']";

        XPathFactory factory = XPathFactory.newInstance();

        XPath xpath = factory.newXPath();
        NodeList nodeList = (NodeList) xpath.evaluate(expression, xmlDocument, XPathConstants.NODESET);
        return nodeList;

    }

    public final NodeList getElementByNameAndAttributes(String name, Map<String, String> attributes)
            throws XPathExpressionException {
        String attributeExpression = "[@%s='%s']";
        StringBuilder expressionBuilder = new StringBuilder().append("//").append(name);
        for (String key : attributes.keySet()) {
            expressionBuilder.append(String.format(attributeExpression, key, attributes.get(key)));
        }

        XPathFactory factory = XPathFactory.newInstance();

        XPath xpath = factory.newXPath();
        NodeList nodeList = (NodeList) xpath.evaluate(expressionBuilder.toString(), xmlDocument, XPathConstants.NODESET);
        return nodeList;

    }

    public final void prettyPrint() throws Exception {

        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        StreamResult out = new StreamResult(xmlFile);
        tf.transform(new DOMSource(xmlDocument), out);

    }

    public final Element createElementWithAttributes(String ElementName, Map<String, String> attributes) {
        Element element = xmlDocument.createElement(ElementName);


        for (String key : attributes.keySet()) {
            element.setAttribute(key, attributes.get(key));
        }

        xmlDocument.getDocumentElement().appendChild(element);
        return element;
    }

}
