import configuration.RootConfig;
import model.Address;
import model.Department;
import model.Employee;
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
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.Date;
import java.util.List;

/*

Procedury wbudowane, nie dzialaja bo nie ma ich w bazie danych.

Ponizej dodatkowo przyklady jak mozna stworzyc "NamedStoredProcedureQuery"

a)
    @NamedStoredProcedureQuery(
    name="hello",
    procedureName="hello",
    parameters={
    @StoredProcedureParameter(name="name", type=String.class,
    mode=ParameterMode.INOUT)
    })
b)
    @NamedStoredProcedureQuery(
    name="fetch_emp",
    procedureName="fetch_emp",
    parameters={
    @StoredProcedureParameter(name="empList", type=void.class,
    mode=ParameterMode.REF_CURSOR)
    },
    resultClasses=Employee.class)

c)
    @NameStoredProcedureQuery(
    name="queryEmployeeAndManager",
    procedureName="fetch_emp_and_mgr",
    resultSetMappings = "EmployeeAndManager"
    )
 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryAdvancedStoredProcedure {

    Date today = new Date();
    Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Before
    public void prepareData() {
        // Employees
        Employee mark = new Employee();
        mark.setId(11);
        mark.setName("Mark");
        mark.setSalary(12400);

        Address markAddress = new Address();
        markAddress.setCity("Gdanski");
        markAddress.setZip("02-120");
        markAddress.setStreet("Mlynska");
        markAddress.setState("Pomorskie");

        mark.setAddress(markAddress);
        mark.setManager(mark);


        Employee john = new Employee();
        john.setId(12);
        john.setName("John");
        john.setSalary(6400);

        Address johnAddress = new Address();
        johnAddress.setCity("Warsaw");
        johnAddress.setZip("02-495");
        johnAddress.setStreet("Krakowiakow");
        johnAddress.setState("Mazowieckie");

        john.setAddress(johnAddress);
        john.setManager(mark);

        Employee bob = new Employee();
        bob.setName("Bob");
        bob.setSalary(2800);

        Address bobAddress = new Address();
        bobAddress.setCity("Poznan");
        bobAddress.setZip("01-333");
        bobAddress.setStreet("Mniszka");
        bobAddress.setState("Wielkopolskie");

        bob.setAddress(bobAddress);
        bob.setManager(john);


        Employee mike = new Employee();
        mike.setId(13);
        mike.setName("Mike");
        mike.setSalary(3200);

        Address mikeAddress = new Address();
        mikeAddress.setCity("Krakow");
        mikeAddress.setZip("04-888");
        mikeAddress.setStreet("Smocza");
        mikeAddress.setState("Malopolskie");

        mike.setAddress(mikeAddress);
        mike.setManager(john);

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


        entityManager.persist(john);
        entityManager.persist(mark);
        entityManager.flush();
    }

    @Test
    @Transactional
    @Rollback(false)
    public void query_advanced_constructor_result_mapping() {
        System.out.println(" *** Advanced queries - invoke stored procedure***");

        invokeStoredProcedure();
        findAllEmployees();
    }

    public String invokeStoredProcedure() {
        StoredProcedureQuery q = entityManager.createStoredProcedureQuery("hello");
        q.registerStoredProcedureParameter("name", String.class, ParameterMode.INOUT);
        q.execute();
        return (String) q.getOutputParameterValue("name");
    }

    public List findAllEmployees() {
        List result = null;
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("readEmps", Employee.class);
        query.registerStoredProcedureParameter("p1", Integer.class, ParameterMode.IN);
        query.setParameter("p1", 5);
        boolean r = query.execute();
        System.out.println("************************* STORED PROC: execute returned: " + r + " ************************");
        r = query.hasMoreResults();
        System.out.println("************************* hasMoreResults: " + r + " *************************");
        if (r) {
            result = query.getResultList();
            System.out.println("************************* Results: " + result + " *************************");
        }
        r = query.hasMoreResults();
        System.out.println("************************* hasMoreResults: " + r + " *************************");
        return result;
    }


}