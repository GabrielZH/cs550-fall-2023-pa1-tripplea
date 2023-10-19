import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class GetParameter {

    public static String byName(String name)  {
        String retString = "get parameters error";
        String filePath = "config.properties";
        HashMap<String, String> hashMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("="); 
                
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    
                    hashMap.put(key, value);
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get the property value and print it out
        switch (name) {
        case "cisId":
            retString = hashMap.get("CentralIndexingServerId");
            break;
        case "peerIndex":
            retString = hashMap.get("PeerIndex");
            break;
        case "latencyExpPeerIndex":
            retString = hashMap.get("LatencyExpPeerIndex");
            break;
        case "throughputExpPeerIndices":
            retString = hashMap.get("ThroughputExpPeerIndices");
            break;
        case "numPeers":
            retString = hashMap.get("NumberOfPeers");
            break;
        case "numSmallFiles":
            retString = hashMap.get("NumberOfSmallFiles");
            break;
        case "numLargeFiles":
            retString = hashMap.get("NumberOfLargeFiles");
            break;
        case "numRequests":
            retString = hashMap.get("NumberOfRequests");
            break;
            case "peer1Id":
            retString = hashMap.get("Peer1ID");
            break;
        case "peer1SharedDirectory":
            retString = hashMap.get("Peer1SharedDirectory");
            break;
        case "peer1Neighbors":
            retString = hashMap.get("Peer1Neighbors");
            break;
        case "peer2Id":
            retString = hashMap.get("Peer2ID");
            break;
        case "peer2SharedDirectory":
            retString = hashMap.get("Peer2SharedDirectory");
            break;
        case "peer2Neighbors":
            retString = hashMap.get("Peer2Neighbors");
            break;
        case "peer3Id":
            retString = hashMap.get("Peer3ID");
            break;
        case "peer3SharedDirectory":
            retString = hashMap.get("Peer3SharedDirectory");
            break;
        case "peer3Neighbors":
            retString = hashMap.get("Peer3Neighbors");
            break;
        case "peer4Id":
            retString = hashMap.get("Peer4ID");
            break;
        case "peer4SharedDirectory":
            retString = hashMap.get("Peer4SharedDirectory");
            break;
        case "peer4Neighbors":
            retString = hashMap.get("Peer4Neighbors");
            break;
        case "peer5Id":
            retString = hashMap.get("Peer5ID");
            break;
        case "peer5SharedDirectory":
            retString = hashMap.get("Peer5SharedDirectory");
            break;
        case "peer5Neighbors":
            retString = hashMap.get("Peer5Neighbors");
            break;
        case "peer6Id":
            retString = hashMap.get("Peer6ID");
            break;
        case "peer6SharedDirectory":
            retString = hashMap.get("Peer6SharedDirectory");
            break;
        case "peer6Neighbors":
            retString = hashMap.get("Peer6Neighbors");
            break;
        case "peer7Id":
            retString = hashMap.get("Peer7ID");
            break;
        case "peer7SharedDirectory":
            retString = hashMap.get("Peer7SharedDirectory");
            break;
        case "peer7Neighbors":
            retString = hashMap.get("Peer7Neighbors");
            break;
        case "peer8Id":
            retString = hashMap.get("Peer8ID");
            break;
        case "peer8SharedDirectory":
            retString = hashMap.get("Peer8SharedDirectory");
            break;
        case "peer8Neighbors":
            retString = hashMap.get("Peer8Neighbors");
            break;
        case "peer9Id":
            retString = hashMap.get("Peer9ID");
            break;
        case "peer9SharedDirectory":
            retString = hashMap.get("Peer9SharedDirectory");
            break;
        case "peer9Neighbors":
            retString = hashMap.get("Peer9Neighbors");
            break;
        case "peer10Id":
            retString = hashMap.get("Peer10ID");
            break;
        case "peer10SharedDirectory":
            retString = hashMap.get("Peer10SharedDirectory");
            break;
        case "peer10Neighbors":
            retString = hashMap.get("Peer10Neighbors");
            break;
        case "peer11Id":
            retString = hashMap.get("Peer11ID");
            break;
        case "peer11SharedDirectory":
            retString = hashMap.get("Peer11SharedDirectory");
            break;
        case "peer11Neighbors":
            retString = hashMap.get("Peer11Neighbors");
            break;
        case "peer12Id":
            retString = hashMap.get("Peer12ID");
            break;
        case "peer12SharedDirectory":
            retString = hashMap.get("Peer12SharedDirectory");
            break;
        case "peer12Neighbors":
            retString = hashMap.get("Peer12Neighbors");
            break;
        case "peer13Id":
            retString = hashMap.get("Peer13ID");
            break;
        case "peer13SharedDirectory":
            retString = hashMap.get("Peer13SharedDirectory");
            break;
        case "peer13Neighbors":
            retString = hashMap.get("Peer13Neighbors");
            break;
        case "peer14Id":
            retString = hashMap.get("Peer14ID");
            break;
        case "peer14SharedDirectory":
            retString = hashMap.get("Peer14SharedDirectory");
            break;
        case "peer14Neighbors":
            retString = hashMap.get("Peer14Neighbors");
            break;
        case "peer15Id":
            retString = hashMap.get("Peer15ID");
            break;
        case "peer15SharedDirectory":
            retString = hashMap.get("Peer15SharedDirectory");
            break;
        case "peer15Neighbors":
            retString = hashMap.get("Peer15Neighbors");
            break;
        case "peer16Id":
            retString = hashMap.get("Peer16ID");
            break;
        case "peer16SharedDirectory":
            retString = hashMap.get("Peer16SharedDirectory");
            break;
        case "peer16Neighbors":
            retString = hashMap.get("Peer16Neighbors");
            break;
        case "peerNode":
            retString = hashMap.get("Peer_Node");
            break;
        case "fileNode":
            retString = hashMap.get("File_Node");
            break;
        }

        return retString;

    }
}