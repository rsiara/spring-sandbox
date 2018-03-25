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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/*
If a fetch graph is used, only the attributes specified by the entity graph will be treated as FetchType.EAGER.
All other attributes will be lazy. If a load graph is used,
attributes that are not specified by the entity graph will keep their default fetch type.

If you're talking about basic types, then, by default,
Hibernate will always fetch them. The only way to enable basic type lazy loading is to use bytecode enhancement,
as explained in this article.

If you're talking about basic types, then, by default, Hibernate will always fetch them.
The only way to enable basic type lazy loading is to use bytecode enhancement, as explained in this article.
If you're talking about EAGER associations, then Hibernate cannot override them to LAZY, even if the JPA standard says it should.

Both 1. and 2. are not mandatory requirements from a JPA perspective because LAZY is just a hint for the JPA provider.

All in all, JPA entity graphs are a suboptimal way of fetching data. Avoiding EAGER associations, using subentities and DTO projections are much better than entity graphs.

Most of the time, you don't even need to fetch entities because entities make sense only if you plan to modify. Otherwise, a DTO projection will always be way more efficient.

https://stackoverflow.com/questions/42211312/how-to-limit-columns-used-in-a-hibernate-entity-graph

 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EntityGraphNamedEntityGraphBasic {

    Date today = new Date();
    Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Before
    public void prepareData() {
        // My address


        Address myAddress = new Address();
        myAddress.setCity("Gdanski");
        myAddress.setZip("02-120");
        myAddress.setStreet("Mlynska");
        myAddress.setState("Pomorskie");

        entityManager.persist(myAddress);
        entityManager.flush();
    }

    @Test
    @Transactional
    @Rollback(false)
    public void query_advanced_constructor_result_mapping() {
        System.out.println(" *** Entity graphs - named entity graph basic***");

        for (Address address : findAllAddress()) {
            System.out.println(address.getCity());
        }

    }

    public List<Address> findAllAddress() {
        return entityManager.createQuery("SELECT e FROM Address e", Address.class)
                .getResultList();
    }


}