package org.jminiorm.attributeconverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter
public abstract class JsonAttributeConverter<T> implements AttributeConverter<T, String> {

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
		} catch (JsonProcessingException e) {
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
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
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
			reader = getMapper().readerFor(getJavaType());
		}
		return reader;
	}

	protected JavaType getJavaType() {
		if (javaType == null) {
			JavaType thisType = getMapper().getTypeFactory().constructType(getClass());
			javaType = getMapper().getTypeFactory().findTypeParameters(thisType, JsonAttributeConverter.class)[0];
		}
		return javaType;
	}

}