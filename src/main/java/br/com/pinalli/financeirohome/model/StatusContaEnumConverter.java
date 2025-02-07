package br.com.pinalli.financeirohome.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusContaEnumConverter implements AttributeConverter<StatusConta, String> {

    @Override
    public String convertToDatabaseColumn(StatusConta status) {
        return status != null ? status.name() : null;
    }

    @Override
    public StatusConta convertToEntityAttribute(String dbData) {
        return dbData != null ? StatusConta.valueOf(dbData) : null;
    }
}