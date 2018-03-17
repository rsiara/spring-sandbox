import configuration.RootConfig;
import model.Employee;
import model.Evaluation;
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
import java.util.ArrayList;
import java.util.List;

/*
When specified, the orphanRemoval element causes the child entity to be removed when the relationship
between the parent and the child is broken. This can be done either by setting to null the attribute that holds the
related entity, or additionally in the one-to-many case by removing the child entity from the collection. The provider
is then responsible, at flush or commit time (whichever comes first), for removing the orphaned child entity.


orphanRemoval has nothing to do with ON DELETE CASCADE.

orphanRemoval is an entirely ORM-specific thing. It marks "child" entity to be removed
when it's no longer referenced from the "parent" entity, e.g. when you remove the child
entity from the corresponding collection of the parent entity.

ON DELETE CASCADE is a database-specific thing, it deletes the "child" row in the database when the "parent" row is deleted.

 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class OrphanRemovalTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void compound_join_table_columns() {
        System.out.println(" *** Orphan removal ***");

        Employee john = new Employee();
        john.setId(11);
        john.setName("John Manager");
        john.setEvals(new ArrayList<Evaluation>() {{
            add(new Evaluation(1, "Eval-1"));
            add(new Evaluation(2, "Eval-2"));
            add(new Evaluation(3, "Eval-3"));
            add(new Evaluation(4, "Eval-4"));
        }});

        Employee mark = new Employee();
        mark.setId(22);
        mark.setName("Mark Manager");
        mark.setEvals(new ArrayList<Evaluation>() {{
            add(new Evaluation(5, "Eval-5"));
            add(new Evaluation(6, "Eval-6"));
            add(new Evaluation(7, "Eval-7"));
            add(new Evaluation(8, "Eval-8"));

        }});

        entityManager.persist(john);
        entityManager.persist(mark);
        entityManager.flush();

        List<Evaluation> evaluationFromDb = findAllEvaluation();
/*
        All Evaluations #1
*/
        for (Evaluation e : evaluationFromDb) {
            System.out.println(e);
        }

        mark.getEvals().clear();
        entityManager.persist(mark);

        evaluationFromDb = findAllEvaluation();
/*
        All Evaluations #2
*/
        for (Evaluation e : evaluationFromDb) {
            System.out.println(e);
        }

    }

    public List<Evaluation> findAllEvaluation() {
        return entityManager.createQuery("SELECT e FROM Evaluation e", Evaluation.class)
                .getResultList();
    }


}