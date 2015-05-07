import org.eclipse.jgit.lib.Repository;
import org.gitective.core.BlobUtils;

public class FileRetriever {
	
	public static String retrieveFile(Repository repository, String sha1, String filename) {
		String content = BlobUtils.getContent(repository, sha1, filename);
		return content;
	}
}
