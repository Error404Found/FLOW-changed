package com.abhinendra.services;

import java.io.IOException;
import java.util.Date;

import com.abhinendra.domain.*;

public interface TransactionService{

    public Transaction saveTransaction(Transaction transaction) throws Exception;
    public void Polling() throws IOException,InterruptedException;
    public void readFile(String filename);
    public Object findAllTransaction();
    public void createTransactionFile(String filename);
}
