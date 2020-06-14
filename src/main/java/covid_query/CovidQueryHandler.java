package covid_query;

import java.util.Hashtable;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

public class CovidQueryHandler {
	
	private static void parseQuery(String Regiao, String Propriedade) {
		
		//TODO switch case of different queries
		
		String query = "//*[contains(@about,'"+Regiao+"')]/"+Propriedade+"/text()";
		System.out.println("parseQuery - For the query: " + Regiao + " and " + Propriedade);
		writeHTML(performQuery(query));
	}
	
	private static Object performQuery(String query) {
		System.out.println("Query - " + query);
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		Object results = null;
		Document doc = CovidFormCreator.openDocument();
		try {
			XPathExpression expr = xpath.compile(query);
			results = expr.evaluate(doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		System.out.println("results " + results);
		return results;
	}
	
	private static void writeHTML(Object results) {
		System.out.println("writing results aaaaaaaaaaaaaaa");
		System.out.println(cgi_lib.HtmlTop("Query Result"));
		System.out.println("<h2>"+results+"</h2>");
	}
	
	public static void main(String[] args) {
		System.out.println(cgi_lib.Header());

		// Parse the form data into a Hashtable.
		Hashtable form_data = cgi_lib.ReadParse(System.in);
		String Regiao = form_data.get("Regiao").toString();
		String Propriedade = form_data.get("Propriedade").toString();
		CovidQueryHandler.parseQuery(Regiao, Propriedade);
		System.out.println(cgi_lib.HtmlBot());
	}
}
