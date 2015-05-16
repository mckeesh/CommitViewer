

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

class CombinedFile implements JSONAware {
	
	public class Tuple{
		public Chunk beginning;
		public Chunk end;
	}

	private List<Chunk> chunks = new ArrayList<Chunk>();

	public void addChunk(ChunkOwner owner, String content) {
		chunks.add(new Chunk(owner, content));
	}

	public String getVersion(ChunkOwner owner) {
		String version = chunks.stream().filter((chunk) -> chunk.isOwner(owner))
				.map((chunk) -> chunk.getContent())
				.collect(Collectors.joining());
		return version;
	}

	@Override
	public String toJSONString() {
		return "{\"A-only\":\n" + JSONObject.escape(getVersion(ChunkOwner.A)) +
				", \"B-only\":\n" + JSONObject.escape(getVersion(ChunkOwner.B)) + "}";
	}
	
	public String toStringAll(){
		String str = "";
		for(Chunk chunk : chunks){
				str += (chunk.getContent());
		}
		return str;
	}
	
	public String toStringA(){
		String str = "";
		for(Chunk chunk : chunks){
			if(chunk.owner == ChunkOwner.A) {
				str += (chunk.getContent());
			}
		}
		return str;
	}
	
	public String toStringB(){
		String str = "";
		for(Chunk chunk : chunks){
			if(chunk.owner == ChunkOwner.B) {
				str += (chunk.getContent());
			}
		}
		return str;
	}
	
	public String toStringBoth(){
		String str = "";
		for(Chunk chunk : chunks){
			if(chunk.owner == ChunkOwner.BOTH) {
				str += (chunk.getContent());
			}
		}
		return str;
	}
	
	public String getUnresolvedString(String conflictedFile){
		String unresolvedText = conflictedFile + 
		  "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\nVERSION A:\n" + 
		  toStringA() + 
		  "\n------------------------------------------------------------\nVERSION B:\n" + 
		  toStringB() + 
		  ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
		return unresolvedText;
	}
	
	public List<Chunk> getChunks(){
		return chunks;
	}
	
	public String getDiff(ChunkOwner owner){
		String diff;
		String formattedDiff = "";
		if(owner == ChunkOwner.A){
			diff = this.toStringA();
		} else if(owner == ChunkOwner.B){
			diff = this.toStringB();
		} else {
			diff = this.toStringBoth();
		}
		
		diff = replaceTabs(diff);
		
		String lines[] = diff.split("\\\\n");
		for(String jsonElement : lines) {
			formattedDiff += (jsonElement + "\n");
		}
		
		return formattedDiff;
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