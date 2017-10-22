package com.springstart.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
public class PersistenceJPAConfig {

  @Bean
  public EntityManagerFactory entityManagerFactory() {
    EntityManagerFactory em = Persistence.createEntityManagerFactory("ifinanceDatabase");
    return em;
  }

  @Bean
  public EntityManager entityManager(){
    return entityManagerFactory().createEntityManager();
  }

}
