package chunk;

public class Chunk {
    public static final int CHUNK_MAX_SIZE = 64000;

    public int chunkNo;
    public int fileID;

    public int repDegree;

    public byte[] data;

    public Chunk(int fileID, int chunkNo, int repDegree, byte[] data){
        this.chunkNo = chunkNo;
        this.fileID = fileID;
        this.data = data;
        this.repDegree = repDegree;
    }

    public int getFileID(){
        return fileID;
    }

    public int getChunkNo(){
        return chunkNo;
    }

    public int getRepDegree(){
        return repDegree;
    }

    public byte[] getData(){
        return getData();
    }
}