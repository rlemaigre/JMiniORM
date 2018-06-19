package org.jminiorm.attributeconverter;

import java.io.IOException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.type.JavaType;

@Converter
public abstract class LegacyJsonAttributeConverter<T> implements AttributeConverter<T, String> {

	private JavaType javaType;
	private ObjectMapper mapper;
	private ObjectReader reader;
	private ObjectWriter writer;

	@Override
	public String convertToDatabaseColumn(T propertyValue) {
		if (propertyValue == null)
			return null;
		try {
			return getWriter().writeValueAsString(propertyValue);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public T convertToEntityAttribute(String columnValue) {
		if (columnValue == null)
			return null;
		try {
			return getReader().readValue(columnValue);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected ObjectMapper getMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper();
		}
		return mapper;
	}

	protected ObjectWriter getWriter() {
		if (writer == null) {
			writer = getMapper().writer();
		}
		return writer;
	}

	protected ObjectReader getReader() {
		if (reader == null) {
			reader = getMapper().reader(getJavaType());
		}
		return reader;
	}

	protected JavaType getJavaType() {
		if (javaType == null) {
			JavaType thisType = getMapper().getTypeFactory().constructType(getClass());
			javaType = getMapper().getTypeFactory().findTypeParameters(thisType, LegacyJsonAttributeConverter.class)[0];
		}
		return javaType;
	}

}