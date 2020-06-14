package covid_query;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Classe para obter os dados do formulário e aplicar queries
 * 
 * @author jmjmf-iscteiul
 *
 */
public class CovidQueryHandler {

	/**
	 * Constrói a query a utilizar
	 * @param Regiao - a regiao para ser usada na query
	 * @param Propriedade - a propriedade a ser usada na query
	 */
	private static void parseQuery(String Regiao, String Propriedade) {

		// TODO switch case of different queries

		String query = "//*[contains(@about,'" + Regiao + "')]/" + Propriedade + "/text()";
		writeHTML(performQuery(query));
	}

	/**
	 * Devolve o documento rdf para fazer os queries
	 * 
	 * @return Document
	 */
	static Document openDocument() {
		File inputFile = new File("covid19spreading.rdf");
		if (!inputFile.isFile()) {
			CovidFormCreator.downloadFile();
			inputFile = new File("covid19spreading.rdf");
		}
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inputFile);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * Aplica a query ao documento e devolve a resposta
	 * @param query - a query a aplicar
	 * @return o resultado da query
	 */
	private static Object performQuery(String query) {
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		Object result = null;
		Document doc = openDocument();
		try {
			XPathExpression expr = xpath.compile(query);
			result = expr.evaluate(doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		System.out.println("result " + result);
		return result;
	}

	/**
	 * Escreve os resultados da query na página
	 * 
	 * @param results - resultados a escrever
	 */
	private static void writeHTML(Object results) {
		System.out.println(cgi_lib.HtmlTop("Query Result"));
		System.out.println("<h2>" + results + "</h2>");
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
