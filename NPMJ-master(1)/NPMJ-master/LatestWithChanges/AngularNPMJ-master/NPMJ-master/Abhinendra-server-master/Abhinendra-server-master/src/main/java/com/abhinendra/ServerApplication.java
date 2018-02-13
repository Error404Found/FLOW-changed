package com.abhinendra;

import java.io.IOException;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import com.abhinendra.services.PersonService;
import com.abhinendra.services.SanctionService;
import com.abhinendra.services.TransactionService;

@SpringBootApplication
@EntityScan(basePackages = { "com.abhinendra.domain" })
@ComponentScan(basePackages = "com.abhinendra")
public class ServerApplication {

	@Autowired
	TransactionService transactionservice;
	
	@Autowired
	SanctionService sanctionService;
	
	@Autowired
	PersonService personService;

	@PostConstruct
    public void combine()
	{
		//personService.createUser("CustomerList.txt");
		//transactionservice.createTransactionFile("sample.txt");
		try 
		{
			sanctionService.readSanctionList("sanctionList.txt");
			transactionservice.Polling();
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
		//transactionservice.readFile("sample.txt");
	 
    }
	
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
}
