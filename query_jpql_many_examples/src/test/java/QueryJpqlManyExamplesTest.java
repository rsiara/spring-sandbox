import configuration.RootConfig;

import model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/*


 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryJpqlManyExamplesTest {


    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    Date today = new Date();
    Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
    Date todayplustwo = new Date(today.getTime() + (1000 * 60 * 60 * 24) * 2);
    Date todayplustree = new Date(today.getTime() + (1000 * 60 * 60 * 24) * 3);
    Date todayplusfour = new Date(today.getTime() + (1000 * 60 * 60 * 24) * 4);
    Date todayplusfive = new Date(today.getTime() + (1000 * 60 * 60 * 24) * 5);


    @Before
    public void init() {
        System.out.println("BEFORE TESTS");
    }

    @Test
    @Transactional
    @Rollback(false)
    public void query_jpql_many_examples_test() {

        prepare_data();

        //language=JPAQL
        executeAndPrintQuery("SELECT e FROM Employee e");

        //language=JPAQL
        executeAndPrintQuery("SELECT d FROM Department d");

    /*    The keyword OBJECT can be used to indicate that the result type of the query is the entity bound to the
        identification variable. It has no impact on the query, but it can be used as a visual clue.
        SELECT OBJECT(d)
                FROM Department d
        The only problem with using OBJECT is that even though path expressions can resolve to an entity type,
        the syntax of the OBJECT keyword is limited to identification variables. The expression OBJECT(e.department)
        is illegal even though Department is an entity type. For that reason, we do not recommend the OBJECT syntax.*/
        //language=JPAQL
        executeAndPrintQuery("SELECT OBJECT(d) FROM Department d");

        //language=JPAQL
        executeAndPrintQuery("SELECT e.name FROM Employee e");

        //language=JPAQL
        executeAndPrintQuery("SELECT e.department FROM Employee e");

        //language=JPAQL
        executeAndPrintQuery("SELECT DISTINCT e.department FROM Employee e");

        //language=JPAQL
        executeAndPrintQuery("SELECT d.employees FROM Department d");

        //language=JPAQL
        executeAndPrintQuery("SELECT e.name, e.salary FROM Employee e");

        //language=JPAQL
        executeAndPrintQuery("SELECT NEW model.EmployeeDetails(e.name, e.salary, e.department.name) FROM Employee e");

        //language=JPAQL
        executeAndPrintQuery("SELECT p FROM Project p WHERE p.employees IS NOT EMPTY");

        //language=JPAQL
        executeAndPrintQuery("SELECT p FROM Employee e JOIN e.phones p");


        /*EJBQL as defined by the EJB 2.0 and EJB 2.1 specifications used a special operator IN in the FROM clause to
        map collection associations to identification variables. Support for this operator was carried over to JP QL.
                The equivalent form of the query used earlier in this section might be specified as:
        SELECT DISTINCT p
        FROM Employee e, IN(e.phones) p
        The IN operator is intended to indicate that the variable p is an enumeration of the phones collection. The JOIN
        operator is a more powerful and expressive way to declare relationships and is the recommended operator
        for queries.*/
        //language=JPAQL
        executeAndPrintQuery("SELECT DISTINCT p FROM Employee e, IN(e.phones) p");

        //language=JPAQL
        executeAndPrintQuery("SELECT p.number FROM Employee e JOIN e.phones p");

        //language=JPAQL
        executeAndPrintQuery("SELECT d  FROM Employee e JOIN e.department d");

        //language=JPAQL
        executeAndPrintQuery("SELECT e.department  FROM Employee e");

        //language=JPAQL
        executeAndPrintQuery("SELECT DISTINCT e.department FROM Project p JOIN p.employees e");

        //language=JPAQL
        executeAndPrintQuery("SELECT DISTINCT e.department FROM Project p JOIN p.employees e " +
                "WHERE p.name = 'Design project of website' AND e.address.state = 'Pomorskie'");

        //language=JPAQL
        executeAndPrintQuery("SELECT DISTINCT d FROM Project p " +
                "JOIN p.employees e " +
                "JOIN e.department d " +
                "JOIN e.address a " +
                "WHERE p.name = 'Design project of website' AND e.address.state = 'Pomorskie'");

        /*Join zrobiony za pomoca klauzuli WHERE*/
        //language=JPAQL
        executeAndPrintQuery("SELECT DISTINCT d " +
                "FROM Department d, Employee  e " +
                "WHERE d = e.department");

        //language=JPAQL
        executeAndPrintQuery("SELECT d, e " +
                "FROM Department d, Employee  e " +
                "WHERE d = e.department AND e.directs IS NOT EMPTY");

        //language=JPAQL
        executeAndPrintQuery("SELECT DISTINCT p " +
                "FROM Department d " +
                "JOIN d.employees e " +
                "JOIN e.projects p");

        //language=JPAQL
        executeAndPrintQuery("SELECT e " +
                "FROM Employee e " +
                "JOIN e.address ");

        /*Fetch joins are intended to help application designers optimize their database access and prepare query results for
        detachment. They allow queries to specify one or more relationships that should be navigated and prefetched by the
        query engine so that they are not lazy loaded later at runtime.
        For example, if we have an Employee entity with a lazy loading relationship to its address, the following query can
        be used to indicate that the relationship should be resolved eagerly during query execution:*/
        //language=JPAQL
        executeAndPrintQuery("SELECT e " +
                "FROM Employee e " +
                "JOIN FETCH e.address ");

        //language=JPAQL
        executeAndPrintQuery("SELECT e, a " +
                "FROM Employee e " +
                "JOIN e.address a ");

        //language=JPAQL
        executeAndPrintQuery("SELECT d " +
                "FROM Department d " +
                "LEFT JOIN FETCH d.employees ");

        //language=JPAQL
        executeAndPrintQuery("SELECT d, e " +
                "FROM Department d " +
                "LEFT JOIN d.employees e ");

        //language=JPAQL
        executeAndPrintQuery("SELECT e " +
                "FROM Employee e " +
                "WHERE e.salary BETWEEN 2000 AND 4000");

        //language=JPAQL
        executeAndPrintQuery("SELECT e " +
                "FROM Employee e " +
                "WHERE e.salary >= 2000 AND e.salary <= 4000");

        //language=JPAQL
        executeAndPrintQuery("SELECT d " +
                "FROM Department d " +
                "WHERE d.name LIKE '%_-_Web%'");

        /*If the pattern string contains an underscore or percent sign that should be literally matched, the ESCAPE clause
        can be used to specify a character that, when prefixing a wildcard character, indicates that it should be treated literally:*/
        //language=JPAQL
        executeAndPrintQuery("SELECT d " +
                "FROM Department d " +
                "WHERE d.name LIKE 'IT\\_Board' ESCAPE '\\'");

        //language=JPAQL
        executeAndPrintQuery("SELECT e " +
                "FROM Employee e " +
                "WHERE e.salary = (SELECT MAX(e2.salary) FROM Employee  e2)");

        //language=JPAQL
        executeAndPrintQuery("SELECT e " +
                "FROM Employee e " +
                "WHERE EXISTS (SELECT p FROM Phone p WHERE p.employee = e AND p.type = 'Mobile')");

        //language=JPAQL
        executeAndPrintQuery("SELECT e " +
                "FROM Employee e " +
                "WHERE EXISTS (SELECT p FROM e.phones p WHERE p.type = 'Mobile')");

        /*The IN expression can be used to check whether a single-valued path expression is a member of a collection.
        The collection can be defined inline as a set of literal values or can be derived from a subquery. The following
        query demonstrates the literal notation by selecting all the employees who live in New York or California:*/
        //language=JPAQL
        executeAndPrintQuery("SELECT e " +
                "FROM Employee e " +
                "WHERE e.address.state IN ('Pomorskie', 'Mazowieckie')");

        //language=JPAQL
        executeAndPrintQuery("SELECT e " +
                "FROM Employee e " +
                "WHERE e.department IN " +
                "(SELECT DISTINCT d " +
                "FROM Department d " +
                "JOIN d.employees de " +
                "JOIN de.projects p " +
                "WHERE p.name LIKE 'Design%')");

        //language=JPAQL
        executeAndPrintQuery("SELECT p " +
                "FROM Phone p " +
                "WHERE p.type NOT IN ('Office', 'Home')");

        //language=JPAQL
        executeAndPrintQuery("SELECT e " +
                "FROM Employee e " +
                "WHERE e.directs IS NOT EMPTY");

        //language=JPAQL
        executeAndPrintQuery("SELECT m " +
                "FROM Employee m " +
                "WHERE (SELECT COUNT(e) " +
                "FROM Employee e " +
                "WHERE e.manager = m) > 0");

        //language=JPAQL
        executeAndPrintQuery("SELECT e " +
                "FROM Employee e " +
                "WHERE e MEMBER OF e.directs");

        //language=JPAQL
        executeAndPrintQuery("SELECT e " +
                "FROM Employee e " +
                "WHERE NOT EXISTS " +
                "(SELECT p " +
                "FROM e.phones p " +
                "WHERE p.type = 'Mobile')");

        /*Example: Manager ktory zarabia wiecej od kazdego ze swoich pracownikow*/
        //language=JPAQL
        executeAndPrintQuery("SELECT e " +
                "FROM Employee e " +
                "WHERE e.directs IS NOT EMPTY AND " +
                "e.salary > ALL " +
                "(SELECT d.salary " +
                "FROM e.directs d)");

        //language=JPAQL
        executeAndPrintQuery("SELECT d " +
                "FROM Department d " +
                "WHERE SIZE(d.employees) = 2");

        //language=JPAQL
        executeAndPrintQuery("SELECT d " +
                "FROM Department d " +
                "WHERE " +
                "(SELECT COUNT(e) " +
                "FROM d.employees e) = 2");

        //language=JPAQL
        executeAndPrintQuery("SELECT e " +
                "FROM Employee e " +
                "ORDER BY e.name DESC");

        //language=JPAQL
        executeAndPrintQuery("SELECT e " +
                "FROM Employee e " +
                "JOIN e.department d " +
                "ORDER BY d.name, e.name DESC");

        //language=JPAQL
        executeAndPrintQuery("SELECT e.name " +
                "FROM Employee e " +
                "ORDER BY e.salary DESC");

        /*A subclass attribute could be accessed directly if the query ranged over only the subclass entities,
        but when the query ranges over a superclass, downcasting must be used.*/
        //language=JPAQL
        executeAndPrintQuery("SELECT e, q.name, q.qa_rating " +
                "FROM Employee e JOIN TREAT(e.projects AS QualityProject) q " +
                "WHERE q.qa_rating > 4");

        //language=JPAQL
        executeAndPrintQuery("SELECT AVG(e.salary) " +
                "FROM Employee e");

        //language=JPAQL
        executeAndPrintQuery("SELECT d.name, AVG(e.salary) " +
                "FROM Department d " +
                "JOIN d.employees e " +
                "GROUP BY d.name");

        //language=JPAQL
        executeAndPrintQuery("SELECT d.name, AVG(e.salary) " +
                "FROM Department d " +
                "JOIN d.employees e " +
                "WHERE e.directs IS EMPTY " +
                "GROUP BY d.name");

        //language=JPAQL
        executeAndPrintQuery("SELECT d.name, AVG(e.salary) " +
                "FROM Department d " +
                "JOIN d.employees e " +
                "WHERE e.directs IS EMPTY " +
                "GROUP BY d.name " +
                "HAVING AVG(e.salary) > 2500");

        //language=JPAQL
        executeAndPrintQuery("SELECT d.name, e.salary " +
                "FROM Department d " +
                "JOIN d.employees e " +
                "WHERE e.directs IS EMPTY ");

        //language=JPAQL
        executeAndPrintQuery("SELECT e, COUNT(p), COUNT(DISTINCT  p.type) " +
                "FROM Employee e " +
                "JOIN e.phones p " +
                "GROUP BY e");

        //language=JPAQL
        executeAndPrintQuery("SELECT e, COUNT(p), COUNT(DISTINCT  p.type) " +
                "FROM Employee e " +
                "JOIN e.phones p " +
                "GROUP BY e");

        //language=JPAQL
        executeAndPrintQuery("SELECT d.name, COUNT(e) " +
                "FROM Department d " +
                "JOIN d.employees e " +
                "GROUP BY d.name");

        //language=JPAQL
        executeAndPrintQuery("SELECT d.name, COUNT(e), AVG(e.salary) " +
                "FROM Department d " +
                "JOIN d.employees e " +
                "GROUP BY d.name");

        //language=JPAQL
        executeAndPrintQuery("SELECT d.name, e.salary, COUNT(p) " +
                "FROM Department d " +
                "JOIN d.employees e " +
                "JOIN e.projects p " +
                "GROUP BY d.name, e.salary");

        //language=JPAQL
        executeAndPrintQuery("SELECT COUNT(e), AVG(e.salary) " +
                "FROM Employee e");

        //language=JPAQL
        executeAndPrintQuery("SELECT e, COUNT(p) " +
                "FROM Employee e " +
                "JOIN e.projects p " +
                "GROUP BY e " +
                "HAVING COUNT(p) >= 2");

        //language=JPAQL
        executeAndPrintDMLQuery("UPDATE Employee e " +
                "SET e.salary = 2999 " +
                "WHERE e.salary = 2800");

        //language=JPAQL
        executeAndPrintDMLQuery("UPDATE Employee e " +
                "SET e.salary = e.salary + 4000 " +
                "WHERE EXISTS (" +
                "SELECT p " +
                "FROM e.projects p " +
                "WHERE p.name = 'Design project of website')");

        /*Przyklad Updatu dwoch kolumn na raza*/

        /*CONCAT - String Concatenation

        The CONCAT(str1, str2, ...) function returns the concatenation of the specified strings.

        For example:

        CONCAT('Serbia', ' and ', 'Montenegro') is evaluated to 'Serbia and Montenegro'.*/


        /*SUBSTRING - Getting a Portion of a String

        The SUBSTRING(str, pos [, length]) function returns a substring of a specified string.

        For example:

        SUBSTRING('Italy', 3) is evaluated to 'aly'.
                SUBSTRING('Italy', 3, 2) is evaluated to 'al'.

                Notice that positions are one-based (as in SQL) rather
        then zero based (as in Java). If length is not specified (the third optional argument),
        the entire string suffix, starting at the specified position, is returned.*/

        /*LOCATE - Locating Substrings

        The LOCATE(str, substr [, start]) function searches a substring and returns its position.

        For example:

        LOCATE('India', 'a') is evaluated to 5.
        LOCATE('Japan', 'a', 3) is evaluated to 4.

        LOCATE('Mexico', 'a') is evaluated to 0.

        Notice that positions are one-based (as in SQL) rather then zero-based (as in Java). Therefore, the position of the first character is 1. Zero (0) is returned if the substring is not found.

                The third argument (when present) specifies from which position to start the search.*/


        //language=JPAQL
        executeAndPrintDMLQuery("UPDATE Phone p " +
                "SET p.number = CONCAT('288', SUBSTRING(p.number, LOCATE('-',p.number), 4)), " +
                "p.type = 'Mobile' " +
                "WHERE  p.type = 'Home'");

        //language=JPAQL
        executeAndPrintDMLQuery("DELETE " +
                "FROM Employee e " +
                "WHERE e.department IS NULL");


    }


    private void prepare_data() {

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
        bob.setStartDate(todayplustwo);
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
        mike.setStartDate(todayplustwo);
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


    private void executeAndPrintQuery(String queryString) {
        try {
            Query query = entityManager.createQuery(queryString);
            printQueryResult(queryString, query.getResultList());
        } finally {
            entityManager.close();
        }
    }

    private void executeAndPrintDMLQuery(String queryString) {
        try {
            Query query = entityManager.createQuery(queryString);
            query.executeUpdate();
        } finally {
            entityManager.close();
        }
    }

    private void printQueryResult(String queryString, List resultList) {
        System.out.println("\n + QUERY: " + queryString);
        if (resultList.isEmpty()) {
            System.out.println("  -> No results Found");
        } else {
            System.out.println();
            for (Object o : resultList) {
                System.out.println("  -> " + resultAsString(o));
            }
        }
        System.out.println();
    }

    private String resultAsString(Object o) {
        if (o instanceof Object[]) {
            return Arrays.asList((Object[]) o).toString();
        } else {
            return String.valueOf(o);
        }
    }

}