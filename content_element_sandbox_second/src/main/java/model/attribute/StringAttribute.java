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

@Entity(name = "STRING_ATTRIBUTE")
@Access(AccessType.FIELD)
public class StringAttribute extends BasicAttribute<List<String>, String> {

    @ElementCollection
    @CollectionTable(name = "STRING_VALUES", joinColumns = @JoinColumn(name = "STRING_ATTRIBUTE_ID"))
    @Column(name = "STRING_VALUE")
    private List<String> values = new ArrayList<String>();

    @Override
    public List<String> getValue() {
        return values;
    }

    @Override
    public void setValues(List<String> values) {
        if (values != null) {
            this.values = values;
            return;
        }
        throw new IllegalArgumentException("Value list cannot be null");
    }

    @Override
    public void addValue(String value) {
        if (value != null) {
            this.values.add(value);
            return;
        }
        throw new IllegalArgumentException("Value cannot be null");
    }
}
