package peer.channels;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * SenderTCP
 */
public class SenderTCP implements Sender {

	private Socket socket;

	public SenderTCP() {
		
	}

	@Override
	public void send(byte[] message) {
		
	}

	@Override
	public void init(InetAddress address, int port) {
		try {
			socket = new Socket(address, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {

	}

	
}