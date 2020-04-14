package peer.protocols.reclaim;

import chunk.Chunk;
import chunk.ChunkInfo;
import disk.ChunkManagement;
import peer.Peer;
import peer.protocols.backup.BackupChunk;

public class Removed implements Runnable {

    private Peer peer;
    private String fileId;
    private int chunkNo;

    public Removed(Peer peer, String fileId, int chunkNo) {
        this.peer = peer;
        this.fileId = fileId;
        this.chunkNo = chunkNo;
    }

    public void startRemoved() {

        Chunk chunk = null;

        for (ChunkInfo chunkInfo : peer.getDisk().getChunksStored()) {
            if (chunkInfo.getFileID() == fileId && chunkInfo.getChunkNo() == chunkNo) {
                chunk = peer.getDisk().getChunk(fileId, chunkNo);
                break;
            }
        }

        if (chunk == null) {
            return;
        }

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

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }
}