import java.io.IOException;

import org.eclipse.jgit.lib.Repository;


public class Main {

	public static void main(String[] args) {
		Repository repo = RepositoryRetriever.get("/Users/Shane/TestGitRepo/.git");
		DataScraper scraper = null;
		try {
			scraper = new DataScraper("/Users/Shane/workspace2/CommitViewer/src/results/", "results.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
		assert (scraper != null);
		scraper.walkRepo(repo);
	}

}
