import java.io.File;
import java.io.FileNotFoundException;

import org.eclipse.jgit.lib.Repository;


public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		String repoPath = "/Users/Shane/mergeconflict/repos/android/.git";
		File file = new File(repoPath);
		if(!file.exists() && !file.isDirectory()){
			throw new FileNotFoundException("Can't find your beloved repo.");
		}
		Repository repo = RepositoryRetriever.get(repoPath);
//		Repository repo = RepositoryRetriever.get("/Users/Shane/TestGitRepo/.git");
		
		ConflictViewerUI ui = new ConflictViewerUI(1440, 900, repo);
	    ui.setVisible(true);
	    ui.setResizable(true);
	}
}
