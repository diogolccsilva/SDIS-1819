package peer.channels;

import java.io.IOException;
import java.net.DatagramPacket;

import chunk.Chunk;
import peer.Peer;
import message.*;

public class Handler implements Runnable {

    Peer peer;
    DatagramPacket packet;
    Message msg;
    MessageHeader msgHeader;

    public Handler(Peer peer, DatagramPacket packet){
        this.peer = peer;
        this.packet = packet;
    }

    @Override
    public void run() {
        this.msg = new Message(this.packet.getData());
        this.msgHeader = msg.getHeader();

        if (this.peer.getPeerId() != this.msgHeader.getSenderId())
        switch (this.msgHeader.getMessageType()){
            case "PUTCHUNK":
                handlePUTCHUNK();
                break;
            case "GETCHUNK": 
                handleGETCHUNK();
                break;
            case "CHUNK":
                handleCHUNK();
                break;
            case "DELETE":
                handleDELETE();
                break;
            default:
                break;
        }
    }

    public void handlePUTCHUNK(){
        if (this.peer.getDisk().getFreeSpace() < (packet.getLength())){
            System.out.println("Peer " + this.peer.getPeerId() +"- PUTCHUNK request: Not enough space to store chunk");
            return;
        }

        Chunk chunk = new Chunk(this.msgHeader.getFileId(), this.msgHeader.getChunkNo(), this.msgHeader.getReplicaDeg(),  this.msg.getBody());
        this.peer.getDisk().storeChunk(chunk);
        this.sendSTORED(chunk);

        Message m = Message.parseStoredMessage(chunk, this.peer.getPeerId());
        try{
            this.peer.sendToMc(m);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void handleGETCHUNK(){

    }

    public void handleCHUNK(){

    }

    public void handleDELETE(){

    }

    public void sendSTORED(Chunk chunk) {
        


    }
}