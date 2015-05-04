

import java.util.ArrayList;
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
		return "{\"A-only\": " + JSONObject.escape(getVersion(ChunkOwner.A)) +
				", \"B-only\": " + JSONObject.escape(getVersion(ChunkOwner.B)) + "}";
	}

}