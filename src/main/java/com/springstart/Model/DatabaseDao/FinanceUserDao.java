package com.springstart.Model.DatabaseDao;

import com.springstart.Model.Entity.DatabaseEntity.FinanceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class FinanceUserDao {

  EntityManager entityManager;

  public FinanceUser createFinanceUser(FinanceUser financeUser) {
    entityManager.getTransaction().begin();
    entityManager.persist(financeUser);
    entityManager.getTransaction().commit();
    return financeUser;
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }

  @PersistenceContext(unitName = "ifinanceDatabase")
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
}
