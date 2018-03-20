/*
*   Name: Roberto Sanchez
*   Date: March 14, 2018
*   Assignment 06 - RMI Project
*   Purpose: Handles the RMI from Client
*/
import java.rmi.*;
import java.rmi.server.*;   
 
public class Assn6Server {
	   public static void main (String[] argv) {
		   try {
			   System.SetProperty("java.rmi.server.hostname",argv[0]);
			   Method localMethods = new Method();			   		   
			   Naming.rebind("rmi://"+argv[0]+"/cecs327", localMethods);
 
			   System.out.println("Server is ready. The arguments should be factorial n or fibonacci n");
			   }catch (Exception e) {
				   System.out.println("Server failed: " + e);
				}
		   }
}
