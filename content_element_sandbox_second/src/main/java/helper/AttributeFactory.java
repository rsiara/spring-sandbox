package helper;

import model.attribute.Attribute;
import model.attribute.BooleanAttribute;
import model.attribute.ComplexAttribute;
import model.attribute.IntegerAttribute;
import model.attribute.StringAttribute;
import model.type.AttributeType;

public class AttributeFactory {

    public Attribute createAttribute(AttributeType attributeType) {
        switch (attributeType) {
            case STRING:
                return new StringAttribute();
            case BOOLEAN:
                return new BooleanAttribute();
            case INTEGER:
                return new IntegerAttribute();
            case COMPLEX:
                return new ComplexAttribute();
            default:
                throw new IllegalArgumentException("Unsupported attributeType: " + attributeType);
        }
    }
}
