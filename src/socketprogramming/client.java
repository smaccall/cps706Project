package socketprogramming;
import java.awt.Desktop;
import java.io.*;
import java.net.*;
import java.util.*;

public class client {
									//fix fileToRecv to be the destination of the file to be created
	private final static String fileToRecv = "C:\\Users\\smaccall\\workspace\\CPS706\\src\\socketprogramming\\videos.txt";
	private final static int clientPort = 40080, localDNSPort = 40080, hisServerPort = 40080, hisDNSPort = 40080, herDNSPort = 40080, herServerPort = 40080;
	private final static String clientIP="", localIP="", hisCinemaIP="", hisDNSIP="", herDNSIP="", herCDNIP="";
	public static void main(String[] args) throws Exception {
		String hostname = "www.hiscinema.com";
		System.out.println("Hostname: "+ hostname);
		DatagramSocket clientSocket = new DatagramSocket(clientPort);
		
		InetAddress IPAddress = InetAddress.getByName(localIP);
		
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];

		sendData = hostname.getBytes();
		//SENDS UDP PACKET TO LOCAL DNS
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, localDNSPort);
		clientSocket.send(sendPacket);
		//RECEIVES IP ADDRESS BACK FROM LOCAl DNS
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		String serverIP = parseByteData(receivePacket.getData());
		System.out.println("FROM LOCAL DNS:" + serverIP);
		System.out.println("DONE UPD STARTING TCP");
		
		//START TCP HERE
		Socket clientTCPSocket = new Socket(serverIP, hisServerPort);	//different port here because using same machine	
		InputStream inFromServer = clientTCPSocket.getInputStream();
		System.out.println("TCP SOCKET MADE");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int bytesRead;
		byte[] aByte = new byte[1];
		FileOutputStream fos = null;
		File file = new File(fileToRecv);	
        if (inFromServer != null) {
        	// receive the file from hisServer
        	System.out.println("File Transfer from: " + serverIP);
            BufferedOutputStream bos = null;
            try {
                fos = new FileOutputStream( file );
                bos = new BufferedOutputStream(fos);
                bytesRead = inFromServer.read(aByte, 0, aByte.length);

                do {
                        baos.write(aByte);
                        bytesRead = inFromServer.read(aByte);
                } while (bytesRead != -1);
                System.out.println("File has been read.");
                bos.write(baos.toByteArray());
                bos.flush();
                bos.close();
                clientTCPSocket.close();
            } catch (IOException ex) {
            	System.out.println(ex.getMessage());
            }
        }
        BufferedReader inFile = new BufferedReader(new FileReader(file));

        
        ArrayList<String> videoURLS = new ArrayList<String>(); 
        String inLine;
        while ((inLine = inFile.readLine()) != null) 
        {
            videoURLS.add(inLine);
        }
        inFile.close(); 
        file.delete();
        
        System.out.println(Arrays.toString(videoURLS.toArray()));
        
        System.out.println("Select index of video file (starting at 0)");
        Scanner in = new Scanner(System.in);         
        int videoIndex;
        do {
        	videoIndex = in.nextInt();
        } while(videoIndex < 0 || videoIndex > videoURLS.size()-1);
        
        String selected = videoURLS.get(videoIndex);
        
        sendData = new byte[1024];
		receiveData = new byte[1024];
		
		//send
		sendData = selected.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, localDNSPort);
		clientSocket.send(sendPacket);
		
		//receive
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		String serverIP2 = parseByteData(receivePacket.getData());
		System.out.println("FROM LOCAL DNS:" + serverIP2 + "     "+serverIP);
		clientSocket.close();

		//send request for file AND Pick place for file to be created
		final  String FILE_TO_RECEIVED = "C:\\Users\\smaccall\\Desktop\\videos.mp4";  
	    final  int FILE_SIZE = 100000000; //100MB max size
		bytesRead = 0;
	    int current = 0;
	    fos = null;
	    BufferedOutputStream bos = null;
	    Socket sock = null;
	    try {
	    	sock = new Socket(herCDNIP, herServerPort);
	    	System.out.println("Connecting to herCDN");
	    	
	    	for(int i=0;i<3;i++){
	    		//send the selected video
	    		DataOutputStream outToServer = new DataOutputStream(sock.getOutputStream());
	    		outToServer.write(videoIndex);
		    	// receive the file
		    	byte [] mybytearray  = new byte [FILE_SIZE];
		    	InputStream is = sock.getInputStream();
		    	fos = new FileOutputStream(FILE_TO_RECEIVED);
		    	bos = new BufferedOutputStream(fos);
		    	bytesRead = is.read(mybytearray,0,mybytearray.length);
		    	current = bytesRead;
	
		    	do {
		    		bytesRead =
		    				is.read(mybytearray, current, (mybytearray.length-current));
		    		if(bytesRead >= 0) current += bytesRead;
		    	} while(bytesRead > -1);
	
		    	bos.write(mybytearray, 0 , current);
		    	bos.flush();
		    	System.out.println("File " + FILE_TO_RECEIVED
		    			+ " downloaded (" + current + " bytes read)");
		    	Desktop.getDesktop().open(new File(FILE_TO_RECEIVED));
		    	
		    	System.out.println("Select Next Video Index to download. type -1 to quit.");		    	
		    	System.out.println(Arrays.toString(videoURLS.toArray()));
		    	in = new Scanner(System.in); 
		    	do { 
		    		videoIndex = in.nextInt(); 
		    	} while(videoIndex > videoURLS.size() -1);
		        if(videoIndex < 0) break;
		        //repeat
		        sock = new Socket(serverIP, herServerPort);
		    	System.out.println("Connecting...");
		    	
		        bytesRead = 0;
	    	    current = 0;
	    	    fos = null;
	    	    bos = null;
	    	    mybytearray = null;
	    	    is = null;
	    	}
	    }
	    finally {
	    	if (fos != null) fos.close();
	    	if (bos != null) bos.close();
	    	if (sock != null) sock.close();
	    	in.close();
	    }
	    
	    //done

		
	}
	
	public static String parseByteData(byte[] arr)
	{
		int i;
		for (i = 0; i < arr.length && arr[i] != 0; i++) { }
		String str = new String(arr, 0, i);
		return str;
	}

}