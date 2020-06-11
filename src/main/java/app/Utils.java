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
	 * Tentar aceder ao ficheiro daqui para todos usarmos :)
	 * 
	 * 
	 * 
	 */

	void accessGit() {
		if(repository == null) {
			try {
				git = Git.cloneRepository()
				  .setURI("https://github.com/vbasto-iscte/ESII1920.git")
				  .setDirectory(new File("/ESIIGrupo26"))
				  .call();
				//TODO isto é bué assim
				repository = git.getRepository();
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
		else {
			//TODO fazer pull, also meter isto inteligente :)
		}
	}

	void readFile() {
		accessGit();
		// a RevWalk allows to walk over commits based on some filtering that is defined
		try {
			System.out.println(repository.getBranch());
			ObjectId lastCommitId = repository.resolve(Constants.HEAD);
			System.out.println(lastCommitId.toString());
			RevWalk revWalk = new RevWalk(repository);
			RevCommit commit = revWalk.parseCommit(lastCommitId);
			// and using commit's tree find the path
			RevTree tree = commit.getTree();
			System.out.println("Having tree: " + tree);

			// now try to find a specific file
			try {
				TreeWalk treeWalk = new TreeWalk(repository);
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);
				treeWalk.setFilter(PathFilter.create("covid19spreading.rdf"));
				if (!treeWalk.next()) {
					throw new IllegalStateException("Did not find expected file 'covid19spreading.rdf'");
				}

				ObjectId objectId = treeWalk.getObjectId(0);
				ObjectLoader loader = repository.open(objectId);

				// and then one can the loader to read the file
				loader.copyTo(System.out);
			} catch (IOException e) {
				e.printStackTrace();
			}

			revWalk.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args) {
		new Utils().readFile();
	}
}
