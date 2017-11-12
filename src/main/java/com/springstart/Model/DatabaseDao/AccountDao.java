package com.springstart.Model.DatabaseDao;

import com.springstart.Model.Entity.DatabaseEntity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AccountDao {

    EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public Account save(Account account) {
        entityManager.persist(account);
        return account;
    }
}
