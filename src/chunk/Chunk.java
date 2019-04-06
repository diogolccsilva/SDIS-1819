package chunk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class Chunk {
    public static final int CHUNK_MAX_SIZE = 64000;

    public int chunkNo;
    public String fileID;

    public int repDegree;

    public byte[] data;

    public Chunk(String fileID, int chunkNo, int repDegree, byte[] data) {
        this.chunkNo = chunkNo;
        this.fileID = fileID;
        this.data = data;
        this.repDegree = repDegree;
    }

    /**
     * @return the chunk's file ID
     */
    public String getFileID() {
        return fileID;
    }

    /**
     * @return the chunk number
     */
    public int getChunkNo() {
        return chunkNo;
    }

    /**
     * @return the chunk replication degree
     */
    public int getRepDegree() {
        return repDegree;
    }

    /**
     * @return the chunk data byte array
     */
    public byte[] getData() {
        return data;
    }

    public static Chunk[] splitFile(File file, int repDegree) {
        int nChunks = (int) (file.getTotalSpace() / CHUNK_MAX_SIZE);
        Chunk[] chunks = new Chunk[nChunks];
        try (RandomAccessFile data = new RandomAccessFile(file, "r")) {
            byte[] eight = new byte[8];
            for (int i = 0; i < nChunks; i++) {
                data.readFully(CHUNK_MAX_SIZE);
                // do something with the 8 bytes
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return chunks;
    }

}
