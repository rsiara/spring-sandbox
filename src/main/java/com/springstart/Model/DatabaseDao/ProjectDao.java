package com.springstart.Model.DatabaseDao;

import com.springstart.Model.Entity.DatabaseEntity.Project;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ProjectDao {

    EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Project save(Project project) {
        entityManager.persist(project);
        return project;
    }

    public Project getById(Long id) {
        return entityManager.find(Project.class, id);
    }
}
