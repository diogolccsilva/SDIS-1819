package peer.protocols.restore;

import java.io.File;

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
		Sender sender = new SenderUDP();
		sender.init(peer.getMcAddress(), peer.getMcPort());
		sender.send(message.getBody());
		sender.destroy();
	}

	public void restore() {
		File originalFile = new File(filePath);
		if (!originalFile.exists()) {
			return;
		}
		int chunksNo = Chunk.getNumberOfFileChunks(originalFile);
		for (int i = 0;i < chunksNo;i++) {
			sendGetChunk(i+1);
		}
	}

	@Override
	public void run() {
		restore();
	}

	
}