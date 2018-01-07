import configuration.RootConfig;
import model.Employee;
import model.ParkingSpace;
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
public class OneToOneUnidirectionalTest {

  private EntityManager entityManager;

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Test
  @Transactional
  @Rollback(false)
  public void One_to_one_unidirectional_Test() {

    Employee john = new Employee();
    john.setName("John");
    john.setSalary(2800);

    ParkingSpace parkingSpace = new ParkingSpace();
    parkingSpace.setLot(2);
    parkingSpace.setLocation("AC 32");

    john.setParkingSpace(parkingSpace);

    entityManager.persist(parkingSpace);

    entityManager.persist(john);
  }
}