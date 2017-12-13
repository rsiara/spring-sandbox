package com.springstart.Model.DatabaseDao;


import com.springstart.Model.Entity.DatabaseEntity.ParkingPlace;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ParkingPlaceDao {

    EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(ParkingPlace parkingPlace){
        entityManager.persist(parkingPlace);
    }

    public ParkingPlace get(Integer id){
        return entityManager.find(ParkingPlace.class, id);
    }
}
