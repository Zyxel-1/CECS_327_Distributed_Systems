/*
    Name: Roberto Sanchez (014587792)
    Date: March 7, 2018
    Assignment 05 - TCP Server
    Purpose: This is the recieving end of the TCP Client. It takes in a string 
    and reverses it back to the client.
*/
import java.net.*;
import java.io.*;
public class Assn5Server{
    public static void main (String args[]){
        try{
            int serverPort = 7896;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while(true){
                Socket clientSocket = listenSocket.accept();
                Connection c = new Connection(clientSocket);
            }
        }catch(IOException e){
            System.out.println("Listen: " + e.getMessage());
        }
    }
}
class Connection extends Thread{
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    public Connection(Socket aClientSocket){
        try{
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.start();
        }catch(IOException e){
            System.out.println("Connection: " + e.getMessage());
        }
    }
    public void run(){
        try{
            String data = in.readUTF();
            // Reversing Input and sending it out
            StringBuilder input = new StringBuilder();
            input.append(data);
            String output = input.reverse().toString();
            out.writeUTF(output);
        }catch(EOFException e){
            System.out.println("EOF:"+e.getMessage());
        }catch(IOException e){
            System.out.println("IO:"+e.getMessage());
        }finally{
            try{
                clientSocket.close();
            }catch(IOException e){
                /*close failed*/
            }
        }
    }
}