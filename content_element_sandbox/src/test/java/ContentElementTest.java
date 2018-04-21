import configuration.RootConfig;
import helper.AttributeFactory;
import model.ContentElement;
import model.attribute.Attribute;
import model.attribute.BasicAttribute;
import model.attribute.ComplexAttribute;
import model.attribute.definition.AttributeDefinition;
import model.attribute.definition.BasicAttributeDefinition;
import model.attribute.definition.ComplexAttributeDefinition;
import model.type.AttributeType;
import model.type.ContentElementType;
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
import java.util.List;

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
        full_scenario_test();

    }

    public void full_scenario_test() {
        // Creating new "content element type" with basic and complex attributes
        ContentElementType articleContentElementType = new ContentElementType();
        articleContentElementType.setName("Artykul");

        // Add 3 basic attribute definition
        BasicAttributeDefinition authorAttributeDefinition = new BasicAttributeDefinition();
        authorAttributeDefinition.setName("Author");
        authorAttributeDefinition.setPriority(1);
        authorAttributeDefinition.setAttributeType(AttributeType.STRING);
        authorAttributeDefinition.setMultivalue(true);

        BasicAttributeDefinition sponsoredAttributeDefinition = new BasicAttributeDefinition();
        sponsoredAttributeDefinition.setName("Sponsorowany");
        sponsoredAttributeDefinition.setPriority(2);
        sponsoredAttributeDefinition.setAttributeType(AttributeType.BOOLEAN);
        sponsoredAttributeDefinition.setMultivalue(false);

        BasicAttributeDefinition numberOfSthAttributeDefinition = new BasicAttributeDefinition();
        numberOfSthAttributeDefinition.setName("Liczba czegos");
        numberOfSthAttributeDefinition.setPriority(3);
        numberOfSthAttributeDefinition.setAttributeType(AttributeType.INTEGER);
        numberOfSthAttributeDefinition.setMultivalue(false);


        articleContentElementType.addAttributeDefinition(authorAttributeDefinition);
        articleContentElementType.addAttributeDefinition(sponsoredAttributeDefinition);
        articleContentElementType.addAttributeDefinition(numberOfSthAttributeDefinition);

        //Add 1 Complex attribute definition
        ComplexAttributeDefinition tagComplexAttributeDefinition = new ComplexAttributeDefinition();
        tagComplexAttributeDefinition.setName("Tag");
        tagComplexAttributeDefinition.setPriority(4);
        tagComplexAttributeDefinition.setAttributeType(AttributeType.COMPLEX);


        //Assembling tag complex attribute definition using 2 basic sub attributes
        BasicAttributeDefinition tagColorSubAttributeDefinition = new BasicAttributeDefinition();
        tagColorSubAttributeDefinition.setName("Kolor");
        tagColorSubAttributeDefinition.setPriority(1);
        tagColorSubAttributeDefinition.setAttributeType(AttributeType.STRING);
        tagColorSubAttributeDefinition.setMultivalue(false);

        BasicAttributeDefinition tagLabelTextSubAttributeDefinition = new BasicAttributeDefinition();
        tagLabelTextSubAttributeDefinition.setName("Tekst tagu");
        tagLabelTextSubAttributeDefinition.setPriority(2);
        tagLabelTextSubAttributeDefinition.setAttributeType(AttributeType.STRING);
        tagLabelTextSubAttributeDefinition.setMultivalue(false);

        tagComplexAttributeDefinition.addAttributeDefinition(tagColorSubAttributeDefinition);
        tagComplexAttributeDefinition.addAttributeDefinition(tagLabelTextSubAttributeDefinition);

        articleContentElementType.addAttributeDefinition(tagComplexAttributeDefinition);

        entityManager.persist(articleContentElementType);

        // Creating new "content element" based on above "content element type"
        ContentElement contentElement = new ContentElement();
        // Set selected "content element typ" in this example "Artykul"
        contentElement.setContentElementType(articleContentElementType);

        // Load list of all attribute definitions for this "content element type"
        List<AttributeDefinition> articleAttributeDefinitions = articleContentElementType.getAttributeDefinitions();

        //Attribute factory is helper class for pupropse of this test
        AttributeFactory attributeFactory = new AttributeFactory();

        // UI Content element creator simulation
        for (AttributeDefinition attributeDefinition : articleAttributeDefinitions) {

            Attribute attribute = attributeFactory.createAttribute(attributeDefinition.getAttributeType());
            attribute.setAttributeDefinition(attributeDefinition);

            String attributeName = attribute.getAttributeDefinition().getName();
            System.out.println(attributeName);

            if (attribute instanceof BasicAttribute) {
                switch (attributeName) {
                    case "Author":
                        ((BasicAttribute) attribute).set("Pan dziennikarz");
                        break;
                    case "Sponsorowany":
                        ((BasicAttribute) attribute).set(true);
                        break;
                    case "Liczba czegos":
                        ((BasicAttribute) attribute).set(23);
                        break;
                }

            } else {
                ComplexAttributeDefinition complexAttributeDefinition = (ComplexAttributeDefinition) attribute.getAttributeDefinition();
                for (BasicAttributeDefinition subAttributeDefinition : complexAttributeDefinition.getSubAttributeDefinitions()) {

                    Attribute subAttribute = attributeFactory.createAttribute(subAttributeDefinition.getAttributeType());
                    subAttribute.setAttributeDefinition(subAttributeDefinition);

                    String subAttributeName = subAttribute.getAttributeDefinition().getName();
                    System.out.println(subAttributeName);

                    switch (subAttributeName) {
                        case "Kolor":
                            ((BasicAttribute) subAttribute).set("CZERWONY");
                            break;
                        case "Tekst tagu":
                            ((BasicAttribute) subAttribute).set("Artykul");
                            break;
                    }
                    //Add subattribute to complex attribute
                    ((ComplexAttribute) attribute).addSubAttribute((BasicAttribute) subAttribute);
                }
            }
            //Add filled attribute to content element
            contentElement.addAttribute(attribute);
        }

        //Save "content element"
        entityManager.persist(contentElement);
    }

}