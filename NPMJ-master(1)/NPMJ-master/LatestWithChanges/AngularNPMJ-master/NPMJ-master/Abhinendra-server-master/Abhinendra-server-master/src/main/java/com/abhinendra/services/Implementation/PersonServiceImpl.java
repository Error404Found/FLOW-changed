package com.abhinendra.services.Implementation;

import com.abhinendra.domain.Person;
import com.abhinendra.domain.SanctionList;
import com.abhinendra.domain.Transaction;
import com.abhinendra.repositories.PersonRepository;
import com.abhinendra.services.PersonService;
import com.querydsl.core.types.Predicate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("personService")
public class PersonServiceImpl implements PersonService {
	//boolean FLAGFORCOMMIT=true;
    Predicate predicate;
    @Autowired
    PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person savePerson(Person person) throws Exception {
        return personRepository.save(person);
    }

	@Override
	public void createUser(Transaction transaction) 
	{
	        Random rand = new Random();
	        int range = rand.nextInt(1000);
	        float balance = rand.nextFloat()*range;
	       
	        Person person = null;
	        try {
	            person = new Person(transaction.getPayerAccount(),transaction.getPayerName(),balance);
				savePerson(person);
		        balance = rand.nextFloat()*range;
				person = new Person(transaction.getPayeeAccount(),transaction.getPayeeName(),balance);
				savePerson(person);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	//why do you need this method?
    public ArrayList<Person> selectRecord() 
    {
        return (ArrayList<Person>) personRepository.findAll();
    }

	@Override
	public Person findOnebyId(Transaction transaction) {
		Person person = personRepository.findOne(transaction.getPayerAccount());
		return person;
	}
	
	 public void showall()
	{
		for(Person p: personRepository.findAll())
		{
			System.out.println(p.getAccount()+" "+p.getName()+" "+p.getBalance());
		}
	}
	 
	public boolean CheckBalance(String fromName,float amt) 
	{
		try 
		{
			withdraw(fromName,amt);
			showall();
			return true;
		} 
		catch (InsufficientAmtException e)
		{
			System.out.println("in catch");
			return false;
		}
		
	}
	
	@Transactional(rollbackOn=InsufficientAmtException.class) 
	public void withdraw(String payerName, float Amt) throws InsufficientAmtException
	{
	
		System.out.println("**************inside withdraw with name:"+payerName);
		Person per = personRepository.findByname(payerName);
		float bal = per.getBalance();
		bal = bal - Amt;
		if(bal < 0) 
		{
			throw new InsufficientAmtException("exception occured");
		}
		else
		{
			bal=bal+Amt;
			per.setBalance(bal);
			personRepository.save(per);
		}
	}

/*	@Override
	@Transactional
	public void performTransaction(Transaction transaction)
	{
		
		
	}*/
	
	/*public void deposit(String payeeName, float Amt) 
	{
		System.out.println("******************inside deposit with name:"+payeeName);
		Person per = personRepository.findByname(payeeName);
		float bal = per.getBalance();
		bal = bal + Amt;
		per.setBalance(bal);
		personRepository.save(per);
	}*/

}
