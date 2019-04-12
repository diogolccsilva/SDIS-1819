package peer.channels;

import java.net.InetAddress;

/**
 * Sender
 */
public interface Sender {

	public void send(byte[] message);

	public void init(InetAddress address, int port);

	public void destroy();
}