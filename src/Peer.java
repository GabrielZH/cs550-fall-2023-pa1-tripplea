import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Random;


class PeerClient {
	private String index;
    private String id;
	private String ipAddr;
	private int port;
	private String cisId;
	private String cisIpAddr;
	private int cisPort;
	private HashMap<String, Integer> id2Index;
    private Socket cisSocket;
    private String sharedDirectory;
	private Random random;

    PeerClient(){
        this.index = GetParameter.byName("peerIndex");
        this.id = GetParameter.byName("peer" + index + "Id");
		String[] ipAddrAndPort = this.id.split(":");
		this.ipAddr = ipAddrAndPort[0].trim();
		this.port = Integer.parseInt(ipAddrAndPort[1].trim());
        this.sharedDirectory = GetParameter.byName("peer" + index + "SharedDirectory");
		if (!this.sharedDirectory.endsWith("/")) {
			this.sharedDirectory += "/";
		}

		this.cisId = GetParameter.byName("cisId");
		String[] cisIpAddrAndPort = this.cisId.split(":");
        this.cisIpAddr = cisIpAddrAndPort[0].trim();
        this.cisPort = Integer.parseInt(cisIpAddrAndPort[1].trim());

		this.random = new Random();

		try {
        	this.cisSocket = new Socket(this.cisIpAddr, this.cisPort);
		}
		catch(IOException e){
			e.printStackTrace();
		}
    }

    public void runBatch(String experiment) {
		String peerIndex = GetParameter.byName("peerIndex");
		boolean isSelectedPeer;
		int numRequests = Integer.parseInt(
			GetParameter.byName("numRequests")
		);
		String[] throughputExpPeerIndices = GetParameter.byName(
			"throughputExpPeerIndices").split(",");
		int count = 0;

		// Define the file path
		String filePath = "benchmark.txt";

		try {
			Scanner readCIS = new Scanner(cisSocket.getInputStream());
			PrintStream writeCIS = new PrintStream(cisSocket.getOutputStream());

			//Automatically registering files as the peer is connected
			File peerDirectory = new File(this.sharedDirectory);
			File[] sharedFiles = peerDirectory.listFiles();

			// Create a BufferedWriter and a FileWriter to write to the file
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			PrintWriter pwriter = new PrintWriter(writer);
				
			for(int i = 0; i < sharedFiles.length; i++){
				writeCIS.println("1");
				writeCIS.println(id + " " + sharedFiles[i].getName());
				String retResult = readCIS.nextLine();

				count++;
				if(count % 500 == 0){
					System.out.println("Registering " + Integer.toString(count) + " records...");
				}
			}

            switch (experiment) {
                case "query_latency":
				    String latencyExpPeerIndex = GetParameter.byName(
						"latencyExpPeerIndex");
				    isSelectedPeer = peerIndex.equals(latencyExpPeerIndex);
				    queryLatency(isSelectedPeer, numRequests, readCIS, writeCIS, pwriter);
				    break;
				case "query_throughput":
				    isSelectedPeer = false;
					if (Arrays.asList(throughputExpPeerIndices).contains(peerIndex)) {
						isSelectedPeer = true;
					}
			        queryThroughput(isSelectedPeer, numRequests, readCIS, writeCIS, pwriter);
				    break;
				case "transfer_throughput_small":
				    isSelectedPeer = false;
			        if (Arrays.asList(throughputExpPeerIndices).contains(peerIndex)) {
					    isSelectedPeer = true;
				    }
			        transferThroughput(isSelectedPeer, numRequests, "small", readCIS, writeCIS, pwriter);
				    break;
				case "transfer_throughput_large":
				    isSelectedPeer = false;
			        if (Arrays.asList(throughputExpPeerIndices).contains(peerIndex)) {
					    isSelectedPeer = true;
				    }
			        transferThroughput(isSelectedPeer, numRequests, "large", readCIS, writeCIS, pwriter);
				    break;
				default:
				    pwriter.close();
					break;
			}
			// if(level.equalsIgnoreCase("")){
			// 	count = 0;
			// 	for(int round=0; round<1; round++){
			// 		pwriter.println(Integer.toString(round) + " round search time:");
			// 		startTime = System.currentTimeMillis();
			// 		for(int i=0; i<10000; i++){
			// 			fileName = "1KB_" + fileNode + "_" + Integer.toString(i+1) + ".txt" ;
			// 			writeCIS.println("6");
			// 			writeCIS.println(fileName);
			// 			retResult = readCIS.nextLine();

			// 			count++;
			// 			if(count%500 == 0){
			// 				System.out.println("Now is search " + Integer.toString(count) + " record");
			// 			}
			// 		}
			// 		endTime = System.currentTimeMillis();
			// 		content="10K requests for peer" + fileNode + " :" +(endTime-startTime);
			// 		pwriter.println(content);
			// 		pwriter.println("\n");
			// 	}
			// 	// Close the writer when done
			// 	pwriter.close();

			// }else if(level.equalsIgnoreCase("strong")){

			// 	for(int round=0; round<1; round++){
			// 		pwriter.println(Integer.toString(round) + " round search and transfer time:");
			// 		startTime = System.currentTimeMillis();
			// 		for(int i=0; i<10000; i++){
			// 			fileName = "1KB_" + fileNode + "_" + Integer.toString(i+1) + ".txt" ;
			// 			writeCIS.println("6");
			// 			writeCIS.println(fileName);
			// 			retResult = readCIS.nextLine();
			// 			String ownerId = retResult.split(" ")[0];
			// 			obtain(fileName, ownerId, this.sharedDirectory);
			// 		}
			// 		endTime = System.currentTimeMillis();
			// 		content="10K small files (1KB) for peer" + fileNode +" :" +(endTime-startTime);
			// 		pwriter.println(content);

			// 		startTime = System.currentTimeMillis();
			// 		for(int i=0; i<1000; i++){
			// 			fileName = "1MB_" + fileNode + "_" + Integer.toString(i+1) + ".txt" ;
			// 			writeCIS.println("6");
			// 			writeCIS.println(fileName);
			// 			retResult = readCIS.nextLine();
			// 			String ownerId = retResult.split(" ")[0];
			// 			obtain(fileName, ownerId, this.sharedDirectory);
			// 		}
			// 		endTime = System.currentTimeMillis();
			// 		content="1K medium files (1MB) for peer" + fileNode +" :" +(endTime-startTime);
			// 		pwriter.println(content);

			// 		startTime = System.currentTimeMillis();
			// 		for(int i=0; i<8; i++){
			// 			fileName = "1GB_" + fileNode + "_" + Integer.toString(i+1) + ".bin" ;
			// 			writeCIS.println("6");
			// 			writeCIS.println(fileName);
			// 			retResult = readCIS.nextLine();
			// 			String ownerId = retResult.split(" ")[0];
			// 			obtain(fileName, ownerId, this.sharedDirectory);
			// 		}
			// 		endTime = System.currentTimeMillis();
			// 		content="8 large files (1GB) for peer" + fileNode +" :" +(endTime-startTime);
			// 		pwriter.println(content);
			// 		pwriter.println("\n");
			// 	}
			// }
		// Close the writer when done
		pwriter.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}

		System.exit(0);
    }

