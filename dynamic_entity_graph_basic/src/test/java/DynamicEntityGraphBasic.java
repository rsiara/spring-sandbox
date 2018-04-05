import configuration.RootConfig;
import model.Address;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/*
!!! IMPORTANT !!!
If a fetch graph is used, only the attributes specified by the entity graph will be treated as FetchType.EAGER.


 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class DynamicEntityGraphBasic {

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
        System.out.println(" *** Entity graphs - dynamic entity graphs***");

        for (Address address : findAllAddresses()) {
            System.out.println(address);
        }

    }

    public List<Address> findAllAddresses() {
        TypedQuery<Address> query = entityManager.createQuery(
                "SELECT a FROM Address a",
                Address.class);
        query.setHint("javax.persistence.fetchgraph", constructEntityGraph());
        return query.getResultList();
    }

    public EntityGraph<Address> constructEntityGraph() {
        EntityGraph<Address> graph = entityManager.createEntityGraph(Address.class);
        graph.addAttributeNodes("street", "city", "state", "zip");
        return graph;
    }


}