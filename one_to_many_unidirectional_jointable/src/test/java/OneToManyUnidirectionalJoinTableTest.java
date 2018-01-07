import configuration.RootConfig;
import model.Employee;
import model.Phone;
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
public class OneToManyUnidirectionalJoinTableTest {

  private EntityManager entityManager;

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Test
  @Transactional
  @Rollback(false)
  public void One_to_many_unidirectional_join_table_Test() {

    Employee john = new Employee();
    john.setName("John");
    john.setSalary(2800);

    Phone homePhone = new Phone();
    homePhone.setType("Stationary phone");
    homePhone.setNumber("+48 22 780 15 33");

    Phone mobilePhone = new Phone();
    mobilePhone.setType("Mobile phone");
    mobilePhone.setNumber("665 517 445");

    entityManager.persist(john);
  }
}