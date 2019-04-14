package client;

import utils.Utils;

import java.lang.Integer;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import peer.PeerInterface;

import java.lang.Float;

/**
 * TestApp
 */
public class TestApp {

    private static String peer_ap;
    private static String operation;
    private static String filePath;
    private static float diskSpace;
    private static int replicationDeg;
    private static PeerInterface pInterface;

    public static void main(String[] args) {
        if (!handleInputs(args)) {
            usage();
            return;
        }

        connect();

        switch (operation) {
        case "BACKUP":
            System.out.println("BACKUP:\n\tFile: " + filePath + "\n\tReplication Degree: " + replicationDeg);
            try {
                pInterface.backup(filePath, replicationDeg);
            } catch (RemoteException e) {
                System.out.println("BACKUP ERROR!");
                e.printStackTrace();
            }
            System.out.println("BACKUP SUCCESSFUL!");
            break;
        case "RESTORE":
            System.out.println("RESTORE:\n\tFile: " + filePath);
            try {
                pInterface.restore(filePath);
            } catch (RemoteException e) {
                System.out.println("RESTORE ERROR!");
                e.printStackTrace();
            }
            System.out.println("RESTORE SUCCESSFUL!");
            break;
        case "DELETE":
            System.out.println("DELETE:\n\tFile: " + filePath);
            try {
                pInterface.delete(filePath);
            } catch (RemoteException e) {
                System.out.println("DELETE ERROR!");
                e.printStackTrace();
            }
            System.out.println("DELETE SUCCESSFUL!");
            break;
        case "RECLAIM":
            System.out.println("RECLAIM:\n\tDisk Space: " + diskSpace);
            try {
                pInterface.reclaim(diskSpace);
            } catch (RemoteException e) {
                System.out.println("RECLAIM ERROR!");
                e.printStackTrace();
            }
            System.out.println("RECLAIM SUCCESSFUL!");
            break;
        case "STATE":
            System.out.println("STATE:");
            try {
                pInterface.state();
            } catch (RemoteException e) {
                System.out.println("STATE ERROR!");
                e.printStackTrace();
            }
            System.out.println("STATE SUCCESSFUL!");
                break;
        default:
            break;
        }
    }

    private static boolean handleInputs(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: Invalid number of arguments!");
            return false;
        }

        peer_ap = args[0];
        
        operation = args[1].toUpperCase();
        switch (operation) {
        case "BACKUP":
            if (args.length != 4) {
                System.out.println(operation + " error: Invalid number of arguments!");
                return false;
            }
            if (!Utils.fileExists(args[2])) {
                System.out.println(operation + " error: File doesn't exist!");
                return false;
            }
            filePath = args[2];
            if (!Utils.isInteger(args[3])) {
                System.out.println(operation + " error: Replication Degree invalid!");
                return false;
            }
            replicationDeg = Integer.parseInt(args[3]);
            break;
        case "RESTORE":
            if (args.length != 3) {
                System.out.println(operation + " error: Invalid number of arguments!");
                return false;
            }
            if (!Utils.fileExists(args[2])) {
                System.out.println(operation + " error: File doesn't exist!");
                return false;
            }
            filePath = args[2];
            break;
        case "DELETE":
            if (args.length != 3) {
                System.out.println(operation + " error: Invalid number of arguments!");
                return false;
            }
            if (!Utils.fileExists(args[2])) {
                System.out.println(operation + " error: File doesn't exist!");
                return false;
            }
            filePath = args[2];
            break;
        case "RECLAIM":
            if (args.length != 3) {
                System.out.println(operation + " error: Invalid number of arguments!");
                return false;
            }
            if (!Utils.isFloat(args[2])) {
                System.out.println(operation + " error: Maximum amount of disk space invalid!");
                return false;
            }
            diskSpace = Float.parseFloat(args[2]);
            break;
        case "STATE":
            if (args.length != 2) {
                System.out.println(operation + " error: Invalid number of arguments!");
                return false;
            }
            break;
        default:
            System.out.println("Error: Invalid operation!");
            return false;
        }
        return true;
    }

    private static void usage() {
        System.out.println(
                "Usage:\n\tjava TestApp <peer_ap> <operation> <opnd_1> <opnd2>\n\t\t<peer_app>: Peer's access point;\n\t\t<operation>: Operation the peer of backup service must execute. It must be one of: BACKUP, RESTORE, DELETE, RECLAIM, STATE;\n\t\t<opnd_1>: Path Name of the file in case of operations BACKUP, RESTORE or DELETE or maximum amount of disk space (in KByte) in case of operation RECLAIM\n\t\t<opnd_2>: Integer that specifies the desired replication degree for operation BACKUP");
        return;
    }

    public static void connect() {
        try {
            pInterface = (PeerInterface) Naming.lookup("rmi://" + peer_ap);
        } catch (MalformedURLException | RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}