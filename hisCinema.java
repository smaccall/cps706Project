package socketprogramming;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class hisCinema
{
		private final static String fileToSend = "C:\\Users\\jalfante\\workspace\\cps706Project\\src\\socketprogramming\\toSend.txt";
		private final static int clientPort = 40080, localDDNSPort = 40080, hisServerPort = 40080, hisDDNSPort = 40080, herDDNSPort = 40080, herServerPort = 40080;
		//assume IP address from www.hiscinema.com & www.herCDN.com are well known
		public static void main(String [] args) throws IOException
		{			
			while (true) {
	            ServerSocket welcomeSocket = null;
	            Socket connectionSocket = null;
	            BufferedOutputStream outToClient = null;
	            System.out.println("STARTING hisCinema");
	            try {
	                welcomeSocket = new ServerSocket(hisServerPort);
	                connectionSocket = welcomeSocket.accept();
	                System.out.println("Connection from client");
	                
	                outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
	            } catch (IOException ex) {
	                // Do exception handling
	            }
	            
	            if (outToClient != null) {
	                File myFile = new File( fileToSend );
	                byte[] mybytearray = new byte[(int) myFile.length()];

	                FileInputStream fis = null;

	                try {
	                    fis = new FileInputStream(myFile);
	                } catch (FileNotFoundException ex) {
	                    // Do exception handling
	                	System.out.println(ex.getMessage());
	                }
	                BufferedInputStream bis = new BufferedInputStream(fis);

	                try {
	                    bis.read(mybytearray, 0, mybytearray.length);
	                    outToClient.write(mybytearray, 0, mybytearray.length);
	                    outToClient.flush();
	                    outToClient.close();
	                    connectionSocket.close();

	                    // File sent, exit the main method
	                    return;
	                } catch (IOException ex) {
	                    // Do exception handling
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
