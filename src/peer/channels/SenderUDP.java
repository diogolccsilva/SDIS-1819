package peer.channels;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * SenderUDP
 */
public class SenderUDP implements Sender {

	private DatagramSocket socket;

	public SenderUDP() {
	}

	@Override
	public void send(byte[] message) {
		DatagramPacket packet = new DatagramPacket(message, message.length);
		try {
			socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void init(InetAddress address, int port) {
		try {
			socket = new DatagramSocket(port, address);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		socket.disconnect();
	}



}