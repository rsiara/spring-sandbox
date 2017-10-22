package com.springstart.Service.DatabaseService;

import com.springstart.Model.DatabaseDao.FinanceUserDao;
import com.springstart.Model.Entity.DatabaseEntity.FinanceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinanceUserService {

  FinanceUserDao financeUserDao;

  public FinanceUser createFinanceUser(FinanceUser financeUser){
    return financeUserDao.createFinanceUser(financeUser);
  }

  public FinanceUserDao getFinanceUserDao() {
    return financeUserDao;
  }

  @Autowired
  public void setFinanceUserDao(FinanceUserDao financeUserDao) {
    this.financeUserDao = financeUserDao;
  }
}
