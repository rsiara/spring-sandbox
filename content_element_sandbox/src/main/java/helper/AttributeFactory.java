package helper;

import model.attribute.Attribute;
import model.attribute.BooleanAttribute;
import model.attribute.ComplexAttribute;
import model.attribute.StringAttribute;
import model.type.AttributeType;

public class AttributeFactory {

    public Attribute createAttribute(AttributeType attributeType) {
        switch (attributeType) {
            case String:
                return new StringAttribute();
            case Boolean:
                return new BooleanAttribute();
            case Complex:
                return new ComplexAttribute();
            default:
                throw new IllegalArgumentException("Unsupported attributeType: " + attributeType);
        }
    }
}
