package peer.protocols.delete;

import message.Message;
import peer.Peer;

/**
 * Delete
 */
public class Delete implements Runnable {

	private Peer peer;
	private String fileId;

	public Delete(Peer peer, String fileId) {
		this.peer = peer;
		this.fileId = fileId;
	}

	public void sendDelete() {
		Message message = Message.parseDeleteMessage(fileId, peer.getPeerId());
	}

	public void delete() {
		peer.getDisk().deleteFileDirectory(fileId);
		sendDelete();
	}

	@Override
	public void run() {

	}

	
}