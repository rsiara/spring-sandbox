package com.springstart.Service.DatabaseService;

import com.springstart.Model.DatabaseDao.BankDao;
import com.springstart.Model.Entity.DatabaseEntity.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankService {

  private BankDao bankDao;

  public Bank createBank(Bank bank) {
    return bankDao.save(bank);
  }

  public BankDao getBankDao() {
    return bankDao;
  }

  @Autowired
  public void setBankDao(BankDao bankDao) {
    this.bankDao = bankDao;
  }
}




