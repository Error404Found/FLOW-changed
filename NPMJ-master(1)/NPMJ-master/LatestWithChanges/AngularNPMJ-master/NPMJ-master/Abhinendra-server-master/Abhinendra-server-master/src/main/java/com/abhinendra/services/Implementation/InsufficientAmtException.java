package com.abhinendra.services.Implementation;

public class InsufficientAmtException extends Exception 
{

	InsufficientAmtException(String string)
	{
		System.out.println("YOU DO NOT HAVE SUFFICIENT BALANCE. HENCE TRANSACTION IS ROLLED BACK");
	}
	
}
