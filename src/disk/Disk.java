package disk;

import chunk.*;
import java.io.File;

/**
 * Disk
 */
public class Disk {

	public static final String DEFAULT_DISK_LOCATION = "../../files/peers/";

	private long size; /* Disk size in bytes */
	private String diskLocation;
	private File directory;

	public Disk(String diskName) {
		this(diskName, 1);
	}

	public Disk(String diskName, int size) {
		this.diskLocation = DEFAULT_DISK_LOCATION + diskName;
		this.size = size * 1000;
		this.directory = new java.io.File(this.diskLocation);
	}

	public boolean createDiskDirectory() {
		File dir = new File(this.diskLocation);
		boolean success = dir.mkdirs();
		/*
		 * if (!success) { System.out.println("Failed to create peer's directory"); }
		 * else { System.out.println("Created peer's directory"); }
		 */
		return success;
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

		return 0;
	}

	public int deleteChunk() {

		return 0;
	}

}
