import configuration.RootConfig;
import model.Employee;
import org.junit.Before;
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

/*

Constraint Attributes Description

    @Null Element must be null.

    @NotNull Element must not be null.

    @AssertTrue Element must be true.

    @AssertFalse Element must be false.

    @Min long value() Element must have a value greater than or equal to the minimum.

    @Max long value() Element must have a value less than or equal to the maximum.

    @DecimalMin String value() Element must have a value greater than or equal to the minimum.

    @DecimalMax String value() Element must have a value less than or equal to the maximum.

    @Size int min() Element must be of a length within the specified limits.
    int max()

    @Digits int integer() Element must be a number within the specified range.
    int fraction()

    @Past Element must be a date in the past.

    @Future Element must be a date in the future.

    @Pattern String regexpr() Element must match the specified regular expression.
    Flag[] flags (Flags offer regular expression settings.)

 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ConstraintAnnotation {

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
    public void constraint_annotation() {
        System.out.println(" *** Constraints - cusotm validation annotation***");

        Employee employee = new Employee();
        employee.setName("John - name exceed 40 chars ");
        employee.setId(12);
        employee.setVacationHours(3);

        entityManager.persist(employee);
        entityManager.flush();
    }

    public Employee findEmployee(int id) {
        return entityManager.find(Employee.class, id);
    }


}