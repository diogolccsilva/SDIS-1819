package peer.protocols.restore;

import java.io.IOException;

import chunk.Chunk;
import disk.ChunkManagement;
import message.Message;
import peer.Peer;

/**
 * GetChunk
 */
public class GetChunk implements Runnable{

    private Peer peer;
    private String fileId;
    private int chunkNo;

	public GetChunk(Peer peer, String fileId, int chunkNo) {
		this.peer = peer;
        this.fileId = fileId;
        this.chunkNo = chunkNo;
    }

    public void sendChunk(Chunk chunk) {
        Message message = Message.parseChunkMessage(chunk,peer);
        try {
			peer.sendToMc(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public boolean getChunk() {
        Chunk chunk = peer.getDisk().getChunk(fileId, chunkNo);
        if (chunk!= null){
            try {
                Thread.sleep((long) (Math.random() * 400 + 1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (ChunkManagement.getInstance().getRestoreChunks().contains(chunk)){
                ChunkManagement.getInstance().getRestoreChunks().remove(chunk);
                return false;
            }
            sendChunk(chunk);
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        getChunk();
    }

    
}