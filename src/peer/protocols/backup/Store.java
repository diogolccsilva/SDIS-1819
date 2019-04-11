package peer.protocols.backup;

import chunk.Chunk;
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
		Message storedMessage = Message.parseStoredMessage(chunk, peer.getPeerId());
	}

	public boolean storeChunk() {
		if (peer.getDisk().storeChunk(chunk)) {
			sendStored();
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		storeChunk();
	}

}