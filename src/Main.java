import org.eclipse.jgit.lib.Repository;


public class Main {

	public static void main(String[] args) {
		Repository repo = RepositoryRetriever.get("/Users/Shane/mergeconflict/repos/AndEngine/.git");
//		Repository repo = RepositoryRetriever.get("/Users/Shane/TestGitRepo/.git");
		
		ConflictViewerUI ui = new ConflictViewerUI(1440, 900, repo);
	    ui.setVisible(true);
	    ui.setResizable(true);
	}
}
