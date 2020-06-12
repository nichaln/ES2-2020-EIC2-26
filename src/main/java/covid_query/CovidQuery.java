package covid_query;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.shared.io.download.DownloadFailedException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class CovidQuery {

	public CovidQuery() {

	}

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
				// loader.copyTo(System.out);
				byte[] bytes = loader.getBytes();
				FileOutputStream fos = new FileOutputStream("covid19spreading.rdf");
			} catch (IOException e) {
				e.printStackTrace();
			}

			revWalk.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
