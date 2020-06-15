package covid_query;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.apache.commons.lang3.StringUtils;

/**
 * Classe responsável por gerar o Formulário
 * 
 * @author jmjmf-iscteiul
 *
 */
public class CovidFormCreator {

	private static String[] operators = { "=", "!", "!=", "<", ">", "<=", ">=" };

	/**
	 * Faz download do ficheiro "covidspreading.rdf" do repositório
	 */
	static void downloadFile() {
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

	/**
	 * Devolve o documento rdf para fazer ir buscar as propriedades e regioes
	 * 
	 * @return documento
	 */
	static Document openDocument() {
		File inputFile = new File("covid19spreading.rdf");
		if (!inputFile.isFile()) {
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

	/**
	 * Devolve todas as regioes presentes no ficheiro "covidspreading.rdf"
	 * 
	 * @return as regioes
	 */
	private static ArrayList<String> getRegions() {
		ArrayList<String> arrayList = new ArrayList<String>();
		String query = "/RDF/NamedIndividual/@*";
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		XPathExpression expr;
		try {
			expr = xpath.compile(query);
			NodeList nl = (NodeList) expr.evaluate(openDocument(), XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				arrayList.add(StringUtils.substringAfter(nl.item(i).getNodeValue(), "#"));
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	/**
	 * Devolve todas as propriedades presentes no ficheiro "covidspreading.rdf"
	 * 
	 * @return as propriedades
	 */
	private static ArrayList<String> getDatatypeProperty() {
		ArrayList<String> arrayList = new ArrayList<String>();
		String query = "/RDF/DatatypeProperty/@*";
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		XPathExpression expr;
		try {
			expr = xpath.compile(query);
			NodeList nl = (NodeList) expr.evaluate(openDocument(), XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				arrayList.add(StringUtils.substringAfter(nl.item(i).getNodeValue(), "#"));
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	/**
	 * Cria o código HTML do formulário
	 * 
	 */
	public static void createHTMLForm() {
		FileWriter fWriter = null;
		BufferedWriter writer = null;
		try {
			String path = System.getProperty("user.home") + "\\wordpress\\html\\wp-admin\\formulario.html";
//			String path = "C:\\Users\\jmjmf\\git\\ES2-2020-EIC2-26\\src\\main\\java\\covid_query\\formulario.html";
			fWriter = new FileWriter(path);
			writer = new BufferedWriter(fWriter);
			String codigohtml = "<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n"
					+ "<meta charset=\"ISO-8859-1\">\r\n" + "<title>Covid Query</title>\r\n"
					+ "<h1>Welcome to Covid Query</h1>\r\n" + "</head>\r\n" + "\r\n" + "<style>\r\n" +
					// Definir estilo (?)
					"</style>\r\n" + "<body>\r\n" +
					/*
					 * Corpo !
					 */
					"	<form action=\"http://192.168.99.100/cgi-bin/cgi-java.sh\" method=\"POST\">\r\n"
					+ "		<label for=\"Regiao\">Região:</label>\r\n"
					+ "		<select id=\"Regiao\" name=\"Regiao\">\r\n";
			for (String s : getRegions()) {
				codigohtml += "		<option value=" + s + ">" + s + "</option>\r\n";
			}
			codigohtml += "		</select><p>\r\n" + "		<label for=\"Propriedade\">Propriedade:</label>\r\n"
					+ "		<select id=\"Propriedade\" name=\"Propriedade\">\r\n";
			for (String s : getDatatypeProperty()) {
				codigohtml += "		<option value=" + s + ">" + s + "</option>\r\n";
			}
			codigohtml += "		</select><p>\r\n";
			codigohtml += "		<label for=\"Operadores\">Operadores:</label>\r\n"
					+ "		<select id=\"Operadores\" name=\"Operadores\">\r\n";
			for (String s : operators) {
				codigohtml += "		<option value=" + s + ">" + s + "</option>\r\n";
			}
			codigohtml += "		</select><p>\r\n";
			codigohtml += "		<input type=\"text\" id=\"Valor\" name=\"Valor\"><p>\r\n";

			codigohtml += "		<input type=\"submit\" value=\"Submit\">\r\n" + "	</form>\r\n" + "</body>\r\n"
					+ "</html>";
			writer.write(codigohtml);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		CovidFormCreator.createHTMLForm();
	}
}
