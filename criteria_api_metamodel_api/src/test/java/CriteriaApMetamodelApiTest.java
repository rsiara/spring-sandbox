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

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/*

 ***   METAMODEL API   ***
 *
 *
 * How genereate metamodel:
 * 1. Add to maven dependency: <artifactId>hibernate-jpamodelgen</artifactId>
 * 2. Go to InteliJ and for particular module/project enable Annotation Processors
 * a) Settings -> Build, Execution, Deployment -> Annotation Processors
 * b) Check Enable annotation processing checkbox
 * c) In "Store generated sources relative to:" select Module content root.
 * 3. Rebuild Project (dependent on your chose you find canonical model for example in target directory)
 * 4. Mark directory with canonical as "Generated Sources Root"

 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CriteriaApMetamodelApiTest {

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
        prepare_data();
    }

    @Test
    @Transactional
    @Rollback(false)
    public void criteria_api_basic_queries_main_test() {

        get_entity_information_from_metamodel();

        canonical_metamodel();

        query_using_string_based_query_without_metamodel();

        query_using_generated_canonical_metamodel();

    }

    @Test
    @Transactional
    @Rollback(false)
    public void get_entity_information_from_metamodel() {
        System.out.println(" *** Get entity information from metamodel *** ");

        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<Employee> emp_ = metamodel.entity(Employee.class);

        for (Attribute<? super Employee, ?> attribute : emp_.getAttributes()) {
            System.out.println(
                    attribute.getName() + " " +
                            attribute.getJavaType().getName() + " " +
                            attribute.getPersistentAttributeType());
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void canonical_metamodel() {
        System.out.println(" *** Using canonical metamodel *** ");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<Employee> emp = criteriaQuery.from(Employee.class);

        criteriaQuery.select(emp.get(Employee_.name));

        //Execute query
        List<Object> resultList = entityManager.createQuery(criteriaQuery).getResultList();

        //Print result
        for (Object singleResult : resultList) {
            System.out.println(singleResult);
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void query_using_string_based_query_without_metamodel() {
        System.out.println(" *** Using ordinary method with string based query WITHOUT metamodel *** ");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<Department> departmentRoot = subquery.from(Department.class);

        Join<Employee, Project> employeeProjectJoin = departmentRoot.join("employees").join("projects");

        subquery.select(departmentRoot.<Integer>get("id"))
                .distinct(true)
                .where(criteriaBuilder.like(employeeProjectJoin.<String>get("name"), "Small%"));

        criteriaQuery.select(employeeRoot)
                .where(criteriaBuilder.in(employeeRoot.get("dept").get("id")).value(subquery));


        //Execute query
        List<Employee> resultList = entityManager.createQuery(criteriaQuery).getResultList();

        //Print result
        for (Object singleResult : resultList) {
            System.out.println(singleResult);
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void query_using_generated_canonical_metamodel() {
        System.out.println(" *** Using generated canonical metamodel *** ");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<Department> departmentRoot = subquery.from(Department.class);

        Join<Employee, Project> employeeProjectJoin = departmentRoot.join(Department_.employees).join(Employee_.projects);

        subquery.select(departmentRoot.get(Department_.id))
                .distinct(true)
                .where(criteriaBuilder.like(employeeProjectJoin.get(Project_.name), "Small%"));

        criteriaQuery.select(employeeRoot)
                .where(criteriaBuilder.in(employeeRoot.get(Employee_.dept).get(Department_.id)).value(subquery));


        //Execute query
        List<Employee> resultList = entityManager.createQuery(criteriaQuery).getResultList();

        //Print result
        for (Object singleResult : resultList) {
            System.out.println(singleResult);
        }
    }


    private void prepare_data() {

        // Employees
        Employee mark = new Employee();
        mark.setStartDate(today);
        mark.setName("Mark");
        mark.setSalary(12400);

        Address markAddress = new Address();
        markAddress.setCity("Gdansk");
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
        mark.setDept(executiveDepartment);
        john.setDept(executiveDepartment);

        Department itDepartment = new Department();
        itDepartment.setName("IT - Web subdepartment");
        bob.setDept(itDepartment);
        mike.setDept(itDepartment);

        // Project

        Project bigProject = new Project();
        bigProject.setName("Big Project");
        bigProject.addEmployee(mark);
        bigProject.addEmployee(john);

        Project smallProject = new Project();
        smallProject.setName("Small Project");
        smallProject.addEmployee(bob);
        smallProject.addEmployee(mike);

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

        entityManager.persist(bigProject);
        entityManager.persist(smallProject);

        entityManager.flush();

    }


}