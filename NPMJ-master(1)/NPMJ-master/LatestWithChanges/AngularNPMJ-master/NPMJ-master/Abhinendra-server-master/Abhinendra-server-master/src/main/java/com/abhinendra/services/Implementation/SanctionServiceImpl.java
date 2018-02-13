package com.abhinendra.services.Implementation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abhinendra.domain.Person;
import com.abhinendra.domain.SanctionList;
import com.abhinendra.domain.Transaction;
import com.abhinendra.repositories.SanctionRepository;
import com.abhinendra.services.SanctionService;

@Service("sanctionService")
public class SanctionServiceImpl implements SanctionService{

	@Autowired
	SanctionRepository sanctionRepository;
	@Override
	public SanctionList saveSanctionEntry(SanctionList sanctionList) throws Exception {
		return sanctionRepository.save(sanctionList);
	}

	@Override
	public void readSanctionList(String filename) {		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = new String();
			while((line=br.readLine()) != null)
			{
			    if(line.contains("Name"))
			    {
				    line = line.substring(9);
				    StringTokenizer st = new StringTokenizer(line,",");
				    SanctionList sanction;
				    while (st.hasMoreTokens()) 
				    {
			    		sanction = new SanctionList(st.nextToken().replaceAll("\\s", ""));
			    		try {
							saveSanctionEntry(sanction);
						} catch (Exception e) {
							e.printStackTrace();
						}
				    } 
			    }
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}		
	}
	
	   SanctionRepository sanctionRepo;
	
	@Override
	public boolean CheckSanctionList(Transaction tran)
	{
    	/*System.out.println("INSIDE CHECK SANCTION LIST FUNCTION");
    	for(SanctionList s: sanctionRepo.findAll())
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
    	return true;*/
		
		System.out.println("HELOOOOOOOOOOOOOOOOOOOOO HIIIIIIIIIIIIII HOW ARE YOUUUUUUUUUUUUUUUUU?");
		
		return true;
    	
	}
	
}
