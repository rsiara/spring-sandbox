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

   Identity sequencing uses special IDENTITY columns in the database to allow the database to
   automatically assign an id to the object when its row is inserted. Identity columns are supported in
   many databases, such as MySQL, DB2, SQL Server, Sybase and Postgres. Oracle does not support IDENTITY
   columns but they can be simulated through using sequence objects and triggers.
 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class IdDbIdendityGenerationTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void id_db_idendity_generation_test() {
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