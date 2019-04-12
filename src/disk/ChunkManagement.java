package disk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javafx.util.Pair;

/**
 * ChunkManagement
 */
public class ChunkManagement {

	private static ChunkManagement chunkManagementInstance = null;

	private Map<String, Map<Integer, Integer>> storesCounter;

	private ChunkManagement() {
		storesCounter = new HashMap<String, Map<Integer, Integer>>();
	}

	public static ChunkManagement getInstance() {
		if (chunkManagementInstance == null) {
			chunkManagementInstance = new ChunkManagement();
		}
		return chunkManagementInstance;
	}

	public int getStores(String fileId, int chunkNo) {
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
}