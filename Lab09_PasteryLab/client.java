/*
  Name: Roberto Sanchez
  Date: 4/24/2018
  Assignment 9 - Pastery Lab
  Purpose: A P2P client.
*/
import java.net.*;
import java.io.*;
import java.util.*;

public class client{
     // Setting up client with the inputs: [pastryID] [serverIPAddress]
     public static void main(String[] args) {
        DatagramSocket aSocket = null;
         try{
            // Storing pastryID in DatagramSocket
            aSocket = new DatagramSocket();
            byte[] m = args[0].getBytes();
            // Storing IP Address of the server
            InetAddress aHost = InetAddress.getByName(args[1]);
            // Connecting to port 32710 on the server
            int serverPort = 32710;
            // Prepping request
            DatagramPacket request = new DatagramPacket(m , m.length, aHost, serverPort);
            // Sending request
            aSocket.send(request);
            // Getting response
            byte[] buffer = new byte [1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            // Receive Response from server
            aSocket.receive(reply);
            // Printing out response
            System.out.println("Reply: " + new String(reply.getData()) + "\n");
         }
         catch (SocketException e){
            // Socket Exception was thrown
            System.out.println("Socket: " + e.getMessage());
         }
         catch (IOException e){
            // IO Exception was thrown
             System.out.println("IO: " + e.getMessage());
         }
         finally{
            // Close socket
             if(aSocket != null)
                 aSocket.close();
         }
     }
}
