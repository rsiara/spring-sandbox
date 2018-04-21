package model.attribute;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "INTEGER_ATTRIBUTE")
@Access(AccessType.FIELD)
public class IntegerAttribute extends BasicAttribute<List<Integer>, Integer> {

    @ElementCollection
    @CollectionTable(name = "INTEGER_VALUES", joinColumns = @JoinColumn(name = "INTEGER_ATTRIBUTE_ID"))
    @Column(name = "INTEGER_VALUE")
    private List<Integer> values = new ArrayList<Integer>();

    @Override
    public List<Integer> getValue() {
        return values;
    }

    @Override
    public void setValues(List<Integer> values) {
        if (values != null) {
            this.values = values;
            return;
        }
        throw new IllegalArgumentException("Value list cannot be null");
    }

    @Override
    public void addValue(Integer value) {
        if (value != null) {
            this.values.add(value);
            return;
        }
        throw new IllegalArgumentException("Value cannot be null");
    }
}
