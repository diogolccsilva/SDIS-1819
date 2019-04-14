package peer.protocols.delete;

import java.io.File;
import java.io.IOException;

import chunk.Chunk;
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
		File originalFile = new File(fileId);
		this.fileId = Chunk.generateFileId(originalFile);
	}

	public void sendDelete() {
		Message message = Message.parseDeleteMessage(fileId, peer);
		try {
			peer.sendToMc(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		peer.getDisk().deleteFileDirectory(fileId);
		sendDelete();
	}

	@Override
	public void run() {
		delete();
	}

	
}