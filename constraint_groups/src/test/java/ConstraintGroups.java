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
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

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
public class ConstraintGroups {

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

    /*
    !IMPORTANT! To run this test you have to uncomment:
            properties.setProperty("javax.persistence.validation.group.pre-remove", "model.FullTime");
    in JpaConfig
    */


    @Test
    @Transactional
    @Rollback(false)
    public void lifecycle_callbacks_inheritance() {
        System.out.println(" *** Constraints - some validator***");

        Employee employee = new Employee();
        employee.setName("John - name exceed 40 chars ");
        employee.setId(12);
        employee.setStartDate(tomorrow);


        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // Perform manual validation on the new employee
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        for (ConstraintViolation<Employee> constraintViolation : violations) {
            System.out.printf(constraintViolation.getMessage());
        }

        entityManager.persist(employee);
        entityManager.flush();
        entityManager.clear();


        employee = findEmployee(employee.getId());
        entityManager.remove(employee);
    }

    public Employee findEmployee(int id) {
        return entityManager.find(Employee.class, id);
    }


}