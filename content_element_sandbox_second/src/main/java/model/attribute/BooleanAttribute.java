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

@Entity(name = "BOOLEAN_ATTRIBUTE")
@Access(AccessType.FIELD)
public class BooleanAttribute extends BasicAttribute<List<Boolean>, Boolean> {

    @ElementCollection
    @CollectionTable(name = "BOOLEAN_VALUES", joinColumns = @JoinColumn(name = "BOOLEAN_ATTRIBUTE_ID"))
    @Column(name = "BOOLEAN_VALUE")
    private List<Boolean> values = new ArrayList<Boolean>();

    @Override
    public List<Boolean> getValue() {
        return values;
    }

    @Override
    public void setValues(List<Boolean> values) {
        if (values != null) {
            this.values = values;
            return;
        }
        throw new IllegalArgumentException("Value list cannot be null");
    }

    @Override
    public void addValue(Boolean value) {
        if (value != null) {
            this.values.add(value);
            return;
        }
        throw new IllegalArgumentException("Value cannot be null");
    }
}
