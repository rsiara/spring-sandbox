package model.attribute;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "STRING_ATTRIBUTE")
@Access(AccessType.FIELD)
public class StringAttribute extends BasicAttribute {

    @Column
    String value;

    @Override
    public Object get() {
        return value;
    }

    @Override
    public void set(Object value) {
        if (value != null) {
            if (value instanceof String) {
                this.value = (String) value;
                return;
            }
            throw new IllegalArgumentException("String type of value exptected");
        }
    }

    @Override
    public String toString() {
        return "StringAttribute{" +
                "value=" + value +
                ", name='" + name + '\'' +
                '}';
    }
}
