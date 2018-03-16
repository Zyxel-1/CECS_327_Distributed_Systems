/*
*   Name: Roberto Sanchez
*   Date: March 14, 2018
*   Assignment 06 - RMI Project
*   Purpose:
*/
import java.rmi.*;
public interface MethodInterface extends Remote {
	
	public int Factorial(int a) throws RemoteException;
	public int Fibonacci(int a) throws RemoteException;

}