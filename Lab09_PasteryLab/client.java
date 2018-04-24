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
     public static void main(String[] args) {
        DatagramSocket aSocket = null;
         try{
            aSocket = new DatagramSocket();
            byte[] m = args[0].getBytes();

            InetAddress aHost = InetAddress.getByName(args[1]);

            int serverPort = 32710;
            DatagramPacket request = new DatagramPacket(m , m.length, aHost, serverPort);
            aSocket.send(request);

            byte[] buffer = new byte [1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);

            System.out.println("Reply: "+new String(reply.getData())+"\n");
         }
         catch (SocketException e){
            System.out.println("Socket: "+e.getMessage());
         }

         catch (IOException e){
             System.out.println("IO: "+e.getMessage());

         }
         finally{
             if(aSocket != null)
                 aSocket.close();
         }
     }
}
