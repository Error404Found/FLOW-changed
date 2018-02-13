package com.abhinendra.services;

import java.util.ArrayList;
import java.util.List;

import com.abhinendra.domain.*;
import com.abhinendra.services.Implementation.InsufficientAmtException;

public interface PersonService{

    public Person savePerson(Person person) throws Exception;
    public void createUser(Transaction transaction);
    public Person findOnebyId(Transaction transaction);
    public void showall();
    public boolean CheckBalance(String fromName,float amt);
    public void withdraw(String payerName, float Amt) throws InsufficientAmtException;
    

    public ArrayList<Person> selectRecord();
	//public void performTransaction(Transaction transaction);
	
}