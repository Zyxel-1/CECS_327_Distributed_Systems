/*
*   Name: Roberto Sanchez
*   Date: March 14, 2018
*   Assignment 06 - RMI Project
*   Purpose:
*/
// Actually Implement the Factorial and Fibonacci
import java.rmi.*;
import java.rmi.server.*;
 
public class Method extends UnicastRemoteObject
         implements MethodInterface {
 
      public Method () throws RemoteException {   }
 
      public int add(int a, int b) throws RemoteException {
    	  int result = a + b;
    	  return result;
      }
      public int Fibonacci(int a) throws RemoteException{
          if(a <= 1){
              return a;
          }
          return Fibonacci(a-1) + Fibonacci(a-2);

      }
      public int Factorial(int a) throws RemoteException{
          int fact = 1;
          for(int i = 1; i <= a; i++){
              fact = fact * i;
          }
          return fact;
      }
 }