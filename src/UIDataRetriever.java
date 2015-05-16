import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;


public class UIDataRetriever {
	
	private InMemoryMerger mMerger;
	private Repository mRepository;
	
	public UIDataRetriever(Repository repository){
		mMerger = new InMemoryMerger(repository);
		mRepository = repository;
	}

	public Map<RevCommit, MetaMerge> walkRepo() {
		Map<RevCommit, MetaMerge> commitToMergeMap = new HashMap<RevCommit, MetaMerge>();
		RevWalk rw = new RevWalk(mRepository);
		ObjectId HEAD;
		
		try {
			HEAD = mRepository.resolve("HEAD");
			rw.markStart(rw.parseCommit(HEAD));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RevisionSyntaxException e1) {
			e1.printStackTrace();
		}
		
		Iterator<RevCommit> it = rw.iterator();
		
		while(it.hasNext()) {
		  RevCommit commit = it.next();
		  MetaMerge mergeInfo = createMetaMergeMap(commit);
		  if(mergeInfo.conflictExists){
			  	commitToMergeMap.put(commit, mergeInfo);
			  }
		}
		
		rw.dispose();
		
		return commitToMergeMap;
	}
	
	private MetaMerge createMetaMergeMap(RevCommit commit){
		MetaMerge metaMerge = new MetaMerge();
		try{
			metaMerge.commitName = commit.name();
			
			CommitStatus commitStatus = mMerger.recreateMerge(commit);
			List<String> conflictedFiles = commitStatus.getListOfConflictingFiles();
			
			 if(conflictedFiles.size() != 0){
				  for(String conflictedFile : conflictedFiles){
					  CombinedFile combinedFile = commitStatus.getCombinedFile(conflictedFile);
					  metaMerge.fileToA.put(conflictedFile, combinedFile.toStringA());
					  metaMerge.fileToB.put(conflictedFile, combinedFile.toStringB());
					  String diffSolved = StringUtils.difference(commitStatus.getSolvedVersion(conflictedFile), combinedFile.toStringAll());
					  metaMerge.fileToCompletedMerge.put(conflictedFile, diffSolved);
					  metaMerge.fileToUnresolved.put(conflictedFile, combinedFile.getUnresolvedString(conflictedFile)); 
				  }
				  
				  metaMerge.conflictExists = true;
			  }
			 
			 return metaMerge;
		
		} catch(IllegalArgumentException e) {
			metaMerge.conflictExists = false;
			return metaMerge;
		}
	}
}
