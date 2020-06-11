package covid_query;

import java.io.File;
import org.apache.commons.lang3.StringUtils;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XmlProject {
   public static void main(String[] args){
      try {	
         File inputFile = new File("covid19spreading.rdf");    	      	  
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();         
         
         String query = "/RDF/NamedIndividual/@*";
         System.out.println("Query para obter a lista das regiões: " + query);
         XPathFactory xpathFactory = XPathFactory.newInstance();
         XPath xpath = xpathFactory.newXPath();
         XPathExpression expr = xpath.compile(query);         
         NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
         for (int i = 0; i < nl.getLength(); i++) {
        	 System.out.println(nl.item(i).getNodeValue());
        	 System.out.println(StringUtils.substringAfter(nl.item(i).getNodeValue(), "#"));
         }

         query = "//*[contains(@about,'Algarve')]/Testes/text()";  
         System.out.println("Query para obter o número de testes feitos no Algarve: " + query);
         expr = xpath.compile(query);     
         System.out.println(expr.evaluate(doc, XPathConstants.STRING));
         
         query = "//*[contains(@about,'Algarve')]/Infecoes/text()";
         System.out.println("Query para obter o número de infeções no Algarve: " + query);
         expr = xpath.compile(query);
         System.out.println(expr.evaluate(doc, XPathConstants.STRING));
         
         query = "//*[contains(@about,'Algarve')]/Internamentos/text()";
         System.out.println("Query para obter o número de internamentos no Algarve: " + query);
         expr = xpath.compile(query);     
         System.out.println(expr.evaluate(doc, XPathConstants.STRING));
         
	} catch (Exception e) { e.printStackTrace(); }
   }
}
