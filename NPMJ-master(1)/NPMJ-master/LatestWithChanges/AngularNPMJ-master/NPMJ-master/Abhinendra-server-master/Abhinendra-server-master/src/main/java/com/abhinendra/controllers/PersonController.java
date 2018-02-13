package com.abhinendra.controllers;

import com.abhinendra.domain.Person;
import com.abhinendra.domain.QPerson;
import com.abhinendra.domain.Transaction;
import com.abhinendra.services.PersonService;
import com.abhinendra.services.TransactionService;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/person")
//@Api(tags = "/person", description = "Operations pertaining to person")

@CrossOrigin(origins = "http://localhost:4200" , allowedHeaders = "*")

public class PersonController {
    @Autowired
    private PersonService personService;
    
    @Autowired
    private TransactionService transactionService;

//    @GetMapping(value = "/{id}", produces = "application/json")
//    public Person getPersonById(@PathVariable("id") int id) throws Exception {
//        return personService.findPersonById(id);
//    }
//
//    @GetMapping(value="/",produces="application/json")
//    public Person findPersonByName(@RequestParam("name") String  name) throws Exception {
//        System.out.println("Path param : "+name);
//        return personService.findPersonByName(name);
//    }
//
//    @PostMapping(value = "/{payerName}",produces = "application/json")
//    public Person savePerson(@PathVariable("payerName")String payerName,String payeeName,Date date,float amount) throws Exception {
//         return personService.savePerson(payerName,payeeName,date,amount);
//    }
    
    @RequestMapping("/api")
    public List<Transaction> getTransaction()
    {
		return (List<Transaction>)transactionService.findAllTransaction();
	}



 

}

