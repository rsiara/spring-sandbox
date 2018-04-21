package model.attribute.definition;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "BASIC_ATTRIBUTE_DEFINITION")
@Access(AccessType.FIELD)
public class BasicAttributeDefinition extends AttributeDefinition {

    @Column
    boolean multivalue;

    public boolean isMultivalue() {
        return multivalue;
    }

    public void setMultivalue(boolean multivalue) {
        this.multivalue = multivalue;
    }
}
