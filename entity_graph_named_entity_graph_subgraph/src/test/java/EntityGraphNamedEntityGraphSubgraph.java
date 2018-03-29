import configuration.RootConfig;
import model.Address;
import model.Department;
import model.DesignProject;
import model.Employee;
import model.Phone;
import model.QualityProject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/*
If a fetch graph is used, only the attributes specified by the entity graph will be treated as FetchType.EAGER.
All other attributes will be lazy. If a load graph is used,
attributes that are not specified by the entity graph will keep their default fetch type.

If you're talking about basic types, then, by default,
Hibernate will always fetch them. The only way to enable basic type lazy loading is to use bytecode enhancement,
as explained in this article.

If you're talking about basic types, then, by default, Hibernate will always fetch them.
The only way to enable basic type lazy loading is to use bytecode enhancement, as explained in this article.
If you're talking about EAGER associations, then Hibernate cannot override them to LAZY, even if the JPA standard says it should.

Both 1. and 2. are not mandatory requirements from a JPA perspective because LAZY is just a hint for the JPA provider.

All in all, JPA entity graphs are a suboptimal way of fetching data. Avoiding EAGER associations, using subentities and DTO projections are much better than entity graphs.

Most of the time, you don't even need to fetch entities because entities make sense only if you plan to modify. Otherwise, a DTO projection will always be way more efficient.

https://stackoverflow.com/questions/42211312/how-to-limit-columns-used-in-a-hibernate-entity-graph

 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EntityGraphNamedEntityGraphSubgraph {

    Date today = new Date();
    Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Before
    public void prepareData() {
        // My address

        // Employees
        Employee mark = new Employee();
        mark.setStartDate(today);
        mark.setName("Mark");
        mark.setSalary(12400);

        Address markAddress = new Address();
        markAddress.setCity("Gdanski");
        markAddress.setZip("02-120");
        markAddress.setStreet("Mlynska");
        markAddress.setState("Pomorskie");

        mark.setAddress(markAddress);
        mark.setManager(mark);

        Phone markMobilePhone = new Phone();
        markMobilePhone.setNumber("780 12 23");
        markMobilePhone.setType("Mobile");

        Phone markHomePhone = new Phone();
        markHomePhone.setNumber("+48 22 890 23 12");
        markHomePhone.setType("Home");

        mark.addPhone(markMobilePhone);
        mark.addPhone(markHomePhone);


        Employee john = new Employee();
        john.setStartDate(tomorrow);
        john.setName("John");
        john.setSalary(6400);

        Address johnAddress = new Address();
        johnAddress.setCity("Warsaw");
        johnAddress.setZip("02-495");
        johnAddress.setStreet("Krakowiakow");
        johnAddress.setState("Mazowieckie");

        john.setAddress(johnAddress);
        john.setManager(mark);

        Phone johnMobilePhone = new Phone();
        johnMobilePhone.setNumber("720 22 33");
        johnMobilePhone.setType("Mobile");

        Phone johnHomePhone = new Phone();
        johnHomePhone.setNumber("+48 56 141 23 12");
        johnHomePhone.setType("Home");

        john.addPhone(johnMobilePhone);
        john.addPhone(johnHomePhone);


        Employee bob = new Employee();
        bob.setStartDate(tomorrow);
        bob.setName("Bob");
        bob.setSalary(2800);

        Address bobAddress = new Address();
        bobAddress.setCity("Poznan");
        bobAddress.setZip("01-333");
        bobAddress.setStreet("Mniszka");
        bobAddress.setState("Wielkopolskie");

        bob.setAddress(bobAddress);
        bob.setManager(john);

        Phone bobMobilePhone = new Phone();
        bobMobilePhone.setNumber("710 11 22");
        bobMobilePhone.setType("Mobile");

        Phone bobHomePhone = new Phone();
        bobHomePhone.setNumber("+24 51 142 25 14");
        bobHomePhone.setType("Home");

        bob.addPhone(bobMobilePhone);
        bob.addPhone(bobHomePhone);


        Employee mike = new Employee();
        mike.setStartDate(tomorrow);
        mike.setName("Mike");
        mike.setSalary(3200);

        Address mikeAddress = new Address();
        mikeAddress.setCity("Krakow");
        mikeAddress.setZip("04-888");
        mikeAddress.setStreet("Smocza");
        mikeAddress.setState("Malopolskie");

        mike.setAddress(mikeAddress);
        mike.setManager(john);

    /*  Phone mikeMobilePhone = new Phone();
        mikeMobilePhone.setNumber("515 554 322");
        mikeMobilePhone.setType("Mobile");*/

        Phone mikeHomePhone = new Phone();
        mikeHomePhone.setNumber("+32 22 123 123");
        mikeHomePhone.setType("Home");

        /*    mike.addPhone(mikeMobilePhone);*/
        mike.addPhone(mikeHomePhone);


        mark.addDirect(john);
        mark.addDirect(bob);
        mark.addDirect(mike);

        john.addDirect(bob);
        john.addDirect(mike);


        //Department

        Department executiveDepartment = new Department();
        executiveDepartment.setName("IT_Board");
        mark.setDepartment(executiveDepartment);
        john.setDepartment(executiveDepartment);

        Department itDepartment = new Department();
        itDepartment.setName("IT - Web subdepartment");
        bob.setDepartment(itDepartment);
        mike.setDepartment(itDepartment);


        //Project

        DesignProject designProjectOfWebsite = new DesignProject();
        designProjectOfWebsite.setName("Design project of website");
        designProjectOfWebsite.addEmployee(bob);
        designProjectOfWebsite.addEmployee(mark);

        QualityProject qualityProjectOfWebsite = new QualityProject();
        qualityProjectOfWebsite.setName("Quality project of website");
        qualityProjectOfWebsite.setQaRating(5);
        qualityProjectOfWebsite.addEmployee(mark);
        qualityProjectOfWebsite.addEmployee(john);
        qualityProjectOfWebsite.addEmployee(mike);
        qualityProjectOfWebsite.addEmployee(bob);


        //Persist
        entityManager.persist(markAddress);
        entityManager.persist(johnAddress);
        entityManager.persist(mikeAddress);
        entityManager.persist(bobAddress);

        entityManager.persist(markHomePhone);
        entityManager.persist(markMobilePhone);
        entityManager.persist(johnHomePhone);
        entityManager.persist(johnMobilePhone);
        entityManager.persist(mikeHomePhone);
        /* entityManager.persist(mikeMobilePhone);*/
        entityManager.persist(bobHomePhone);
        entityManager.persist(bobMobilePhone);

        entityManager.persist(executiveDepartment);
        entityManager.persist(itDepartment);

        entityManager.persist(mark);
        entityManager.persist(mike);
        entityManager.persist(john);
        entityManager.persist(bob);

        entityManager.persist(designProjectOfWebsite);
        entityManager.persist(qualityProjectOfWebsite);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void query_advanced_constructor_result_mapping() {
        System.out.println(" *** Entity graphs - named entity graph multiple defs***");

        for (Employee employee : findAllEmployees()) {
            System.out.println(employee);
        }

    }

    public List<Employee> findAllEmployees() {
        return entityManager.createQuery("SELECT e FROM Employee e", Employee.class)
                .getResultList();
    }


}