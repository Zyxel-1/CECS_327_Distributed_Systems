/*
*   Name: Roberto Sanchez
*   Date: March 14, 2018
*   Assignment 06 - RMI Project
*   Purpose: Implements the methods for RMI
*/
import java.rmi.*;
import java.rmi.server.*;
 
public class Method extends UnicastRemoteObject
         implements MethodInterface {
 
      public Method () throws RemoteException { super(1100)  }
    public int fibonacci(int n) throws RemoteException{
        if(n <= 1){
              return n;
          }
          return fibonacci(n-1) + fibonacci(n-2);
    }
	public int factorial(int n) throws RemoteException{
        int fact = 1;
          for(int i = 1; i <= n; i++){
              fact = fact * i;
          }
          return fact;
    }
 }
