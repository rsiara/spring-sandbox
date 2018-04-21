package model.type;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "CONTENT_ELEMENT_TYPE")
public class ContentElementType {

    @ElementCollection(targetClass = AttributeType.class)
    @JoinTable(name = "CONTENT_ELEMENT_TYPE_CONFIGURATION", joinColumns = @JoinColumn(name = "CONTENT_ELEMENT_ID"))
    @Column(name = "ATTRIBUTE_TYPE_ID", nullable = false)
    @Enumerated(EnumType.STRING)
    List<AttributeType> attributeConfiguration = new ArrayList<>();

    public List<AttributeType> getAttributeConfiguration() {
        return attributeConfiguration;
    }

    public void setAttributeConfiguration(List<AttributeType> attributeConfiguration) {
        this.attributeConfiguration = attributeConfiguration;
    }
}
