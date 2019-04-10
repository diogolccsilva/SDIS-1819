package peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class SocketPair {

    private MulticastSocket in;
    private MulticastSocket out;

    private int port;
    private InetAddress group;

    public SocketPair(String address, int port) throws UnknownHostException, IOException {

        this.group = InetAddress.getByName(address);
        this.port = port;

        this.in = new MulticastSocket(this.port);
        this.out = new MulticastSocket(this.port);

        this.in.joinGroup(this.group);
        this.out.joinGroup(this.group);

    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return this.group;
    }

    public void receive(DatagramPacket packet) throws IOException{
        this.in.receive(packet);
    }

    public void send(DatagramPacket packet) throws IOException{
        this.out.setTimeToLive(1);
        this.out.send(packet);
    }

    public void close() {
        this.in.close();
        this.out.close();
    }

}