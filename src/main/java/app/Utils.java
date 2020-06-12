package app;

import java.io.*;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.*;
import org.eclipse.jgit.treewalk.*;
import org.eclipse.jgit.treewalk.filter.*;

public class Utils {
	Git git;
	Repository repository;
	
	/*
	 * Consegui aceder ao ficheiro daqui para todos usarmos :)
	 * 
	 */

	void accessGit() {
		File rep = new File("/Repositorio");
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
				System.err.println("Error - GitAPI " + e);
				e.printStackTrace();
			}
		
		}
		repository = git.getRepository();
	}

	void readFile() {
		accessGit();
		// a RevWalk allows to walk over commits based on some filtering that is defined
		try {
			//System.out.println(repository.getBranch());
			ObjectId lastCommitId = repository.resolve(Constants.HEAD);
			RevWalk revWalk = new RevWalk(repository);
			RevCommit commit = revWalk.parseCommit(lastCommitId);
			// and using commit's tree find the path
			RevTree tree = commit.getTree();
			// now try to find a specific file
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
	public static void main(String[] args) {
		new Utils().readFile();
	}
}
