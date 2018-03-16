/*
*   Name: Roberto Sanchez
*   Date: March 14, 2018
*   Assignment 06 - RMI Project
*   Purpose: Sends an integer to the server to be processed.
*   Inputs: [IP-Address] [Function] [Number]
*   Example: java Assn6Client xxx.xxx.xx.x Factorial 5
*/
import java.rmi.*;
import java.util.*;
 
public class Assn6Client {
        
	public static void main (String[] args) {
        MethodInterface serverMethods;
		try {
            // Printing out Inputs to verify if reading properly.
            System.out.println("Your IP Address: " + args[0]);
            System.out.println("You have selected: " + args[1]);
            System.out.println("Input number was: " + args[2]);

            // Connects to server and communicates with the server.
            serverMethods = (MethodInterface)Naming.lookup(args[0]);
            // Local Development connection
            //serverMethods = (MethodInterface)Naming.lookup("rmi://localhost/ABC");
            // Parse input number
            int input = Integer.parseInt(args[2]);
            int result = -1;
            // Determine which function user choose
            switch(args[1]){
                case "Fibonacci":
                    result = serverMethods.Fibonacci(input);
                    break;
                case "Factorial":
                    result = serverMethods.Factorial(input);
                    break;
                default:
                    System.out.println("(!) An Error occured, check your inputs.");
            }
            // Display Results
			System.out.println("Result is :"+result);
			}catch (Exception e) {
				System.out.println("IO exception: " + e);
				}
		}
}