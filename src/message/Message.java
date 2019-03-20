package message;

import chunk.Chunk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class Message {
    
    public static final int MESSAGE_PACKET_MAX_SIZE = Chunk.CHUNK_MAX_SIZE + MessageHeader.MESSAGE_HEADER_MAX_SIZE;

    private MessageHeader header;
    private byte[] body;

    public Message(byte[] data) {
        String rawMessage = new String(data);
        int index = rawMessage.indexOf("\r\n\r\n");
        String rawHeader = rawMessage.substring(0,index).trim();
        header = new MessageHeader(rawHeader);
        body = rawMessage.substring(index).getBytes();
    }

    public Message(MessageHeader header, byte[] body) {
        this.header = header;
        this.body = body;
    }
    
    public byte[] toBytes() throws IOException
    {
        byte[] result;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(header.toString().getBytes());
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
    /*public static boolean isValid(String message)
    {
        String myString = message.substring(0,message.indexOf("\r\n\r\n"));
        
        return myString.matches(".* [0-9].[0-9] [0-9]+ .{32} .{6} [0-9]\r\n\r\n(.*)?");
    }*/
}