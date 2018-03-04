package model;

import java.net.URL;
import java.net.MalformedURLException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/*
    A URL converter is declared with the autoApply option enabled. This will cause
    every persistent attribute of type URL in the persistence unit to be converted to a string when the entity that contains it
    is written to the database.

    We can override the conversion on a per-attribute basis. If, instead of the auto-applied converter, we want to
    use a different converter for a given attribute, then we can annotate the attribute with the @Convert annotation and
    specify the converter class we want to use. Alternatively, if we want to disable the conversion altogether and let the
    provider revert to serialization of the URL, then we can use the disableConversion attribute:
*/
@Converter(autoApply = true)
public class URLConverter implements AttributeConverter<URL, String> {

    public String convertToDatabaseColumn(URL attrib) {
        return attrib.toString();
    }

    public URL convertToEntityAttribute(String dbData) {
        try {
            return new URL(dbData);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("DB data: " + dbData + " is not a legal URL");
        }
    }
}
