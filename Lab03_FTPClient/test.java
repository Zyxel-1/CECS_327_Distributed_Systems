import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTP;

// IP of my AWS instance is 54.241.121.103
// username for FTP is cecs327
// password for FTP is cecs327

/*
    Testing
    javac -cp commons-net-3.6.jar Assn3.java
    java -cp .:commons-net-3.6.jar Assn3 54.241.121.103 cecs327:cecs327
     java -cp .:commons-net-3.6.jar Assn3 54.241.121.103 cecs327:cecs327 "ls" "put /Users/Felix/Desktop/testing.rtf"

*/

/* 
    Commands that will be supported by this program
    ls 
    cd
    cd ..
    delete
    get 'filename'
    get 'directory name'
    put 'file name'
    mkdir 'directory name'
    rmdir 'directory name'
*/

public class test {
    public static void main(String[] args) {

        ArrayList<String> execution = new ArrayList<String>();
        
        // store all the args that come in to an array list
       for(int i = 0; i < args.length; i++) {
           
           execution.add(args[i]);
           
       }
        // TESTING ONLY: printing out the array list to make sure the right args go through
       System.out.println(execution);

        FTPClient ftpClient = new FTPClient();
        try {
            // pass in the argument for the server IP which is stored as the first argument
            ftpClient.connect(execution.get(0));
            showServerReply(ftpClient);
           
            int replyCode = ftpClient.getReplyCode();
            if(!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Connection failed");
                return;
            }

            // pass in the arguments for the username and password which is stored as the second argument
            // first, I need to split the username and password (args[1]) by the ':' character
            String[] username = parseUsername(execution.get(1));
            boolean success = ftpClient.login(username[0], username[1]);
            showServerReply(ftpClient);

            if(!success) {
                System.out.println("Login failed");
                return;
            }
            
            // FTP operations will begin at the second argument and continue till there are 
            // no more operations to execute
            for (int command_pointer = 2; command_pointer < execution.size(); command_pointer++)
            {
                // pass in the individual arguments
                System.out.println("\n----------> Executing "+ execution.get(command_pointer));
                HandleCommand(execution.get(command_pointer), ftpClient);
            }
        
         } catch (IOException exc) {
             System.out.println("Something went wrong");
             exc.printStackTrace();
         }
        
        // at the end, logout and disconnect from FTP client
         finally {
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                        System.out.println("\nDisconnected from FTP client\n");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
         }

    }

