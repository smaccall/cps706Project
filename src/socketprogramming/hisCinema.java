package socketprogramming;

import java.io.*;
import java.net.*;

public class hisCinema
{
						//Create correct path to toSend.txt file
		private final static String fileToSend = "C:\\Users\\jalfante\\workspace\\cps706Project\\src\\socketprogramming\\toSend.txt";
		private final static int clientPort = 40080, localDNSPort = 40080, hisServerPort = 40080, hisDNSPort = 40080, herDNSPort = 40080, herServerPort = 40080;
		private final static String clientIP="", localIP="", hisCinemaIP="", hisDNSIP="", herDNSIP="", herCDNIP="";
		//assume IP address from www.hiscinema.com & www.herCDN.com are well known
		public static void main(String [] args) throws IOException
		{			
			while (true) {
	            ServerSocket welcomeSocket = null;
	            Socket connectionSocket = null;
	            BufferedOutputStream outToClient = null;
	            System.out.println("Starting hisCinema");
	            try {
	                welcomeSocket = new ServerSocket(hisServerPort);
	                connectionSocket = welcomeSocket.accept();
	                System.out.println("Connection from client has been made");
	                
	                outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
	            } catch (IOException ex) {
	                System.out.print(ex.getMessage());
	            }
	            
	            if (outToClient != null) {
	                File myFile = new File( fileToSend );
	                byte[] mybytearray = new byte[(int) myFile.length()];

	                FileInputStream fis = null;

	                try {
	                    fis = new FileInputStream(myFile);
	                } catch (FileNotFoundException ex) {
	                	System.out.println(ex.getMessage());
	                }
	                BufferedInputStream bis = new BufferedInputStream(fis);

	                try {
	                    bis.read(mybytearray, 0, mybytearray.length);
	                    outToClient.write(mybytearray, 0, mybytearray.length);
	                    outToClient.flush();
	                    outToClient.close();
	                    connectionSocket.close();

	                    return;
	                } catch (IOException ex) {
	                	System.out.println(ex.getMessage());
	                }
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
		
		
		

}