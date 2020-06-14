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
		writeHTML(performQuery(query));
	}
	
	private static Object performQuery(String query) {
		Document doc = CovidFormCreator.openDocument();
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		Object results = null;
		try {
			XPathExpression expr = xpath.compile(query);
			results = expr.evaluate(doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	private static void writeHTML(Object results) {
		System.out.println(cgi_lib.Header());
		System.out.println(cgi_lib.HtmlTop("Query Result"));
		System.out.println("<h2>"+results+"</h2>");
		System.out.println(cgi_lib.HtmlBot());
	}
	
	public static void main(String[] args) {
		System.out.println(cgi_lib.Header());
		

	      // Parse the form data into a Hashtable.
		Hashtable form_data = cgi_lib.ReadParse(System.in);
	      
//	    System.out.println(cgi_lib.Variables(form_data));
		String Regiao = form_data.get("Regiao").toString();
		String Propriedade = form_data.get("Propriedade").toString();
		CovidQueryHandler.parseQuery(Regiao, Propriedade);
	      
//	    System.out.println(cgi_lib.HtmlBot());
	}
}
