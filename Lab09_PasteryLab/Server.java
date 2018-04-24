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

public class Server {
    public static void main(String[] args) {
        DatagramSocket aSocket = null;

        try{
            aSocket = new DatagramSocket(32710);

            while (true)
            {

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
            if (aSocket != null)
            {
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

                if(value > 3)
                {
                    valid = false;
                    break;
                }
            }
        }catch(Exception e){
            System.out.println("Invalid Request" + args);
            return("Invalid!!");
        }

        HashMap< String, String> leafSet = new HashMap< String, String>();
        /*
        leafSet.put("3220", "18.144.47.170");
        leafSet.put("3222", "18.188.79.150");
        // My Set: 3230 18.216.96.80
        leafSet.put("3231", "18.217.123.98");
        leafSet.put("3302", "50.18.92.71");
        */
        leafSet.put("3001","13.57.5.20");
        leafSet.put("3011","47.156.67.63");
        // My Set: 3013, 13.56.191.118
        leafSet.put("3020","13.58.105.45");
        leafSet.put("3021","54.153.33.88");
        HashMap<String, String> routingTable = new HashMap<String, String>();

         // Row 0 of the routing Table
         routingTable.put("0", "011:52.8.111.209");
         routingTable.put("1", "022:13.58.192.214");
         routingTable.put("2", "022:18.218.254.202");
         routingTable.put("3", "230:18.216.96.80");

         // Row 1 with common Prefix 3
         routingTable.put("30","11:47.156.67.63");
         routingTable.put("31","11:18.217.11.51");
         routingTable.put("32","30:18.216.96.80");
         routingTable.put("33","02:50.18.92.71");

         // Row 2 with common Prefix 32
         routingTable.put("320","x:nul");
         routingTable.put("321","0:54.215.182.174");
         routingTable.put("322","0:18.144.47.170");
         routingTable.put("323","0:18.216.96.80");

         // Row 3 with common Prefix 323
         routingTable.put("3230",":18.216.96.80");
         routingTable.put("3231",":18.217.123.98");
         routingTable.put("3232",":null");
         routingTable.put("3233",":null");

         String input = args.trim();

         if(input.equals("3230"))
         {
             String reply = "Pastry ID: "+ input + "\nAddress: 18.217.123.98";
             return reply;
         }

         else if (leafSet.containsKey(input))
         {
             String leafSetVal = leafSet.get(input);
             String reply = "Pastry ID: "+ input+ "\nAddress: "+ leafSetVal;
             return reply;
         }

         else
         {
             return pastryRoute (routingTable, input);
         }
    }

    public static String pastryRoute(HashMap<String, String> routingTable, String input){

        int times = input.length();
        int count = 0;

        String temp = "";
        while(count < times)
        {
            temp += Character.toString(input.charAt(count));
            if(routingTable.containsKey(temp))
            {
                if(times > (count + 1))
                {
                    count +=1;
                }
                else
                {
                    String  reply = input + routingTable.get(temp);
                    return reply;
                }
            }
            else
            {
                input = temp.substring(0 , count);
                return pastryRoute(routingTable, input);
            }
        }

        return input +": null";
    }
}
