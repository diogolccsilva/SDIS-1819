package peer.protocols;

import chunk.Chunk;
import message.Message;

/**
 * Backup
 */
public class Backup {

	public static void main(String[] args) {
		
	}

	public Backup() {

	}

	public void backupFile(String filePath, int repDegree) {
		Chunk[] chunks = Chunk.splitFile(filePath, repDegree);
		for (int i = 0;i<chunks.length;i++){
			backupChunk(chunks[i]);
		}
	}

	public void backupChunk(Chunk chunk) {
		//Message message = new Message(, );
	}
}