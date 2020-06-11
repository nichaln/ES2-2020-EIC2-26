package app;

import java.io.*;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.*;
import org.eclipse.jgit.treewalk.*;
import org.eclipse.jgit.treewalk.filter.*;

public class Utils {
	Repository repository;
	
	/*
	 * Tentar aceder ao ficheiro daqui para todos usarmos :)
	 * 
	 * 
	 * 
	 */

	void accessGit() {
		try {
			Git git = Git.open(new File("https://github.com/vbasto-iscte/ESII1920.git"));
			//TODO isto é bola assim
			repository = git.getRepository();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void readFile() {
		// a RevWalk allows to walk over commits based on some filtering that is defined
		try {
			ObjectId lastCommitId = repository.resolve(Constants.HEAD);
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
			} catch (Exception e) {
				e.printStackTrace();
			}

			revWalk.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args) {
		new Utils().readFile();
	}
}
