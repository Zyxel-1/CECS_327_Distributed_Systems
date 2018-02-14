/*
 Name: Roberto Sanchez
 Date: Feb 13, 18
 Class: CECS 475 Distributed Systems
 Assignment: Create a FTP Client to connect to existing
             FTP server.
*/
//------------------------------------------------
import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;

public class Assn3 {

	public static void main(String[] args) {
		System.out.println("Hello World");
		FTPClient ftp = new FTPClient();
		try {
			// Setting up connection to server
			String server = "13.57.240.111";
			ftp.connect(server);
			// Passing Credentials through
			boolean login = ftp.login("cecs327", "cecs327");
			// Did I login?
			if(login) {
				System.out.println("Login Successful");
			}else {
				System.out.println("Login Failed");
			}
			System.out.println("Connected to " + server + ".");
			System.out.print("Reply String: "+ ftp.getReplyString());
			System.out.println("Reply Code: "+ ftp.getReplyCode());
			
		}catch(IOException e)
		{
			System.out.println("Something went wrong.");
		}
		
	}
}
