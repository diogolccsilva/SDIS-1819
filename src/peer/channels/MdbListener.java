package peer.channels;

import peer.Peer;

public class MdbListener implements Runnable{

    private Peer peer;

    public MdbListener(Peer peer){
        this.peer = peer;
    }

    @Override
    public void run() {
        while (true) {
            
        }
    }

}