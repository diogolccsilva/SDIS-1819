package peer.protocols.restore;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import chunk.Chunk;
import disk.ChunkManagement;
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
	private File file;

	public Restore(Peer peer, String filePath) {
		this.peer = peer;
		this.filePath = filePath;
		file = new File(filePath);
	}

	public void sendGetChunk(int chunkNo) {
		Message message = Message.parseGetChunkMessage(Chunk.generateFileId(file), chunkNo, peer);
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
		Chunk[] chunks = new Chunk[chunksNo];
		String fileId = Chunk.generateFileId(originalFile);
		int i=0;
		while (i < chunksNo) {
			sendGetChunk(i + 1);
			BlockingQueue<Chunk> queue = ChunkManagement.getInstance().getRestoreChunks();
			try {
				System.out.println("Waiting for chunk " + (i+1));
				Chunk chunk = queue.take();
				if (chunk.getFileID().equals(fileId) && chunk.getChunkNo() == i+1){
					chunks[i] = chunk;
					i++;
					System.out.println("received chunk " + chunk.getChunkNo());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		peer.getDisk().restoreFile(chunks,originalFile.getName());
	}

	@Override
	public void run() {
		restore();
	}

}
