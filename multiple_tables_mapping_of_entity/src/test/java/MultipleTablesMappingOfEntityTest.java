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
To map this table structure to the Employee entity, we must declare EMP_ADDRESS as a secondary table and use
the table element of the @Column annotation for every attribute stored in that table. Listing 10-29 shows the mapped
entity. The primary key of the EMP_ADDRESS table is in the EMP_ID column. If it had been named ID, then we would
not have needed to use the name element in the @PrimaryKeyJoinColumn annotation. It defaults to the name of the
primary key column in the primary table.

*/

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MultipleTablesMappingOfEntityTest {

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
        john.setCity("Warsaw");
        john.setState("Mazowsze");
        john.setStreet("Kopernika");
        john.setZip("02-495");

        entityManager.persist(john);
    }

    /*public List<Evaluation> findAllEvaluation() {
        return entityManager.createQuery("SELECT e FROM Evaluation e", Evaluation.class)
                .getResultList();
    }*/

    @Test
    @Transactional
    @Rollback(false)
    public void dump_test() {
        System.out.println(" *** Dump test ***");

        String stringToReplace = " wor12d prefix otherWord   afterTwoSpaceWord";

        stringToReplace = stringToReplace.replaceAll("(?<=\\s|^)(?=\\S)", "ux-");
        System.out.println(stringToReplace);

        stringToReplace = stringToReplace.replaceAll("ux-", "");
        System.out.println(stringToReplace);
    }
}