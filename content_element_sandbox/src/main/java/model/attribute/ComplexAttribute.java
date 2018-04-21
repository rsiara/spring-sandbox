package model.attribute;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "COMPLEX_ATTRIBUTE")
@Access(AccessType.FIELD)
public class ComplexAttribute extends Attribute {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "COMPLEX_ATTR_TO_BASICS_ATTR",
            joinColumns = @JoinColumn(name = "COMPLEX_ATTR_ID"),
            inverseJoinColumns = @JoinColumn(name = "BASIC_ATTR_ID"))
    List<BasicAttribute> basicAttributes = new ArrayList<>();

    public void addAttribute(BasicAttribute basicAttribute) {
        if (basicAttribute != null) {
            basicAttributes.add(basicAttribute);
        }
    }

    public List<BasicAttribute> getBasicAttributes() {
        return basicAttributes;
    }

    @Override
    public String toString() {
        return "ComplexAttribute{" +
                "basicAttributes=" + basicAttributes +
                ", name='" + name + '\'' +
                '}';
    }
}

