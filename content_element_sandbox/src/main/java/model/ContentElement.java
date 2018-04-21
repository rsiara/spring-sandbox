package model;

import model.attribute.Attribute;
import model.type.ContentElementType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    List<Attribute> attributeList = new ArrayList<>();

    public void addAttribute(Attribute attribute) {
        if (attribute != null) {
            attributeList.add(attribute);
            return;
        }
        throw new NullPointerException("Attribute parameter is null");
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }

    public ContentElementType getContentElementType() {
        return contentElementType;
    }

    public void setContentElementType(ContentElementType contentElementType) {
        this.contentElementType = contentElementType;
    }
}
