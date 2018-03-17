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
Previously, when discussing embedded objects, we mapped the address fields of the Employee entity into
an Address embedded type. With the address data in a secondary table, it is still possible to do this by specifying
the mapped table name as part of the column information in the @AttributeOverride annotation. Listing 10-30
demonstrates this approach. Note that we have to enumerate all of the fields in the embedded type even though the
column names may match the correct default values.

*/

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MultipleTablesMappingOfEmbeddedTypeTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void multiple_tables_mapping_of_entity() {
        System.out.println(" *** Mapping entity across two tables ***");

        Employee john = new Employee();
        john.setId(11);
        john.setName("John");
        john.setSalary(2800);

        Address address = new Address();
        address.setCity("Warsaw");
        address.setState("Mazowieckie");
        address.setStreet("Kopernika");
        address.setZip("02-495");

        john.setAddress(address);

        entityManager.persist(john);
    }

    /*public List<Evaluation> findAllEvaluation() {
        return entityManager.createQuery("SELECT e FROM Evaluation e", Evaluation.class)
                .getResultList();
    }*/
}