package peer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PeerInterface extends Remote {
    void backup(String path,int replicationDeg) throws RemoteException;
    void delete(String path) throws RemoteException;
    void restore(String path) throws RemoteException;
    void reclaim(float space) throws RemoteException;
    String state() throws  RemoteException;
}