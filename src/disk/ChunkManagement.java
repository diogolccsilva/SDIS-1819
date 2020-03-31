package disk;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import chunk.Chunk;

/**
 * ChunkManagement
 */
public class ChunkManagement {

	private static ChunkManagement chunkManagementInstance = null;

	private Map<String, Map<Integer, Integer>> storesCounter;

	private Map<String, BlockingQueue<Chunk>> restoreChunks;

	private ChunkManagement() {
		storesCounter = new HashMap<String, Map<Integer, Integer>>();
		restoreChunks = new HashMap<String, BlockingQueue<Chunk>>();
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

	public int registerStored(String fileId, int chunkNo) {
		if (!storesCounter.containsKey(fileId)) {
			storesCounter.put(fileId, new HashMap<Integer, Integer>());
		}
		if (!storesCounter.get(fileId).containsKey(chunkNo)) {
			storesCounter.get(fileId).put(chunkNo, 1);
		} else {
			int nStores = storesCounter.get(fileId).get(chunkNo);
			storesCounter.get(fileId).put(chunkNo, nStores + 1);
		}
		return storesCounter.get(fileId).get(chunkNo);
	}

	public int registerRemoved(String fileId, int chunkNo) {
		if (!storesCounter.containsKey(fileId)) {
			return 0;
		}
		if (!storesCounter.get(fileId).containsKey(chunkNo)) {
			return 0;
		} else {
			int nStores = storesCounter.get(fileId).get(chunkNo);
			storesCounter.get(fileId).put(chunkNo, nStores - 1);
			return storesCounter.get(fileId).get(chunkNo);
		}
	}

	public void deleteStores(String fileId) {
		storesCounter.remove(fileId);
	}

	public boolean createFileChunksQueue(String fileId) {
		if (!restoreChunks.containsKey(fileId)) {
			restoreChunks.put(fileId,new LinkedBlockingDeque<Chunk>());
			return true;
		}
		return false;
	}

	public void addRestoreChunk(Chunk chunk) {
		String fileId = chunk.getFileID();
		if (!restoreChunks.containsKey(fileId)) {
			createFileChunksQueue(fileId);
		}
		restoreChunks.get(fileId).add(chunk);
	}

	/**
	 * @return the restoreChunks
	 */
	public BlockingQueue<Chunk> getRestoreChunks(String fileId) {
		if (!restoreChunks.containsKey(fileId)) {
			createFileChunksQueue(fileId);
		}
		return restoreChunks.get(fileId);
	}

	/**
	 * Checks if a chunks is already in the queue
	 * @param chunk
	 * @return
	 */
	public boolean checkChunk(Chunk chunk) {
		return getRestoreChunks(chunk.getFileID()).contains(chunk);
	}

	public boolean removeChunk(Chunk chunk) {
		return getRestoreChunks(chunk.getFileID()).remove(chunk);
	}

}
