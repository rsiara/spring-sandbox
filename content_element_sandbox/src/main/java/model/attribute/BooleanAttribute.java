package model.attribute;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "BOOLEAN_ATTRIBUTE")
@Access(AccessType.FIELD)
public class BooleanAttribute extends BasicAttribute {

    @Column
    boolean value;

    @Override
    public Object get() {
        return new Boolean(value);
    }

    @Override
    public void set(Object value) {
        if (value != null) {
            if (value instanceof Boolean) {
                value = value;
                return;
            }
            throw new IllegalArgumentException("Boolean type of value exptected");
        }
    }

    @Override
    public String toString() {
        return "BooleanAttribute{" +
                "value=" + value +
                ", name='" + name + '\'' +
                '}';
    }
}
