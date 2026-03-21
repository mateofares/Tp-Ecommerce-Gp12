package com.tpo.ecommerce.exceptions;

public class DuplicateResourceException extends RuntimeException {
    private final String resource;
    private final String field;
    private final Object value;

    public DuplicateResourceException(String resource, String field, Object value) {
        super(resource + " duplicado: " + field + "=" + value);
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
