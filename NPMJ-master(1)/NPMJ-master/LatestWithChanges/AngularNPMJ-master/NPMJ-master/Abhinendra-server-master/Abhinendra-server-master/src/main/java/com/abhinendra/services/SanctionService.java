package com.abhinendra.services;

import com.abhinendra.domain.*;

public interface SanctionService {
    public SanctionList saveSanctionEntry(SanctionList sanctionList) throws Exception;
    public void readSanctionList(String filename);
    public boolean CheckSanctionList(Transaction tran);
}