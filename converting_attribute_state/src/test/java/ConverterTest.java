import configuration.RootConfig;
import model.Department;
import model.Employee;
import model.EmployeeName;
import model.SecurityInfo;
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

import javax.transaction.Transactional;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*

 ***   CONVERTING ATTRIBUTE STATE   ***
 *
 *
 *  LIMITATIONS
 *
    There are a few restrictions placed on converters, mostly to prevent users from doing things that would get them into
    trouble. For instance, converters cannot be used on identifier attributes, version attributes, or relationship attributes
    (unless you are converting the key part of a relationship Map, as in Listing 10-4). Hopefully this will not come as a
    surprise to you, since in most cases converting these types of attributes would arguably be a pretty bad idea. These
    attributes are heavily used by the provider as it manages the entities; changing their shape or even their value could
    cause inconsistencies or incorrect results.
    Converters also cannot be used on attributes annotated with @Enumerated or @Temporal, but that doesn’t mean
    you can’t convert enumerated types or temporal types. It just means that if you use the standard @Enumerated or
    @Temporal annotations to map those types, then you also cannot use custom converters. If you are using a custom
    converter class, then you are taking over control of the value that gets stored and there is no need for you to use any of
    those annotations to get the JPA provider to do the conversion for you. Put simply, if you are doing custom conversion
    using converters on enumerated or temporal types, just leave the @Enumerated or @Temporal annotations off.


    CONVERTERS AND QUERIES

    The first is that the query processor will apply the converter to both the attributes targeted for conversion as
    well as the literals they are being compared against in a query. However, the operators are not modified. This means
    that only certain comparison operators will work once the query is converted. To illustrate this point, consider the
    following query:
    SELECT e FROM Employee e WHERE e.bonded = true
    This query will work fine if bonded is set to be converted from boolean to integer. The generated SQL will
    have converted both the bonded attribute and the literal “true” to the corresponding integer by invoking the
    convertToDatabaseColumn() method on it and the equals operator will work just as well on integers as it does on
    booleans.
    However, we may want to query for all of the employees who are not bonded:
    SELECT e FROM Employee e WHERE NOT e.bonded
    If we try to execute this query the parser will have no problem with it, but when it comes time to execute it
    the resulting SQL will contain a NOT and the value of e.bonded will have been converted to be an integer. This will
    generally cause a database exception since the NOT operation cannot be applied to an integer.
    It is possible that you will bump into an issue or two if you do any significant querying across converted
    attributes. While you can usually rely on conversion of literals and input parameters used in comparison, if they are
    contained within a function, such as UPPER() or MOD(), they probably will not be converted. Even some of the more
    advanced comparison operations, such as LIKE, may not apply conversion to the literal operand. The moral is to try
    not to use converted attributes in queries, and if you do, play around and do some experimenting to make sure your
    queries work as expected.
 *
 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ConverterTest {

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

    /*
        Basic attribute converted declaratively - attribute true will be saved as integer of value = 1
    */
    @Test
    @Transactional
    @Rollback(false)
    public void declarative_basic_attribute_conversion() throws MalformedURLException {
        System.out.println(" *** Declarative basic attribute conversion *** ");

        Employee employee = new Employee();

        EmployeeName employeeName = new EmployeeName();
        employeeName.setFirstName("John");
        employeeName.setLastName("Zimbra");

        employee.setEmpName(employeeName);

        URL homePage = new URL("http://baeldung.com");
        employee.setHomePage(homePage);

        SecurityInfo securityInfo = new SecurityInfo();
        securityInfo.setBonded(true);
        securityInfo.setDateBonded(today);

        employee.setSecurityInfo(securityInfo);

        // basic attribute converted declaratively - attribute true will be saved as integer of value = 1
        employee.setFullTime(true);

        entityManager.persist(employee);
    }

    /*
    If the attribute to be converted is part of an embeddable type and we are converting it from within a referencing
    entity, then we use the attributeName element to specify the attribute to convert.
    */
    @Test
    @Transactional
    @Rollback(false)
    public void declarative_embeddable_attribute_conversion() throws MalformedURLException {
        System.out.println(" *** Declarative embeddable attribute conversion *** ");

        Employee employee = new Employee();

        EmployeeName employeeName = new EmployeeName();
        employeeName.setFirstName("John");
        employeeName.setLastName("Zimbra");

        employee.setEmpName(employeeName);

        URL homePage = new URL("http://baeldung.com");
        employee.setHomePage(homePage);

        SecurityInfo securityInfo = new SecurityInfo();
        securityInfo.setBonded(true);
        securityInfo.setDateBonded(today);

        // embeddable attribute converted declaratively - attribute true will be saved as integer of value = 1,
        // attribute name has been precised in @Convert annotation
        employee.setSecurityInfo(securityInfo);

        employee.setFullTime(true);

        entityManager.persist(employee);
    }

    /*
      Element collections of basic types are simple to convert. They are annotated the same way as basic attributes that
      are not collections. Thus, if we had an attribute that was of type List<Boolean>, we would annotate it just as we did
      our bonded attribute above, and all of the boolean values in the collection would be converted:
      @ElementCollection
      @Convert(converter=BooleanToIntegerConverter.class)
      private List<Boolean> securityClearances;
    */
    @Test
    @Transactional
    @Rollback(false)
    public void converting_collection() throws MalformedURLException {
        System.out.println(" *** Converting collection *** ");

        Employee employee = new Employee();

        EmployeeName employeeName = new EmployeeName();
        employeeName.setFirstName("John");
        employeeName.setLastName("Zimbra");

        employee.setEmpName(employeeName);

        URL homePage = new URL("http://baeldung.com");
        employee.setHomePage(homePage);

        SecurityInfo securityInfo = new SecurityInfo();
        securityInfo.setBonded(true);
        securityInfo.setDateBonded(today);

        employee.setSecurityInfo(securityInfo);

        employee.setFullTime(true);

        entityManager.persist(employee);

        Department department = new Department();
        department.setName("Big department");

        /*Using the domain model, if we wanted to convert
        the employee last name to be stored as uppercase characters (assuming we have defined the corresponding converter
        class), we would annotate the attribute as shown*/
        Map<EmployeeName, Employee> employeeMap = new HashMap<EmployeeName, Employee>();
        employeeMap.put(employeeName, employee);
        department.setEmployees(employeeMap);

        entityManager.persist(department);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void conversion_automatic() throws MalformedURLException {
        System.out.println(" *** Automatic convertion *** ");

        Employee employee = new Employee();

        EmployeeName employeeName = new EmployeeName();
        employeeName.setFirstName("John");
        employeeName.setLastName("Zimbra");

        employee.setEmpName(employeeName);

        /* Automatically converted attribute by type */
        URL homePage = new URL("http://baeldung.com");
        employee.setHomePage(homePage);

        SecurityInfo securityInfo = new SecurityInfo();
        securityInfo.setBonded(true);
        securityInfo.setDateBonded(today);

        employee.setSecurityInfo(securityInfo);

        employee.setFullTime(true);

        entityManager.persist(employee);
    }
}