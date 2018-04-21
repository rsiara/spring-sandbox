package model.attribute.definition;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "COMPLEX_ATTRIBUTE_DEFINITION")
@Access(AccessType.FIELD)
public class ComplexAttributeDefinition extends AttributeDefinition {
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "COMPLEX_ATTR_DEF_TO_BASICS_ATTR_DEF",
            joinColumns = @JoinColumn(name = "COMPLEX_ATTR_DEF_ID"),
            inverseJoinColumns = @JoinColumn(name = "BASIC_ATTR_DEF_ID"))
    List<BasicAttributeDefinition> subAttributeDefinitions = new ArrayList<>();

    public List<BasicAttributeDefinition> getSubAttributeDefinitions() {
        return subAttributeDefinitions;
    }

    public void setSubAttributeDefinitions(List<BasicAttributeDefinition> subAttributeDefinitions) {
        this.subAttributeDefinitions = subAttributeDefinitions;
    }

    public void addAttributeDefinition(BasicAttributeDefinition basicAttributeDefinition) {
        if (basicAttributeDefinition != null) {
            subAttributeDefinitions.add(basicAttributeDefinition);
            return;
        }
        throw new NullPointerException("BasicAttributeDefinition is null");
    }
}
