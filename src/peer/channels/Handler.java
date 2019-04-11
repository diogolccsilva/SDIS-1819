package peer.channels;

import java.net.DatagramPacket;

import peer.Peer;
import message.*;

public class Handler implements Runnable {

    Peer peer;
    DatagramPacket packet;

    public Handler(Peer peer, DatagramPacket packet){
        this.peer = peer;
        this.packet = packet;
    }

    @Override
    public void run() {
        Message msg = new Message(this.packet.getData());
        MessageHeader header = msg.getHeader();

        if (this.peer.getPeerId() != header.getSenderId())
        switch (header.getMessageType()){
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

    }

    public void handleGETCHUNK(){

    }

    public void handleCHUNK(){

    }

    public void handleDELETE(){

    }
}