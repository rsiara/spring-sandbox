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
public class OneToManyBidirectionalJoinTableTest {

  private EntityManager entityManager;

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Test
  @Transactional
  @Rollback(false)
  public void One_to_many_bidirectional_join_table_Test() {

    Department department = new Department();
    department.setName("Human Resources");

    Employee john = new Employee();
    john.setName("John");
    john.setSalary(2800);

    john.setDepartment(department);

    Employee mike = new Employee();
    mike.setName("Mike");
    mike.setSalary(3400);

    mike.setDepartment(department);

    entityManager.persist(department);

    entityManager.persist(john);
    entityManager.persist(mike);
  }
}