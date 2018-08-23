package org.jminiorm.attributeconverter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;

/**
 * @author Alexandre Neuville
 */
public class AttributeConverterUtils {
    public static Class<?> getConvertionResultType(AttributeConverter converter) {
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
