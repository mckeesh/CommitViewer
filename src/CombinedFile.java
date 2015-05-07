

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

class CombinedFile implements JSONAware {

	private class Chunk {
		private ChunkOwner owner;
		private String content;

		public Chunk(ChunkOwner owner, String content) {
			this.owner = owner;
			this.content = content;
		}

		public boolean isOwner(ChunkOwner a) {
			if (owner.equals(ChunkOwner.BOTH))
				return true;
			
			return owner.equals(a);
		}

		public String getContent() {
			return content;
		}
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
	
	public String toJSONStringA(){
		return JSONObject.escape(getVersion(ChunkOwner.A)); 
	}
	
	public String toJSONStringB(){
		return JSONObject.escape(getVersion(ChunkOwner.B)); 
	}
	
	public String getDiff(Owner owner){
		String diff;
		String formattedDiff = "";
		if(owner == Owner.A){
			diff = this.toJSONStringA();
		} else {
			diff = this.toJSONStringB();
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