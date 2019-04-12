package peer.protocols.restore;

import peer.Peer;

/**
 * Restore
 */
public class Restore implements Runnable {

	private Peer peer;
	private String fileId;

	public Restore(Peer peer, String fileId) {
		this.peer = peer;
		this.fileId = fileId;
	}

	public void sendGetChunk() {

	}

	public void restore() {
		sendGetChunk();
	}

	@Override
	public void run() {
		restore();
	}

	
}