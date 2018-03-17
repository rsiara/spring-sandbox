import configuration.RootConfig;
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

/*
Up to this point, we have dealt with the physical relationship mapping only as a join column, but, if the primary
key that we are referencing is composed of multiple fields, then we will need multiple join columns. This is why we
have the plural @JoinColumns annotation that can hold as many join columns as we need to put into it.
There are no default values for join column names when we have multiple join columns. The simplest answer
is to require the user to assign them, so, when multiple join columns are used, both the name element and the
referencedColumnName element, which indicates the name of the primary key column in the target table, must be
specified.

 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CompoundJoinTableColumnsTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void compound_join_table_columns() {
        System.out.println(" *** Compound join table columns ***");

        //Many to one - example of using Compound join table columsn

        Employee john = new Employee();
        john.setName("Mike Manager");
        john.setSalary(3400);
        john.setCountry("Poland");
        john.setId(11);

        Employee mark = new Employee();
        mark.setName("Mark");
        mark.setSalary(4800);
        mark.setCountry("Germany");
        mark.setId(22);
        mark.setManager(john);

        Employee mike = new Employee();
        mike.setName("Mike");
        mike.setSalary(6200);
        mike.setCountry("Italy");
        mike.setId(33);
        mike.setManager(john);

        entityManager.persist(john);
        entityManager.persist(mark);
        entityManager.persist(mike);
    }


}