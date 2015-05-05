import java.io.IOException;
import java.util.Arrays;

import org.eclipse.jgit.lib.Repository;


public class Main {

	public static void main(String[] args) {
		Repository repo = RepositoryRetriever.get("/Users/Shane/TestGitRepo/.git");
		DataScraper scraper = null;
		try {
			scraper = new DataScraper("/Users/Shane/workspace2/CommitViewer/src/results/", "results.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		assert (scraper != null);
		scraper.walkRepo(repo);
	}

}