	public void queryLatency(boolean isSelectedPeer, int numRequests, Scanner readCIS, PrintStream writeCIS, PrintWriter pwriter) {
		
		// pwriter.println(Integer.toString(round) + " round search time:");

		double startTime = System.currentTimeMillis();
		for(int i = 0; i < numRequests; i++){
			String randFileName = getRandomFile("small");
			writeCIS.println("6");
			writeCIS.println(randFileName);
			// String retResult = readCIS.nextLine();
		}
		double endTime = System.currentTimeMillis();
		String content="Query latency: " + (endTime - startTime);
		pwriter.println(content);
		pwriter.println("\n");
		
		// Close the writer when done
		pwriter.close();
	}

	public void queryThroughput(boolean isSelectedPeer, int numRequests, Scanner readCIS, PrintStream writeCIS, PrintWriter pwriter) {
		// pwriter.println(Integer.toString(round) + " round search and transfer time:");
		double startTime = System.currentTimeMillis();
		for(int i=0; i < numRequests; i++) {
			String randFileName = getRandomFile("small");
			writeCIS.println("6");
			writeCIS.println(randFileName);
			// String retResult = readCIS.nextLine();
		}
		double endTime = System.currentTimeMillis();
		String content="Query throughput: " + (endTime - startTime);
		pwriter.println(content);
		pwriter.close();
	}

