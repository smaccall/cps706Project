package socketprogramming;
import java.io.*;
import java.net.*;

public class herCDN
{
		//CHECK TO SEE IF PATH TO FILE IS CORRECT
		private final static String[] files= {"C:\\Users\\smaccall\\Desktop\\video1.mp4",
			"C:\\Users\\smaccall\\Desktop\\video2.mp4",
			"C:\\Users\\smaccall\\Desktop\\video3.mp4",
			"C:\\Users\\smaccall\\Desktop\\video4.mp4"};
		
		private final static int herServerPort = 40080;
		private final static String clientIP="", localIP="", hisCinemaIP="", hisDNSIP="", herDNSIP="", herCDNIP="";
		
		public static void main(String [] args) throws IOException
		{			
			FileInputStream fInputStream = null;
		    BufferedInputStream bInputStream = null;
		    OutputStream outputStream = null;
		    ServerSocket servsock = null;
		    Socket socket = null;
		    try {
		      servsock = new ServerSocket(herServerPort);
		      while (true) {
		        System.out.println("Waiting for connection to begin.");
		        try {
		          socket = servsock.accept();
		          System.out.println("Accepted connection : " + socket);
		          // receive the selection client has made
		          BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));		          
		          int index = inFromClient.read();
		          // send the correct file to client
		          System.out.println("File index: " + index);
		          File myFile = new File (files[index]);
		          byte [] mybytearray  = new byte [(int)myFile.length()];
		          fInputStream = new FileInputStream(myFile);
		          bInputStream = new BufferedInputStream(fInputStream);
		          bInputStream.read(mybytearray,0,mybytearray.length);
		          outputStream = socket.getOutputStream();
		          System.out.println("Sending " + files[index] + "(" + mybytearray.length + " bytes)");
		          outputStream.write(mybytearray,0,mybytearray.length);
		          outputStream.flush();
		          System.out.println("Finished sending.");
		          socket = null;
		          inFromClient = null;
		        }
		        finally {
		          if (bInputStream != null) bInputStream.close();
		          if (outputStream != null) outputStream.close();
		          if (socket!=null) socket.close();
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