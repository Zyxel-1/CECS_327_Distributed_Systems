/*
*   Name: Roberto Sanchez
*   Date: March 14, 2018
*   Assignment 06 - RMI Project
*   Purpose:
*/
import java.rmi.*;
import java.rmi.server.*;   
 
public class Assn6Server {
	   public static void main (String[] argv) {
		   try {
			   Method localMethods = new Method();			   		   
			   Naming.rebind("rmi://localhost/ABC", localMethods);
 
			   System.out.println("Server is ready.");
			   }catch (Exception e) {
				   System.out.println("Server failed: " + e);
				}
		   }
}