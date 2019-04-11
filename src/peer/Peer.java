package peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import disk.Disk;
import message.Message;
import peer.channels.Listener;
import peer.protocols.backup.Backup;

public class Peer extends UnicastRemoteObject implements PeerInterface {
	private int peerId;
	private Disk disk;

	private Registry rmiReg;

	private String pVersion;
	private String accessPoint;

	private MulticastSocket socket;

	private Listener mcChannel;
	private Listener mdbChannel;
	private Listener mdrChannel;

	public Peer(String pVersion, int sid, String accessPoint, String mcAddress, int mcPort, String mdbAddress,
			int mdbPort, String mdrAddress, int mdrPort) throws RemoteException {
		super();
		this.peerId = sid;

		this.disk = new Disk("peer" + peerId);
		this.pVersion = pVersion;
		this.accessPoint = accessPoint;
		this.rmiReg = null;
		try {
			this.socket = new MulticastSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		this.startRMI();

		try {
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
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void startRMI() {
		try {
			this.rmiReg = LocateRegistry.getRegistry();
			rmiReg.rebind(this.accessPoint, this);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void startChannels(String mcA, int mcP, String mdbA, int mdbP, String mdrA, int mdrP) throws IOException {
		
		this.mcChannel = new Listener(this, mcA, mcP);
		this.mdbChannel = new Listener(this, mdbA, mdbP);
		this.mdrChannel = new Listener(this, mdrA, mdrP);
		
		Thread mcListener = new Thread(this.mcChannel);
		Thread mdbListener = new Thread(this.mdbChannel);
		Thread mdrListener = new Thread(this.mdrChannel);

		mcListener.start();
		mdbListener.start();
		mdrListener.start();
	}

	public void backup(String path, int replicationDeg) {
		Thread t = new Thread(new Backup(this, path, replicationDeg));
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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

	public void sendToMc(Message m) throws IOException {

		byte[] buf = m.toBytes();

		DatagramPacket packet = new DatagramPacket(buf, buf.length, this.mcChannel.getAddress(), this.mcChannel.getPort());
		socket.send(packet);

	}


	/**
	 * @return the peerId
	 */
	public int getPeerId() {
		return peerId;
	}

	/**
	 * @return the disk
	 */
	public Disk getDisk() {
		return disk;
	}

	/**
	 * @return the rmiReg
	 */
	public Registry getRmiReg() {
		return rmiReg;
	}

	/**
	 * @return the pVersion
	 */
	public String getpVersion() {
		return pVersion;
	}

	/**
	 * @return the accessPoint
	 */
	public String getAccessPoint() {
		return accessPoint;
	}

	/**
	 * @return the socket
	 */
	public MulticastSocket getSocket() {
		return socket;
	}

	/**
	 * @return the mcChannel
	 */
	public Listener getMcChannel() {
		return mcChannel;
	}

	/**
	 * @return the mdbChannel
	 */
	public Listener getMdbChannel() {
		return mdbChannel;
	}

	/**
	 * @return the mdrChannel
	 */
	public Listener getMdrChannel() {
		return mdrChannel;
	}
	
}