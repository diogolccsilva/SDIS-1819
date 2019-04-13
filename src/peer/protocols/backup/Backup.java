package peer.protocols.backup;

import java.io.File;

import chunk.Chunk;
import peer.Peer;

/**
 * Backup
 */
public class Backup implements Runnable {

	private Peer peer;
	private File file;
	private int repDegree;

	public Backup(Peer peer, String path, int repDegree) {
		file = new 	File(path);
		this.peer = peer;
		this.repDegree = repDegree;
	}

	public void backupFile() {
		Chunk[] chunks = Chunk.splitFile(file, repDegree);
		for (int i = 0;i<chunks.length;i++){
			System.out.println("BackupFile iteration: " + i + " chunkNo: " + chunks[i].getChunkNo() + " fileId: " + chunks[i].getFileID());
			BackupChunk backupChunk = new BackupChunk(peer, chunks[i]);
			Thread bThread = new Thread(backupChunk);
			bThread.start();
		}
	}

	@Override
	public void run() {
		backupFile();
	}
}