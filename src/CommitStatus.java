import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.lib.Repository;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class CommitStatus implements JSONAware {
	
	private Repository repository;
	private String sha1;
	private Map<String, CombinedFile> conflictingFiles;
	private Map<String, String> solvedVersions;
	
	public CommitStatus(Repository repository, String sha1, Map<String, CombinedFile> conflictingFiles) {
		this.sha1 = sha1;
		this.conflictingFiles = conflictingFiles;
		this.solvedVersions = new HashMap<String, String>(conflictingFiles.size());
		Set<String> conflictingFilesSet = conflictingFiles.keySet();
		for (String file : conflictingFilesSet) {
			String fileContents = FileRetriever.retrieveFile(repository, sha1, file);
			solvedVersions.put(file, fileContents);
		}
	}
	
	public List<String> getListOfConflictingFiles() {
		List<String> files = new ArrayList<>();
		files.addAll(conflictingFiles.keySet());
		return files;
	}
	
	public String getSolvedVersion(String fileName) {
		return solvedVersions.get(fileName);
	}
	
	public CombinedFile getCombinedFile(String fileName) {
		return conflictingFiles.get(fileName);
	}
	
	public String getSHA1() {
		return sha1;
	}

	public String toJSONString() {
		String json = "{";
		json += "\"sha1\": \"" + sha1 + "\", ";
		json += "\"conflictingFiles:\": " + JSONObject.toJSONString(conflictingFiles) + ",";
		json += "\"developersSolution:\": "+ JSONObject.toJSONString(solvedVersions);
		json += "}";
		return json;
	}
}
