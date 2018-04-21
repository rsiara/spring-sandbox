package model.attribute;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity(name = "BASIC_ATTRIBUTE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class BasicAttribute extends Attribute {

    public abstract Object get();

    public abstract void set(Object values);
}
