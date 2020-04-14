package peer.protocols.reclaim;

import java.io.IOException;

import chunk.ChunkInfo;
import disk.Disk;
import message.Message;
import peer.Peer;

/**
 * Reclaim
 */
public class Reclaim implements Runnable {

    private Peer peer;
    private long spaceToReclaim;

    public Reclaim(Peer peer, long space) {
        this.peer = peer;
        this.spaceToReclaim = space;
    }

    public void reclaim() {
        Disk disk = peer.getDisk();
        long spaceReclaimed = 0;
        for (ChunkInfo chunkI : disk.getChunksStored()) {
            if (disk.deleteChunk(chunkI)) {
                spaceReclaimed += chunkI.getSize();
                Message m = Message.parseRemovedMessage(chunkI.getFileID(), chunkI.getChunkNo(), this.peer);
                try {
                    peer.sendToMc(m);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (spaceReclaimed >= spaceToReclaim) {
                break;
            }
        }
    }

    @Override
    public void run() {
        reclaim();
    }

}