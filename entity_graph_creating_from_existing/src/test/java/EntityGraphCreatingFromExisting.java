import configuration.RootConfig;
import model.ContractEmployee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.AttributeNode;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/*
!!! IMPORTANT !!!
If a fetch graph is used, only the attributes specified by the entity graph will be treated as FetchType.EAGER.


 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EntityGraphCreatingFromExisting {

    Date today = new Date();
    Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Before
    public void prepareData() {


    }

    @Test
    @Transactional
    @Rollback(false)
    public void query_advanced_constructor_result_mapping() {
        System.out.println(" *** Entity graphs - creating from existing ***");

        printAllEntityGraphs();
        addEntityGraph();
        printAllEntityGraphs();

    }

    public void addEntityGraph() {
        EntityGraph<?> graph = entityManager.createEntityGraph("Employee.graph2");
        graph.addAttributeNodes("projects");
        entityManager.getEntityManagerFactory().addNamedEntityGraph("Employee.newGraph", graph);
        System.out.println("Added EntityGraph Employee.newGraph2to Employee <br/>");
    }

    public void printAllEntityGraphs() {
        System.out.println("EntityGraphs for ContractEmployee: <br/>");
        List<EntityGraph<? super ContractEmployee>> egList = entityManager.getEntityGraphs(ContractEmployee.class);
        for (EntityGraph<? super ContractEmployee> graph : egList) {
            System.out.println("EntityGraph: " + graph.getName() + "<br/>");
            List<AttributeNode<?>> attribs = graph.getAttributeNodes();
            for (AttributeNode<?> attr : attribs) {
                System.out.println(">>> Attribute: " + attr.getAttributeName() + "<br/>");
            }
        }
    }


}