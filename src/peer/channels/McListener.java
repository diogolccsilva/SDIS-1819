package peer.channels;

import peer.Peer;

public class McListener implements Runnable{

    private Peer peer;

    public McListener(Peer peer){
        this.peer = peer;
    }

    @Override
    public void run() {
        while(true) {

        }
    }

}

