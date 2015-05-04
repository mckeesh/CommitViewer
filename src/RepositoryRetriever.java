import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;


public class RepositoryRetriever {
	public static Repository get(String repoName){
		Repository repository = null;
		try {
			repository = FileRepositoryBuilder.create(new File(repoName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return repository;
	}
}
