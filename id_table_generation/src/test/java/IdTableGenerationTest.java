import configuration.RootConfig;
import model.Address;
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
    Przez hibernate jest wykorzystywana metoda prawidlowa dla danej bazy.
    Można jawnie zdefiniować strategię jako Auto, albo wcale jej nie definiować wtedy domyślnie zostanie użyta strategia auto.

    Jednak tak jak pokazuje ten przyklad (baza H2) gdy nie zdefiniuje sie strategii jako auto, to persystencja sie nie uda.
    Wiec lepiej zawsze podac strategie chocby jako AUTO.

 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class IdTableGenerationTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void id_table_generation_test() {
        Employee john = new Employee();
        john.setName("John");
        john.setSalary(3400);

        Address johnAddress = new Address();
        johnAddress.setCity("Warsaw");
        johnAddress.setState("Mazowieckie");
        johnAddress.setStreet("Kopernika");
        johnAddress.setZip("02-495");

        john.setAddress(johnAddress);

        Employee mark = new Employee();
        mark.setName("Mark");
        mark.setSalary(3800);

        Address markAddress = new Address();
        markAddress.setCity("Cracow");
        markAddress.setState("Malopolskie");
        markAddress.setStreet("Lipska");
        markAddress.setZip("01-366");

        mark.setAddress(markAddress);


        Employee stephen = new Employee();
        stephen.setName("Stephen");
        stephen.setSalary(4200);

        Address stephenAddress = new Address();
        stephenAddress.setCity("Breslau");
        stephenAddress.setState("Dolnoslaskie");
        stephenAddress.setStreet("Mackowiaka");
        stephenAddress.setZip("03-822");

        stephen.setAddress(stephenAddress);

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