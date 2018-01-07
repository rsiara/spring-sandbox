import configuration.RootConfig;
import model.Department;
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
public class SharingEmbeddableKeyMappingsWithValuesTest {

  private EntityManager entityManager;

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Test
  @Transactional
  @Rollback(false)
  public void Sharing_embeddable_key_mappings_with_values() {
    Department department = new Department();
    department.setName("Human resources");

    Employee john = new Employee();
    john.setFirstName("John");
    john.setLastName("Malkovic");
    john.setSalary(2800);

    Employee mark = new Employee();
    mark.setFirstName("Greg");
    mark.setLastName("Zorba");
    mark.setSalary(9000);

    department.addEmployee(john);
    department.addEmployee(mark);

    entityManager.persist(department);
  }
}