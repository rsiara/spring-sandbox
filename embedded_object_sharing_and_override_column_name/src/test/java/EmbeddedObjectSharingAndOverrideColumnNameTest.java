
import configuration.RootConfig;
import model.Address;
import model.Company;
import model.Employee;
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


@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EmbeddedObjectSharingAndOverrideColumnNameTest {

  private EntityManager entityManager;

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Test
  @Transactional
  @Rollback(false)
  public void embedded_object_sharing_and_override_column_name_test() {

    Address address = new Address();
    address.setCity("Warsaw");
    address.setState("MZ");
    address.setStreet("Apartamentowa");
    address.setZip("02-495");

    Employee employee = new Employee();
    employee.setName("John");
    employee.setSalary(3400);

    Company company = new Company();
    company.setAddress(address);


    employee.setAddress(address);

    entityManager.persist(employee);
    entityManager.persist(company);
  }


}