package socketprogramming;

import java.io.IOException;
import java.net.*;

import socketprogramming.DDNS.DDNSTable;
/*

 */
public class herDNS {
	private static DDNSTable herDDNSTable;
	private final static int herDDNSPort = 40080;
	//assume IP address from www.hiscinema.com & www.herCDN.com are well known
	public static void main(String [] args) throws IOException{
		herDDNSTable = new DDNSTable();			
						
		herDDNSTable.addEntry("herCDN.com", "141.117.232.92", "A"); //modify with ip of her server
		
		DatagramSocket serverSocket = new DatagramSocket(herDDNSPort);//new port number			
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		while(true){
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);			
			
			String hostname = parseByteData(receivePacket.getData());				
			System.out.println(hostname);
			String IPRecord = herDDNSTable.findValue(hostname);				
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			
			sendData = IPRecord.getBytes();
			DatagramPacket sendPacket =	new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
			
		}
		//serverSocket.close();
	}
	public static String parseByteData(byte[] arr)
	{
		int i;
		for (i = 0; i < arr.length && arr[i] != 0; i++) { }
		String str = new String(arr, 0, i);
		return str;
	}	

}
