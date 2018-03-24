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

/*
 * Domyslny poziom dostepu to AccessType
 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MappingTransientTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void mapping_transient_test() {
        Employee employee = new Employee();
        employee.setName("John");
        employee.setSalary(3400);

        //Transient field
        System.out.println(employee.getConvertedName());

        entityManager.persist(employee);
        entityManager.flush();
        entityManager.clear();
        entityManager.close();

        //Get object from database
        Employee employeeLoadedFromDb = entityManager.find(Employee.class, employee.getId());

        //Transient field
        System.out.println(employeeLoadedFromDb.getConvertedName());

    }


}