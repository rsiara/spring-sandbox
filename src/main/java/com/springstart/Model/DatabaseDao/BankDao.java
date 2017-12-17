package com.springstart.Model.DatabaseDao;

import com.springstart.Model.Entity.DatabaseEntity.Bank;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class BankDao {

  EntityManager entityManager;

  public EntityManager getEntityManager() {
    return entityManager;
  }

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public Bank save(Bank bank) {
    entityManager.persist(bank);
    return bank;
  }
}
