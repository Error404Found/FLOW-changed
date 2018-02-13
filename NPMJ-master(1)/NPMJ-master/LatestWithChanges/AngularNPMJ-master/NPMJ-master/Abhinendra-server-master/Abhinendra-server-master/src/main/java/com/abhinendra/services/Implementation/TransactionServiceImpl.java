package com.abhinendra.services.Implementation;

import com.abhinendra.domain.Transaction;
import com.abhinendra.domain.Person;
import com.abhinendra.domain.SanctionList;
import com.abhinendra.domain.Status;
import com.abhinendra.repositories.PersonRepository;
import com.abhinendra.repositories.SanctionRepository;
import com.abhinendra.repositories.TransactionRepository;
import com.abhinendra.services.PersonService;
import com.abhinendra.services.SanctionService;
import com.abhinendra.services.TransactionService;
import com.querydsl.core.types.Predicate;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {
    Predicate predicate;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    PersonService personService;
    
    SanctionService sanctionService;
    
 

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) throws Exception {
        return transactionRepository.save(transaction);
    }
    
    @Override
    public void createTransactionFile(String filename)
    {
    	ArrayList<Person> personList = personService.selectRecord();
    	int noOfrecords = personList.size();
    	String charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    	Random rand = new Random();
    	File file = new File(filename);
    	String payerName,payeeName;
    	String date;
    	String spaces1 = "",spaces2 = "",spaces3 = "";
    	String payerAccount,payeeAccount;
    	int index;
    	float amount;
    	BufferedWriter writer = null;
    	try {
    		writer = new BufferedWriter(new FileWriter(filename));
	    	
	    	for(int numOfRecords = 0 ; numOfRecords < 50 ; numOfRecords++)
	    	{
	    		spaces1 = "";
	    		DecimalFormat f = new DecimalFormat("##.00");
	            StringBuilder refId = new StringBuilder();
	            for(int i = 0 ; i < 12 ; i++)
	                refId.append(charSet.charAt(rand.nextInt(charSet.length())));
	            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	            Calendar cal = Calendar.getInstance();
	            date = dateFormat.format(cal.getTime());
	            date = date.replaceAll("/","");
	            
	            index = rand.nextInt(noOfrecords);
	            payerName = personList.get(index).getName();
	            for(int space = 0 ; space < (35 - payerName.length()) ; space++)
	            	spaces1 = spaces1 + " ";
	            payerAccount = personList.get(index).getAccount();
	            
	            index = rand.nextInt(noOfrecords);
	            spaces2 = "";
	            payeeName = personList.get(index).getName();
	            for(int space = 0 ; space < (35 - payeeName.length()) ; space++)
	            	spaces2 = spaces2 + " ";
	            payeeAccount = personList.get(index).getAccount();
	            
	            amount = rand.nextFloat()*1000000;
	            String string_form = f.format(amount);
	            string_form = string_form.substring(0,string_form.indexOf('.'));
	            spaces3 = "";
	            for(int space = 0 ; space < (6 - string_form.length()) ; space++)
	            	spaces3 = spaces3 + " ";
	            
	            writer.append(refId.toString());
	            writer.append(date);
	            writer.append(payerName);
	            writer.append("\r"+spaces1);
	            writer.append(payerAccount);
	            writer.append(payeeName);
	            writer.append("\r"+spaces2);
	            writer.append(payeeAccount);
	            writer.append("    ");
	            writer.append("\r"+spaces3);
	            writer.append(new Float(amount).toString());
	            writer.append("\r\n");
	    	}

	    	writer.close();
    	}catch(Exception e) {}
    }
    
    @Override
	public void readFile(String filename)
    {
    	BufferedReader br;
        try 
        {
            br = new BufferedReader(new FileReader(filename));
            String line = new String();
            Transaction transaction;
            boolean value=false;
            while((line=br.readLine()) != null)
            {
                System.out.println(line);
                transaction = setParameters(line);
                String status;
                boolean balanceCheck = false;
                boolean sanctionCheck = false;
                try {
					value = fieldValidate(transaction);
					System.out.println("value:"+value);
					System.out.println("CHECKED THE FIELD VALIDATION");
					if(value == true)
					{
						status = new String(new Status().fieldValidPass);
						transaction.setStatus(status);
						
						String payerName = transaction.getPayerName();
		            	float balance = transaction.getAmount();
		      
		            	balanceCheck=personService.CheckBalance(payerName,balance);
		            	System.out.println("CHECKED THE BALANCE");
		            }
					else
					{
						status = new String(new Status().fieldValidFail);
						transaction.setStatus(status);
					}
					
					saveTransaction(transaction);
					
        	if(balanceCheck==true)
        	{
        		System.out.println("inside sanction");
        	/*	transaction.toString();
        		sanctionCheck=sanctionService.CheckSanctionList(transaction);
        		System.out.println("sanctionCheck is:$$$$$$$$$$$$$$$$$$$$$"+sanctionCheck);*/
      
	        	if(sanctionCheck==true)
	        	{
	        		status = new String(new Status().statusValidPass);
	        		transaction.setStatus(status);
	        		saveTransaction(transaction);
	        		//personService.performTransaction(transaction);
	        	}
	        	else if(sanctionCheck==false)
	        	{
	        		status = new String(new Status().statusValidFail);
	        		transaction.setStatus(status);
	        		saveTransaction(transaction);
	        	}
	            	
            }
				}
                
                catch (Exception e) {}
            }
                     
            System.out.println("FILE PROCESSED SUCCESSFULYY!!!");
	        String current = new File( "." ).getCanonicalPath();
	        
	        Path temp = Files.move(Paths.get(current+"\\"+filename),Paths.get(current+"\\Archive\\"+filename));
	 
	        if(temp != null)
	            System.out.println("File renamed and moved successfully");
	        else
	            System.out.println("Failed to move the file");
        } 
        catch (Exception e){
            e.printStackTrace();
        }
    }

	public boolean fieldValidate(Transaction transaction) throws ParseException
	{
        Calendar cal = Calendar.getInstance();
        Date date1;
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        date1 = (Date) formatter.parse(formatter.format(cal.getTime()));
		boolean value = true;
		
		//personService.showall();
		if(!isAlphaNumeric(transaction.getId()) || !isAlphaNumeric(transaction.getPayerName()) || !isAlphaNumeric(transaction.getPayeeName()) || !isAlphaNumeric(transaction.getPayerAccount()) || !isAlphaNumeric(transaction.getPayeeAccount()))
			value = false;
		if((transaction.getAmount() < 0 )|| transaction.getDate().compareTo(date1) != 0)
			value = false;
		return value;
	}
    public boolean isAlphaNumeric(String s)
    {
        String pattern= "^[a-zA-Z0-9]*$";
        return s.matches(pattern);
    }
    
    public Transaction setParameters(String line)
    {
        char CharArr[]=new char[127];
        CharArr=line.toCharArray();
        char TransChar[]=new char[12];
        char PayerName[]=new char[35];
        char PayeeName[]=new char[35];
        char PayerAccount[] = new char[12];
        char PayeeAccount[] = new char[12];
        String payeeName = null,payerName = null,str = "",RefId = null;
        float amount = 0;
        char Amount[] = new char[12];
        Date date = null;
        int lineIndex = 0 , size = 0;

    	for(int counter = 0 ; counter < 12 ; counter++,lineIndex++)											// Extracting the reference ID of transaction
    		TransChar[counter] = CharArr[lineIndex];
    	RefId = new String(TransChar);
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    	for(int counter = 0 ; counter < 8 ; counter++,lineIndex++)
    	{
            str = str + CharArr[lineIndex];
            if( counter == 1 || counter == 3 )
            	str = str + '/';
    	}
        try {
            date = (Date) formatter.parse(str);										// Extracting the date of transaction
         }catch (ParseException e){
         }
        
        for(int counter = 0 ; CharArr[lineIndex] != ' ' ; counter++,lineIndex++,size++)
            PayerName[counter]=CharArr[lineIndex];
        PayerName = Arrays.copyOfRange(PayerName, 0 , size);
        size = 0;
        lineIndex = 55;
        payerName = new String(PayerName);											// Extracting the Payer name of transaction
        
        for(int counter = 0 ; counter < 12 ; counter++,lineIndex++)
        	PayerAccount[counter] = CharArr[lineIndex];
        String payerAccount = new String(PayerAccount);								// Extracting the Payer Account of transaction
        
        for(int counter = 0 ; CharArr[lineIndex] != ' ' ; counter++,lineIndex++,size++)
            PayeeName[counter]=CharArr[lineIndex];
        PayeeName = Arrays.copyOfRange(PayeeName, 0 , size);
        lineIndex = 102;
        payeeName = new String(PayeeName);											// Extracting the Payee name of transaction
        
        for(int counter = 0 ; counter < 12 ; counter++,lineIndex++)
        	PayeeAccount[counter] = CharArr[lineIndex];
        String payeeAccount = new String(PayeeAccount);								// Extracting the Payee Account of transaction
        
        while(CharArr[lineIndex] == ' ' && lineIndex >= 114)
            lineIndex++;
        int index=lineIndex;           
        for(int counter = 0 ; counter < 127-index ; counter++,lineIndex++)
            Amount[counter] = CharArr[lineIndex];     
        String amt = new String(Amount);    
        amount = Float.parseFloat(amt);												// Extracting the Amount of transaction
        String status = new String(new Status().fieldValidPass);
        Transaction transaction = new Transaction(RefId,payerName,payeeName,date,amount,payerAccount,payeeAccount,status);
    	personService.createUser(transaction);
        return transaction;
    }
    @Override
    public void Polling() throws IOException,InterruptedException 
    {
         String current = new File( "." ).getCanonicalPath();
		 Path faxFolder = Paths.get(current);
		 WatchService watchService = FileSystems.getDefault().newWatchService();
		 faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		 String fileName=new String();
		 boolean valid = true;
		 do{
			WatchKey watchKey = watchService.take();
		
			for (WatchEvent event : watchKey.pollEvents()) {
				WatchEvent.Kind kind = event.kind();
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) 
				{
					fileName = event.context().toString();
					System.out.println("File Created: " + fileName);
					
					readFile(fileName);	
				}
			}
			valid = watchKey.reset();
		} while (valid);	 
	}
    
    @Override
    public Object findAllTransaction()
    {
    	return transactionRepository.findAll();
    }
    
    /*PersonRepository personRepository;
    public boolean CheckSanctionList(Transaction tran)
	{
    	System.out.println("INSIDE CHECK SANCTION LIST FUNCTION");
    	/*for(SanctionList s: sanctionRepo.f)
		{
    		System.out.println("insdie for loop of the sacntion list");
			String Name1=tran.getPayerName();
			String Name2=tran.getPayeeName();
			
			System.out.println("name 1:"+Name1);
			
			if(Name1.equalsIgnoreCase(s.getName()) || Name2.equalsIgnoreCase(s.getName()))
			{
				System.out.println("OOPS YOUR NAME IS IN THE SANCTION LIST");
				return false;
			}
		}
    	return true;
    	
		for(Person p: personRepository.findAll())
		{
			System.out.println(p.getAccount()+" "+p.getName()+" "+p.getBalance());
		}
    	return true;
	}*/

}
