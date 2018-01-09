import configuration.RootConfig;
import model.Address;
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
public class CollectionOfEmbeddedTest {

  private EntityManager entityManager;

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Test
  @Transactional
  @Rollback(false)
  public void collection_of_embedded_test() {

    Address addressHome = new Address();
    addressHome.setCity("Warsaw");
    addressHome.setState("MZ");
    addressHome.setStreet("Apartamentowa");
    addressHome.setZip("02-495");

    Address addressWork = new Address();
    addressWork.setCity("Warsaw");
    addressWork.setState("MZ");
    addressWork.setStreet("1 Sierpnia");
    addressWork.setZip("02-325");

    Employee employee = new Employee();
    employee.setName("John");
    employee.setSalary(3400);

    employee.getAddresses().add(addressHome);
    employee.getAddresses().add(addressWork);

    entityManager.persist(employee);
  }


}