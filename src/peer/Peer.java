package peer;

import java.io.File;

public class Peer {
	private int peerId;

	public Peer(int peerId) {
		this.peerId = peerId;
		createDir();
	}

	public static void main(String[] args) {
		int id = Integer.parseInt(args[0]);
		Peer peer = new Peer(id);
	}

	public boolean createDir() {
		File dir = new File("./peers/peer" + peerId);
		boolean success = dir.mkdirs();
		/*if (!success) {
			System.out.println("Failed to create peer's directory");
		} else {
			System.out.println("Created peer's directory");
		}*/
		return success;
	}
}