import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;


public class DataScraper {
	PrintWriter writer = null;
	
	public DataScraper(String resultsFilePath, String resultsFileName) throws IOException{
		
		File file = new File(resultsFilePath, resultsFileName);
	    if (!file.isFile() && !file.createNewFile()){
			throw new IOException("Error creating new file: " + file.getAbsolutePath());
	    }
	    
		try {
			writer = new PrintWriter(resultsFilePath + resultsFileName, "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	public void walkRepo(Repository repository){
		RevWalk rw = new RevWalk(repository);
		ObjectId HEAD;
		
		try {
			HEAD = repository.resolve("HEAD");
			rw.markStart(rw.parseCommit(HEAD));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RevisionSyntaxException e1) {
			e1.printStackTrace();
		}
		
		Iterator<RevCommit> it = rw.iterator();
		RevCommit previousCommit = it.next();
		
		while(it.hasNext()) {
		  RevCommit commit = it.next();
		  printMergedResult(repository, previousCommit, commit);
		  previousCommit = commit;
		}
		rw.dispose();
		writer.close();
	}
	
	private void printMergedResult(Repository repository, RevCommit commitA, RevCommit commitB){
		Merger merger = new Merger();
		Map<String, CombinedFile> fileMap = null;
		
		try {
			fileMap = merger.merge(repository, commitA, commitB);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(fileMap != null){
			for(String str : fileMap.keySet()){
				System.out.println(str);
				writer.println(str);
				CombinedFile combined = fileMap.get(str);
				if(combined != null){
					String json = combined.toJSONString();
					System.out.println(json.contains("\\n"));
					String lines[] = json.split("\\\\n");
					for(String jsonElement : lines) {
						System.out.println(jsonElement);
					}
					
					for(String jsonElement : json.split("\\n")) {
						writer.println(jsonElement);
					}
				}
			}
		}
	}
}
