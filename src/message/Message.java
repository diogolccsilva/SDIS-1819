package message;

import chunk.Chunk;
import peer.Peer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Message {

    public static final int MESSAGE_PACKET_MAX_SIZE = Chunk.CHUNK_MAX_SIZE + MessageHeader.MESSAGE_HEADER_MAX_SIZE;

    private MessageHeader header;
    private byte[] body;

    public Message(byte[] data) {
        String rawMessage = new String(data);
        int index = rawMessage.indexOf(MessageHeader.CRLF) + 4;
        byte[] rawHeader = new byte[index+1];
        System.arraycopy(data, 0, rawHeader, 0, index - 4);
        header = new MessageHeader(new String(rawHeader));
        int bodyLength = data.length - index;
        body = new byte[bodyLength];
        System.arraycopy(data, index, body, 0, bodyLength);
    }

    public Message(MessageHeader header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public byte[] toBytes() throws IOException {
        byte[] result;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(header.toString().getBytes());
        if (body != null)
            bos.write(body);
        result = bos.toByteArray();
        return result;
    }

    public MessageHeader getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }

    /*
     * public static boolean isValid(String message) { String myString =
     * message.substring(0,message.indexOf("\r\n\r\n"));
     * 
     * return
     * myString.matches(".* [0-9].[0-9] [0-9]+ .{32} .{6} [0-9]\r\n\r\n(.*)?"); }
     */

    public static Message parsePutChunkMessage(Chunk chunk, Peer peer) {
        MessageHeader header = new MessageHeader("PUTCHUNK", peer.getpVersion(), peer.getPeerId(), chunk.getFileID(), chunk.getChunkNo(),
                chunk.getRepDegree());
        Message message = new Message(header, chunk.getData());

        return message;
    }

    public static Message parseStoredMessage(Chunk chunk, Peer peer) {
        try {
            MessageHeader header = new MessageHeader("STORED", peer.getpVersion(), peer.getPeerId(), chunk.getFileID(), chunk.getChunkNo());
            Message message = new Message(header, null);
            return message;
        } catch (InvalidHeaderParameters e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Message parseDeleteMessage(String fileId, Peer peer) {
        try {
            MessageHeader header = new MessageHeader("DELETE", peer.getpVersion(), peer.getPeerId(), fileId);
            Message message = new Message(header, null);
            return message;
        } catch (InvalidHeaderParameters e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Message parseGetChunkMessage(String fileId, int chunkNo, Peer peer) {
        try {
            MessageHeader header = new MessageHeader("GETCHUNK", peer.getpVersion(), peer.getPeerId(), fileId, chunkNo);
            Message message = new Message(header, null);
            return message;
        } catch (InvalidHeaderParameters e) {
            e.printStackTrace();
        }
        return null;
    }

	public static Message parseChunkMessage(Chunk chunk, Peer peer) {
		try {
            MessageHeader header = new MessageHeader("CHUNK", peer.getpVersion(), peer.getPeerId(), chunk.getFileID(), chunk.getChunkNo());
            Message message = new Message(header, chunk.getData());
            return message;
        } catch (InvalidHeaderParameters e) {
            e.printStackTrace();
        }
        return null;
	}

}