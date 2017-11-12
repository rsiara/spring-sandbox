package com.springstart.Controller.DatabaseController;

import com.springstart.Model.Entity.DatabaseEntity.Account;
import com.springstart.Model.Entity.DatabaseEntity.Transaction;
import com.springstart.Service.DatabaseService.AccountService;

import com.springstart.Service.DatabaseService.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

@RestController
@RequestMapping(value = "/database")
public class DatabaseOperationSandboxController {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseOperationSandboxController.class);

    private AccountService accountService;
    private TransactionService transactionService;

    @RequestMapping(path = "/umanytoone", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Transaction unidirectionalManyToOne(){

        Transaction beltPurchase = createNewBeltPurchase();
        Account account = createNewAccount();
        Transaction savedTransaction = transactionService.saveTransactionForAccount(beltPurchase, account);
        return savedTransaction;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    private static Transaction createNewBeltPurchase() {
        Transaction beltPurchase = new Transaction();
        beltPurchase.setTitle("Dress Belt");
        beltPurchase.setAmount(new BigDecimal("50.00"));
        beltPurchase.setClosingBalance(new BigDecimal("0.00"));
        beltPurchase.setCreatedBy("Kevin Bowersox");
        beltPurchase.setCreatedDate(new Date());
        beltPurchase.setInitialBalance(new BigDecimal("0.00"));
        beltPurchase.setLastUpdatedBy("Kevin Bowersox");
        beltPurchase.setLastUpdatedDate(new Date());
        beltPurchase.setNotes("New Dress Belt");
        beltPurchase.setTransactionType("Debit");
        return beltPurchase;
    }

    private static Account createNewAccount() {
        Account account = new Account();
        account.setCloseDate(new Date());
        account.setOpenDate(new Date());
        account.setCreatedBy("Kevin Bowersox");
        account.setInitialBalance(new BigDecimal("50.00"));
        account.setName("Savings Account");
        account.setCurrentBalance(new BigDecimal("100.00"));
        account.setLastUpdatedBy("Kevin Bowersox");
        account.setLastUpdatedDate(new Date());
        account.setCreatedDate(new Date());
        return account;
    }
}
