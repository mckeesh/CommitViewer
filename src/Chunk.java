	public class Chunk {
		public ChunkOwner owner;
		public String content;

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