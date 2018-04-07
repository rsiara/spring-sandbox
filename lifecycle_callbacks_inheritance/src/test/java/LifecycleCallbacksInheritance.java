import configuration.RootConfig;
import model.ContractEmployee;
import model.Employee;
import model.FullTimeEmployee;
import org.junit.Before;
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
import java.util.Date;

/*

 JPA Callbacks Method
JPA provides callback methods to listen saving, fetching, updating and removing data from database. These callback methods annotated in a listener bean class must have return type void and accept one argument.

@PrePersist: The method annotated with @PrePersist in listener bean class is called before persisting data by entity manager persist() method.

@PostPersist: The method annotated with @PostPersist is called after persisting data.

 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class LifecycleCallbacksInheritance {

    Date today = new Date();
    Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Before
    public void prepareData() {

    }

    @Test
    @Transactional
    @Rollback(false)
    public void lifecycle_callbacks_inheritance() {
        System.out.println(" *** Lifecycle callbacks inheritance - @ExcludeSuperclassListeners ***");

        System.out.println("-------- Invoke all listeners of superclass --------");
        FullTimeEmployee fullTimeEmployee = new FullTimeEmployee();
        fullTimeEmployee.setId(12);
        fullTimeEmployee.setName("John");
        entityManager.persist(fullTimeEmployee);
        entityManager.flush();
        entityManager.clear();


        System.out.println("-------- @ExcludeSuperclassListeners--------");
        Employee contractEmployee = new ContractEmployee();
        contractEmployee.setId(13);
        contractEmployee.setName("Mark");
        entityManager.persist(contractEmployee);
        entityManager.flush();
        entityManager.clear();


    }

    public Employee findEmployee(int id) {
        return entityManager.find(Employee.class, id);
    }


}