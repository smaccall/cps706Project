package socketprogramming;
import java.io.*;
import java.net.*;

import socketprogramming.DDNS.DDNSTable;

public class localDNS {
	private final static int clientPort = 40080, localDDNSPort = 40080, hisServerPort = 40080, hisDDNSPort = 40080, herDDNSPort = 40080, herServerPort = 40080;
	private static DDNSTable localDDNSTable;
	public static void main(String[] args) throws IOException {
		
		localDDNSTable = new DDNSTable();
		//not sure about this
		localDDNSTable.addEntry("www.hiscinema.com","hiscinema.com","CN");
		
		localDDNSTable.addEntry("hiscinema.com", "NShiscinema.com", "NS");
		localDDNSTable.addEntry("NShiscinema.com", "141.117.232.93", "A"); //modify with ip of his ddns
		
		DatagramSocket serverSocket = null;
		try {
			serverSocket = new DatagramSocket(localDDNSPort);
		} catch (SocketException e1) {
			System.out.println("WUNNN");
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
				System.out.println("TUUUUU");
				e1.printStackTrace();
			}
			String recData = parseByteData(receivePacket.getData());				
			System.out.println("received: "+recData);

			String IPRecord;
			InetAddress clientIPAddress = null;	

			if(recData.equals("www.hiscinema.com")){
				System.out.println("IT ISSS");
				IPRecord = searchTable(recData);
				clientIPAddress = receivePacket.getAddress();
				//clientPort = receivePacket.getPort();
				System.out.println(clientIPAddress);
				sendData = IPRecord.getBytes();
				
				DatagramPacket sendPacket =	new DatagramPacket(sendData, sendData.length, clientIPAddress, clientPort);
				try {
					serverSocket.send(sendPacket);
					System.out.println(sendPacket);
					System.out.println("SENT BACK TO CLIENT");
				} catch (IOException e) {
					System.out.println("THURIIII");
					e.printStackTrace();
				}
			}
			//contact hisDDNS			
			else if(recData.contains("video"))				
			{				
				//look for hiscinemaDNS server address
				IPRecord = searchTable("hiscinema.com");
				InetAddress hisDDNSIPAddress = InetAddress.getByName(IPRecord);				
				
				//send hisDDNS the DDDNSEntry to search for
				sendData = recData.getBytes();
				DatagramPacket sendPacket =	new DatagramPacket(sendData, sendData.length, hisDDNSIPAddress, hisDDNSPort);
				serverSocket.send(sendPacket);				
			}			
			else 
			{				
				InetAddress herIP = InetAddress.getByName(recData);			
				sendData = "herCDN.com".getBytes();
				//System.out.println();
				DatagramPacket sendPacket =	new DatagramPacket(sendData, sendData.length, herIP, herDDNSPort);
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
			//serverSocket.close();
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
