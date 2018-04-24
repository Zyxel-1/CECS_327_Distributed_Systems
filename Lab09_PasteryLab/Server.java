/*
  Name: Roberto Sanchez
  Date: 4/24/2018
  Assignment 9 - Pastery Lab
  Purpose: A P2P server.
*/
import java.net.*;
import java.util.HashMap;
import java.io.*;
import java.utl.*;

import javax.lang.model.util.ElementScanner6;

public class server {
    public static void main(String[] args) {
        // Setting up Socket
        DatagramSocket aSocket = null;
        try{
            aSocket = new DatagramSocket(32710);
            while (true){
                byte [] buffer = new byte[1000];
                DatagramPacket request  = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                System.out.println("Request received from: " + request.getAddress().toString());
                request.setData(pastryTable(new String( request.getData(), "UTF-8")).getBytes());
                DatagramPacket reply = new DatagramPacket( request.getData(), request.getLength(), request.getAddress(), request.getPort());
                aSocket.send(reply);
            }
        }catch(SocketException e){
            System.out.println("Socket Exception: "+ e.getMessage());
        }catch(IOException e){
            System.out.println("IO Exception: "+ e.getMessage());
        }finally{
            if (aSocket != null){
                System.out.println("Socket Failed");
                aSocket.close();
            }
        }
    }
    public static String pastryTable(String args){
        args = args.trim();
        boolean valid = true;
        try{
            for (char argChar : args.toCharArray()) {
                int value = Integer.parseInt(Character.toString(argChar));
                if(value > 3){
                    valid = false;
                    break;
                }
            }
        }catch(Exception e){
            System.out.println("Invalid Request" + args);
            return("Invalid!!");
        }
        // Setting up leaf table and routing table
        HashMap< String, String> leafSet = new HashMap< String, String>();
        // Lower
        leafSet.put("3001","13.57.5.20");
        leafSet.put("3011","47.156.67.63");
        // Upper
        leafSet.put("3020","13.58.105.45");
        leafSet.put("3021","54.153.33.88");

        HashMap<String, String> routingTable = new HashMap<String, String>();
        // Row 0 of the routing Table
        routingTable.put("0", "120:18.218.214.209");
        routingTable.put("1", "123:13.57.66.133");
        routingTable.put("2", "133:54.183.67.190");
        routingTable.put("3", "210:54.215.182.174");

        // Row 1 with common Prefix 3
        routingTable.put("30","13:13.56.191.118");
        routingTable.put("31","12:54.241.121.103");
        routingTable.put("32","22:18.144.47.170");
        routingTable.put("33","10:54.177.53.121");

        // Row 2 with common Prefix 30
        routingTable.put("300","1:13.57.5.20");
        routingTable.put("301","3:13.56.191.118");
        routingTable.put("302","0:13.58.105.45");
        routingTable.put("303","x:null");

        // Row 3 with common Prefix 301
        routingTable.put("3010","-x:null");
        routingTable.put("3011",":47.156.67.63");
        routingTable.put("3012","-x:null");
        routingTable.put("3013","13.56.191.118");

         String input = args.trim();
         // Verifying whether input matches with my pastry id and returning response
         if(input.equals("3013")){
             String reply = "Pastry ID: "+ input + "\nAddress: 13.56.191.118";
             return reply;
         }
         else if (leafSet.containsKey(input)){
           // Returning other values stored from the routing table
             String leafSetVal = leafSet.get(input);
             String reply = "Pastry ID: "+ input+ "\nAddress: "+ leafSetVal;
             return reply;
         }
         else
             return pastryRoute (routingTable, input);

    }
    public static String pastryRoute(HashMap<String, String> routingTable, String input){

        int times = input.length();
        int count = 0;

        String temp = "";
        while(count < times){
            temp += Character.toString(input.charAt(count));
            if(routingTable.containsKey(temp)){
                if(times > (count + 1)){
                    count += 1;
                }
                else{
                    String  reply = input + routingTable.get(temp);
                    return reply;
                }
            }
            else{
                input = temp.substring(0 , count);
                return pastryRoute(routingTable, input);
            }
        }
        return input +": null";
    }
}
