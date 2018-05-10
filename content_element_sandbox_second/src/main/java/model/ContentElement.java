package model;

import model.attribute.Attribute;
import model.attribute.definition.AttributeDefinition;
import model.type.ContentElementType;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "CONTENT_ELEMENT")
@Access(AccessType.FIELD)
public class ContentElement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "CONTENT_ELEMENT_TYPE_ID")
    ContentElementType contentElementType;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "CONTENT_ELEMENT_TO_ATTRIBUTES",
            joinColumns = @JoinColumn(name = "CONTENT_ELEMENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "ATTRIBUTE_ID"))
    @MapKeyJoinColumn(name = "attributeDefinition")
    Map<AttributeDefinition, Attribute> attributeMap = new HashMap<>();

    public void addAttribute(Attribute attribute) {
        if (attribute != null) {
            attributeMap.put(attribute.getAttributeDefinition(), attribute);
            return;
        }
        throw new NullPointerException("Attribute parameter is null");
    }

    public ContentElementType getContentElementType() {
        return contentElementType;
    }

    public void setContentElementType(ContentElementType contentElementType) {
        this.contentElementType = contentElementType;
    }
}
