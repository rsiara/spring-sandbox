
import configuration.RootConfig;
import model.Employee;
import model.Project;
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
import javax.persistence.Query;
import java.util.Collection;


@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ManyToManyBidirectionalTest {

  private EntityManager entityManager;

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Test
  @Transactional
  @Rollback(false)
  public void many_to_many_bidirectional_using_join_table_test() {
    Employee john = createEmployee("John", new Long(2400));
    Employee mike = createEmployee("Mike", new Long(3400));

    Project project = createProject("Big project");

    project.addEmployee(john);
    project.addEmployee(mike);

    entityManager.persist(project);
    entityManager.flush();
    entityManager.clear();
    // expect

    Query query = entityManager.createQuery("select e from Employee e");
    Collection<Employee> employees = query.getResultList();

     for(Employee e : employees){
       System.out.println(e);
     }

    query = entityManager.createQuery("select p from Project p");
    Collection<Project> projects = query.getResultList();

    for(Project p : projects){
      System.out.println(p);
      for(Employee e : p.getEmployees()){
        System.out.println(e);
      }
    }

  }

  private Employee createEmployee(String name,Long salary){
    Employee employee  = new Employee();
    employee.setName(name);
    employee.setSalary(salary);
    return employee;
  }

  private Project createProject(String name){
    Project project = new Project();
    project.setName(name);
    return project;
  }


}