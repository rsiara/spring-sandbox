package model.attribute;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "INTEGER_ATTRIBUTE")
@Access(AccessType.FIELD)
public class IntegerAttribute extends BasicAttribute {

    @Column
    int value;

    @Override
    public Object get() {
        return new Integer(value);
    }

    @Override
    public void set(Object value) {
        if (value != null) {
            if (value instanceof Integer) {
                this.value = (Integer) value;
                return;
            }
            throw new IllegalArgumentException("Integer type of value exptected");
        }
    }

    @Override
    public String toString() {
        return "BooleanAttribute{" +
                "value=" + value +
                '}';
    }
}
