package client;

import utils.Utils;

import java.lang.Integer;
import java.lang.Float;

/**
 * TestApp
 */
public class TestApp {

    private static int peer_ap;
    private static String operation;
    private static String filePath;
    private static float diskSpace;
    private static int replicationDeg;

    public static void main(String[] args) {
        if (!handleInputs(args)){
            usage();
            return;
        }    
    }

    private static boolean handleInputs(String[] args) {
        if (args.length < 2){
            System.out.println("Error: Invalid number of arguments!");
            return false;
        }
        
        if (Utils.isInteger(args[0])){
            peer_ap = Integer.parseInt(args[0]);
        }
        else{
            System.out.println("Error: Invalid peer's acess point!");
            return false;
        }

        operation = args[1].toUpperCase();
        switch (operation) {
            case "BACKUP":
                if (args.length != 4){
                    System.out.println(operation + " error: Invalid number of arguments!");
                    return false;
                }
                if (!Utils.fileExists(args[2])){
                    System.out.println(operation + " error: File doesn't exist!");
                    return false;
                }
                filePath = args[2];
                if (!Utils.isInteger(args[3])){
                    System.out.println(operation + " error: Replication Degree invalid!");
                    return false;
                }
                replicationDeg = Integer.parseInt(args[3]);
                break;
            case "RESTORE":
                if (args.length != 3){
                    System.out.println(operation + " error: Invalid number of arguments!");
                    return false;
                }
                if (!Utils.fileExists(args[2])){
                    System.out.println(operation + " error: File doesn't exist!");
                    return false;
                }
                filePath = args[2];
                break;
            case "DELETE":
                if (args.length != 3){
                    System.out.println(operation + " error: Invalid number of arguments!");
                    return false;
                }
                if (!Utils.fileExists(args[2])){
                    System.out.println(operation + " error: File doesn't exist!");
                    return false;
                }
                filePath = args[2];
                break;
            case "RECLAIM":
                if (args.length != 3){
                    System.out.println(operation + " error: Invalid number of arguments!");
                    return false;
                }
                if (!Utils.isFloat(args[2])){
                    System.out.println(operation + " error: Maximum amount of disk space invalid!");
                    return false;
                }
                diskSpace = Float.parseFloat(args[2]);
                break;
            case "STATE":
                if (args.length != 2){
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

    private static void usage(){
        System.out.println("Usage:\n\tjava TestApp <peer_ap> <operation> <opnd_1> <opnd2>\n\t\t<peer_app>: Peer's acess point;\n\t\t<operation>: Operation the peer of backup service must execute. It must be one of: BACKUP, RESTORE, DELETE, RECLAIM, STATE;\n\t\t<opnd_1>: Path Name of the file in case of operations BACKUP, RESTORE or DELETE or maximum amount of disk space (in KByte) in case of operation RECLAIM\n\t\t<opnd_2>: Integer that specifies the desired replication degree for operation BACKUP");
        return;
    }
}