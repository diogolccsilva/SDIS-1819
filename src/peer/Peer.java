package peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import disk.Disk;
import message.Message;
import peer.channels.Listener;
import peer.protocols.backup.Backup;
import peer.protocols.delete.Delete;
import peer.protocols.restore.Restore;

public class Peer implements PeerInterface {

	// private static final long serialVersionUID = 4988282307993613946L;

	private int peerId;
	private Disk disk;

	private Registry rmiReg;

	private String pVersion;
	private String accessPoint;

	private MulticastSocket socket;

	private InetAddress mcAddress;
	private InetAddress mdbAddress;
	private InetAddress mdrAddress;
	private int mcPort;
	private int mdbPort;
	private int mdrPort;

	private Listener mcChannel;
	private Listener mdbChannel;
	private Listener mdrChannel;

	public static void main(String[] args) {
		int id = Integer.parseInt(args[1]);
		int mcPort = Integer.parseInt(args[4]);
		int mdbPort = Integer.parseInt(args[6]);
		int mdrPort = Integer.parseInt(args[8]);
		try {
			new Peer(args[0], id, args[2], args[3], mcPort, args[5], mdbPort, args[7], mdrPort);
		} catch (RemoteException e) {
			e.printStackTrace();
			return;
		}
	}

	public Peer(String pVersion, int sid, String accessPoint, String mcAddress, int mcPort, String mdbAddress,
			int mdbPort, String mdrAddress, int mdrPort) throws RemoteException {

		this.peerId = sid;

		try {
			this.mcAddress = InetAddress.getByName(mcAddress);
			this.mdbAddress = InetAddress.getByName(mdbAddress);
			this.mdrAddress = InetAddress.getByName(mdrAddress);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		this.mcPort = mcPort;
		this.mdbPort = mdbPort;
		this.mdrPort = mdrPort;

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

		System.out.println("Peer " + this.peerId + " running...");
	}

	public void startRMI() {
		try {
			try {
				this.rmiReg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			} catch (Exception e) {
				this.rmiReg = LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
			}
			PeerInterface stub = (PeerInterface) UnicastRemoteObject.exportObject(this, 0);
			rmiReg.rebind(this.accessPoint, stub);
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
		Thread t = new Thread(new Delete(this, path));
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void restore(String path) {
		Thread t = new Thread(new Restore(this, path));
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void reclaim(float space) {

	}

	public String state() {
		return "test";
	}

	public void sendToMc(Message m) throws IOException {

		byte[] buf = m.toBytes();

		DatagramPacket packet = new DatagramPacket(buf, buf.length, this.mcChannel.getAddress(),
				this.mcChannel.getPort());
		socket.send(packet);

	}

	public void sendToMdb(Message m) throws IOException {

		byte[] buf = m.toBytes();

		DatagramPacket packet = new DatagramPacket(buf, buf.length, this.mdbChannel.getAddress(),
				this.mdbChannel.getPort());
		socket.send(packet);
	}

	public void sendToMdr(Message m) throws IOException {

		byte[] buf = m.toBytes();

		DatagramPacket packet = new DatagramPacket(buf, buf.length, this.mdrChannel.getAddress(),
				this.mdrChannel.getPort());
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

	/**
	 * @return the mcAddress
	 */
	public InetAddress getMcAddress() {
		return mcAddress;
	}

	/**
	 * @return the mcPort
	 */
	public int getMcPort() {
		return mcPort;
	}

	/**
	 * @return the mdbAddress
	 */
	public InetAddress getMdbAddress() {
		return mdbAddress;
	}

	/**
	 * @return the mdbPort
	 */
	public int getMdbPort() {
		return mdbPort;
	}

	/**
	 * @return the mdrAddress
	 */
	public InetAddress getMdrAddress() {
		return mdrAddress;
	}

	/**
	 * @return the mdrPort
	 */
	public int getMdrPort() {
		return mdrPort;
	}

	public void deleteFile(String fileID) {
		disk.deleteFileDirectory(fileID);
	}

}