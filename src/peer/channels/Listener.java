package peer.channels;

import peer.Peer;
import peer.channels.Handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import message.Message;

import java.net.DatagramPacket;

public class Listener implements Runnable {

    private MulticastSocket socket;
    private InetAddress address;
    private int port;

    private Peer peer;

    public Listener(Peer peer, String address, int port) {
        try {
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

        while (true) {
            byte[] buf = new byte[Message.MESSAGE_PACKET_MAX_SIZE];

            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("MC Listener: Packet Received!");
            Thread handler = new Thread(new Handler(peer, packet));
            handler.start();
            try {
                handler.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }

    /**
     * @return the socket
     */
    public MulticastSocket getSocket() {
        return socket;
    }

    /**
     * @return the address
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @return the peer
     */
    public Peer getPeer() {
        return peer;
    }
}

