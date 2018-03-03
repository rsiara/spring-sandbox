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
import javax.transaction.Transactional;
import java.util.*;

/*

CRITERIA API

 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CriteriaApiBasicQueriesTest {

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

        predicates_using_conjuction_disjuntion();

        subquery_example_one();

        subquery_example_two();

        subquery_example_three();

        subquery_example_four();

        in_expression_one();

        in_expression_shortcut_version();

        in_expression_and_subuery();

        case_expression();

        case_expression_two();

        treat_expression_for_downcasting_join();

        treat_expression_for_downcasting_path();

        subquery_sandbox();

        join_simple();

        join_outer();

        order_by_expression();

        update_bulk_query();

        delete_query();

        group_by_and_having_clause();

        join_cascade();

        join_fetch();

        using_aliases();

        select_single_column();

        select_multiple_column_by_tuple_query_and_tuple_result();

        select_multiple_column_by_multiselect_and_array_of_object_result();

        select_multiple_column_by_multiselect_and_object_construction_in_result();

        select_multiple_column_by_select_and_object_construction_using_construct_method_result();

        select_multiple_column_by_multiselect_and_tuple_result();

        path_expression();

        query_roots();

        select();

        conjuction();

        conjuction_using_add();

        complex_query_find_employee();
    }

    @Test
    @Transactional
    @Rollback(false)
    public void predicates_using_conjuction_disjuntion() {
        System.out.println(" *** Predicate using conjuction disjuntion *** ");

        //Parameters
        String name = "Mark";
        String departmentName = "IT_Board";
        String projectName = "Big Project";
        String city = "Gdansk";

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //As parameter of createQuery set expected return type
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
        Join<Employee, Project> projectJoin = employeeRoot.join("projects");

        criteriaQuery.multiselect(employeeRoot.get("name"), projectJoin.get("name"));

        Predicate criteria = criteriaBuilder.conjunction();
        ParameterExpression<String> parameterExpression = criteriaBuilder.parameter(String.class, "name");
        criteria = criteriaBuilder.and(criteria, criteriaBuilder.equal(employeeRoot.get("name"), parameterExpression));

        parameterExpression = criteriaBuilder.parameter(String.class, "departmentName");
        criteria = criteriaBuilder.and(criteria, criteriaBuilder.equal(employeeRoot.get("dept").get("name"), parameterExpression));

        parameterExpression = criteriaBuilder.parameter(String.class, "project");
        criteria = criteriaBuilder.and(criteria, criteriaBuilder.equal(projectJoin.get("name"), parameterExpression));

        parameterExpression = criteriaBuilder.parameter(String.class, "city");
        criteria = criteriaBuilder.and(criteria, criteriaBuilder.equal(employeeRoot.get("address").get("city"), parameterExpression));

        criteriaQuery.where(criteria);

        //Execute query
        List<Object[]> resultArray = entityManager.createQuery(criteriaQuery)
                .setParameter("name", name)
                .setParameter("departmentName", departmentName)
                .setParameter("project", projectName)
                .setParameter("city", city)
                .getResultList();

        //Print result
        for (Object[] singleRow : resultArray) {
            for (Object singleColumn : singleRow) {
                System.out.print(" " + singleColumn + " ");
            }
            System.out.println();
        }
    }

    /*
    SELECT e
    FROM Employee e
    WHERE e IN
        (SELECT emp
            FROM Project p
            JOIN p.employees emp
            WHERE p.name = :project)
    */

    @Test
    @Transactional
    @Rollback(false)
    public void subquery_example_one() {
        System.out.println(" *** Subquery example one *** ");

        //Parameters
        String projectName = "Big Project";


        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Parameter of criateQuery is result type
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        criteriaQuery.select(employeeRoot);

        List<Predicate> criteria = new ArrayList<Predicate>();
        //Subquery
        Subquery<Employee> subquery = criteriaQuery.subquery(Employee.class);
        Root<Project> subqueryProjectRoot = subquery.from(Project.class);
        Join<Project, Employee> projectEmployeeJoin = subqueryProjectRoot.join("employees");

        subquery.select(projectEmployeeJoin)
                .where(criteriaBuilder.equal(subqueryProjectRoot.get("name"),
                        criteriaBuilder.parameter(String.class, "projectName")));


        criteria.add(criteriaBuilder.in(employeeRoot).value(subquery));

        criteriaQuery.where(criteriaBuilder.and(criteria.toArray(new Predicate[0])));

        //Execute query
        List<Employee> resultList = entityManager.createQuery(criteriaQuery)
                .setParameter("projectName", projectName)
                .getResultList();

        //Print result
        for (Employee singleRow : resultList) {
            System.out.println(singleRow);
        }
    }

    /*
    SELECT e
    FROM Employee e
        WHERE EXISTS (SELECT p
        FROM Project p JOIN p.employees emp
        WHERE emp = e AND
    p.name = :name)
    */
    @Test
    @Transactional
    @Rollback(false)
    public void subquery_example_two() {
        System.out.println(" *** Subquery example two *** ");

        //Parameters
        String projectName = "Big Project";

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Parameter of criateQuery is result type
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        criteriaQuery.select(employeeRoot);

        List<Predicate> criteria = new ArrayList<Predicate>();
        //Subquery
        Subquery<Project> subquery = criteriaQuery.subquery(Project.class);
        Root<Project> subqueryProjectRoot = subquery.from(Project.class);
        Join<Project, Employee> projectEmployeeJoin = subqueryProjectRoot.join("employees");

        subquery.select(subqueryProjectRoot)
                .where(criteriaBuilder.equal(projectEmployeeJoin, employeeRoot),
                        criteriaBuilder.equal(subqueryProjectRoot.get("name"), criteriaBuilder.parameter(String.class, "projectName"))
                );

        criteria.add(criteriaBuilder.exists(subquery));

        criteriaQuery.where(criteriaBuilder.and(criteria.toArray(new Predicate[0])));

        //Execute query
        List<Employee> resultList = entityManager.createQuery(criteriaQuery)
                .setParameter("projectName", projectName)
                .getResultList();

        //Print result
        for (Employee singleRow : resultList) {
            System.out.println(singleRow);
        }
    }


    /*
    SELECT e
    FROM Employee e
        WHERE EXISTS (SELECT p
        FROM e.projects p
        WHERE p.name = :name)
    */
    @Test
    @Transactional
    @Rollback(false)
    public void subquery_example_three() {
        System.out.println(" *** Subquery example two *** ");

        //Parameters
        String projectName = "Big Project";

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Parameter of criateQuery is result type
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        criteriaQuery.select(employeeRoot);

        List<Predicate> criteria = new ArrayList<Predicate>();
        //Subquery - parameter is result type
        Subquery<Project> subquery = criteriaQuery.subquery(Project.class);
        Root<Project> subqueryProjectRoot = subquery.from(Project.class);
        Join<Project, Employee> projectEmployeeJoin = subqueryProjectRoot.join("employees");

        subquery.select(subqueryProjectRoot)
                .where(criteriaBuilder.equal(projectEmployeeJoin, employeeRoot),
                        criteriaBuilder.equal(subqueryProjectRoot.get("name"), criteriaBuilder.parameter(String.class, "projectName"))
                );

        criteria.add(criteriaBuilder.exists(subquery));

        criteriaQuery.where(criteriaBuilder.and(criteria.toArray(new Predicate[0])));

        //Execute query
        List<Employee> resultList = entityManager.createQuery(criteriaQuery)
                .setParameter("projectName", projectName)
                .getResultList();

        //Print result
        for (Employee singleRow : resultList) {
            System.out.println(singleRow);
        }
    }

    /*
    SELECT p
    FROM Project p JOIN p.employees e
    WHERE TYPE(p) = DesignProject AND
    e.directs IS NOT EMPTY AND
            (SELECT AVG(d.salary)
    FROM e.directs d) >= :value
    */
    @Test
    @Transactional
    @Rollback(false)
    public void subquery_example_four() {
        System.out.println(" *** Subquery example four *** ");

        //Parameters
        Double salaryValue = new Double(2000);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Parameter of criateQuery is result type
        CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);

        Root<Project> projectRoot = criteriaQuery.from(Project.class);
        Join<Project, Employee> projectEmployeeJoin = projectRoot.join("employees");


        Subquery<Double> subquery = criteriaQuery.subquery(Double.class);
        /*
        correlate() It gives you access to the employee referenced by the main query so you can use it and its tables in the subquery

        You wouldn't have access on the Employees within the emp Join object.
        Thus you would not be able to use emp in your subquery sq where you do the join to Employee.directs collection
       */
        Join<Project, Employee> subqueryProjectEmployeeJoin = subquery.correlate(projectEmployeeJoin);
        Join<Employee, Employee> employeeDirects = subqueryProjectEmployeeJoin.join("directs");


        //Build query
        criteriaQuery.select(projectRoot)
                .where(
                        criteriaBuilder.isNotEmpty(projectEmployeeJoin.<Collection>get("directs")),
                        criteriaBuilder.ge(subquery.select(criteriaBuilder.avg(employeeDirects.<Double>get("salary"))), criteriaBuilder.parameter(Double.class, "salaryValue"))
                );


        //Execute query
        List<Project> resultList = entityManager.createQuery(criteriaQuery)
                .setParameter("salaryValue", salaryValue)
                .getResultList();

        //Print result
        for (Project singleRow : resultList) {
            System.out.println(singleRow);
        }
    }

    /*
    SELECT e
    FROM Employee e
    WHERE e.address.state IN ('NY', 'CA')
    */
    @Test
    @Transactional
    @Rollback(false)
    public void in_expression_one() {
        System.out.println(" *** IN expression one *** ");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Parameter of criateQuery is result type
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        criteriaQuery.select(employeeRoot)
                .where(criteriaBuilder.in(employeeRoot.get("address")
                        .get("state")).value("Pomorskie").value("Mazowieckie"));

        //Execute query
        List<Employee> resultList = entityManager.createQuery(criteriaQuery)
                .getResultList();

        //Print result
        for (Employee singleRow : resultList) {
            System.out.println(singleRow);
        }
    }

    /*
  SELECT e
  FROM Employee e
  WHERE e.address.state IN ('NY', 'CA')
  */
    @Test
    @Transactional
    @Rollback(false)
    public void in_expression_shortcut_version() {
        System.out.println(" *** IN expression shortcut version *** ");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Parameter of criateQuery is result type
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        criteriaQuery.select(employeeRoot)
                .where(employeeRoot.get("address")
                        .get("state").in("Pomorskie", "Mazowieckie"));

        //Execute query
        List<Employee> resultList = entityManager.createQuery(criteriaQuery)
                .getResultList();

        //Print result
        for (Employee singleRow : resultList) {
            System.out.println(singleRow);
        }
    }

    /*
    SELECT e
    FROM Employee e
    WHERE e.department IN
            (SELECT DISTINCT d
                    FROM Department d JOIN d.employees de JOIN de.project p
                    WHERE p.name LIKE 'QA%')
    */
    @Test
    @Transactional
    @Rollback(false)
    public void in_expression_and_subuery() {
        System.out.println(" *** IN expression and subuery in one query *** ");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Parameter of criateQuery is result type
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<Department> departmentRoot = subquery.from(Department.class);

        Join<Employee, Project> employeeProjectJoin =
                departmentRoot.join("employees").join("projects");

        subquery.select(departmentRoot.<Integer>get("id"))
                .distinct(true)
                .where(criteriaBuilder.like(employeeProjectJoin.<String>get("name"), "Small%"));

        criteriaQuery.select(employeeRoot)
                .where(criteriaBuilder.in(employeeRoot.get("dept").get("id")).value(subquery));

        //Execute query
        List<Employee> resultList = entityManager.createQuery(criteriaQuery)
                .getResultList();

        //Print result
        for (Employee singleRow : resultList) {
            System.out.println(singleRow);
        }
    }

    /*
    SELECT p.name,
        CASE WHEN TYPE(p) = DesignProject THEN 'Development'
            WHEN TYPE(p) = QualityProject THEN 'QA'
            WHEN TYPE(p) = Project THEN 'Parent'
            ELSE 'Non-Development'
        END
    FROM Project p
    WHERE p.employees IS NOT EMPTY
    */
    @Test
    @Transactional
    @Rollback(false)
    public void case_expression() {
        System.out.println(" *** Case expression *** ");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Parameter of criateQuery is result type
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Project> projectRoot = criteriaQuery.from(Project.class);

        criteriaQuery.multiselect(projectRoot.get("name"), criteriaBuilder.selectCase()
                .when(criteriaBuilder.equal(projectRoot.type(), DesignProject.class), "The very BIG ")
                .when(criteriaBuilder.equal(projectRoot.type(), QualityProject.class), "Quality Project")
                .when(criteriaBuilder.equal(projectRoot.type(), Project.class), "Parent project class entity")
                .otherwise("Non Project class type"))
                .where(criteriaBuilder.isNotEmpty(projectRoot.<List<String>>get("employees")));

        //Execute query
        List<Object[]> resultList = entityManager.createQuery(criteriaQuery)
                .getResultList();

        //Print result
        for (Object[] singleRow : resultList) {
            for (Object singleColumn : singleRow) {
                System.out.print(" " + singleColumn + " ");
            }
            System.out.println();
        }
    }


    /*
    SELECT p.name,
        CASE TYPE(p)
            WHEN DesignProject THEN 'Development'
            WHEN QualityProject THEN 'QA'
            ELSE 'Non-Development'
        END
    FROM Project p
    WHERE p.employees IS NOT EMPTY
    */

    @Test
    @Transactional
    @Rollback(false)
    public void case_expression_two() {
        System.out.println(" *** Case expression - second example *** ");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Parameter of criateQuery is result type
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Project> projectRoot = criteriaQuery.from(Project.class);

        criteriaQuery.multiselect(projectRoot.get("name"), criteriaBuilder.selectCase(projectRoot.type())
                .when(DesignProject.class, "The very BIG ")
                .when(QualityProject.class, "Quality Project")
                .when(Project.class, "Parent project class entity")
                .otherwise("Non Project class type"))
                .where(criteriaBuilder.isNotEmpty(projectRoot.<List<String>>get("employees")));

        //Execute query
        List<Object[]> resultList = entityManager.createQuery(criteriaQuery)
                .getResultList();

        //Print result
        for (Object[] singleRow : resultList) {
            for (Object singleColumn : singleRow) {
                System.out.print(" " + singleColumn + " ");
            }
            System.out.println();
        }
    }


    /*
        SELECT COALESCE(d.name, d.id)
        FROM Department
    */

    /*
        COALESCE dziala tak ze jezeli pierwsze wyrazenie jest nullem to zwracan jest wartosc drugieg
     */
    @Test
    @Transactional
    @Rollback(false)
    public void coalesce_expression() {
        System.out.println(" *** Coalesce expression - second example *** ");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Parameter of criateQuery is result type
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery(Object.class);
        Root<Department> departmentRoot = criteriaQuery.from(Department.class);

        criteriaQuery.select(criteriaBuilder.coalesce()
                .value(departmentRoot.get("name"))
                .value(departmentRoot.get("id")));

        //Execute query
        List<Object> resultList = entityManager.createQuery(criteriaQuery)
                .getResultList();

        //Print result
        for (Object singleResult : resultList) {
            System.out.print(" " + singleResult + " ");
        }
    }

    /*
    SELECT e FROM Employee e JOIN TREAT(e.projects AS QualityProject) qp
    WHERE qp.qualityRating > 5
    */
    @Test
    @Transactional
    @Rollback(false)
    public void treat_expression_for_downcasting_join() {
        System.out.println(" *** Treat expression - join downcasting *** ");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Parameter of criateQuery is result type
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);


        Join<Employee, Project> employeeProjectJoin = employeeRoot.join("projects");
        Join<Employee, QualityProject> employeeQualityProjectJoin = criteriaBuilder.treat(employeeProjectJoin, QualityProject.class);


        criteriaQuery.select(employeeRoot)
                .where(criteriaBuilder.gt(employeeQualityProjectJoin.<Number>get("qa_rating"), 5));

        //Execute query
        List<Employee> resultList = entityManager.createQuery(criteriaQuery)
                .getResultList();

        //Print result
        for (Object singleResult : resultList) {
            System.out.print(" " + singleResult + " ");
        }
    }

    /*
     * Pozwala na rzutowaniu w 'miejscu', tzn:
     * Ze mozna w locie rzutowac pobieranie danego atrybutu
     * */

    @Test
    @Transactional
    @Rollback(false)
    public void treat_expression_for_downcasting_path() {
        System.out.println(" *** Treat expression - path downcasting *** ");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Parameter of criateQuery is result type
        CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
        Root<Project> projectRoot = criteriaQuery.from(Project.class);

        criteriaQuery.select(projectRoot)
                .where(criteriaBuilder.or(
                        criteriaBuilder.gt(criteriaBuilder.treat(projectRoot, QualityProject.class).<Number>get("qa_rating"), 5),
                        criteriaBuilder.gt(criteriaBuilder.treat(projectRoot, DesignProject.class).<Number>get("id"), 5)
                ));


        //Execute query
        List<Project> resultList = entityManager.createQuery(criteriaQuery)
                .getResultList();

        //Print result
        for (Object singleResult : resultList) {
            System.out.print(" " + singleResult + " ");
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void subquery_sandbox() {
        System.out.println(" *** Subquery example two *** ");

        //Parameters
        String projectName = "Big Project";

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Parameter of criateQuery is result type
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        criteriaQuery.select(employeeRoot);

        List<Predicate> criteria = new ArrayList<Predicate>();
        //Subquery - parameter is result type
        Subquery<Project> subquery = criteriaQuery.subquery(Project.class);
        Root<Project> subqueryProjectRoot = subquery.from(Project.class);
        Join<Project, Employee> projectEmployeeJoin = subqueryProjectRoot.join("employees");

        subquery.select(subqueryProjectRoot)
                .where(criteriaBuilder.equal(projectEmployeeJoin, employeeRoot));

        criteria.add(criteriaBuilder.exists(subquery));

        criteriaQuery.where(criteriaBuilder.and(criteria.toArray(new Predicate[0])));

        //Execute query
        List<Employee> resultList = entityManager.createQuery(criteriaQuery)
                .getResultList();

        //Print result
        for (Employee singleRow : resultList) {
            System.out.println(singleRow);
        }
    }

    /*
        The join methods return an object of type Join<X, Y>,
        where X is the source entity and Y is the target of the join.
    */
    @Test
    @Transactional
    @Rollback(false)
    public void join_simple() {
        System.out.println(" *** Join simple *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Jako parametr podajemy jakiego typu bedzie wynik
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
        Join<Employee, Employee> directs = employeeRoot.join("directs");


        criteriaQuery.multiselect(employeeRoot.get("name"), employeeRoot.get("salary"), directs.get("name"));

        //Execute query
        List<Object[]> resultArray = entityManager.createQuery(criteriaQuery).getResultList();

        //Print result
        for (Object[] singleRow : resultArray) {
            for (Object singleColumn : singleRow) {
                System.out.print(" " + singleColumn + " ");
            }
            System.out.println();
        }
    }

    /*
     SELECT e FROM Employee e JOIN e.projects p ON p.name = 'Zooby'
    */
    /*
     * Support for the ON condition of outer join queries was added in JPA 2.1.
     * */
    @Test
    @Transactional
    @Rollback(false)
    public void join_outer() {
        System.out.println(" *** Join outer *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Jako parametr podajemy jakiego typu bedzie wynik
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        Join<Employee, Project> employeeProjectJoin = employeeRoot.join("projects", JoinType.LEFT);
        Predicate onCondition = criteriaBuilder.equal(employeeProjectJoin.get("name"), "Small Project");
        employeeProjectJoin.on(onCondition);

        criteriaQuery.select(employeeRoot);

        //Execute query
        List<Employee> resultArray = entityManager.createQuery(criteriaQuery).getResultList();

        //Print result
        for (Employee singleRow : resultArray) {
            System.out.print(" " + singleRow + " ");
        }
    }

    /*
    SELECT d.name, e.name
    FROM Employee e JOIN e.dept d
    ORDER BY d.name DESC, e.name
    */

    @Test
    @Transactional
    @Rollback(false)
    public void order_by_expression() {
        System.out.println(" *** Order by expression *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Jako parametr podajemy jakiego typu bedzie wynik
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        Join<Employee, Department> employeeDepartmentJoin = employeeRoot.join("dept", JoinType.LEFT);


        criteriaQuery.multiselect(employeeDepartmentJoin.get("name").alias("dept_name"), employeeRoot.get("name").alias("emp_name"));
        criteriaQuery.orderBy(criteriaBuilder.desc(employeeDepartmentJoin.get("name")),
                criteriaBuilder.asc(employeeRoot.get("name")));

        //Execute query
        List<Tuple> resultList = entityManager.createQuery(criteriaQuery).getResultList();

        //Print query
        for (Tuple tuple : resultList) {
            String employeeName = (String) tuple.get("emp_name");
            String departmentName = (String) tuple.get("dept_name");

            System.out.println("Employee name: " + employeeName + " Department name: " + departmentName);
        }
    }

    /*
    UPDATE Employee e
    SET e.salary = e.salary + 5000
    WHERE EXISTS (SELECT p
    FROM e.projects p
    WHERE p.name = 'Release2')
    */
    @Test
    @Transactional
    @Rollback(false)
    public void update_bulk_query() {
        System.out.println(" *** Update bulk query *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Jako parametr podajemy jakiego typu bedzie wynik
        CriteriaUpdate<Employee> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Employee.class);
        Root<Employee> employeeRoot = criteriaUpdate.from(Employee.class);

        Subquery<Project> subquery = criteriaUpdate.subquery(Project.class);
        Root<Employee> employeeSubquery = subquery.correlate(employeeRoot);
        Join<Employee, Project> employeeProjectJoin = employeeSubquery.join("projects");

        subquery.select(employeeProjectJoin)
                .where(criteriaBuilder.equal(employeeProjectJoin.get("name"), "Small Project"));

        Expression<Integer> updateSalaryValue = criteriaBuilder.sum(employeeRoot.<Integer>get("salary"), 7777);

        criteriaUpdate.set(employeeRoot.<Integer>get("salary"), updateSalaryValue)
                .where(criteriaBuilder.exists(subquery));


        //Execute query
        Query query = entityManager.createQuery(criteriaUpdate);
        int updateResult = query.executeUpdate();

        //Print query
        System.out.println("Update result: " + updateResult);
    }

    /*
     DELETE FROM Phone e
     WHERE e.department IS NULL
    */
    @Test
    @Transactional
    @Rollback(false)
    public void delete_query() {
        System.out.println(" *** Delete query *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaDelete<Phone> phoneCriteriaDelete = criteriaBuilder.createCriteriaDelete(Phone.class);
        Root<Phone> phoneRoot = phoneCriteriaDelete.from(Phone.class);
        Path<Employee> employeePath = phoneRoot.get("employee");
        phoneCriteriaDelete.where(
                criteriaBuilder.isNotNull(employeePath.get("dept")));

        //Execute query
        Query queryPhone = entityManager.createQuery(phoneCriteriaDelete);
        int phoneDeleteResult = queryPhone.executeUpdate();

        //Print query
        System.out.println("Phone delete result: " + phoneDeleteResult);
    }

    /*
    SELECT e, COUNT(p)
    FROM Employee e JOIN e.projects p
    GROUP BY e
    HAVING COUNT(p) >= 2
    */
    @Test
    @Transactional
    @Rollback(false)
    public void group_by_and_having_clause() {
        System.out.println(" *** Group by and having clause *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Jako parametr podajemy jakiego typu bedzie wynik
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        Join<Employee, Project> employeeProjectJoin = employeeRoot.join("projects");


        criteriaQuery.multiselect(employeeRoot, criteriaBuilder.count(employeeProjectJoin))
                .groupBy(employeeRoot)
                .having(criteriaBuilder.ge(criteriaBuilder.count(employeeProjectJoin), 1));

        //Execute query
        List<Object[]> resultList = entityManager.createQuery(criteriaQuery).getResultList();

        //Print query
        for (Object[] singleRow : resultList) {
            for (Object singleColumn : singleRow) {
                System.out.print(" " + singleColumn + " ");
            }
            System.out.println();
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void join_cascade() {
        System.out.println(" *** Join cascade *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Jako parametr podajemy jakiego typu bedzie wynik
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

        Root<Department> departmentRoot = criteriaQuery.from(Department.class);
        Join<Employee, Project> departmentEmployeesProjects = departmentRoot.join("employees").join("projects");


        criteriaQuery.multiselect(departmentRoot.get("name"), departmentEmployeesProjects.get("name"));

        //Execute query
        List<Object[]> resultArray = entityManager.createQuery(criteriaQuery).getResultList();

        //Print result
        for (Object[] singleRow : resultArray) {
            for (Object singleColumn : singleRow) {
                System.out.print(" " + singleColumn + " ");
            }
            System.out.println();
        }
    }

    /*
      A "fetch" join allows associations or collections of values to be initialized along with their parent objects using a single select.
      This is particularly useful in the case of a collection.
      It effectively overrides the outer join and lazy declarations of the mapping file for associations and collections.
    */
    /*
      SELECT e FROM Employee e JOIN FETCH e.address
    */
    @Test
    @Transactional
    @Rollback(false)
    public void join_fetch() {
        System.out.println(" *** Join fetch *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Jako parametr podajemy jakiego typu bedzie wynik
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
        employeeRoot.fetch("address");
        criteriaQuery.select(employeeRoot);

        //Execute query
        TypedQuery<Employee> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Employee> employees = typedQuery.getResultList();

        //Print query
        for (Employee employee : employees) {
            System.out.println("Employee: " + employee.getName() + " " + employee.getAddress());
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void using_aliases() {
        System.out.println(" *** Using aliases *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Jako parametr podajemy jakiego typu bedzie wynik
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();

        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        criteriaQuery.multiselect(employeeRoot.get("id").alias("id"), employeeRoot.get("name").alias("fullName"));

        //Execute query
        TypedQuery<Tuple> typedQuery = entityManager.createQuery(criteriaQuery);

        //Print query
        for (Tuple tuple : typedQuery.getResultList()) {
            Integer id = tuple.get("id", Integer.class);
            String fullName = tuple.get("fullName", String.class);

            System.out.println("id: " + id + " full name: " + fullName);
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void select_single_column() {
        System.out.println(" *** Select single column *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Jako parametr podajemy jakiego typu bedzie wynik
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);

        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        criteriaQuery.select(employeeRoot.<String>get("name"));

        //Execute query
        TypedQuery<String> typedQuery = entityManager.createQuery(criteriaQuery);
        List<String> resultList = typedQuery.getResultList();

        //Print query
        for (String row : resultList) {
            System.out.println("Result row: " + row);
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void select_multiple_column_by_tuple_query_and_tuple_result() {
        System.out.println(" *** Select multiple column: using tupleQuery and Tuple result *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Jako parametr podajemy jakiego typu bedzie wynik
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();

        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        criteriaQuery.select(criteriaBuilder.tuple(employeeRoot.get("name"), employeeRoot.get("salary")));

        //Execute query
        List<Tuple> resultList = entityManager.createQuery(criteriaQuery).getResultList();

        //Print query
        for (Tuple singleRow : resultList) {
            for (Object singleColumn : singleRow.toArray()) {
                System.out.print(" " + singleColumn + " ");
            }
            System.out.println();
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void select_multiple_column_by_multiselect_and_array_of_object_result() {
        System.out.println(" *** Select multiple column: using multiselect and array of object result *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Jako parametr podajemy jakiego typu bedzie wynik
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        criteriaQuery.multiselect(employeeRoot.get("name"), employeeRoot.get("salary"));

        //Execute query
        List<Object[]> resultList = entityManager.createQuery(criteriaQuery).getResultList();

        //Print query
        for (Object[] singleRow : resultList) {
            for (Object singleColumn : singleRow) {
                System.out.print(" " + singleColumn + " ");
            }
            System.out.println();
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void select_multiple_column_by_multiselect_and_object_construction_in_result() {
        System.out.println(" *** Select multiple column: using multiselect and object construction in result result *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Jako parametr podajemy jakiego typu bedzie wynik
        CriteriaQuery<EmployeeDetails> criteriaQuery = criteriaBuilder.createQuery(EmployeeDetails.class);

        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        criteriaQuery.multiselect(employeeRoot.get("name"), employeeRoot.get("salary"), employeeRoot.get("dept").get("name"));

        List<EmployeeDetails> employeeDetails = entityManager.createQuery(criteriaQuery).getResultList();

        //Execute query
        List<EmployeeDetails> resultList = entityManager.createQuery(criteriaQuery).getResultList();

        //Print query
        for (EmployeeDetails singleRow : resultList) {
            System.out.println(singleRow);
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void select_multiple_column_by_select_and_object_construction_using_construct_method_result() {
        System.out.println(" *** Select multiple column: by select and object construction using onstruct method result *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Jako parametr podajemy jakiego typu bedzie wynik
        CriteriaQuery<EmployeeDetails> criteriaQuery = criteriaBuilder.createQuery(EmployeeDetails.class);

        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        criteriaQuery.select(criteriaBuilder.construct(
                EmployeeDetails.class,
                employeeRoot.get("name"),
                employeeRoot.get("salary"),
                employeeRoot.get("dept").get("name")));

        //Execute query
        List<EmployeeDetails> resultList = entityManager.createQuery(criteriaQuery).getResultList();

        //Query result
        for (EmployeeDetails singleRow : resultList) {
            System.out.println(singleRow);
        }

    }

    @Test
    @Transactional
    @Rollback(false)
    public void select_multiple_column_by_multiselect_and_tuple_result() {
        System.out.println(" *** Select multiple column: using multiselect and tuple result *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Jako parametr podajemy jakiego typu bedzie wynik
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);

        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        criteriaQuery.multiselect(employeeRoot.get("name"), employeeRoot.get("salary"));

        //Result
        List<Tuple> resultList = entityManager.createQuery(criteriaQuery).getResultList();

        for (Tuple singleRow : resultList) {
            for (Object singleColumn : singleRow.toArray()) {
                System.out.print(" " + singleColumn + " ");
            }
            System.out.println();
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void path_expression() {
        System.out.println(" *** Path expression *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Jako parametr podajemy jakiego typu bedzie wynik
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        String city = "Warsaw";

        criteriaQuery.select(employeeRoot)
                .where(criteriaBuilder.equal(employeeRoot.get("address").get("city"), city));

        //Execute query
        List<Employee> resultList = entityManager.createQuery(criteriaQuery).getResultList();

        //Print result
        for (Employee singleRow : resultList) {
            System.out.println(singleRow);
        }
    }

    /*SELECT DISTINCT d
       FROM Department d, Employee e
       WHERE d = e.department*/

    @Test
    @Transactional
    @Rollback(false)
    public void query_roots() {
        System.out.println(" *** Query roots *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        //Jako parametr podajemy jakiego typu bedzie wynik
        CriteriaQuery<Department> criteriaQuery = criteriaBuilder.createQuery(Department.class);

        Root<Department> departmentRoot = criteriaQuery.from(Department.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        criteriaQuery.select(departmentRoot)
                .distinct(true)
                .where(criteriaBuilder.equal(departmentRoot, employeeRoot.get("dept")));

        //Executre query
        List<Department> resultList = entityManager.createQuery(criteriaQuery).getResultList();

        //Print result
        for (Department singleRow : resultList) {
            System.out.println(singleRow);
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void select() {
        System.out.println(" *** Select *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        criteriaQuery.select(employeeRoot);
        criteriaQuery.where(criteriaBuilder.equal(employeeRoot.get("name"), "John"));

        //Execute query
        List<Employee> resultList = entityManager.createQuery(criteriaQuery).getResultList();

        //Print result
        for (Employee singleRow : resultList) {
            System.out.println(singleRow);
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void conjuction() {
        System.out.println(" *** Conjuction *** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        String name = "Bob";
        Integer salary = 2800;

        criteriaQuery.select(employeeRoot);

        Predicate nameAndSalaryConjuction = criteriaBuilder.conjunction();
        nameAndSalaryConjuction = criteriaBuilder.and(nameAndSalaryConjuction, criteriaBuilder.equal(employeeRoot.get("name"), name));
        nameAndSalaryConjuction = criteriaBuilder.and(nameAndSalaryConjuction, criteriaBuilder.equal(employeeRoot.get("salary"), salary));

        criteriaQuery.where(nameAndSalaryConjuction);

        //Execute query
        List<Employee> resultList = entityManager.createQuery(criteriaQuery).getResultList();

        //Print result
        for (Employee singleRow : resultList) {
            System.out.println(singleRow);
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void conjuction_using_add() {
        System.out.println(" *** Conjuction using add() method*** ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        String name = "Bob";

        List<Predicate> criteria = new ArrayList<Predicate>();

        criteriaQuery.select(employeeRoot);

        criteria.add(criteriaBuilder.equal(employeeRoot.get("name"), name));

        criteriaQuery.where(criteriaBuilder.and(criteria.get(0)));

        //Execute query
        List<Employee> resultList = entityManager.createQuery(criteriaQuery).getResultList();

        //Print result
        for (Employee singleRow : resultList) {
            System.out.println(singleRow);
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void complex_query_find_employee() {
        System.out.println(" *** Complex query: find employee *** ");

        String name = "Mark";
        String deptName = "IT_Board";
        String projectName = "Big Project";
        String city = "Gdansk";


        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
        criteriaQuery.select(employeeRoot);
        criteriaQuery.distinct(true);

        Join<Employee, Project> projectJoin = employeeRoot.join("projects", JoinType.LEFT);

        List<Predicate> criteria = new ArrayList<Predicate>();

        if (name != null) {
            ParameterExpression<String> parameterExpression = criteriaBuilder.parameter(String.class, "name");
            criteria.add(criteriaBuilder.equal(employeeRoot.get("name"), parameterExpression));
        }
        if (deptName != null) {
            ParameterExpression<String> parameterExpression = criteriaBuilder.parameter(String.class, "dept");
            criteria.add(criteriaBuilder.equal(employeeRoot.get("dept").get("name"), parameterExpression));
        }
        if (projectName != null) {
            ParameterExpression<String> parameterExpression = criteriaBuilder.parameter(String.class, "project");
            criteria.add(criteriaBuilder.equal(projectJoin.get("name"), parameterExpression));
        }
        if (city != null) {
            ParameterExpression<String> parameterExpression = criteriaBuilder.parameter(String.class, "city");
            criteria.add(criteriaBuilder.equal(employeeRoot.get("address").get("city"), parameterExpression));
        }

        if (criteria.size() == 0) {
            throw new RuntimeException("no criteria");
        } else if (criteria.size() == 1) {
            criteriaQuery.where(criteria.get(0));
        } else {
            criteriaQuery.where(criteriaBuilder.and(criteria.toArray(new Predicate[criteria.size()])));
        }

        TypedQuery<Employee> typedQuery = entityManager.createQuery(criteriaQuery);
        if (name != null) {
            typedQuery.setParameter("name", name);
        }
        if (deptName != null) {
            typedQuery.setParameter("dept", deptName);
        }
        if (projectName != null) {
            typedQuery.setParameter("project", projectName);
        }
        if (city != null) {
            typedQuery.setParameter("city", city);
        }

        //Execute query
        List<Employee> resultList = typedQuery.getResultList();

        //Print result
        for (Employee singleRow : resultList) {
            System.out.println(singleRow);
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