package peer.protocols.restore;

import java.io.File;
import java.io.IOException;

import chunk.Chunk;
import message.Message;
import peer.Peer;
import peer.channels.Sender;
import peer.channels.SenderUDP;

/**
 * Restore
 */
public class Restore implements Runnable {

	private Peer peer;
	private String filePath;

	public Restore(Peer peer, String filePath) {
		this.peer = peer;
		this.filePath = filePath;
	}

	public void sendGetChunk(int chunkNo) {
		Message message = Message.parseGetChunkMessage(filePath, chunkNo, peer.getPeerId());
		System.out.println("Sending GETCHUNK " + chunkNo);
		try {
			peer.sendToMc(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void restore() {
		File originalFile = new File(filePath);
		if (!originalFile.exists()) {
			System.out.println("file not found");
			return;
		}
		int chunksNo = Chunk.getNumberOfFileChunks(originalFile);
		for (int i = 0; i < chunksNo; i++) {
			sendGetChunk(i + 1);
		}
	}

	@Override
	public void run() {
		restore();
	}

}