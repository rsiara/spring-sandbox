package model.attribute;

import model.attribute.definition.AttributeDefinition;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "ATTRIBUTE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "ATTRIBUTE_DEF_ID")
    AttributeDefinition attributeDefinition;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AttributeDefinition getAttributeDefinition() {
        return attributeDefinition;
    }

    public void setAttributeDefinition(AttributeDefinition attributeDefinition) {
        this.attributeDefinition = attributeDefinition;
    }


}
