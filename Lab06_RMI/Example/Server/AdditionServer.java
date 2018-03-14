import java.rmi.*;
import java.rmi.server.*;   
 
public class AdditionServer {
	   public static void main (String[] argv) {
		   try {
			   Addition Hello = new Addition();			   		   
			   Naming.rebind("rmi://localhost/ABC", Hello);
 
			   System.out.println("Addition Server is ready.");
			   }catch (Exception e) {
				   System.out.println("Addition Server failed: " + e);
				}
		   }
}