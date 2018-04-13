package socketprogramming;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import socketprogramming.DDNS.DDNSTable;

public class hisDNS {			
		private static DDNSTable hisDDNSTable;
		private final static int clientPort = 40080, localDDNSPort = 40080, hisServerPort = 40080, hisDDNSPort = 40080, herDDNSPort = 40080, herServerPort = 40080;
		//assume IP address from www.hiscinema.com & www.herCDN.com are well known
		public static void main(String [] args) throws IOException{
			hisDDNSTable = new DDNSTable();			
			
			hisDDNSTable.addEntry("video1.hiscinema.com","herCDN.com","V");				
			hisDDNSTable.addEntry("video2.hiscinema.com","herCDN.com","V");	
			hisDDNSTable.addEntry("video3.hiscinema.com","herCDN.com","V");	
			hisDDNSTable.addEntry("video4.hiscinema.com","herCDN.com","V");	
			hisDDNSTable.addEntry("video5.hiscinema.com","herCDN.com","V");	

			hisDDNSTable.addEntry("herCDN.com", "141.117.232.94", "A");		//modify with ip of her ddns
			
			System.out.println("hisDNS STARTED");
			DatagramSocket serverSocket = new DatagramSocket(hisDDNSPort);//new port number			
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			while(true){
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);			
				System.out.println("RECEIVED SHIT: ");
				String hostname = parseByteData(receivePacket.getData());				
				System.out.println(hostname);
				String IPRecord = searchTable(hostname);	
				int port = receivePacket.getPort();
				InetAddress IPAddress = receivePacket.getAddress();
				System.out.println("SENDING SHIT.....");
				sendData = IPRecord.getBytes();
				DatagramPacket sendPacket =	new DatagramPacket(sendData, sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);
				System.out.println("SHIT SENT!");
				
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
		public static String searchTable(String hostname){
			String newhost = hostname;
			String type = hisDDNSTable.findType(hostname);
			while(!type.equals("A")){
				newhost = hisDDNSTable.findValue(newhost);
				type = hisDDNSTable.findType(newhost);				
			}
			return hisDDNSTable.findValue(newhost);			
		}
		
		

}
