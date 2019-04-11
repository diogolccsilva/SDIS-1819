package message;

public class MessageHeader {

    public static final int MESSAGE_HEADER_MAX_SIZE = 1000;

    private String messageType, version, fileId;
    private int senderId, chunkNo, replicaDeg;

    public MessageHeader(String rawHeader) {
        String[] sArray = rawHeader.split(" ");
        this.messageType = sArray[0];
        this.version = sArray[1];
        this.senderId = Integer.parseInt(sArray[2]);
        this.fileId = sArray[3];
        if (this.messageType.equals("PUTCHUNK") || this.messageType.equals("STORED") || this.messageType.equals("GETCHUNK") || this.messageType.equals("REMOVED")){
            this.chunkNo = Integer.parseInt(sArray[4]);
        }
        if (this.messageType.equals("PUTCHUNK")){
            this.replicaDeg = Integer.parseInt(sArray[5]);
        }
    }

    public MessageHeader(String messageType, String version, int senderId, String fileId, int chunkNo, int replicaDeg)
    {
        this.messageType = messageType;
        this.version = version;
        this.senderId = senderId;
        this.fileId = fileId;
        this.chunkNo = chunkNo;
        this.replicaDeg = replicaDeg;
    }

    public String getMessageType()
    {
        return this.messageType;
    }

    public String getVersion()
    {
        return this.version;
    }

    public int getSenderId()
    {
        return this.senderId;
    }

    public String getFileId()
    {
        return this.fileId;
    }

    public int getChunkNo()
    {
        return this.chunkNo;
    }

    public int getReplicaDeg()
    {
        return this.replicaDeg;
    }

    @Override
    public String toString()
    {
        return this.messageType + " " + this.version + " " + Integer.toString(this.senderId) + " " + this.fileId + " " + Integer.toString(this.chunkNo) + " " + Integer.toString(this.replicaDeg)+"\r\n\r\n";
    }
}
