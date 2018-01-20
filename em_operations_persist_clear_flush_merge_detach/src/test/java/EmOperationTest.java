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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.Transactional;

/*
    Przez hibernate jest wykorzystywana metoda prawidlowa dla danej bazy.
    Można jawnie zdefiniować strategię jako Auto, albo wcale jej nie definiować wtedy domyślnie zostanie użyta strategia auto.

    Jednak tak jak pokazuje ten przyklad (baza H2) gdy nie zdefiniuje sie strategii jako auto, to persystencja sie nie uda.
    Wiec lepiej zawsze podac strategie chocby jako AUTO.

 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EmOperationTest {

    private EntityManager entityManager;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void id_auto_generation_test() {
        Department department = new Department();
        department.setName("Humar Resources");

        Employee john = new Employee();
        john.setName("John");
        john.setSalary(3800);

        Employee mark = new Employee();
        mark.setName("Mark");
        mark.setSalary(4200);

        department.addEmployee(john);
        department.addEmployee(mark);

        System.out.println("Before persist(): " + entityManager.contains(department));
        entityManager.persist(department);
        System.out.println("After persist(): " + entityManager.contains(department));
        entityManager.flush();
        System.out.println("After flush(): " + entityManager.contains(department));
        entityManager.detach(department);
        System.out.println("After detach(): " + entityManager.contains(department));
        Department mergedDepartment = entityManager.merge(department);
        System.out.println("After merge() source instance: " + entityManager.contains(department));
        System.out.println("After merge() returned instance: " + entityManager.contains(mergedDepartment));
        entityManager.clear();
        System.out.println("After clear() returned merged instance: " + entityManager.contains(mergedDepartment));


    }


}