package peer.protocols.backup;

import java.io.IOException;

import chunk.Chunk;
import disk.ChunkManagement;
import message.Message;
import peer.Peer;

/**
 * Store
 */
public class Store implements Runnable {

	private Peer peer;
	private Chunk chunk;

	public Store(Peer peer, Chunk chunk) {
		this.peer = peer;
		this.chunk = chunk;
	}

	public void sendStored() {
		Message storedMessage = Message.parseStoredMessage(chunk, peer);
		try {
			peer.sendToMc(storedMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean storeChunk() {
		try {
			Thread.sleep((long) (Math.random() * 400 + 1));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (ChunkManagement.getInstance().getStores(chunk.getFileID(), chunk.getChunkNo()) < chunk.getRepDegree()) {
			if (peer.getDisk().storeChunk(chunk)) {
				sendStored();
				return true;
			}
		}
		return false;
	}

	@Override
	public void run() {
		storeChunk();
	}

}