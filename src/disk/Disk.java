package disk;

import chunk.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FilenameFilter;

/**
 * Disk
 */
public class Disk {

	public static final String DEFAULT_DISK_LOCATION = "./files/peers/";

	private long size; /* Disk size in bytes */
	private String diskLocation;
	private File directory;

	public static void main(String[] args) {
		Disk disk = new Disk("peer1");
	}

	public Disk(String diskName) {
		this(diskName, 1);
	}

	public Disk(String diskName, float size) {
		this.diskLocation = DEFAULT_DISK_LOCATION + diskName;
		this.size = (long) (size * 1000);
		if (!this.createDiskDirectory()) {
			// throw (DiskFailedToInitialize);
			System.out.println("Failed to create directory");
		}
	}

	public boolean createDiskDirectory() {
		File dir = new File(this.diskLocation);
		if (dir.mkdirs()) {
			this.directory = dir;
			return true;
		}
		return false;
	}

	public String getDiskLocation() {
		return diskLocation;
	}

	public long getSize() {
		return size;
	}

	public long getOccupiedSpace() {
		return folderSize(this.directory);
	}

	public long getFreeSpace() {
		return size - getOccupiedSpace();
	}

	public static long folderSize(File directory) {
		long length = 0;
		for (File file : directory.listFiles()) {
			if (file.isFile())
				length += file.length();
			else
				length += folderSize(file);
		}
		return length;
	}

	public int storeChunk(Chunk chunk) {
		String fileName = chunk.getFileID() + "-" + chunk.getChunkNo() + "-" + chunk.getRepDegree();

		File chunkFile = new File(diskLocation + "/" + fileName);

		try (FileOutputStream fos = new FileOutputStream(chunkFile)) {
			fos.write(chunk.getData());
		} catch (Exception e) {

		}

		return 0;
	}

	public int deleteChunk() {

		return 0;
	}

	public Chunk[] getFileChunks(String fileId) {
		File chunkFiles[] = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				boolean accepts = false;
				String parsedName[] = name.split("-");
				if (parsedName[0] == fileId) {
					accepts = true;
				}
				return accepts;
			}
		});

		Chunk[] chunks = new Chunk[chunkFiles.length];

		for (int i = 0; i < chunkFiles.length; i++) {
			chunks[i] = parseFileToChunk(chunkFiles[i]);
		}

		return chunks;
	}

	public Chunk getChunk(String fileId, int chunkId) {
		File[] chunkFiles = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(fileId + "-" + chunkId);
			}
		});

		File chunkFile = chunkFiles[0];

		return parseFileToChunk(chunkFile);
	}

	public Chunk parseFileToChunk(File chunkFile) {
		String fileName = chunkFile.getName();
		String parsedName[] = fileName.split("-");

		String fileId = parsedName[0];
		int chunkId = Integer.parseInt(parsedName[1]);
		int repDegree = Integer.parseInt(parsedName[2]);

		byte[] data = new byte[(int) chunkFile.length()];

		try (FileInputStream fis = new FileInputStream(chunkFile)) {
			fis.read(data);
		} catch (Exception e) {

		}

		Chunk chunk = new Chunk(fileId, chunkId, repDegree, data);

		return chunk;
	}

}
