import java.util.HashMap;
import java.util.Map;


public class MetaMerge {
	public boolean conflictExists;
	public String commitName;
	public Map<String, String> fileToCompletedMerge;
	public Map<String, String> fileToA;
	public Map<String, String> fileToB; 
	
	public MetaMerge(){
		fileToCompletedMerge = new HashMap<String, String>();
		fileToA = new HashMap<String, String>();
		fileToB = new HashMap<String, String>();
	}
}
