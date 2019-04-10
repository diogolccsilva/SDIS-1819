package peer.channels;

import peer.Peer;

public class MdrListener implements Runnable{

    private Peer peer;

    public MdrListener(Peer peer){
        this.peer = peer;
    }

    @Override
    public void run() {
        while (true) {
            
        }
    }

}