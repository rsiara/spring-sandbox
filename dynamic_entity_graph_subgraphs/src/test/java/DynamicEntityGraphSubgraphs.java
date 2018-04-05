import configuration.RootConfig;
import model.Department;
import model.Employee;
import model.Phone;
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
import javax.persistence.Subgraph;
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
public class DynamicEntityGraphSubgraphs {

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

        for (Employee employee : findAllEmployees()) {
            System.out.println(employee);
        }

    }

    public List<Employee> findAllEmployees() {
        TypedQuery<Employee> query = entityManager.createQuery(
                "SELECT e FROM Employee e",
                Employee.class);
        query.setHint("javax.persistence.fetchgraph", constructEntityGraph());
        return query.getResultList();
    }

    public EntityGraph<Employee> constructEntityGraph() {
        EntityGraph<Employee> graph = entityManager.createEntityGraph(Employee.class);
        graph.addAttributeNodes("name", "salary", "address");
        Subgraph<Phone> phone = graph.addSubgraph("phones");
        phone.addAttributeNodes("number", "type");
        Subgraph<Department> dept = graph.addSubgraph("department");
        dept.addAttributeNodes("name");
        return graph;
    }


}