package peer.protocols.reclaim;

import disk.ChunkManagement;
import peer.Peer;

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
        
    }

    @Override
    public void run() {
        reclaim();
    }

    
}