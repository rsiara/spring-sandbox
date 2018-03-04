import configuration.RootConfig;
import model.Address;
import model.ContactInfo;
import model.Customer;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*

 ***   RELATIONS OVERRIDING   ***
 *
 *
    Są dwie encje (CUSTOMER, EMPLOYEE) które korzystają z typu embeddable (CONTACT_INFO),
    który zawiera kolekcje innego typu embeddable (PHONE), ten typ powiązany jest relacją z encją (EMPLOYEE).

    Aby encja CUSTOMER mogła przechowywać informaje o swoich relacjach z obiektami PHONE
    w innej tabeli jak EMPLOYEE taką relacje trzeba nadpisać.

    Taką relacje trzeba nadpisać. I można to zrobić przy użyciu notacji @AssociationOverride.
 *
 *
 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EmbeddedObjectOverridingEmbeddedRelationshipsTest {

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

    @Test
    @Transactional
    @Rollback(false)
    public void overriding_embedded_ralationships() {
        System.out.println(" *** Overriding relationships *** ");

        //Employee

        Employee employee = new Employee();
        employee.setName("John");
        employee.setSalary(2800);
        Address addr = new Address();
        addr.setStreet("Mickiewicza");
        addr.setCity("Warsaw");
        addr.setState("Mazowieckie");
        addr.setZip("02-495");


        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setResidence(addr);

        Phone phone = new Phone();
        phone.setNum("1112222");
        phone.setType("home");
        contactInfo.setPrimaryPhone(phone);
        Map<String, Phone> phones = new HashMap<String, Phone>();
        phones.put(phone.getType(), phone);
        contactInfo.setPhones(phones);

        employee.setContactInfo(contactInfo);

        entityManager.persist(phone);
        entityManager.persist(employee);


        // Customer

        Customer customer = new Customer();

        Address address = new Address();
        address.setStreet("Kopernika");
        address.setCity("Krakow");
        address.setState("Malopolskie");
        address.setZip("02-311");

        ContactInfo customerContactInfo = new ContactInfo();
        customerContactInfo.setResidence(address);

        Phone customerPhone = new Phone();
        customerPhone.setNum("123-4567");
        customerPhone.setType("cell");
        customerContactInfo.setPrimaryPhone(customerPhone);
        Map<String, Phone> customerPhoneMap = new HashMap<String, Phone>();
        phones.put(customerPhone.getType(), customerPhone);
        customerContactInfo.setPhones(customerPhoneMap);

        customer.setContactInfo(customerContactInfo);

        entityManager.persist(customerPhone);
        entityManager.persist(customer);
    }
}