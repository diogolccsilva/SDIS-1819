package message;

public class MessageHeader {

    public static final int MESSAGE_HEADER_MAX_SIZE = 1000;

    private String messageType, version, fileId;
    private int senderId, chunkNo, replicaDeg;

    public MessageHeader(String rawHeader) {
        System.out.println("Messageheader: " + rawHeader);
        String[] sArray = rawHeader.trim().split(" ");
        this.messageType = sArray[0];
        this.version = sArray[1];
        this.senderId = Integer.parseInt(sArray[2]);
        this.fileId = sArray[3];
        System.out.println("fileid: " + fileId);
        if (this.messageType.equals("PUTCHUNK") || this.messageType.equals("STORED")
                || this.messageType.equals("GETCHUNK") || this.messageType.equals("REMOVED")
                || this.messageType.equals("CHUNK")) {
            String cn = sArray[4].replaceAll("[^\\d]", "");
            this.chunkNo = Integer.parseInt(cn);
        }
        if (this.messageType.equals("PUTCHUNK")) {
            String rep = sArray[5].replaceAll("[^\\d]", "");
            this.replicaDeg = Integer.parseInt(rep);
        }
    }

    public MessageHeader(String messageType, String version, int senderId, String fileId, int chunkNo)
            throws InvalidHeaderParameters {
        this.messageType = messageType;
        if (this.messageType.equals("PUTCHUNK")) {
            throw new InvalidHeaderParameters();
        }
        this.version = version;
        this.senderId = senderId;
        this.fileId = fileId;
        this.chunkNo = chunkNo;
    }

    public MessageHeader(String messageType, String version, int senderId, String fileId, int chunkNo, int replicaDeg) {
        this.messageType = messageType;
        this.version = version;
        this.senderId = senderId;
        this.fileId = fileId;
        this.chunkNo = chunkNo;
        this.replicaDeg = replicaDeg;
    }

    public MessageHeader(String messageType, String version, int senderId, String fileId)
            throws InvalidHeaderParameters {
        this.messageType = messageType;
        if (!this.messageType.equals("DELETE")) {
            throw new InvalidHeaderParameters();
        }
        this.version = version;
        this.senderId = senderId;
        this.fileId = fileId;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public String getVersion() {
        return this.version;
    }

    public int getSenderId() {
        return this.senderId;
    }

    public String getFileId() {
        return this.fileId;
    }

    public int getChunkNo() {
        return this.chunkNo;
    }

    public int getReplicaDeg() {
        return this.replicaDeg;
    }

    @Override
    public String toString() {
        switch (messageType) {
        case "GETCHUNK":
            return this.messageType + " " + this.version + " " + Integer.toString(this.senderId) + " " + this.fileId
                    + " " + Integer.toString(this.chunkNo) + "\r\n\r\n";
        case "PUTCHUNK":
            return this.messageType + " " + this.version + " " + Integer.toString(this.senderId) + " " + this.fileId
                    + " " + Integer.toString(this.chunkNo) + " " + Integer.toString(this.replicaDeg) + "\r\n\r\n";
        case "STORED":
            return this.messageType + " " + this.version + " " + Integer.toString(this.senderId) + " " + this.fileId
                    + " " + Integer.toString(this.chunkNo) + "\r\n\r\n";
        case "CHUNK":
            return this.messageType + " " + this.version + " " + Integer.toString(this.senderId) + " " + this.fileId
                    + " " + Integer.toString(this.chunkNo) + "\r\n\r\n";
        case "DELETE":
            return this.messageType + " " + this.version + " " + Integer.toString(this.senderId) + " " + this.fileId
                    + "\r\n\r\n";
        default:
            return "";

        }
        // return this.messageType + " " + this.version + " " +
        // Integer.toString(this.senderId) + " " + this.fileId + " " +
        // Integer.toString(this.chunkNo) + " " +
        // Integer.toString(this.replicaDeg)+"\r\n\r\n";
    }
}
