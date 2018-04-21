package model.type;

import model.attribute.definition.AttributeDefinition;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "CONTENT_ELEMENT_TYPE")
@Access(AccessType.FIELD)
public class ContentElementType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column
    String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "CONTENT_ELEMENT_TYPE_TO_ATTR_DEFINITIONS",
            joinColumns = @JoinColumn(name = "CONTENT_ELEMENT_TYPE_ID"),
            inverseJoinColumns = @JoinColumn(name = "ATTR_DEFINITION_ID"))
    List<AttributeDefinition> attributeDefinitions = new ArrayList<>();

    public void addAttributeDefinition(AttributeDefinition attributeDefinition) {
        if (attributeDefinition != null) {
            attributeDefinitions.add(attributeDefinition);
            return;
        }
        throw new NullPointerException("BasicAttributeDefinition is null");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AttributeDefinition> getAttributeDefinitions() {
        return attributeDefinitions;
    }

    public void setAttributeDefinitions(List<AttributeDefinition> attributeDefinitions) {
        this.attributeDefinitions = attributeDefinitions;
    }
}
