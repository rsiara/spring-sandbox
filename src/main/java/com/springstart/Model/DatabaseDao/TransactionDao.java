package com.springstart.Model.DatabaseDao;

import com.springstart.Model.Entity.DatabaseEntity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class TransactionDao {
    EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Transaction save(Transaction transaction) {
        entityManager.persist(transaction);
        return transaction;
    }
}
