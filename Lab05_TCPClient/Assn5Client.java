/*
    Name: Roberto Sanchez (014587792)
    Date: March 7, 2018
    Assignment 05 - TCP Client
    Purpose: This connects to a server via port 7896 and sends a string to the target
    and receives a response from server.
*/
import java.net.*;
import java.io.*;
public class Assn5Client{
    public static void main (String args[]){
        Socket s = null;
        try{
            int serverPort = 7896;
            s = new Socket(args[0],serverPort);
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            out.writeUTF(args[1]);
            String data = in.readUTF();
            System.out.println("Receive: "+ data);
        }catch(UnknownHostException e){
            System.out.println("Sock: " + e.getMessage());
        }catch(EOFException e){
            System.out.println("EOF: " + e.getMessage());
        }catch(IOException e){
            System.out.println("IO: " + e.getMessage());
        }finally{if(s!=null) try {s.close();}catch(IOException e){/*close failed*/}}
    }
}