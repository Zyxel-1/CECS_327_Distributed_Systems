/*
 * Name: Roberto Sanchez
 * Date: Feb 13, 18
 * Class: CECS 475 Distributed Systems
 * Assignment: Create a FTP Client to connect to existing FTP server.
*/
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTP;
import java.nio.file.Path;
import java.nio.file.Paths;

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
		// Attempting to connect to server
		ConnectToServer(arguments.get(0),ftp);
		// Parsing Credentials
		String[] credentials = arguments.get(1).split(":");
        String userName = credentials[0];
        String password = credentials[1];
		// Attempting to Login
		Login(userName,password,ftp);
		// Begin processing all arguments
		System.out.println("*** Going through arguments...***");
		for(int i = 2; i< arguments.size();i++){
			// Stating what command is going to run
			System.out.println("***Running Argument["+i+"]: "+arguments.get(i));
			// Note: This part will break up each token to sub parts
			//		 which will get executed individualy.
			// Cleaning token received from terminal
			String token = arguments.get(i).replaceAll("\'","");
			// Cleaning up whitespaces and tabs
			String command[] = token.split("\\s+");
			// Since the first part after the credentials is the command we will
			// run it. Once inside a FTP command we will run the rest of the the operations
			switch(command[0]){
				// Checking if command is a 'ls'
				case "ls":
					ls(ftp,command);
					break;
				case "cd":
					cd(ftp,command);
					break;
				case "delete":
					delete(ftp,command);
					break;
				case "get" :
					get(ftp,command);
					break;
				case "put":
					put(ftp,command);
					break;
				case "mkdir":
					mkdir(ftp,command);
					break;
				case "rmdir":
					rmdir(ftp,command);
					break;
				default:
					System.out.println("(!) Command not understood:" + command[0]);
				break;
				}
			}
			// Closing connection
			try{
				ftp.disconnect();
			}catch(IOException e){
				System.out.println("(!) An error has occured while disconnecting...");
			}
	}
	//------------------------------------------------------------------------------
	public static void delete(FTPClient ftp, String arguments[]){
		String dir = "";
		dir = directory(arguments);
		try{
			if(ftp.deleteFile(dir)){
				System.out.println("*** "+dir+" has been deleted from directory.");
				serverResponse(ftp);
			}
		}catch(IOException e){
			System.out.println("(!) An error has occured while deleting.");
		}
	}
	//------------------------------------------------------------------------------
	public static void put(FTPClient ftp, String arguments[]){
		String dir = "";
		dir = directory(arguments);
		try{
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			Path path = Paths.get(dir);
			String remote = path.getFileName().toString();
			File localFile = new File(dir);
			InputStream inputstream = new FileInputStream(localFile);
			if(ftp.storeFile(remote,inputstream)){
				System.out.println("*** "+dir+" has been uploaded");	
			}
		}catch(IOException e){
			System.out.println("(!) An error has occured in put.");
		}
	}
	//------------------------------------------------------------------------------
	public static void get(FTPClient ftp, String arguments[]){
		String dir = "";
		dir = directory(arguments);
		try{
			ftp.enterLocalPassiveMode();
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			File remote = new File(dir);
			FileOutputStream downloadFile = new FileOutputStream(remote);
			if(ftp.retrieveFile(dir,downloadFile)){
				System.out.println("Downloaded the following file: "+ dir);
				serverResponse(ftp);
			}
		}catch(IOException e){
			System.out.println("(!) An error has occured while geting.");
		}
	}
	//------------------------------------------------------------------------------
	public static void rmdir(FTPClient ftp, String arguments[]){
		String dir = "";
		dir = directory(arguments);
		try{
			FTPFile[] files = ftp.listFiles(dir);
			// Does files exist?
			if(files.length > 0){
				for(FTPFile file:files){
					if(file.isDirectory()){
						// FIGURE OUT
					}

				}
			}
		}catch(IOException e){
			System.out.println("(!) An error has occured in rmdir.");
		}
	}
	//------------------------------------------------------------------------------
	public static void mkdir(FTPClient ftp, String arguments[]){
		String dir = "";
		dir = directory(arguments);
		try{
			if(ftp.makeDirectory(dir)){
				System.out.println("*** Successfully Created directory:"+dir);
			}
		}catch(IOException e){
			System.out.println("(!) An error occured while creating a directory.");
		}
	}
	public static String directory(String arguments[]){
		String dir = "";
		for(int j = 1;j<arguments.length;j++){
			if(j == arguments.length - 1){
				dir += arguments[j];
			}else{
				dir += arguments[j]+" ";
			}
		}
		return dir;
	}
	//------------------------------------------------------------------------------
	public static void cd(FTPClient ftp, String arguments[]){
		String dir = "";
		dir = directory(arguments);
		
		try{
			// If user inputed '..' as directory
			if(dir == ".."){
				if(ftp.changeToParentDirectory()){
					System.out.println("Failed to cd ..");
				}
				serverResponse(ftp);
			}else{
			// If user has acutal directory
				if(ftp.changeWorkingDirectory(dir)){
					serverResponse(ftp);
					System.out.println("Current Directory:"+dir);
				}else{
					System.out.println("(!) Could not change directory with: )"+dir);
				}
			}
			}catch(IOException e){
				System.out.println("(!) An error has occured in the CD command. Check your input: "+dir);
			}
	}
	//------------------------------------------------------------------------------
	public static void ls(FTPClient ftp, String arguments[]){
		try{
			// Gests the current directory currently at
			FTPFile[] directory = ftp.listFiles();
			serverResponse(ftp);
			// Printing directory
			for(int j = 0; j < directory.length; j++){
				System.out.println(directory[j]);
			}
		}catch(IOException e){
			System.out.println("(!) An error occured while printing directory.");
		}
	}
	//------------------------------------------------------------------------------
	public static ArrayList<String> ReadTerminalInputs(String[] args){
		// Takes in arguments into ArrayList
		ArrayList<String> arguments = new ArrayList<String>();
		for(int i = 0; i< args.length;i++){
			arguments.add(args[i]);
		}
		return arguments;
	}
	//------------------------------------------------------------------------------
	private static void serverResponse(FTPClient ftp){
		// Prints out the response from server
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
				System.out.println("*** Attempting to connect to: "+IpAddress);
				ftp.connect(IpAddress);
				// Display Response
				serverResponse(ftp);
				if(FTPReply.isPositiveCompletion(ftp.getReplyCode())){
					System.out.println("Connection Success");
				}
		}catch(IOException e){
					System.out.println("(!) Connection Failed.");
		}
	}
	//------------------------------------------------------------------------------
	public static void Login(String userName,String password, FTPClient ftp){
		try{
			System.out.println("*** Attempting to login as: "+userName);
			// Pass credentials in login function, returns true if success, false for failed
			if(ftp.login(userName,password)) {
				System.out.println("Login Successful");
				serverResponse(ftp);
				System.out.println("Reply Code: "+ ftp.getReplyCode());
			}else {
				System.out.println("(!) Login Failed: Check your credentials.");
				return;
			}
		}catch(IOException e){
			System.out.println("(!) An Error occured while Logging In");
		}	
	}
}