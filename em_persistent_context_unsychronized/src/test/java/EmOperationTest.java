import configuration.RootConfig;
import model.Department;
import model.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.SynchronizationType;
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

    @PersistenceContext(type = PersistenceContextType.EXTENDED,
            synchronization = SynchronizationType.UNSYNCHRONIZED)
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    public void em_persistent_context_unsychronized_test() {
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

        //Ta metoda powoduje polaczenie z sesja
        processAllChanges();

        System.out.println("Before persist(): " + entityManager.contains(department));
        entityManager.persist(department);
        System.out.println("After persist(): " + entityManager.contains(department));
        entityManager.flush();


    }

    // End conversation
    public void processAllChanges() {
        entityManager.joinTransaction();
    }


}