package com.springstart.tests;


import com.springstart.Configuration.RootConfig;
import com.springstart.Model.Entity.DatabaseEntity.*;
import com.springstart.Model.Enum.PhoneType;
import com.springstart.Service.DatabaseService.*;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.criterion.CriteriaQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.*;

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class RelationMappingTest {

  EntityManager entityManager;

  private AccountService accountService;
  private TransactionService transactionService;
  @Autowired
  private FinanceUserService financeUserService;
  @Autowired
  private ParkingPlaceService parkingPlaceService;
  @Autowired
  private ProjectService projectService;
  @Autowired
  private BankService bankService;

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }


/*  @Test
  @Transactional
  @Rollback(false)
  public void mapKeyingByBasicTypeTest() {

    FinanceUser financeUser = createNewUser();

    Map<String, String> basicKeyBasicValueMap = new HashMap<String, String>();
    basicKeyBasicValueMap.put("MOBILE", "780152424");
    basicKeyBasicValueMap.put("STATIONARY", "+48226224545");

    financeUser.setPhoneNumbers(basicKeyBasicValueMap);

    financeUserService.createFinanceUser(financeUser);
  }*/

  @Test
  @Transactional
  @Rollback(false)
  public void mapKeyingByEnumTypeTest() {

    FinanceUser financeUser = createNewUser();

    Map<PhoneType, String> enumKeyBasicValueMap = new HashMap<PhoneType, String>();
    enumKeyBasicValueMap.put(PhoneType.MOBILE, "780152424");
    enumKeyBasicValueMap.put(PhoneType.WORK, "+48226224545");
    enumKeyBasicValueMap.put(PhoneType.HOME, "+2278293123");

    financeUser.setPhoneNumbers(enumKeyBasicValueMap);

    financeUserService.createFinanceUser(financeUser);
  }

/* JPA - Using UNIDIRECTIONAL One-To-Many */
/*
  @Test
  @Transactional
  @Rollback(false)
  public void mapValueAsEntityTest(){

    Bank bank = createBank();

    FinanceUser whiteJoe = createNewUser();
    FinanceUser blackBob = createNewUser();

    Map<String, FinanceUser> basicKeyEntityValueMap = new HashMap<String, FinanceUser>();

    basicKeyEntityValueMap.put("WHITE", whiteJoe);
    basicKeyEntityValueMap.put("BLACK", blackBob);

    bank.setUsersBySkingColor(basicKeyEntityValueMap);

    bankService.createBank(bank);

  }*/


/* JPA - Using BIDIRECTIONAL One-To-Many */

/*  @Test
  @Transactional
  @Rollback(false)
  public void mapValueAsEntityTest(){

    Bank bank = createBank();

    FinanceUser whiteJoe = createNewUser();
    FinanceUser blackBob = createNewUser();

    Map<String, FinanceUser> basicKeyEntityValueMap = new HashMap<String, FinanceUser>();

    basicKeyEntityValueMap.put("WHITE", whiteJoe);
    basicKeyEntityValueMap.put("BLACK", blackBob);

    whiteJoe.setBank(bank);
    blackBob.setBank(bank);

    bank.setUsersBySkingColor(basicKeyEntityValueMap);

    bankService.createBank(bank);

  }*/

/* JPA - Using BIDIRECTIONAL Many-To-Many */

/*  @Test
  @Transactional
  @Rollback(false)
  public void mapValueAsEntityTest(){

    Bank bank = createBank();

    FinanceUser whiteJoe = createNewUser();
    FinanceUser blackBob = createNewUser();

    Map<String, FinanceUser> basicKeyEntityValueMap = new HashMap<String, FinanceUser>();

    basicKeyEntityValueMap.put("whitejoe@mbank.pl", whiteJoe);
    basicKeyEntityValueMap.put("blackjoe@mbank.pl", blackBob);

    whiteJoe.setBank(Collections.singletonList(bank));

    bank.setUsersBySkingColor(basicKeyEntityValueMap);

    bankService.createBank(bank);

  }*/

/* JPA - Embeddable KEY */

  @Test
  @Transactional
  @Rollback(false)
  public void mapValueAsEntityTest(){

    Bank bank = createBank();

    FinanceUser whiteJoe = createNewUser();
    FinanceUser blackBob = createNewUser();

    Map<FinanceUserFullName, FinanceUser> basicKeyEntityValueMap = new HashMap<FinanceUserFullName, FinanceUser>();

    FinanceUserFullName whiteJoeFullName = new FinanceUserFullName();
    whiteJoeFullName.setFirst_name("White");
    whiteJoeFullName.setLast_name("Joe");

    FinanceUserFullName blackBobFullName = new FinanceUserFullName();
    blackBobFullName.setFirst_name("Black");
    blackBobFullName.setLast_name("Bob");

    basicKeyEntityValueMap.put(whiteJoeFullName, whiteJoe);
    basicKeyEntityValueMap.put(blackBobFullName, blackBob);

    whiteJoe.setBank(Collections.singletonList(bank));

    bank.setUsersBySkingColor(basicKeyEntityValueMap);

    bankService.createBank(bank);

  }



  @Autowired
  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }

  @Autowired
  public void setTransactionService(TransactionService transactionService) {
    this.transactionService = transactionService;
  }
}
