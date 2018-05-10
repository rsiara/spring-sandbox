package model.attribute;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity(name = "BASIC_ATTRIBUTE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class BasicAttribute<T, E> extends Attribute {

    public abstract T getValue();

    public abstract void setValues(T values);

    public abstract void addValue(E value);

}
