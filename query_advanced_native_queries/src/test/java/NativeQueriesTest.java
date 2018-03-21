import configuration.RootConfig;
import model.Address;
import model.Department;
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
import java.util.Date;
import java.util.List;

/*


 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class NativeQueriesTest {

    private EntityManager entityManager;

    Date today = new Date();
    Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void native_query_test() {
        System.out.println(" *** Advanced queries - native queries ***");

        Employee mark = new Employee();
        mark.setId(11);
        mark.setName("Mark");
        mark.setManager(mark);
        mark.setSalary(6400);

        Address markAddress = new Address();
        markAddress.setCity("Warsaw");
        markAddress.setState("Mazowieckie");
        markAddress.setStreet("Mazowiecka");
        markAddress.setZip("02-495");
        mark.setAddress(markAddress);

        Department executiveDepartment = new Department();
        executiveDepartment.setId(1);
        executiveDepartment.setName("Executive department");

        mark.setDepartment(executiveDepartment);


        Employee john = new Employee();
        john.setId(12);
        john.setName("John");
        john.setManager(mark);
        john.setSalary(4200);

        Address johnAddress = new Address();
        johnAddress.setCity("Krakow");
        johnAddress.setState("Malopolskie");
        johnAddress.setStreet("Stary kazimierz");
        johnAddress.setZip("03-244");
        john.setAddress(johnAddress);

        Department itDepartment = new Department();
        itDepartment.setId(2);
        itDepartment.setName("IT department");

        john.setDepartment(itDepartment);

        Employee mike = new Employee();
        mike.setId(13);
        mike.setName("Mike");
        mike.setManager(mark);
        mike.setSalary(3400);

        Address mikeAddress = new Address();
        mikeAddress.setCity("Rzeszow");
        mikeAddress.setState("Podkarpackie");
        mikeAddress.setStreet("Lwowska");
        mikeAddress.setZip("04-322");
        mike.setAddress(mikeAddress);
        mike.setDepartment(itDepartment);

        entityManager.persist(executiveDepartment);
        entityManager.persist(itDepartment);

        entityManager.persist(mark);
        entityManager.persist(john);
        entityManager.persist(mike);

        entityManager.flush();

        for (Employee employee : findEmployeesReportingTo(mark.getId())) {
            System.out.println(employee);
        }
    }

    public List<Employee> findEmployeesReportingTo(int managerId) {
        return entityManager.createNamedQuery("orgStructureReportingTo")
                .setParameter(1, managerId)
                .getResultList();
    }
}