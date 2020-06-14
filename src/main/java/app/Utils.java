package app;

import java.io.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.*;
import org.eclipse.jgit.treewalk.*;
import org.eclipse.jgit.treewalk.filter.*;

/**
 * 
 * @author Grupo 26
 *
 */
public class Utils {
	List<ObjectId> idsDasTags = new LinkedList<ObjectId>();
	List<Ref> call;
	List<fileInformation> informations = new LinkedList<fileInformation>();
	
	/**
	 * 
	 * @author hapgr@iscte-iul.pt
	 *
	 */
	public class fileInformation {
		private Date timestamp;
		private String fileName, tagName, tagDescription;	
		
		/**
		 * Classe criada para ser mais facil no CovidGraphSpread aceder a todas as informações do commit
		 * @param timestamp
		 * @param filename
		 * @param tagName
		 * @param tagDescription
		 */
		public fileInformation(Date timestamp, String filename, String tagName, String tagDescription) {
			this.timestamp = timestamp;
			this.fileName = filename;
			this.tagName = tagName;
			this.tagDescription = tagDescription;
		}
		/**
		 * @return o timestamp do commit do git
		 */
		public Date getTimestamp() {
			return timestamp;
		}
		/**
		 * @return o nome do ficheiro recebido do git
		 */
		public String getFileName() {
			return fileName;
		}
		/**
		 * @return o nome da tag do commit do git
		 */
		public String getTagName() {
			return tagName;
		}
		/**
		 * @return a descrição do commit no git
		 */
		public String getTagDescription() {
			return tagDescription;
		}
	}

	/**
	 * 
	 * @return o repositório git a que se quer aceder
	 */
	public static Git getGit() {
		File rep = new File("/Repositorio");
		Git git = null;
		if (rep.exists()) { // Repository exists, opening, and 
			try {
				git = Git.open(new File("/Repositorio/.git"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { // Repository doesn't exist, going to create it.
			try {
				git = Git.cloneRepository()
				  .setURI("https://github.com/vbasto-iscte/ESII1920.git")
				  .setDirectory(new File("/Repositorio"))
				  .call();
			} catch (InvalidRemoteException e) {
				System.err.println("Error - Invalid Remote " + e);
				e.printStackTrace();
			} catch (TransportException e) {
				System.err.println("Error - Transport " + e);
				e.printStackTrace();
			} catch (GitAPIException e) {
				System.err.println("Error  - GitAPI " + e);
				e.printStackTrace();
			}
		
		}
		return git;
	}
	
	/**
	 * 
	 * @return o repositório git
	 */
	public static Repository getGitRepository() {
		Git git = getGit();
		return git.getRepository();
	}

	/**
	 * Ir buscar os ficheiros e retirar informação de o ficheiro do repositório git
	 */
	void readFile(){
		Repository repository = getGitRepository();
		try {
			call = getGit().tagList().call();
		} catch (GitAPIException e1) {
			e1.printStackTrace();
		}
		for (Ref ref : call) {
		    idsDasTags.add(ref.getObjectId());
		}
		try {
			RevWalk revWalk = new RevWalk(repository);
			for(int i = 0; i<idsDasTags.size(); i++) {
				RevCommit commit = revWalk.parseCommit(idsDasTags.get(i));
				PersonIdent author = commit.getAuthorIdent();
				Date timestamp = author.getWhen();
				String fileTag = call.get(i).getName();
				String tagDescription = commit.getShortMessage();
				
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
				FileOutputStream fos = new FileOutputStream("covid19spreading"+i+".rdf");
				String fileName = "covid19spreading"+i+".rdf";
				informations.add(new fileInformation(timestamp, fileName, fileTag, tagDescription ));
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
	
	/**
	 * 
	 * @return lista com toda a informação necessária dos commits dos ficheiros
	 */
	public List<fileInformation> getInformations() {
		try {
			readFile();
		} catch (RevisionSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return informations;
	}

	public static void main(String[] args) {
		try {
			new Utils().readFile();
		} catch (RevisionSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
