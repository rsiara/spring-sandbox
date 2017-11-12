package com.springstart.Model.DatabaseDao;

import com.springstart.Model.Entity.DatabaseEntity.FinanceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class FinanceUserDao {


  EntityManager entityManager;

  public FinanceUser createFinanceUser(FinanceUser financeUser) {
//    entityManager.getTransaction().begin();
    entityManager.persist(financeUser);
 //   entityManager.getTransaction().commit();
    return financeUser;
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }


  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

    public List<FinanceUser> findAll() {
     TypedQuery<FinanceUser> query = entityManager.createNamedQuery("FinanceUser.findAll", FinanceUser.class);
      return query.getResultList();
    }
}
