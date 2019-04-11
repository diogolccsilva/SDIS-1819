package peer.channels;

import peer.Peer;
import peer.channels.Handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.net.DatagramPacket;

public class Listener implements Runnable{

    private MulticastSocket socket;
    private InetAddress address;
    private int port;

    private Peer peer;

    public Listener(Peer peer, String address, int port){
        try{
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = port;
        this.peer = peer;
    }

    @Override
    public void run() {
        try {
            this.socket = new MulticastSocket(port);
            socket.setTimeToLive(1);
            socket.joinGroup(address);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while(true) {
            byte[] buf = new byte[64000];

            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("MC Listener: Packet Received!");
            Thread handler = new Thread(new Handler(peer, packet));
            handler.start();
        }
        
    }

}

