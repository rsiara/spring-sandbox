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

 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class IdSequenceGenerationTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void id_sequence_generation_test() {
        Employee john = new Employee();
        john.setName("John");
        john.setSalary(3400);

        Employee mark = new Employee();
        mark.setName("Mark");
        mark.setSalary(3800);

        Employee stephen = new Employee();
        stephen.setName("Stephen");
        stephen.setSalary(4200);

        entityManager.persist(john);
        entityManager.persist(mark);
        entityManager.persist(stephen);

        entityManager.flush();
        entityManager.clear();
        entityManager.close();

        System.out.println("model.Employee after persist");
        System.out.println(john);
        System.out.println(mark);
        System.out.println(stephen);

    }


}