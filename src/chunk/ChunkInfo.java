package chunk;

public class ChunkInfo {

    private int chunkNo;
    private String fileID;
    private int repDegree;
    private long size;

    public ChunkInfo(int chunkNo, String fileID, int repDegree, long size) {
        this.chunkNo = chunkNo;
        this.fileID = fileID;
        this.repDegree = repDegree;
        this.size = size;
    }

    /**
     * @return the chunkNo
     */
    public int getChunkNo() {
        return chunkNo;
    }

    /**
     * @return the fileID
     */
    public String getFileID() {
        return fileID;
    }

    /**
     * @return the repDegree
     */
    public int getRepDegree() {
        return repDegree;
    }

    /**
     * @return the size
     */
    public long getSize() {
        return size;
    }

    @Override
    public boolean equals(Object obj) {
        ChunkInfo objChunkInfo = (ChunkInfo)obj;
        return this.fileID == objChunkInfo.fileID && this.chunkNo == objChunkInfo.chunkNo;
    }
}