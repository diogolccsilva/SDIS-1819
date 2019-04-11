package peer.protocols.backup;

import chunk.Chunk;
import message.Message;
import peer.Peer;

/**
 * BackupChunk
 */
public class BackupChunk implements Runnable{

	private Chunk chunk;
	private Peer peer;

	public BackupChunk(Chunk chunk) {
		this.chunk = chunk;
	}

	public boolean backupChunk() {
		Message message = Message.parsePutChunkMessage(chunk, peer.getPeerId());
		
		return true;
	}

	@Override
	public void run() {
		boolean done = false;
		int tries = 0;
		while(!done) {
			done = backupChunk();
			if (!done) {
				tries++;
				if (tries > 5) {
					break;
				}
			}
		}
	}
}