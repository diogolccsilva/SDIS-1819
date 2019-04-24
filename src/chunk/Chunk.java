package chunk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Base64;

public class Chunk {
    public static final int CHUNK_MAX_SIZE = 64000;

    private int chunkNo;
    private String fileID;

    private int repDegree;

    private byte[] data;

    public Chunk(String fileID, int chunkNo, int repDegree, byte[] data) {
        this.chunkNo = chunkNo;
        this.fileID = fileID;
        this.data = data;
        this.repDegree = repDegree;
    }

    /**
     * @return The chunk's file ID
     */
    public String getFileID() {
        return fileID;
    }

    /**
     * @return The chunk number
     */
    public int getChunkNo() {
        return chunkNo;
    }

    /**
     * @return The chunk replication degree
     */
    public int getRepDegree() {
        return repDegree;
    }

    /**
     * @return The chunk data byte array
     */
    public byte[] getData() {
        return data;
    }

    public static Chunk[] splitFile(String path, int repDegree) {
        File file = new File(path);
        return splitFile(file, repDegree);
    }

    public static int getNumberOfFileChunks(File file) {
        long nFileBytes = file.length();
        return (int) ((nFileBytes - 1) / CHUNK_MAX_SIZE + 1);
    }

    public static Chunk[] splitFile(File file, int repDegree) {
        if (!file.isFile()) {
            return null;
        }
        long nFileBytes = file.length();
        int nChunks = getNumberOfFileChunks(file);
        Chunk[] chunks = new Chunk[nChunks];
        String fileId = generateFileId(file);
        try (RandomAccessFile data = new RandomAccessFile(file, "r")) {
            int nReadBytes = 0;
            for (int i = 0; i < nChunks; i++) {
                int nBytesToRead;
                int nBytesMissing = (int) (nFileBytes - nReadBytes);
                if (nBytesMissing < CHUNK_MAX_SIZE) {
                    nBytesToRead = nBytesMissing;
                } else {
                    nBytesToRead = CHUNK_MAX_SIZE;
                }
                byte[] newData = new byte[nBytesToRead];
                data.readFully(newData);
                Chunk newChunk = new Chunk(generateFileId(file), i + 1, repDegree, newData);
                chunks[i] = newChunk;
                nReadBytes += CHUNK_MAX_SIZE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chunks;
    }

    public static String generateFileId(File file) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        String toEncode = file.getName() + Long.toString(file.length());
        byte[] encodedhash;
        try {
            encodedhash = digest.digest(toEncode.getBytes("UTF-8"));
            return Base64.getEncoder().withoutPadding().encodeToString(encodedhash);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "invalid";
    }

    public static void sortChunkArray(Chunk[] chunks) {
        for (int i = 0; i < chunks.length; i++) {
            Chunk currChunk = chunks[i];
            int nCurrChunk = currChunk.getChunkNo();
            if (nCurrChunk != i + 1) {
                Chunk tempChunk = chunks[i];
                chunks[i] = chunks[nCurrChunk - 1];
                chunks[nCurrChunk - 1] = tempChunk;
            }
        }
    }

    public static void restoreFile(Chunk[] chunks, String filePath) {
        //sortChunkArray(chunks);
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            for (int i = 0; i < chunks.length; i++) {
                fos.write(chunks[i].getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        final Chunk c = (Chunk) obj;
        return (c.chunkNo == chunkNo && c.fileID.equals(fileID) && c.repDegree == repDegree);
    }
}
