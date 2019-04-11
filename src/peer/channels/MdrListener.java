package peer.channels;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MdrListener implements Runnable{

    private MulticastSocket socket;
    private InetAddress address;
    private int port;


    public MdrListener(String address, int port){
        try{
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = port;
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
    }

}

