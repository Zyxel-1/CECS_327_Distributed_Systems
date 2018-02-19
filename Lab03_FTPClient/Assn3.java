/*
 Name: Roberto Sanchez
 Date: Feb 13, 18
 Class: CECS 475 Distributed Systems
 Assignment: Create a FTP Client to connect to existing
             FTP server.
*/
//------------------------------------------------
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPClient;
/*
 * Note: Order of arguments matter
 * 1. IP address of ftp server
 * 2. username and password (ex. cecs327:cecs327)
 * 3. 
 */
public class Assn3 {
	public static void main(String[] args) {
		// Reading in all arguments from terminal
		ArrayList<String> arguments = new ArrayList<String>();
		for(int i = 0; i< args.length;i++){
			arguments.add(args[i]);
		}
		// Creating FTP Object
	    FTPClient ftp = new FTPClient();
		try {
			// Connect to server
			ftp.connect(arguments.get(0));
			// Display Response
            showServerReply(ftp);
            // Reading Login credentials
            String[] credentials =arguments.get(1).split(":");
            String userName = credentials[0];
            String password = credentials[1];
            // Login Attempt
            boolean login = ftp.login(userName,password);
			// Was login successful?
			if(login) {
				System.out.println("Login Successful");
			}else {
				System.out.println("Login Failed");
				return;
			}
			// Printing if connection worked
			System.out.println("Connected to " + arguments.get(0) + ".");
			System.out.print("Reply String: "+ ftp.getReplyString());
			System.out.println("Reply Code: "+ ftp.getReplyCode());
			// Begin processing all arguments
			for(int i = 0; i< arguments.size();i++){
				RunCommand(arguments.get(i),ftp);
			}
			// Closing connection
			ftp.disconnect();
		}catch(IOException e)
		{
			System.out.println("Something went wrong.");
		}
	}
	private static void RunCommand(String token, FTPClient ftp){
		// Note: This part will break up each token to sub parts
		//		 which will get executed individualy.
		
		// Cleaning token received from terminal
		token = token.replaceAll("\'","");
		// Cleaning up whitespaces and tabs
		String parts[] = token.split("\\s+");
		switch(parts[0]){
			case "ls"

		}
	}
}
