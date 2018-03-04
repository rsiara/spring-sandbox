package model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/*      If you frequently use a more semantically rich data type, such as the URL class, then
        you might want every attribute of that type to be converted. You can do this by setting the autoApply option on the
        @Converter annotation. In Listing 10-5, a URL converter is declared with the autoApply option enabled. This will cause
        every persistent attribute of type URL in the persistence unit to be converted to a string when the entity that contains it
        is written to the database.
        Listing

        */
@Converter
public class UpperCaseConverter implements AttributeConverter<String, String> {

    public String convertToDatabaseColumn(String attrib) {
        return attrib.toUpperCase();
    }

    public String convertToEntityAttribute(String dbData) {
        String lower = dbData.substring(1).toLowerCase();
        return dbData.substring(0, 1).concat(lower);
    }
}
