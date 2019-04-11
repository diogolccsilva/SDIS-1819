package peer;

import disk.*;
import peer.channels.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.lang.Thread;

public class Peer extends UnicastRemoteObject implements PeerInterface{
	private int peerId;
	private Disk disk;

	private Registry rmiReg;

	private String pVersion;
	private String accessPoint;

	private Thread mcListener;
	private Thread mdbListener;
	private Thread mdrListener;


	public Peer(String pVersion, int sid, String accessPoint, String mcAddress, int mcPort, String mdbAddress,
			int mdbPort, String mdrAddress, int mdrPort) throws RemoteException {
		super();
		this.peerId = sid;

		this.disk = new Disk("peer" + peerId);
		this.pVersion = pVersion;
		this.accessPoint = accessPoint;
		this.rmiReg = null;

		this.startRMI();
		
		try{
			this.startChannels(mcAddress, mcPort, mdbAddress, mdbPort, mdrAddress, mdrPort);	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Peer " + this.peerId + "running...");
	}

	public static void main(String[] args) {
		int id = Integer.parseInt(args[1]);
		int mcPort = Integer.parseInt(args[4]);
		int mdbPort = Integer.parseInt(args[6]);
		int mdrPort = Integer.parseInt(args[8]);
		try {
			Peer peer = new Peer(args[0], id, args[2], args[3], mcPort, args[5], mdbPort, args[7], mdrPort);
		} catch(RemoteException e) {
			e.printStackTrace();
		}
	}

	public void startRMI(){
		try {
			this.rmiReg = LocateRegistry.getRegistry();
			rmiReg.rebind(this.accessPoint, this);
		} catch(RemoteException e) {
			e.printStackTrace();
		}
	}

	public void startChannels(String mcA, int mcP, String mdbA, int mdbP, String mdrA, int mdrP) throws IOException{
		this.mcListener = new Thread(new McListener(mcA, mcP));
		this.mdbListener = new Thread(new MdbListener(mdbA, mdbP));
		this.mdrListener = new Thread(new MdrListener(mdrA, mdrP));

		this.mcListener.start();
		this.mdbListener.start();
		this.mdrListener.start();	
	}




	public void backup(String path, int ReplicationDeg) {

	}

	public void delete(String path) {

	}

	public void restore(String path) {

	}

	public void reclaim(float space) {

	}

	public String state() {
		return "test";
	}

	/**
	 * @return the peerId
	 */
	public int getPeerId() {
		return peerId;
	}
}