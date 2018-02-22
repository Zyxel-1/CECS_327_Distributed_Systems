/*
 * Name: Roberto Sanchez
 * Date: Feb 13, 18
 * Class: CECS 475 Distributed Systems
 * Assignment: Create a FTP Client to connect to existing FTP server.
*/
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTP;

/*
IP:13.57.240.111
 * Note: Order of arguments matter
 * 1. IP address of ftp server
 * 2. username and password (ex. cecs327:cecs327)
 * 3. From there on its broken up into chunks (ex. [Command] [Stuff with Spaces])
 * 	  a. Be able to separate the command from the 
 */
public class Assn3 {
	
	public static void main(String[] args) {
		// Reading in all arguments from terminal
		ArrayList<String> arguments = ReadTerminalInputs(args);
		// Creating FTP Object
	    FTPClient ftp = new FTPClient();
		ConnectToServer(arguments.get(0),ftp);
		String[] credentials = arguments.get(1).split(":");
        String userName = credentials[0];
        String password = credentials[1];
		Login(userName,password,ftp);
		System.out.println("*** Going through arguments...***");
		// Begin processing all arguments
		for(int i = 2; i< arguments.size();i++){
			//RunCommand(arguments.get(i),ftp);
			System.out.println("Argument["+i+"]"+arguments.get(i));
			// Note: This part will break up each token to sub parts
			//		 which will get executed individualy.
			// Cleaning token received from terminal
			String token = arguments.get(i).replaceAll("\'","");
			// Cleaning up whitespaces and tabs
			System.out.println(token);
			String command[] = token.split("\\s+");
			// Since the first part after the credentials is the command we will
			// run it. Once inside a FTP command we will run the rest of the the operations
			switch(command[0]){
				// Checking if command is a 'ls'
				case "ls":
					System.out.println("ls");
					ls(ftp,command);
					break;
				case "cd":
					System.out.println("cd");
					cd(ftp,command);
					break;
				case "delete":
					System.out.println("Delete");
					delete(ftp,command[0]);
					break;
				case "get" :
					System.out.println("Get");
					get(ftp,command[0]);
					break;
				case "put":
					System.out.println("Put");
					put(ftp,command[0]);
					break;
				case "mkdir":
					System.out.println("mkdir");
					mkdir(ftp,command[0]);
					break;
				case "rmdir":
					System.out.println("rmdir");
					rmdir(ftp,command[0]);
					break;
				default:
					System.out.println("Command not understood:"+command[0]);
				break;
				}
			}
			// Closing connection
			try{
				ftp.disconnect();
			}catch(IOException e){

			}
	}
	//------------------------------------------------------------------------------
	public static void delete(FTPClient ftp, String arguments){
		// Work on
	}
	//------------------------------------------------------------------------------
	public static void put(FTPClient ftp, String arguments){
		// Work on
	}
	//------------------------------------------------------------------------------
	public static void get(FTPClient ftp, String arguments){
		// Work on
	}
	//------------------------------------------------------------------------------
	public static void rmdir(FTPClient ftp, String arguments){
		// Work on
	}
	//------------------------------------------------------------------------------
	public static void mkdir(FTPClient ftp, String arguments){
		// Work on
	}
	//------------------------------------------------------------------------------
	public static void cd(FTPClient ftp, String arguments[]){
		String dir = "";
		for(int j = 1;j<arguments.length;j++){
			if(j == arguments.length - 1){
				dir += arguments[j];
			}else{
				dir += arguments[j]+" ";
			}
		}
		System.out.println(dir);
		try{
			// If user inputed '..' as directory
			if(dir == ".."){
				if(ftp.changeToParentDirectory()){
					System.out.println("Failed to cd ..");
				}
				serverResponse(ftp);
				}else{
					if(ftp.changeWorkingDirectory(dir)){
						System.out.println("Current Directory:"+dir);
					}
					serverResponse(ftp);
				}
			}catch(IOException e){
				System.out.println("An error has occured in the CD command. Check your input: "+dir);
			}
	}
	//------------------------------------------------------------------------------
	public static void ls(FTPClient ftp, String arguments[]){
		try{
						System.out.println("Running ls command:");
						FTPFile[] directory = ftp.listFiles();
						serverResponse(ftp);
						for(int j = 0; j<directory.length;j++){
							System.out.println(directory[j]);
						}
					}catch(IOException e){
						System.out.println("An error occured while printing directory.");
					}
	}
	//------------------------------------------------------------------------------
	public static ArrayList<String> ReadTerminalInputs(String[] args){
		ArrayList<String> arguments = new ArrayList<String>();
		for(int i = 0; i< args.length;i++){
			arguments.add(args[i]);
		}
		return arguments;
	}
	//------------------------------------------------------------------------------
	private static void serverResponse(FTPClient ftp){
		//System.out.println("Response: "+ftp.getReplyStrings());
		System.out.println("|----------Server Response----------|");
		String[] replies = ftp.getReplyStrings();
    if (replies != null && replies.length > 0) {
        for (String aReply : replies) {
                System.out.println(aReply);
            }
        }
    }
	//------------------------------------------------------------------------------
	public static void ConnectToServer(String IpAddress, FTPClient ftp){
		try {
				// Connecting to server
				System.out.println("*** Attempting to connect to:"+IpAddress);
				ftp.connect(IpAddress);
				// Display Response
				serverResponse(ftp);
				if(FTPReply.isPositiveCompletion(ftp.getReplyCode())){
					System.out.println("Connection Success");
				}
		}catch(IOException e){
					System.out.println("Connection Failed.");

		}
	}
	//------------------------------------------------------------------------------
	public static void Login(String userName,String password, FTPClient ftp){
		try{
			System.out.println("*** Attempting to login as: "+userName);
			if(ftp.login(userName,password)) {
				System.out.println("Login Successful");
				serverResponse(ftp);
				System.out.println("Reply Code: "+ ftp.getReplyCode());
			}else {
				System.out.println("Login Failed: Check your credentials.");
				return;
			}
		}catch(IOException e){
			System.out.println("An Error occured while Logging In");
		}	
	}
}