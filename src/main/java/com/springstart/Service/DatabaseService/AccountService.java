package com.springstart.Service.DatabaseService;

import com.springstart.Model.DatabaseDao.AccountDao;
import com.springstart.Model.DatabaseDao.ParkingPlaceDao;
import com.springstart.Model.Entity.DatabaseEntity.Account;
import com.springstart.Model.Entity.DatabaseEntity.ParkingPlace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AccountService {

    private AccountDao accountDao;

    /* Methods */

    public Account getById(Long id){
        return accountDao.getById(id);
    }

    public void save(Account account){
        accountDao.save(account);
    }

    /**/

    public AccountDao getAccountDao() {
        return accountDao;
    }

    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
}
