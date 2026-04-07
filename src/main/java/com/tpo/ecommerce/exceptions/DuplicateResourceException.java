package com.tpo.ecommerce.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends ApiException {
    private final String resource;
    private final String field;
    private final Object value;

    public DuplicateResourceException(String resource, String field, Object value) {
        super(HttpStatus.CONFLICT, resource + " duplicado: " + field + "=" + value);
        this.resource = resource;
        this.field = field;
        this.value = value;
    }

    public String getResource() {
        return resource;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }
}
