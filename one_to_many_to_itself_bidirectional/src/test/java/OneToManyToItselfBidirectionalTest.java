import configuration.RootConfig;
import model.Node;
import model.NodeType;
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
import java.util.List;

/*

 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class OneToManyToItselfBidirectionalTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void one_to_many_itself_bidirectional_test() {

        Node node = new Node();
        node.setCreationDate(new Date());
        node.setUser("Robert Siara");
        node.setNodeType(NodeType.PAGE);

        //TEST ABs

        Node testABone = new Node();
        testABone.setCreationDate(new Date());
        testABone.setUser("Robert Siara");
        testABone.setNodeType(NodeType.TEST_AB);

        Node testABtwo = new Node();
        testABtwo.setCreationDate(new Date());
        testABtwo.setUser("Robert Siara");
        testABtwo.setNodeType(NodeType.TEST_AB);

        //Add to parent TEST ABs

        node.addTestAB(testABone);
        node.addTestAB(testABtwo);

        entityManager.persist(testABone);
        entityManager.persist(testABtwo);

        entityManager.persist(node);

        entityManager.flush();
        entityManager.clear();
        entityManager.close();


        List<Node> listNode = entityManager.createQuery(
                "SELECT n FROM Node n").getResultList();

        for (Node n : listNode) {
        }
    }


}