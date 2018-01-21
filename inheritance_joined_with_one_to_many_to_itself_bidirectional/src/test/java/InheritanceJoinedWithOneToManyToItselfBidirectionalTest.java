import configuration.RootConfig;
import model.Node;
import model.NodeType;
import model.TestAB;
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
public class InheritanceJoinedWithOneToManyToItselfBidirectionalTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void inheritance_joined_with_one_to_many_to_itself_bidirectional() {

        Node node = new Node();
        node.setCreationDate(new Date());
        node.setUser("Robert Siara");
        node.setNodeType(NodeType.PAGE);

        Node node6 = new Node();
        node6.setCreationDate(new Date());
        node6.setUser("Kevin Clein");
        node6.setNodeType(NodeType.PAGE);


        //TEST ABs

        TestAB testABone = new TestAB();
        testABone.setCreationDate(new Date());
        testABone.setUser("Mirko Blank");
        testABone.setNodeType(NodeType.TEST_AB);
        testABone.setTestABstartDate(new Date());
        testABone.setTestABendDate(new Date());

        TestAB testABtwo = new TestAB();
        testABtwo.setCreationDate(new Date());
        testABtwo.setUser("Tony Devito");
        testABtwo.setNodeType(NodeType.TEST_AB);
        testABtwo.setTestABstartDate(new Date());
        testABtwo.setTestABendDate(new Date());

        TestAB testABtree = new TestAB();
        testABtree.setCreationDate(new Date());
        testABtree.setUser("Greg Mockito");
        testABtree.setNodeType(NodeType.TEST_AB);
        testABtree.setTestABstartDate(new Date());
        testABtree.setTestABendDate(new Date());

        //Add to parent TEST ABs

        node.addTestAB(testABone);
        node.addTestAB(testABtwo);
        node.addTestAB(testABtree);
        node6.setParent(node);


        entityManager.persist(testABone);
        entityManager.persist(testABtwo);
        entityManager.persist(testABtree);
        entityManager.persist(node6);


        entityManager.persist(node);

        entityManager.flush();
        entityManager.clear();
        entityManager.close();


        List<TestAB> listNode = entityManager.createQuery(
                "SELECT n FROM Node n").getResultList();

        for (Node n : listNode) {
            System.out.println("- " + n.getId() + " " + n.getUser() + " " + n.getNodeType());
            System.out.println("    + TESTs AN");
            for (TestAB testAB : n.getTestAbCollection())
                System.out.println("- " + testAB.getId() + " " + testAB.getUser() + " " + testAB.getNodeType());
        }
    }


}