	public void transferThroughput(boolean isSelectedPeer, int numRequests, String fileType, Scanner readCIS, PrintStream writeCIS, PrintWriter pwriter) {
        double startTime = System.currentTimeMillis();
		for(int i=0; i < numRequests; i++) {
			String randFileName = getRandomFile("small");
			writeCIS.println("6");
			writeCIS.println(randFileName);
			String retResult = readCIS.nextLine();
			String ownerId = retResult.split(" ")[0];
			obtain(randFileName, ownerId, this.sharedDirectory);
		}
		double endTime = System.currentTimeMillis();
		String content="Query throughput: " + (endTime - startTime);
		pwriter.println(content);
		pwriter.close();
	}

/*
	 * Obtain function -  invoked by a peer to download a file from another peer
	 */
	public static void obtain(String fileName, String ownerId, String downPath) {
		
		//Check if the folder exists
		//Create if it doesn't exist
		File directory = new File(downPath);
		if (!directory.exists()) {
			System.out.println("Creating a new folder named: ");
			directory.mkdir();
			System.out.println("The file will be found at: " + downPath);
		}

		//Make a connection with server to get file from
		String[] peerIpAddrAndPort = ownerId.split(":");
		String peerIpAddr = peerIpAddrAndPort[0].trim();
		int peerPort = Integer.parseInt(peerIpAddrAndPort[1].trim());
		
		try {
			Socket peerSocket = new Socket(peerIpAddr, peerPort);
			System.out.println("Downloading the file, please wait ...");

			//Input & Output for socket Communication
			Scanner readPeer = new Scanner(peerSocket.getInputStream());
			PrintStream writePeer = new PrintStream(peerSocket.getOutputStream());
	
			writePeer.println(fileName);
			writePeer.println(ownerId);	
	
			long buffSize = readPeer.nextLong();
			int newBuffSize = (int) buffSize;
	
			byte[] byteStream = new byte[newBuffSize];
			String filePath = downPath + "/" + fileName;

			//Write the file requested by the peer
			FileOutputStream writeFileStream = new FileOutputStream(filePath);

			writeFileStream.write(byteStream);
			writeFileStream.close();

			System.out.println("Downloaded Successfully");

			peerSocket.close();

		} 
		catch (FileNotFoundException ex){
			System.out.println("FileNotFoundException : " + ex);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	private String getRandomFile(String fileType) {
		int numPeers = Integer.parseInt(GetParameter.byName("numPeers"));
		int numFiles;
		int randPeerIndex, randFileIndex;

		if (fileType.equalsIgnoreCase("small")) {
            numFiles = Integer.parseInt(GetParameter.byName("numSmallFiles"));
			randPeerIndex = 1 + random.nextInt(numPeers);
			randFileIndex = 1 + random.nextInt(numFiles);
			return String.format("10KB_%d_%d.txt", randPeerIndex, randFileIndex);
		} else if (fileType.equalsIgnoreCase("large")) {
            numFiles = Integer.parseInt(GetParameter.byName("numLargeFiles"));
			randPeerIndex = 1 + random.nextInt(numPeers);
			randFileIndex = 1 + random.nextInt(numFiles);
			return String.format("100MB_%d_%d.bin", randPeerIndex, randFileIndex);
		} else {
			throw new IllegalArgumentException();
		}        
	}
}

class PeerServer implements Runnable{
	String fileDownloadPath;
    String id;
    String ipAddr;
	int port;
	HashMap<String, Integer> id2Index;
	Socket peerSocket;
	Scanner readClient;
	PrintStream writeClient;

	public PeerServer() {
		String index = GetParameter.byName("peerIndex");
		this.id = GetParameter.byName("peer" + index + "Id");
		String[] ipAddrAndPort = this.id.split(":");
		this.port = Integer.parseInt(ipAddrAndPort[1].trim());
		this.id2Index = new HashMap<>();
        for (int i = 1; i < 17; i++) {
            this.id2Index.put(GetParameter.byName("peer" + i + "Id"), i);
		}
	}

	public void run(){
		try{
			ServerSocket downloadSocket = new ServerSocket(port);
			System.out.println("Starting client socket now");

			while(true) {
				//accept the connection from the socket
				peerSocket = downloadSocket.accept();
				System.out.println("Client connected for file sharing ...");

				writeClient = new PrintStream(peerSocket.getOutputStream());
				readClient = new Scanner(peerSocket.getInputStream());

				//get the fileName from ClientAskingForFile
				String fileName = readClient.nextLine();
				System.out.println("Requested file is: " + fileName);
				String ownerId = readClient.nextLine();
                int ownerIndex = id2Index.get(ownerId);
				fileDownloadPath = GetParameter.byName("peer" + ownerIndex + "SharedDirectory");
				
				File targetFile = new File(fileDownloadPath + "/" + fileName);
				
				FileInputStream fin = new FileInputStream(targetFile);
				BufferedInputStream buffReader = new BufferedInputStream(fin);
				
				//check if the file exists, for it to be downloaded
				if (!targetFile.exists()) {
					System.out.println("File does not exists");
					buffReader.close();
					return;
				}

				//get the file size, as the buffer needs to be allocated an initial size
				int size = (int) targetFile.length();	//convert from long to int
				byte[] buffContent = new byte[size];
				
				//send file size
				writeClient.println(size);
				
				//allocate a buffer to store contents of file
				int startRead = 0;	//how much is read in total
				int numOfRead = 0;	//how much is read in each read() call

				//read into buffContent, from StartRead until end of file
				while (startRead < buffContent.length && (numOfRead = buffReader.read(buffContent, startRead, buffContent.length - startRead)) >= 0) 
				{
					startRead = startRead + numOfRead;
				}
				//Validate all the bytes have been read
				if (startRead < buffContent.length){
					System.out.println("File Read Incompletely" + targetFile.getName());
				}
				writeClient.println(buffContent);
				buffReader.close();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}

public class Peer {
    private static String experiment = "";

    public static void main(String[] args) {
       
		if(args.length == 1)
        	experiment = args[0];

        PeerClient client = new PeerClient();
        PeerServer server = new PeerServer();

			
		// thread for file download amongst peers
		Thread workThread = new Thread(server);
		workThread.start();
        client.runBatch(experiment);
    }
}
