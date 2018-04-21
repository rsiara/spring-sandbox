package model.attribute;

import javax.persistence.Entity;

@Entity(name = "BASIC_ATTRIBUTE")
public abstract class BasicAttribute extends Attribute {

    public abstract Object get();

    public abstract void set(Object value);

}
