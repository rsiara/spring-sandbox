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
public class ManyToOneUnidirectionalTest {

  private EntityManager entityManager;

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Test
  @Transactional
  @Rollback(false)
  public void Many_to_one_unidirectional_Test() {
    Department department = new Department();
    department.setName("Human resources");

    Employee john = new Employee();
    john.setName("John");
    john.setSalary(2800);

    john.setDepartment(department);

    Employee mark = new Employee();
    mark.setName("Greg");;
    mark.setSalary(9000);

    mark.setDepartment(department);

    entityManager.persist(department);

    entityManager.persist(john);
    entityManager.persist(mark);
  }
}