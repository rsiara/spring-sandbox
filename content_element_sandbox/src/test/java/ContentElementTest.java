import configuration.RootConfig;
import model.attribute.BasicAttribute;
import model.attribute.BooleanAttribute;
import model.attribute.ComplexAttribute;
import model.attribute.StringAttribute;
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
 * Domyslny poziom dostepu to AccessType
 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ContentElementTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void content_element_client() {

        basic_and_complex_attribute_persistence_test();

        content_element_type_persistence_configuration_test();


    }

    private void basic_and_complex_attribute_persistence_test() {
        BasicAttribute visibilityAttribute = new BooleanAttribute();
        visibilityAttribute.setName("Visibility");
        visibilityAttribute.set(true);

        entityManager.persist(visibilityAttribute);

        BasicAttribute transparentBackgroundAttribute = new BooleanAttribute();
        transparentBackgroundAttribute.setName("Background transparence");
        transparentBackgroundAttribute.set(true);

        entityManager.persist(transparentBackgroundAttribute);

        StringAttribute labelTextAttribute = new StringAttribute();
        labelTextAttribute.setName("Label text");
        labelTextAttribute.set("Wydarzenie");

        entityManager.persist(labelTextAttribute);

        ComplexAttribute tagAttribute = new ComplexAttribute();
        tagAttribute.setName("Label tag");
        tagAttribute.addAttribute(transparentBackgroundAttribute);
        tagAttribute.addAttribute(labelTextAttribute);

        entityManager.persist(tagAttribute);

        System.out.println(visibilityAttribute);
        System.out.println(tagAttribute);
    }

    private void basic_and_complex_attribute_test() {
        BasicAttribute visibilityAttribute = new BooleanAttribute();
        visibilityAttribute.setName("Visibility");
        visibilityAttribute.set(true);

        BasicAttribute transparentBackgroundAttribute = new BooleanAttribute();
        transparentBackgroundAttribute.setName("Background transparence");
        transparentBackgroundAttribute.set(true);

        StringAttribute labelTextAttribute = new StringAttribute();
        labelTextAttribute.setName("Label text");
        labelTextAttribute.set("Wydarzenie");

        ComplexAttribute tagAttribute = new ComplexAttribute();
        tagAttribute.setName("Label tag");
        tagAttribute.addAttribute(transparentBackgroundAttribute);
        tagAttribute.addAttribute(labelTextAttribute);

        System.out.println(visibilityAttribute);
        System.out.println(tagAttribute);
    }


}