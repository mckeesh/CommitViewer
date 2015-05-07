import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
//		Map<Owner, String> ownerToDiffsMap = new HashMap<Owner, String>();
		
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
		
//		ownerToDiffsMap.put(Owner.A, aDiffs);
//		ownerToDiffsMap.put(Owner.B, bDiffs);
		
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
					  System.out.println(conflictedFile);
					  metaMerge.fileToCompletedMerge.put(conflictedFile, commitStatus.getSolvedVersion(conflictedFile));
					  CombinedFile combinedFile = commitStatus.getCombinedFile(conflictedFile);
					  metaMerge.fileToA.put(conflictedFile, combinedFile.toJSONStringA());
					  metaMerge.fileToB.put(conflictedFile, combinedFile.toJSONStringB());
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
