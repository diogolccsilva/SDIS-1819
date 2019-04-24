package disk;

import chunk.*;
import utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Disk
 */
public class Disk {

	public static final String fileSeparator = System.getProperty("file.separator");
	public static final String filesPath = "." + fileSeparator + "files" + fileSeparator;
	public static final String resourcesPath = filesPath + "resources" + fileSeparator;
	public static final String defaultDiskLocation = filesPath + "peers" + fileSeparator;
	public static final long defaultDiskSize = 10000000; /* Disk size in KBytes */

	private long size; /* Disk size in bytes */
	private String diskLocation;
	private File directory;
	private File backupDirectory, restoredDirectory;

	public void restoreFile(Chunk[] chunks, String fileName) {
		Chunk.restoreFile(chunks, getRestoredDirectoryPath() + fileSeparator + fileName);
	}

	public Disk(String diskName) {
		this(diskName, defaultDiskSize);
	}

	public Disk(String diskName, float size) {
		this.diskLocation = defaultDiskLocation + diskName;
		this.size = (long) (size * 1000);
		createDiskDirectory();
	}

	/**
	 * @return the backupDirectory
	 */
	public File getBackupDirectory() {
		return backupDirectory;
	}

	/**
	 * @return the defaultDiskLocation
	 */
	public static String getDefaultDiskLocation() {
		return defaultDiskLocation;
	}

	/**
	 * @return the directory
	 */
	public File getDirectory() {
		return directory;
	}

	/**
	 * @return the restoredDirectory
	 */
	public File getRestoredDirectory() {
		return restoredDirectory;
	}

	public String getRestoredDirectoryPath() {
		return restoredDirectory.getAbsolutePath();
	}

	public boolean createDiskDirectory() {
		directory = new File(this.diskLocation);
		directory.mkdirs();
		createBackupDirectory();
		createRestoredDirectory();
		return true;
	}

	public boolean createBackupDirectory() {
		backupDirectory = new File(this.diskLocation + fileSeparator + "backup");
		if (backupDirectory.mkdirs()) {
			return true;
		}
		return false;
	}

	public boolean createRestoredDirectory() {
		restoredDirectory = new File(this.diskLocation + fileSeparator + "restored");
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
		File fileFolder = new File(backupDirectory.getPath() + fileSeparator + fileId);
		fileFolder.mkdirs();
		return fileFolder;
	}

	public boolean storeChunk(Chunk chunk) {
		String fileName = chunk.getChunkNo() + "-" + chunk.getRepDegree();

		File folder = createFileFolder(chunk.getFileID());

		File chunkFile = new File(folder.getPath() + fileSeparator + fileName);

		if (chunkFile.exists()) {
			return false;
		}

		try (FileOutputStream fos = new FileOutputStream(chunkFile)) {
			fos.write(chunk.getData());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public File[] getFileChunkFiles(String fileId) {
		File fileChunkDirectory = getFileChunkDirectory(fileId);
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

	public File getFileChunkDirectory(String fileId) {
		return new File(backupDirectory.getPath() + fileSeparator + fileId);
	}

	public static Chunk parseFileToChunk(String fileId, File chunkFile) {
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

	public void deleteFileDirectory(String fileId) {
		//File fileDir = getFileChunkDirectory(fileId);
		/*try {
			Utils.deleteDirectoryRecursively(fileDir);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
		*/
		File fileDir = new File(backupDirectory + fileSeparator + fileId);
		System.out.println("FileDir: \"" + fileDir + "\"");
		Utils.deleteDirectoryRecursively(fileDir);
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
