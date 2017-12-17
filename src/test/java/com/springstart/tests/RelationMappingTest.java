package com.springstart.tests;


import com.springstart.Configuration.RootConfig;
import com.springstart.Model.Entity.DatabaseEntity.*;
import com.springstart.Service.DatabaseService.*;
import org.hibernate.SessionFactory;
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

  private static Transaction createNewBeltPurchase() {
    Transaction beltPurchase = new Transaction();
    beltPurchase.setTitle("New Belt");
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
    account.setCreatedBy("Adam Svarovski");
    account.setInitialBalance(new BigDecimal("50.00"));
    account.setName("Savings Account");
    account.setCurrentBalance(new BigDecimal("100.00"));
    account.setLastUpdatedBy("Kevin Bowersox");
    account.setLastUpdatedDate(new Date());
    account.setCreatedDate(new Date());
    return account;
  }

  private static FinanceUser createNewUser() {
    FinanceUser financeUser = new FinanceUser();
    financeUser.setBirth_date(new Date());
    financeUser.getAddress().setCity("Glasgow");
    financeUser.setCreated_by("Ninja solo");
    financeUser.setEmail_address("finceuser@com.pl");
    financeUser.setCreated_date(new Date());
    financeUser.setFirst_name("John");
    financeUser.setLast_name("Man");
    financeUser.getAddress().setState("NY");
    financeUser.getAddress().setAddressLine1("Nowy adres 1");
    financeUser.getAddress().setAddressLine2("Nowy adres 2");
    financeUser.getAddress().setZipCode("2");
    financeUser.setLast_updated_by("Greg");
    financeUser.setLast_updated_date(new Date());
    return financeUser;
  }

  private static List<Address> createCollectionOfAddresses(int numberOfAddresses){
    List<Address> addressCollection = new ArrayList<Address>();
    for (int i = 0; i < numberOfAddresses; i++) {
      Address address = new Address();
      address.setState("NY");
      address.setCity("New York");
      address.setAddressLine1("Adres numer jeden #"+i);
      address.setAddressLine2("Adres numer dwa #"+i);
      address.setZipCode("2");
      addressCollection.add(address);
    }
    return addressCollection;
  }

  public Bank createBank(){
    Bank bank = new Bank();
    bank.setAddress( createCollectionOfAddresses(1).get(0));
    bank.setCreatedBy("John Bravo");
    bank.setCreatedDate(new Date());
    bank.setInternational(true);
    bank.setLastUpdatedBy("John Bravo");
    bank.setName("The Bank of UNIT TESTS");
    bank.setLastUpdatedDate(new Date());

    return bank;
  }

  private static Project createNewProject() {
    Project project = new Project();
    project.setName("New bridge");
    project.setInvestor("Budimex");
    return project;
  }

  @Test
  @Transactional
  @Rollback(false)
  public void unidirectionalManyToOne() {

    Transaction beltPurchase = createNewBeltPurchase();
    System.out.println("Transaction: " + beltPurchase);
    Account account = createNewAccount();
    System.out.println("Account: " + account);
    Transaction savedTransaction = transactionService.saveTransactionForAccount(beltPurchase, account);
    System.out.println("Saved Transaction: " + savedTransaction);

  }

  @Test
  @Transactional
  @Rollback(false)
  public void unidirectionalOneToOne() {

    FinanceUser financeUser = createNewUser();

    ParkingPlace parkingPlace = new ParkingPlace();
    parkingPlace.setPlaceNumber("2C");

    System.out.println("Parking palce: " + parkingPlace);

    financeUser.setParking_space(parkingPlace);

    System.out.println("FinanceUser: " + financeUser);

    financeUserService.createFinanceUser(financeUser);
  }

  @Test
  @Transactional
  @Rollback(false)
  public void bidirectionalOneToOne() {

    FinanceUser financeUser = createNewUser();

    ParkingPlace parkingPlace = new ParkingPlace();
    parkingPlace.setPlaceNumber("3E");

    financeUser.setParking_space(parkingPlace);
    parkingPlace.setFinanceUser(financeUser);

    System.out.println("FinanceUser: " + financeUser);
    System.out.println("Parking palce: " + parkingPlace);

    financeUserService.createFinanceUser(financeUser);


    System.out.println("Parking place user after save: " + parkingPlace.getFinanceUser());


        /*Get saved non-owning entity */

    ParkingPlace justSavedParkingPlace = parkingPlaceService.getById(parkingPlace.getId());

    System.out.println("Just saved non-owning entity: " + justSavedParkingPlace);
    System.out.println("User from just saved non-owning entity: " + justSavedParkingPlace.getFinanceUser());
  }

  @Test
  @Transactional
  @Rollback(false)
  public void bidirectionalOneToMany() {

    Transaction whiteBeltPurchase = createNewBeltPurchase();
    whiteBeltPurchase.setTitle("White belt");
    Transaction blackBeltPurchase = createNewBeltPurchase();
    blackBeltPurchase.setTitle("Black belt");

    System.out.println("Belt #1: " + whiteBeltPurchase);
    System.out.println("Belt #2: " + blackBeltPurchase);

    Account account = createNewAccount();

    List<Transaction> transactions = new ArrayList<Transaction>();
    transactions.add(whiteBeltPurchase);
    transactions.add(blackBeltPurchase);

    account.setTransaction(transactions);

    System.out.println("Account: " + account);

    accountService.create(account);

    System.out.println("Saved Account: " + account.toString());

    entityManager.flush();
    entityManager.clear();

    /*Get saved non-owning entity */

    Account justSavedAccount = accountService.getById(account.getAccountId());

    System.out.println("Just saved non-owning entity: " + justSavedAccount);
    System.out.println("Transactions from just saved non-owning entity: " + justSavedAccount.getTransactions());
  }

  @Test
  @Transactional
  @Rollback(false)
  public void bidirectionalManyToMany() {

        /*Project collection first*/

    Project projectFirst = createNewProject();
    projectFirst.setName("First Project");
    Project projectSecond = createNewProject();
    projectSecond.setName("Second Project");

    Collection<Project> projectCollectionFirst = new ArrayList<Project>();
    projectCollectionFirst.add(projectFirst);
    projectCollectionFirst.add(projectSecond);

    System.out.println("Project collection first: " + projectCollectionFirst);



        /*Project collection second*/

    projectSecond.setName("Second Project");

    Collection<Project> projectCollectionSecond = new ArrayList<Project>();
    projectCollectionSecond.add(createNewProject());
    projectCollectionSecond.add(createNewProject());
    projectCollectionSecond.add(createNewProject());

    System.out.println("Project collec  tion second: " + projectCollectionSecond);



        /*Finance users*/

    FinanceUser financeUserFirst = createNewUser();
    financeUserFirst.setFirst_name("FirstUser");
    FinanceUser financeUserSecond = createNewUser();
    financeUserSecond.setFirst_name("SecondUser");

    Collection<FinanceUser> financeUserCollection = new ArrayList<FinanceUser>();
    financeUserCollection.add(financeUserFirst);
    financeUserCollection.add(financeUserSecond);


    System.out.println("FinanceUser collection: " + financeUserCollection);

    financeUserFirst.setProjects(projectCollectionFirst);
    financeUserSecond.setProjects(projectCollectionSecond);

    financeUserService.createFinanceUser(financeUserFirst);
    financeUserService.createFinanceUser(financeUserSecond);
         /*Get saved non-owning entity */


    System.out.println("Get just saved first user projects: " + financeUserService.getById(financeUserFirst.getUser_id()).getProjects());
    System.out.println("Get just saved second user projects: " + financeUserService.getById(financeUserSecond.getUser_id()).getProjects());

    System.out.println("Get users form project: " + projectService.getById(projectFirst.getId()).getFinanceUsers());


  }

  @Test
  @Transactional
  @Rollback(false)
  public void embeddableTypeTest(){
    FinanceUser financeUser = createNewUser();

    Address embeddedAddress = new Address();
    embeddedAddress.setState("MA");
    embeddedAddress.setAddressLine1("Embeded address line1");
    embeddedAddress.setAddressLine2("Embeded address line2");
    embeddedAddress.setCity("New York");
    embeddedAddress.setZipCode("3");

    financeUser.setAddress(embeddedAddress);

    financeUserService.createFinanceUser(financeUser);
  }

  @Test
  @Transactional
  @Rollback(false)
  public void collectionOfEmbeddableTest(){
    FinanceUser financeUser = createNewUser();

    List<Address> loverAddresses = new ArrayList<Address>();
    loverAddresses.addAll(createCollectionOfAddresses(4));

    financeUser.setLoverAddresses(loverAddresses);

    System.out.println(financeUser.toString());

    FinanceUser savedFinanceUser = financeUserService.createFinanceUser(financeUser);
    System.out.println(savedFinanceUser);

  }


  @Test
  @Transactional
  @Rollback(false)
  public void collectionOfBasicsTest() {

    Bank bank = createBank();

    List<String> contacts = new ArrayList<String>();
    contacts.add("Contact one");
    contacts.add("Contact two");
    contacts.add("Contact three");
    contacts.add("Contact four");

    bank.setContacts(contacts);

    bankService.createBank(bank);
  }



  @Test
  @Transactional
  @Rollback(false)
  public void criteriaTest() {

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    javax.persistence.criteria.CriteriaQuery<Account> query = cb.createQuery(Account.class);
    Root<Account> root = query.from(Account.class);

    TypedQuery<Account> tq = entityManager.createQuery(query);
    System.out.println(tq.getResultList().size());
  }

  @Test
  @Transactional
  @Rollback(false)
  public void getProjectById() {

    System.out.println("Get users form project: " + projectService.getById(new Long(154)).getFinanceUsers());

  }

  @Test
  @Transactional
  @Rollback(false)
  public void getUserByID() {

    System.out.println("Get user by ID " + financeUserService.getById(154));

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
