package org.jminiorm.attributeconverter;

import javax.persistence.AttributeConverter;

/**
 * @author Alexandre Neuville
 */
public class EnumNameAttributeConverter<T extends Enum<T>> implements AttributeConverter<T, String> {

    private Class<T> enumClass;

    public EnumNameAttributeConverter(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        return dbData != null ? T.valueOf(enumClass, dbData) : null;
    }
}
