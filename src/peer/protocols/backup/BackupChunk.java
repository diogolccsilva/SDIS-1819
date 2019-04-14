package peer.protocols.backup;

import java.io.IOException;

import chunk.Chunk;
import disk.ChunkManagement;
import message.Message;
import peer.Peer;

/**
 * BackupChunk
 */
public class BackupChunk implements Runnable {

	public static final int backupTries = 10;

	private Chunk chunk;
	private Peer peer;

	public BackupChunk(Peer peer, Chunk chunk) {
		this.chunk = chunk;
		this.peer = peer;
	}

	public void sendPutChunk() {
		Message message = Message.parsePutChunkMessage(chunk, peer);
		try {
			peer.sendToMdb(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean backupChunk() {
		sendPutChunk();
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (ChunkManagement.getInstance().getStores(chunk.getFileID(), chunk.getChunkNo()) >= chunk.getRepDegree()) {
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(400 * chunk.getChunkNo());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean done = false;
		int tries = 0;
		while(!done) {
			done = backupChunk();
			if (!done) {
				tries++;
				if (tries > backupTries) {
					break;
				}
			}
		}
	}
}