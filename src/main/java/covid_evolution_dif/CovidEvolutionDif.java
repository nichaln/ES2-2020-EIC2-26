package covid_evolution_dif;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
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

public class CovidEvolutionDif {

	List<Ref> call;
	Map<ObjectId,Date> map = new HashMap<ObjectId,Date>();
	List<ObjectId> ids = new LinkedList<ObjectId>();

	
	private Git getGit() {
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

	void getTags() {
		
		
		//Buscar referencias para commits com tags e criar uma lista com todas as tags	
		try {
			call = getGit().tagList().call();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Ref ref : call) {
			ids.add(ref.getObjectId());
			System.out.println(ref.toString());
		}
	}
	
	void getDates() {
		Repository repository = getGit().getRepository();
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
			System.out.println(map.toString());
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
		System.out.println("devolvi o id" + id);
		return id;
	}
	
	void getFiles() {
		getTags();
		getDates();
		Repository repository = getGit().getRepository();
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

					//System.out.println("Vou escrever o " + fos.toString());
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

	public static void main(String[] args) {
		new CovidEvolutionDif().getFiles();
	}
}
