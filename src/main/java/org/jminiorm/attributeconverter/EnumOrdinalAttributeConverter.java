package org.jminiorm.attributeconverter;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author u205992
 */
public class EnumOrdinalAttributeConverter<T extends Enum> implements AttributeConverter<T, Integer> {

    private Map<Integer, T> lookup;

    public EnumOrdinalAttributeConverter(Class<T> enumClass) {
        lookup = new HashMap<>();
        Arrays.stream(enumClass.getEnumConstants()).forEach(e -> lookup.put(e.ordinal(), e));
    }

    @Override
    public Integer convertToDatabaseColumn(T attribute) {
        return attribute != null ? attribute.ordinal() : null;
    }

    @Override
    public T convertToEntityAttribute(Integer dbData) {
        return lookup.get(dbData);
    }
}
