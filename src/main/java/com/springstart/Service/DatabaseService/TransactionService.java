package com.springstart.Service.DatabaseService;

import com.springstart.Model.DatabaseDao.AccountDao;
import com.springstart.Model.DatabaseDao.TransactionDao;
import com.springstart.Model.Entity.DatabaseEntity.Account;
import com.springstart.Model.Entity.DatabaseEntity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class TransactionService {

    private AccountDao accountDao;
    private TransactionDao transactionDao;

    public Transaction saveTransactionForAccount(Transaction transaction, Account account){

        transaction.setAccount(account);
        return transactionDao.save(transaction);
    }

    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Autowired
    public void setTransactionDao(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }
}
