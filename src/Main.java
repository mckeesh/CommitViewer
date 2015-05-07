import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;


public class Main {

	public static void main(String[] args) {
		Repository repo = RepositoryRetriever.get("/Users/Shane/mergeconflict/repos/AndEngine/.git");
		
		ConflictViewerUI ui = new ConflictViewerUI(1440, 900, repo);
	    ui.setVisible(true);
	    ui.setResizable(true);
	}
}
