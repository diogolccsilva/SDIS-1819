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
	private File backupDirectory, restoredDirectory;

	public static void main(String[] args) {
		Disk disk = new Disk("peer1");
		disk.createFileFolder("1");
		String data = "DSFAg";
		disk.storeChunk(new Chunk("2", 1, 1, data.getBytes()));
		disk.storeChunk(new Chunk("2", 2, 1, data.getBytes()));
		System.out.println(disk.getFileChunks("2").length);
		Chunk chunk1 = disk.getChunk("2", 1);
		Chunk chunk3 = disk.getChunk("2", 3);
		disk.deleteChunk("2", 2);
		System.out.println(args[0]);
		Chunk[] chunks = Chunk.splitFile(args[0], 1);
		disk.storeChunk(chunks[0]);
	}

	public Disk(String diskName) {
		this(diskName, 1);
	}

	public Disk(String diskName, float size) {
		this.diskLocation = DEFAULT_DISK_LOCATION + diskName;
		this.size = (long) (size * 1000);
		createDiskDirectory();
	}

	public boolean createDiskDirectory() {
		directory = new File(this.diskLocation);
		directory.mkdirs();
		createBackupDirectory();
		createRestoredDirectory();
		return true;
	}

	public boolean createBackupDirectory() {
		backupDirectory = new File(this.diskLocation + "/backup");
		if (backupDirectory.mkdirs()) {
			return true;
		}
		return false;
	}

	public boolean createRestoredDirectory() {
		restoredDirectory = new File(this.diskLocation + "/restored");
		if (restoredDirectory.mkdirs()) {
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

	public File createFileFolder(String fileId) {
		File fileFolder = new File(backupDirectory.getPath() + "/" + fileId);
		fileFolder.mkdirs();
		return fileFolder;
	}

	public int storeChunk(Chunk chunk) {
		String fileName = chunk.getChunkNo() + "-" + chunk.getRepDegree();

		File folder = createFileFolder(chunk.getFileID());

		File chunkFile = new File(folder.getPath() + "/" + fileName);

		try (FileOutputStream fos = new FileOutputStream(chunkFile)) {
			fos.write(chunk.getData());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public File[] getFileChunkFiles(String fileId) {
		File fileChunkDirectory = new File(backupDirectory.getPath() + "/" + fileId);
		return fileChunkDirectory.listFiles();
	}

	public Chunk[] getFileChunks(String fileId) {
		File chunkFiles[] = getFileChunkFiles(fileId);

		Chunk[] chunks = new Chunk[chunkFiles.length];

		for (int i = 0; i < chunkFiles.length; i++) {
			chunks[i] = parseFileToChunk(fileId, chunkFiles[i]);
		}

		return chunks;
	}

	public File getChunkFile(String fileId, int chunkId) {
		File chunkFiles[] = getFileChunkFiles(fileId);
		for (File chunkFile : chunkFiles) {
			if (chunkFile.getName().startsWith(chunkId + "-")) {
				return chunkFile;
			}
		}
		return null;
	}

	public Chunk getChunk(String fileId, int chunkId) {
		Chunk[] chunks = getFileChunks(fileId);

		for (Chunk chunk : chunks) {
			if (chunk.getChunkNo() == chunkId) {
				return chunk;
			}
		}
		return null;
	}

	public Chunk parseFileToChunk(String fileId, File chunkFile) {
		String fileName = chunkFile.getName();
		String parsedName[] = fileName.split("-");

		int chunkId = Integer.parseInt(parsedName[0]);
		int repDegree = Integer.parseInt(parsedName[1]);

		byte[] data = new byte[(int) chunkFile.length()];

		try (FileInputStream fis = new FileInputStream(chunkFile)) {
			fis.read(data);
		} catch (Exception e) {

		}

		Chunk chunk = new Chunk(fileId, chunkId, repDegree, data);

		return chunk;
	}

	public boolean deleteChunk(String fileId, int chunkId) {
		File chunkFile = getChunkFile(fileId, chunkId);
		if (!chunkFile.delete()) {
			System.out.println("Failed to delete " + chunkFile);
			return false;
		}
		return true;
	}

}
