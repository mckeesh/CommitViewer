import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;


public class Main {

	public static void main(String[] args) {
		Repository repo = RepositoryRetriever.get("/Users/Shane/TestGitRepo/.git");
		UIDataRetriever dataRetriever = new UIDataRetriever(repo);
		Map<RevCommit, MetaMerge> commitToMergeMap = dataRetriever.walkRepo();
		
		MetaMerge firstMerge = commitToMergeMap.get(commitToMergeMap.keySet().toArray()[0]);
		String firstFileA = firstMerge.fileToA.get(firstMerge.fileToA.keySet().toArray()[0]);
		String firstFileB = firstMerge.fileToB.get(firstMerge.fileToB.keySet().toArray()[0]);
		String firstFileCombined= firstMerge.fileToCompletedMerge.get(firstMerge.fileToCompletedMerge.keySet().toArray()[0]);
		
		ConflictViewerUI ui = new ConflictViewerUI(1440, 900);
	    ui.setVisible(true);
	    ui.setTextA(Formatter.formatDiff(firstFileA));
	    ui.setTextB(Formatter.formatDiff(firstFileB));
	    ui.setTextSolved(Formatter.formatDiff(firstFileCombined));
	    ui.setResizable(true);
	}
}
