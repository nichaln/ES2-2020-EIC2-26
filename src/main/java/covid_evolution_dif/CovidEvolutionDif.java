package covid_evolution_dif;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import org.apache.commons.io.FileUtils;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;;

public class CovidEvolutionDif {

	List<Ref> call;
	Map<ObjectId,Date> map = new HashMap<ObjectId,Date>();
	List<ObjectId> ids = new LinkedList<ObjectId>();

	void getTags() {
		//Buscar referencias para commits com tags e criar uma lista com todas as tags	
		try {
			call =  app.Utils.getGit().tagList().call();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Ref ref : call) {
			ids.add(ref.getObjectId());
		}
	}
	
	void getDates() {
		Repository repository = app.Utils.getGitRepository();
			RevWalk revWalk = new RevWalk(repository);
			//Percorrer a lista criada de tags, e procurar commits com o ID das tags
			for(int i = 0; i<ids.size(); i++) {
				//Encontrar o commit com id da tag i
				RevCommit commit = null;
				try {
					commit = revWalk.parseCommit(ids.get(i));
				} catch (MissingObjectException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IncorrectObjectTypeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

					//Datas
				PersonIdent author = commit.getAuthorIdent();
				Date timestamp = author.getWhen();
				map.put(ids.get(i), timestamp);
				
			}
			revWalk.close();
	}
	
	ObjectId mostRecent() { 
		Date timestamp = new Date(0L);
		ObjectId id = null;
		for (Map.Entry<ObjectId, Date> entry : map.entrySet()) {
			Date newTimestamp = entry.getValue();
			if(timestamp.before(newTimestamp)) {
				timestamp = newTimestamp;
				id = entry.getKey();				
			}
		}
		map.remove(id);
		return id;
	}
	
	void getFiles() {
		getTags();
		getDates();
		Repository repository = app.Utils.getGitRepository();
		try {
			RevWalk revWalk = new RevWalk(repository);
			//Percorrer a lista criada de tags, e procurar commits com o ID das tags
			for(int i = 0; i<2; i++) {
				//Encontrar o commit com id da tag i
				RevCommit commit = revWalk.parseCommit(mostRecent());
				RevTree tree = commit.getTree();
				// e depois faz o download do covid19spreading.rdf
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
					// and then one can the loader to read the file
					//loader.copyTo(System.out);
					byte[] bytes = loader.getBytes();
					FileOutputStream fos = new FileOutputStream("covid19spreading_recent"+i+".rdf");

					fos.write(bytes);
					fos.close();
					treeWalk.close();
				} catch (IOException e) {
					e.printStackTrace();
				}}

			revWalk.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void writeHTML() {
		getFiles();
		FileWriter fWriter = null;
		BufferedWriter writer = null;
		try {
		    fWriter = new FileWriter("C:\\Users\\Catando\\git\\ES2-2020-EIC2-26\\src\\main\\java\\covid_evolution_dif\\presentDocs.html");
		    writer = new BufferedWriter(fWriter);
		    writer.write("<!DOCTYPE html>\r\n" + 
		    		"<html>\r\n" + 
		    		"<head>\r\n" + 
		    		"<meta charset=\"ISO-8859-1\">\r\n" + 
		    		"<title>COVID Evolution</title>\r\n" + 
		    		"</head>\r\n" + 
		    		"\r\n" + 
		    		"<body>\r\n" + 
		    		"	<div style=\"width: 100%; display: inline-block;\">\r\n" +
		    	    "		<div style=\"width: 49.5%; float: left;\">\r\n" +
		    	    "			<h2>DOC1</h2>\r\n"+
		    	    "				" + writeDocs().get(0) +
		    	    "		</div>\r\n" +
		    	    "		<div style=\"margin-left: 50.5%;\">\r\n" +
		    	    "			<h2>DOC2</h2>\r\n"+
		    	    "				" + writeDocs().get(1) +
		    	    "		</div>\r\n" +
		        	"	</div>\r\n" +
		    		"</body>\r\n" + 
		    		"</html>");
		    writer.close();	    
		} catch (Exception e) {
		  //catch any exceptions here
		}
	}

	List<String> writeDocs(){
		
		List<String> list = new ArrayList<String>();
		
		File file0=new File("C:\\Users\\Catando\\git\\ES2-2020-EIC2-26\\covid19spreading_recent0.rdf");   
		File file1=new File("C:\\Users\\Catando\\git\\ES2-2020-EIC2-26\\covid19spreading_recent1.rdf");
		String doc1="";
		try {
			doc1 = FileUtils.readFileToString(file0, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String doc2="";
		try {
			doc2 = FileUtils.readFileToString(file1, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DiffMatchPatch dmp = new DiffMatchPatch();
		LinkedList<DiffMatchPatch.Diff> diff = dmp.diffMain(doc1, doc2);
		dmp.diffPrettyHtml(diff);
		
		String html0="<p>";
		String html1="<p>";
		for(DiffMatchPatch.Diff vDiff : diff) {
		  if(vDiff.operation.toString().equals("EQUAL")) {
		    html0 += vDiff.text.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
		    html1 += vDiff.text.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
		  } else if(vDiff.operation.toString().equals("DELETE")) {
			  html0 += "<mark>" + vDiff.text.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;") + "</mark>";
		  } else{
			  html1 += "<mark>" + vDiff.text.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;") + "</mark>";
		  }
		}
		html0 += "</p>";
		html1 += "</p>";
		list.add(html0);
		list.add(html1);
		return list ;
	}
	
	public static void main(String[] args) throws IOException {
		new CovidEvolutionDif().writeHTML();
	}
}