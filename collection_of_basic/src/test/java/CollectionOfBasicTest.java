import configuration.RootConfig;
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
public class CollectionOfBasicTest {

  private EntityManager entityManager;

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Test
  @Transactional
  @Rollback(false)
  public void collection_of_basic_test() {

    Employee employee = new Employee();
    employee.setName("John");
    employee.setSalary(3400);

    String contactMonice = "+4822772772772";
    String contactBella = "+4718333222333";
    String contactNatali = "+4412555333555";

    employee.getContacts().add(contactMonice);
    employee.getContacts().add(contactBella);
    employee.getContacts().add(contactNatali);

    entityManager.persist(employee);
  }


}