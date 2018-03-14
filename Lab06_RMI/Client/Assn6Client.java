/*
*   Name: Roberto Sanchez
*   Date: March 14, 2018
*   Assignment 06 - RMI Project
*   Purpose:
*/
import java.rmi.*;
 
public class Assn6Client {
	public static void main (String[] args) {
        // Not sure if I need this on client side
        MethodInterface serverMethods;
		try {
            // Connects to server and communicates with the server.
            //serverMethods = (MethodInterface)Naming.lookup("rmi:13.57.240.111");
            //serverMethods = (MethodInterface)Naming.lookup("rmi:"+ args[0]);
            serverMethods = (MethodInterface)Naming.lookup("rmi://localhost/ABC");
            int input = 10;
            int result = -1;
            System.out.println(args[0]);
			if(args[0] == "fibonacci"){
                // Converts string into integer
                //input = Integer.parseInt(args[0]);
                // Passes integer into method
                result = serverMethods.Fibonacci(input);
            }else if(args[0] == "factorial"){
                // Converts string into integer
                //input = Integer.parseInt(args[0]);
                // Passes integer into method
                result = serverMethods.Factorial(input);
            }else if(args[0] == "add"){
                result = serverMethods.add(input,1);
            }else{
                // Throws error
                System.out.println("(!) An error occured, check your inputs.");
            }
            // Display Results
			System.out.println("Result is :"+result);
			}catch (Exception e) {
				System.out.println("IO exception: " + e);
				}
		}
}