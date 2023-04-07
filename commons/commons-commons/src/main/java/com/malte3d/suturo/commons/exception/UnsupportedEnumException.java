package com.malte3d.suturo.commons.exception;

/**
 * Can be thrown in the default case within a switch-case over an enum. Or if {@link Enum#valueOf} was used to search in vain for an unavailable string representation.
 */
public class UnsupportedEnumException extends IllegalArgumentException {

    public UnsupportedEnumException() {
    }

    public UnsupportedEnumException(String s) {
        super(s);
    }

    public UnsupportedEnumException(Enum<?> enumValue) {
        super("Unsupported Enum! enum=" + enumValue.getClass() + "." + enumValue.name());
    }

    public UnsupportedEnumException(String s, Enum<?> enumValue) {
        super(s + " enum=" + enumValue.getClass() + "." + enumValue.name());
    }

    public UnsupportedEnumException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedEnumException(Throwable cause) {
        super(cause);
    }

}
