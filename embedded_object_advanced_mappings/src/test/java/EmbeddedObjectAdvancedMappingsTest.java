import configuration.RootConfig;
import model.Address;
import model.ContactInfo;
import model.Employee;
import model.Phone;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*

 ***   CONVERTING ATTRIBUTE STATE   ***
 *

 *
 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EmbeddedObjectAdvancedMappingsTest {

    Date today = new Date();
    Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
    Date todayplustwo = new Date(today.getTime() + (1000 * 60 * 60 * 24) * 2);
    Date todayplustree = new Date(today.getTime() + (1000 * 60 * 60 * 24) * 3);
    Date todayplusfour = new Date(today.getTime() + (1000 * 60 * 60 * 24) * 4);
    Date todayplusfive = new Date(today.getTime() + (1000 * 60 * 60 * 24) * 5);
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Before
    public void init() {
        System.out.println("BEFORE TESTS");

    }

    /*

     */
    @Test
    @Transactional
    @Rollback(false)
    public void embedded_object_in_embedded_object() {
        System.out.println(" *** Declarative basic attribute conversion *** ");

        Employee employee = new Employee();
        employee.setName("John");
        employee.setSalary(2800);
        Address addr = new Address();
        addr.setStreet("Mickiewicza");
        addr.setCity("Warsaw");
        addr.setState("Mazowieckie");
        addr.setZip("02-495");


        ContactInfo contactInfo = new ContactInfo();
        Phone phone = new Phone();
        phone.setNum("1");
        phone.setType("cell");

        List<Employee> employees = new ArrayList<Employee>();
        employees.add(employee);

        phone.setEmployees(employees);
        contactInfo.setResidence(addr);
        contactInfo.setPrimaryPhone(phone);
        Map<String, Phone> phones = new HashMap<String, Phone>();
        phones.put(phone.getType(), phone);
        contactInfo.setPhones(phones);
        employee.setContactInfo(contactInfo);

        entityManager.persist(employee);
        entityManager.persist(phone);
    }


}