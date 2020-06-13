package covid_query;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CovidQuery {

	public CovidQuery() {

	}

	void downloadFile() {
		Repository repository = app.Utils.getGitRepository();
		try {
			ObjectId lastCommitId = repository.resolve(Constants.HEAD);
			RevWalk revWalk = new RevWalk(repository);
			RevCommit commit = revWalk.parseCommit(lastCommitId);
			RevTree tree = commit.getTree();
			try {
				TreeWalk treeWalk = new TreeWalk(repository);
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);
				treeWalk.setFilter(PathFilter.create("covid19spreading.rdf"));
				if (!treeWalk.next()) {
					treeWalk.close();
					throw new IllegalStateException("Did not find expected file 'covid19spreading.rdf'");
				}

				ObjectId objectId = treeWalk.getObjectId(0);
				ObjectLoader loader = repository.open(objectId);
//				loader.copyTo(System.out);
				byte[] bytes = loader.getBytes();
				FileOutputStream fos = new FileOutputStream("covid19spreading.rdf");
				fos.write(bytes);
				fos.close();
				treeWalk.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			revWalk.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Document openDocument() {
		File inputFile = new File("covid19spreading.rdf");
		if(!inputFile.isFile()) {
			downloadFile();
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
	
	public void performQuery(String query) {
		Document doc = openDocument();
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			XPathExpression expr = xpath.compile(query);
			System.out.println(expr.evaluate(doc, XPathConstants.STRING));
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	public void createHTMLForm() {
		FileWriter fWriter = null;
		BufferedWriter writer = null;
		try {
			fWriter = new FileWriter("C:\\Users\\jmjmf\\git\\ES2-2020-EIC2-26\\src\\main\\java\\covid_query\\formulario.html");
			writer = new BufferedWriter(fWriter);
			writer.write("<!DOCTYPE html>\r\n" + 
			    		"<html>\r\n" + 
			    		"<head>\r\n" + 
			    		"<meta charset=\"ISO-8859-1\">\r\n" + 
			    		"<title>Covid Query</title>\r\n" +
			    		"<h1>Welcome to Covid Query</h1>\r\n" +
			    		"</head>\r\n" + 
			    		"\r\n" +
			    		"<style>\r\n" +
			    		//Definir estilo (?)
			    		"</style>\r\n" +
			    		"<body>\r\n" + 
			    		/*
			    		 * Corpo !
			    		 */
			    		"	<form action=\"CENINHA DO CGI AHHHHHHHHH\" method=\"get\">\r\n" +
			    		"		<select id=\"Regiao\" name=\"Regiao\">\r\n" +
			    		"			<option value=\"1\">1</option>\r\n" +
			    		"			<option value=\"2\">2</option>\r\n" +
			    		"			<option value=\"3\">3</option>\r\n" +
			    		"			<option value=\"4\">4</option>\r\n" +
			    		"		</select>\r\n" +
			    		"	</form>\r\n" + 
			    		"</body>\r\n" + 
			    		"</html>");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		    
	}

	public static void main(String[] args) {
		
		CovidQuery test = new CovidQuery();
		test.performQuery("//*[contains(@about,'Algarve')]/Internamentos/text()");
		test.createHTMLForm();

		/*try {
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

		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}
