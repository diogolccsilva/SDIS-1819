package peer.protocols.backup;

import java.io.IOException;

import chunk.Chunk;
import message.Message;
import peer.Peer;

/**
 * BackupChunk
 */
public class BackupChunk implements Runnable{

	private Chunk chunk;
	private Peer peer;

	public BackupChunk(Peer peer, Chunk chunk) {
		this.chunk = chunk;
		this.peer = peer;
	}

	public void sendPutChunk() {
		Message message = Message.parsePutChunkMessage(chunk, peer.getPeerId());
		try {
			peer.sendToMdb(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean backupChunk() {
		sendPutChunk();
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