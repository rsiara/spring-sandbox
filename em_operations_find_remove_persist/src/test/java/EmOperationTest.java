import configuration.RootConfig;
import model.Employee;
import model.ParkingSpace;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/*

 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EmOperationTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void em_operations_find_remove_persist_test() {
        ParkingSpace johnParkingSpace = new ParkingSpace();
        johnParkingSpace.setLocation("Warsaw Parking");
        johnParkingSpace.setLot(23);

        Employee john = new Employee();
        john.setName("John");
        john.setParkingSpace(johnParkingSpace);

        ParkingSpace markParkingSpace = new ParkingSpace();
        markParkingSpace.setLocation("Warsaw Parking");
        markParkingSpace.setLot(23);

        Employee mark = new Employee();
        mark.setName("Mark");
        mark.setParkingSpace(markParkingSpace);


        entityManager.persist(john);
        entityManager.persist(mark);

        entityManager.flush();
        entityManager.clear();
        entityManager.close();


        em_operations_find_all_test();

        em_operations_find_by_id_test();

        em_operations_remove_parking_space_by_employee_id_test();

        em_incorect_operations_remove_parking_space_by_employee_id_test();


    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Rollback(false)
    public void em_operations_find_all_test() {
        System.out.println("### FIND ALL ###");
        List<Employee> employeesFromDB = findAllEmployees();

        for (Employee e : employeesFromDB) {
            System.out.println(e);
        }
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Rollback(false)
    public void em_operations_find_by_id_test() {
        System.out.println("### FIND BY ID ###");
        Employee employeeFromDB = findEmployeeById(1);
        System.out.println(employeeFromDB);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Rollback(false)
    public void em_operations_remove_parking_space_by_employee_id_test() {
        System.out.println("### REMOVE PARKING SPACE BY EMPLOYEE ID ###");
        Employee employeeFromDB = removeParkingSpace(1);

        System.out.println(employeeFromDB);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Rollback(false)
    public void em_incorect_operations_remove_parking_space_by_employee_id_test() {
        System.out.println("### INCORECT REMOVE PARKING SPACE BY EMPLOYEE ID ###");
        Employee employeeFromDB = removeParkingSpaceWithFailure(3);

        System.out.println(employeeFromDB);
    }

    public List<Employee> findAllEmployees() {
        return entityManager.createQuery("SELECT e FROM Employee e", Employee.class)
                .getResultList();
    }

    public Employee findEmployeeById(Integer id) {
        return entityManager.find(Employee.class, id);
    }

    // Removing with relationships

    public Employee removeParkingSpace(int empId) {
        Employee emp = entityManager.find(Employee.class, empId);
        ParkingSpace ps = emp.getParkingSpace();
        ps.setEmployee(null);
        emp.setParkingSpace(null);
        entityManager.remove(ps);
        return emp;
    }

    public Employee removeParkingSpaceWithFailure(int empId) {
        // forgetting to null out the relationship will cause a
        // db constraint failure
        Employee emp = entityManager.find(Employee.class, empId);
        entityManager.remove(emp.getParkingSpace());
        entityManager.flush();
        entityManager.clear();
        entityManager.close();
        return emp;
    }


}