package client;

import utils.Utils;

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
                if (!Utils.fileExists(filePath)){
                    System.out.println(operation + " error: File doesn't exist");
                    return false;
                }
                break;
            case "RESTORE":

                break;
            case "DELETE":

                break;
            case "RECLAIM":

                break;
            case "STATE":

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