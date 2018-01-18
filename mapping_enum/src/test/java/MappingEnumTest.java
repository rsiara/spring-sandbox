import configuration.RootConfig;
import model.Employee;
import model.EmployeeType;
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

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MappingEnumTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


/*
    Jezeli zamiast Stringa ma byc przechowywana wartosc liczbowa, wystarczy usunac notacje: @Enumerated(STRING)
    Poniewaz JPA automatycznie obsluguje typy numeryczne. Jednak wartosc tekstowa jest odporna na zmiane kolejnosci enuma.
*/

    @Test
    @Transactional
    @Rollback(false)
    public void mapping_enum_test() {

        Employee employee = new Employee();
        employee.setName("John");
        employee.setSalary(3400);
        employee.setType(EmployeeType.FULL_TIME_EMPLOYEE);

        entityManager.persist(employee);
    }
}