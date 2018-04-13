package socketprogramming;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class herCDN
{
		private final static String[] files= {"C:\\Users\\smaccall\\Desktop\\video1.mp4",
			"C:\\Users\\smaccall\\Desktop\\video2.mp4",
			"C:\\Users\\smaccall\\Desktop\\video3.mp4",
			"C:\\Users\\smaccall\\Desktop\\video4.mp4"};
		
		private final static int herServerPort = 40080;
		//assume IP address from www.hiscinema.com & www.herCDN.com are well known
		public static void main(String [] args) throws IOException
		{			
			FileInputStream fis = null;
		    BufferedInputStream bis = null;
		    OutputStream os = null;
		    ServerSocket servsock = null;
		    Socket sock = null;
		    try {
		      servsock = new ServerSocket(herServerPort);
		      while (true) {
		        System.out.println("Waiting...");
		        try {
		          sock = servsock.accept();
		          System.out.println("Accepted connection : " + sock);
		          // receive selection
		          BufferedReader inFromClient = new BufferedReader(new InputStreamReader(sock.getInputStream()));		          
		          int index = inFromClient.read();
		          // send file
		          System.out.println("File index: " + index);
		          File myFile = new File (files[index]);
		          byte [] mybytearray  = new byte [(int)myFile.length()];
		          fis = new FileInputStream(myFile);
		          bis = new BufferedInputStream(fis);
		          bis.read(mybytearray,0,mybytearray.length);
		          os = sock.getOutputStream();
		          System.out.println("Sending " + files[index] + "(" + mybytearray.length + " bytes)");
		          os.write(mybytearray,0,mybytearray.length);
		          os.flush();
		          System.out.println("Done.");
		          sock = null;
		          inFromClient = null;
		        }
		        finally {
		          if (bis != null) bis.close();
		          if (os != null) os.close();
		          if (sock!=null) sock.close();
		        }
		      }
		    }
		    finally {
		      if (servsock != null) servsock.close();
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
