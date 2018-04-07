import configuration.RootConfig;
import model.Employee;
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
public class LifecycleCallbacks {

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
    public void lifecycle_callbacks() {
        System.out.println(" *** Lifecycle callback - Event Listener and @PrePersist, @PostLoad ***");

        System.out.println("-------- @PostPersist & @PrePersist: --------");
        Employee employee = new Employee();
        employee.setId(12);
        employee.setName("John");
        entityManager.persist(employee);
        entityManager.flush();
        entityManager.clear();

        System.out.println("-------- @PostLoad: --------");
        employee = findEmployee(employee.getId());
        entityManager.persist(employee);
        entityManager.flush();
        entityManager.clear();

        System.out.println("-------- @PostUpdate: --------");
        employee.setName("John 2");
        employee = entityManager.merge(employee);

        System.out.println("-------- @PreRemove: --------");
        entityManager.remove(employee);
        entityManager.flush();
        entityManager.clear();
    }

    public Employee findEmployee(int id) {
        return entityManager.find(Employee.class, id);
    }


}