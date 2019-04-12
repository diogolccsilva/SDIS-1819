package peer.protocols.backup;

import java.io.File;

import chunk.Chunk;
import message.Message;
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