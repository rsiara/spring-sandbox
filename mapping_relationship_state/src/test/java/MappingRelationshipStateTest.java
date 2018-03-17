import configuration.RootConfig;
import model.Employee;
import model.Project;
import model.ProjectAssignment;
import model.ProjectAssignmentId;
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
The Java solution is to turn the relationship into an entity that contains the desired state and map the new entity
to what was previously the join table. The new entity will have a many-to-one relationship to each of the existing
entity types, and each of the entity types will have a one-to-many relationship back to the new entity representing the
relationship. The primary key of the new entity will be the combination of the two relationships to the two entity types.
Listing 10-28 shows all of the participants in the Employee and Project relationship.
 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MappingRelationshipStateTest {

    private EntityManager entityManager;
    private final Date today = new Date();


    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void mapping_relationship_state_test() {
        System.out.println(" *** Mapping relationship state ***");

        Employee john = new Employee();
        john.setId(11);
        john.setName("John Manager");

        Project bigProject = new Project();
        bigProject.setId(22);
        bigProject.setName("Big Project");

        ProjectAssignment projectAssignment = new ProjectAssignment(john, bigProject);

        entityManager.persist(john);
        entityManager.persist(bigProject);
        entityManager.persist(projectAssignment);

        entityManager.flush();

        ProjectAssignmentId projectAssignmentId = new ProjectAssignmentId(john.getId(), bigProject.getId());

        List<ProjectAssignment> projectAssignments = findAllProjectAssigment(projectAssignmentId);

        for(ProjectAssignment projectAssigns : projectAssignments){
            System.out.println(projectAssigns);
        }
    }

    public List<ProjectAssignment> findAllProjectAssigment(ProjectAssignmentId projectAssigmentId) {
        return entityManager.createQuery("SELECT e FROM ProjectAssignment e", ProjectAssignment.class)
                .getResultList();
    }


}