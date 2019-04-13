package peer.protocols.restore;

import java.io.IOException;

import chunk.Chunk;
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
        Message message = Message.parseChunkMessage(chunk,peer.getPeerId());
        try {
			peer.sendToMc(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public boolean getChunk() {
        Chunk chunk = peer.getDisk().getChunk(fileId, chunkNo);
        if (chunk!= null){
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