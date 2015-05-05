import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;


public class DataScraper {
	PrintWriter writerA = null;
	PrintWriter writerB = null;
	public enum Owner {A, B};
	
	public DataScraper(String resultsFilePath, String resultsFileName) throws IOException{
		
//		createFileIfDoesntExist(resultsFilePath, resultsFileName);
	    
		try {
			writerA = new PrintWriter(resultsFilePath + "resultsA.txt", "UTF-8");
			writerB = new PrintWriter(resultsFilePath + "resultsB.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void createFileIfDoesntExist(String resultsFilePath, String resultsFileName) throws IOException {
		File file = new File(resultsFilePath, resultsFileName);
	    if (!file.isFile() && !file.createNewFile()){
			throw new IOException("Error creating new file: " + file.getAbsolutePath());
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
		writerA.close();
		writerB.close();
	}
	
	private void printMergedResult(Repository repository, RevCommit commitA, RevCommit commitB){
		Merger merger = new Merger();
		Map<String, CombinedFile> fileMap = null;
		
		try {
			fileMap = merger.merge(repository, commitA, commitB);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		writeResultsToFile(fileMap, writerA, Owner.A);
		writeResultsToFile(fileMap, writerB, Owner.B);
	}
	
	private void writeResultsToFile(Map<String, CombinedFile> fileMap, PrintWriter writer, Owner owner){
		if(fileMap != null){
			for(String str : fileMap.keySet()){
				System.out.println(str);
				writer.println(str);
				CombinedFile combined = fileMap.get(str);
				if(combined != null){
					String diff;
					if(owner == Owner.A){
						diff = combined.toJSONStringA();
					} else {
						diff = combined.toJSONStringB();
					}
					diff = replaceTabs(diff);
					
					String lines[] = diff.split("\\\\n");
					for(String jsonElement : lines) {
						writer.println(jsonElement);
						System.out.println(jsonElement);
					}
				}
			}
		}
	}
	
	//Java replace function being a real a-hole
	private String replaceTabs(String str){ 
		List<String> splitList = Arrays.asList(str.split("\\\\t"));
		String strSum = "";
		for(String strElement : splitList){
			strSum += strElement + "\t";
		}
		return strSum;
	}
}
