/*
*   Name: Roberto Sanchez
*   Date: March 14, 2018
*   Assignment 06 - RMI Project
*   Purpose:
*/
import java.rmi.*;
import java.util.*;
 
public class Assn6Client {
	public static void main (String[] args) {
        MethodInterface serverMethods;
		try {
            // Printing out Inputs
            System.out.println("Your IP Address: " + args[0]);
            System.out.println("You have selected: " + args[1]);
            System.out.println("Input number was: " + args[2]);
            // Setting up connection to Server
            String connection = "rmi://" + args[0] + "/ABC";
            System.out.println("Connection address: "+connection);
            // Connects to server and communicates with the server.
            serverMethods = (MethodInterface)Naming.lookup(connection);
            // Local Development
            //serverMethods = (MethodInterface)Naming.lookup("rmi://localhost/ABC");
            int input = Integer.parseInt(args[2]);
            int result = -1;
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