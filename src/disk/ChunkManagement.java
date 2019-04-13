package disk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import chunk.Chunk;
import javafx.util.Pair;

/**
 * ChunkManagement
 */
public class ChunkManagement {

	private static ChunkManagement chunkManagementInstance = null;

	private Map<String, Map<Integer, Integer>> storesCounter;

	private BlockingQueue<Chunk> restoreChunks;

	private ChunkManagement() {
		storesCounter = new HashMap<String, Map<Integer, Integer>>();
		restoreChunks = new LinkedBlockingDeque<Chunk>();
	}

	public static ChunkManagement getInstance() {
		if (chunkManagementInstance == null) {
			chunkManagementInstance = new ChunkManagement();
		}
		return chunkManagementInstance;
	}

	public int getStores(String fileId, int chunkNo) {
		if (!storesCounter.containsKey(fileId)){
			return 0;
		}
		if (!storesCounter.get(fileId).containsKey(chunkNo)){
			return 0;
		}
		return storesCounter.get(fileId).get(chunkNo);
	}

	public void registerStored(String fileId, int chunkNo) {
		if (!storesCounter.containsKey(fileId)) {
			storesCounter.put(fileId, new HashMap<Integer, Integer>());
		}
		if (!storesCounter.get(fileId).containsKey(chunkNo)) {
			storesCounter.get(fileId).put(chunkNo, 1);
		} else {
			int nStores = storesCounter.get(fileId).get(chunkNo);
			storesCounter.get(fileId).put(chunkNo, nStores + 1);
		}
	}

	public void deleteStores(String fileId) {
		storesCounter.remove(fileId);
	}

	public void addRestoreChunk(Chunk chunk) {
		restoreChunks.add(chunk);
	}

	/**
	 * @return the restoreChunks
	 */
	public BlockingQueue<Chunk> getRestoreChunks() {
		return restoreChunks;
	}

}