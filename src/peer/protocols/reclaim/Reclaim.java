package peer.protocols.reclaim;

import chunk.Chunk;
import disk.ChunkManagement;
import peer.Peer;
import peer.protocols.backup.BackupChunk;

/**
 * Reclaim
 */
public class Reclaim implements Runnable {

    private Peer peer;
    private String fileId;
    private int chunkNo;

    public Reclaim(Peer peer, String fileId, int chunkNo) {
        this.peer = peer;
        this.fileId = fileId;
        this.chunkNo = chunkNo;
    }

    public void reclaim() {
        Chunk chunk = peer.getDisk().getChunk(fileId, chunkNo);
        if (chunk != null) {
            try {
                Thread.sleep((long) (Math.random() * 400 + 1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (ChunkManagement.getInstance().getStores(fileId, chunkNo) < chunk.getRepDegree()) {
                BackupChunk backupChunk = new BackupChunk(peer, chunk);
                Thread bThread = new Thread(backupChunk);
                bThread.start();
            }
        }

    }

    @Override
    public void run() {
        reclaim();
    }

}