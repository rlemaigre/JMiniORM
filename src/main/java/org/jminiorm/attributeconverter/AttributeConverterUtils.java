package org.jminiorm.attributeconverter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;

/**
 * @author Alexandre Neuville
 */
public class AttributeConverterUtils {

    /**
     * Get the java type for the database column
     * @param converter converter used to convert data
     * @return the class for the database type
     */
    public static Class<?> getConverterDatabaseType(AttributeConverter converter) {
        Class<?> converterClass = converter.getClass();
        try {
            JavaType converterType = new ObjectMapper().getTypeFactory().constructType(converterClass);
            JavaType t = new ObjectMapper().getTypeFactory().findTypeParameters(converterType,
                    AttributeConverter.class)[1];
            return t.getRawClass();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