    private static void showServerReply(FTPClient ftpClient) {
    String[] replies = ftpClient.getReplyStrings();
    if (replies != null && replies.length > 0) {
        for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    private static String[] parseUsername(String command) {
       String[] username = command.split(":");
       return username;
    }
    

    private static void HandleCommand(String command, FTPClient ftpClient) {
        /*
        Create an ArrayList that holds the allowed FTP commands

        ArrayList<String> operations = new ArrayList<String>();
        operations.add("ls"); // DONE
        operations.add("cd"); // DONE
        operations.add("delete"); // DONE
        operations.add("get");     // 
        operations.add("put");  //  DONE
        operations.add("mkdir"); // DONE
        operations.add("rmdir");  // DONE

        */

        // Remove the instances of single quotes
        command = command.replaceAll("\'","");

        // when the commands come in 1 at a time, split each single command by spaces
        String op[] = command.split("\\s+");
        String directoryName = ""; 
        boolean success = false;

        // FTP operation will be stored in op[0]
        switch (op[0]) {
            case "ls":
                try {
                    FTPFile[] files = ftpClient.listFiles();
                    showServerReply(ftpClient);
                    for(int i = 0; i < files.length; i++) {
                        System.out.println(files[i]);
                    }
                 } 
                catch(IOException ioe){
                    System.out.println("Issue while executing ls");
                }
               break;

            case "mkdir": 
                for (int i = 1; i < op.length; i++)
                {
                    if(i == op.length -1) {
                        directoryName += op[i];
                    }
                    else {
                        directoryName += op[i]+" ";
                    }
                    
                }
                try {
                      success = ftpClient.makeDirectory(directoryName);
                      showServerReply(ftpClient);
                } catch(IOException ioe){
                    System.out.println("Issue while executing mkdir. Failed to create directory");
                }
               
                 if (success) {
                     System.out.println("The following directory has been created: " + directoryName);
                 } 

                break;

            case "rmdir":
                directoryName = "";
                success = false;
                for (int i = 1; i < op.length; i++)
                {
                    if(i == op.length -1) {
                        directoryName += op[i];
                    }
                    else {
                        directoryName += op[i]+" ";
                    }
                    
                }
                 try {
                     // list out the files from the directory, if there are any there. 
                     FTPFile[] sub_files=ftpClient.listFiles(directoryName);
                     
                     // if there is something inside that directory, then we need to handle that. 
                     if(sub_files.length > 0)
                     {
                         // traverse through everything that is inside the directory
                        for (FTPFile ftpFile : sub_files)
                        {
                            // if there is a sub-directory inside the parent directory, we need to 
                            // deal with that first
                            if (ftpFile.isDirectory())
                            {
                                System.out.println("The following is a directory inside "+directoryName+": "+ftpFile+"\n");
                                HandleCommand("rmdir "+directoryName+"/"+ftpFile.getName() , ftpClient);
                            }

                            else 
                            {
                                // if there are individual files inside the parent directory, go through 
                                // and delete all of those
                                String deleteFilePath = directoryName+"/"+ftpFile.getName();
                                ftpClient.deleteFile(deleteFilePath);
                                System.out.println("The following file has been removed: "+deleteFilePath+"\n");
                            }
                        }
                     }
                     // remove the entire directory
                      success = ftpClient.removeDirectory(directoryName);
                      showServerReply(ftpClient);
                } catch(IOException ioe){
                    System.out.println("Issue while executing rmdir. Failed to remove directory or files inside directory.");
                }
                if (success) {
                     System.out.println("The following directory has been removed: " + directoryName);
                 } 


                break;

            case "cd":
                directoryName = "";
                success = false;
                for (int i = 1; i < op.length; i++)
                {
                    if(i == op.length -1) {
                        directoryName += op[i];
                    }
                    else {
                        directoryName += op[i]+" ";
                    }
                    
                }
                 try {
                     // if block should run if the command is 'cd ..'
                     if(directoryName == "..")
                     {
              
                        success = ftpClient.changeToParentDirectory();
                        showServerReply(ftpClient);
                     }
                     else
                     {
          
                        success = ftpClient.changeWorkingDirectory(directoryName);
                        showServerReply(ftpClient);
                     }
  
                } catch(IOException ioe){
                    System.out.println("Issue while executing cd. Failed to change directory.");
                }

                if (success) {
                     System.out.println("Entered following directory: " + directoryName);
                 } 

                break;


            case "delete":
                directoryName = "";
                success = false;
                for (int i = 1; i < op.length; i++)
                {
                    if(i == op.length -1) {
                        directoryName += op[i];
                    }
                    else {
                        directoryName += op[i]+" ";
                    }
                    
                }

                try {
                    success = ftpClient.deleteFile(directoryName);
                    showServerReply(ftpClient);
                } catch (IOException ioe) {
                    System.out.println("Issue while executing delete. Failed to delete the file.");
                }

                if (success) {
                     System.out.println("Deleted the following file: " + directoryName);
                 } 

                break;  

            case "put":
                directoryName = "";
                success = false;

                for (int i = 1; i < op.length; i++)
                {
                    if(i == op.length -1) {
                        directoryName += op[i];
                    }
                    else {
                        directoryName += op[i]+" ";
                    }
                }
                    
                try {
                    // ftpClient.enterLocalPassiveMode();
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                     Path p = Paths.get(directoryName);
                    // strip out the file path so we are only left with the file name and file extension
                     String remotefile_name = p.getFileName().toString();

                    File localFile = new File(directoryName);
                    InputStream inputStream = new FileInputStream(localFile);

                    success = ftpClient.storeFile(remotefile_name,inputStream);
                    showServerReply(ftpClient);
                    inputStream.close();
                } catch (IOException ioe) {
                    System.out.println("Issue while executing put. Failed to store the file.");
                }

                if (success) {
                     System.out.println("Stored the following file: " + directoryName);
                 }

                break;

            case "get":
                directoryName = "";
                success = false;

                for (int i = 1; i < op.length; i++)
                {
                    if(i == op.length -1) {
                        directoryName += op[i];
                    }
                    else {
                        directoryName += op[i]+" ";
                    }
                }

                try {
                     ftpClient.enterLocalPassiveMode();
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    File remote_file = new File(directoryName);

                    FileOutputStream dfile = new FileOutputStream(remote_file);
                    
                    success = ftpClient.retrieveFile(directoryName, dfile);
                    dfile.close();
                } catch (IOException ioe) {
                    System.out.println("Issue while executing get. Failed to retrieve the file.");
                }

                if(success) {
                    System.out.println("Retrieved the following file: "+directoryName);
                }
                break;
        }
    }
}