package socketprogramming;
import java.io.*;
import java.net.*;

import socketprogramming.DDNS.DDNSTable;

public class localDNS {
	private final static int clientPort = 40080, localDNSPort = 40080, hisServerPort = 40080, hisDNSPort = 40080, herDNSPort = 40080, herServerPort = 40080;
	private static DDNSTable localDDNSTable;
	private final static String clientIP="", localIP="", hisCinemaIP="", hisDNSIP="", herDNSIP="", herCDNIP="";
	public static void main(String[] args) throws IOException {
		
		localDDNSTable = new DDNSTable();
		
		localDDNSTable.addEntry("www.hiscinema.com","hiscinema.com","CN");
		
		localDDNSTable.addEntry("hiscinema.com", "NShiscinema.com", "NS");
		
						//IP of hisDNS
		localDDNSTable.addEntry("NShiscinema.com", hisDNSIP, "A"); 
		
		DatagramSocket serverSocket = null;
		try {
			serverSocket = new DatagramSocket(localDNSPort);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		while(true) {
			DatagramPacket receivePacket =
					new DatagramPacket(receiveData, receiveData.length);
			try {
				serverSocket.receive(receivePacket);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String recData = parseByteData(receivePacket.getData());				
			System.out.println("received: "+recData);

			String IPRecord;
			InetAddress clientIPAddress = null;	

			if(recData.equals("www.hiscinema.com")){
				IPRecord = searchTable(recData);
				clientIPAddress = receivePacket.getAddress();
				System.out.println(clientIPAddress);
				sendData = IPRecord.getBytes();
				
				DatagramPacket sendPacket =	new DatagramPacket(sendData, sendData.length, clientIPAddress, clientPort);
				try {
					serverSocket.send(sendPacket);
					System.out.println(sendPacket);
					System.out.println("SENT BACK TO CLIENT");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//contact hisDNS			
			else if(recData.contains("video"))				
			{				
				//search for the serer address of hiscinemaDNS
				IPRecord = searchTable("hiscinema.com");
				InetAddress hisDNSIPAddress = InetAddress.getByName(IPRecord);				
				
				//send hisDNS which DDNSentry to look for to 
				sendData = recData.getBytes();
				DatagramPacket sendPacket =	new DatagramPacket(sendData, sendData.length, hisDNSIPAddress, hisDNSPort);
				serverSocket.send(sendPacket);				
			}			
			else 
			{				
				InetAddress herIP = InetAddress.getByName(recData);			
				sendData = "herCDN.com".getBytes();
				DatagramPacket sendPacket =	new DatagramPacket(sendData, sendData.length, herIP, herDNSPort);
				serverSocket.send(sendPacket);	
				
				receiveData = new byte[1024];
				sendData = new byte[1024];	
				DatagramPacket receivePacket2 = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket2);
				String recvData2 = parseByteData(receivePacket2.getData());
				sendData = recvData2.getBytes();
				DatagramPacket sendPacket2 =	new DatagramPacket(sendData, sendData.length, herIP, clientPort);
				serverSocket.send(sendPacket2);	
			}

			String sentence = new String(receivePacket.getData());
			InetAddress IPAddress = receivePacket.getAddress();

			int port = receivePacket.getPort();
			System.out.println(port);
			String capitalizedSentence = sentence.toUpperCase();

			sendData = capitalizedSentence.getBytes();
			
			DatagramPacket sendPacket =
					new DatagramPacket(sendData, sendData.length, IPAddress, port);
			try {
				serverSocket.send(sendPacket);
			} catch (IOException e) {
				System.out.println("END!!!!");
				e.printStackTrace();
			}
		}
	}
	public static String parseByteData(byte[] arr)
	{
		int i;
		for (i = 0; i < arr.length && arr[i] != 0; i++) { }
		String str = new String(arr, 0, i);
		return str;
	}
	public static String searchTable(String hostname){
		String newhost = hostname;
		String type = localDDNSTable.findType(hostname);
		while(!type.equals("A")){
			newhost = localDDNSTable.findValue(newhost);
			type = localDDNSTable.findType(newhost);				
		}
		return localDDNSTable.findValue(newhost);			
	}

}