package peer.protocols.restore;

import message.Message;
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

	public void sendGetChunk(int chunkNo) {
		Message message = Message.parseGetChunkMessage(fileId, chunkNo, peer.getPeerId());
	}

	public void restore() {
		
		for (int i = 0;i < 10;i++) {
			sendGetChunk(i);
		}
	}

	@Override
	public void run() {
		restore();
	}

	
}