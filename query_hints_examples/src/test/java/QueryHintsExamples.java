import configuration.RootConfig;

import model.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;


/*
JPA Hints

Let’s start with the hints defined by the JPA specification:
1. javax.persistence.lock.timeout (Long – milliseconds)

This hint defines the timeout in milliseconds to acquire a pessitimistic lock.
2. javax.persistence.query.timeout (Long – milliseconds)

The javax.persistence.query.timeout hint defines how long a query is allowed to run before it gets canceled. Hibernate doesn’t handle this timeout itself but provides it to the JDBC driver via the JDBC Statement.setTimeout method.
3. javax.persistence.cache.retrieveMode (CacheRetrieveMode – USE | BYPASS)

The retrieveMode hint supports the values USE and BYPASS and tells Hibernate if it shall USE the second-level cache to retrieve an entity or if it shall BYPASS it and get it directly from the database.
4. javax.persistence.cache.storeMode (CacheStoreMode – USE | BYPASS | REFRESH)

This hint defines how Hibernate shall write changed entities to the second-level cache. It can either USE the cache to add entities to the cache and updated existing ones, or BYPASS it for entities that are not already stored in the cache and only update the existing ones or REFRESH the entities located in the cache before they get retrieved from it.
5. javax.persistence.loadgraph (EntityGraph)

The javax.persistence.loadgraph hint allows you to provide an entity graph as a load graph to the query to define eager fetching specifically for this query.

You can read more about entity graphs in JPA 2.1 Entity Graph – Part 1: Named entity graphs and JPA 2.1 Entity Graph – Part 2: Define lazy/eager loading at runtime.
6. javax.persistence.fetchgraph (EntityGraph)

You can use this hint to provide an entity graph as a fetchgraph to a query.

 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryHintsExamples {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void query_hints_examples_test() {

        Employee john = new Employee();
        john.setName("John");
        john.setSalary(2800);

        entityManager.persist(john);

        john = findEmployeeById(john.getId());
        System.out.println("1. Employee: " + john);

        john = findEmployeeByIdSecond(john.getId());
        System.out.println("2. Employee: " + john);
    }

    public Employee findEmployeeById(int empId) {
        TypedQuery<Employee> q = entityManager.createNamedQuery("findEmployeeById", Employee.class);
        q.setParameter(1, empId);
        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Employee findEmployeeByIdSecond(int empId) {
        TypedQuery<Employee> q = entityManager.createQuery("SELECT e FROM Employee e WHERE e.id = ?1", Employee.class);
        // force read from database
        q.setHint("javax.persistence.query.timeout", 10);
        q.setParameter(1, empId);
        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}