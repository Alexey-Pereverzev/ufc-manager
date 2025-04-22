package org.example.constants;

public enum InfoMessage {
    RESOURCE_NOT_FOUND_CODE("RESOURCE_NOT_FOUND"),
    INVALID_PARAMETER_CODE("INVALID_PARAMETER"),
    UNAUTHORIZED_CODE("UNAUTHORIZED"),
    NO_ACCESS_CODE("NO_ACCESS"),
    INPUT_DATA_ERROR_CODE("INPUT_DATA_ERROR"),
    LOGIN_CANNOT_BE_EMPTY("Login cannot be empty"),
    INVALID_LOGIN_CHARACTERS("Invalid login characters. Latin letters A-Z, a-z and numbers 0-9 are acceptable"),
    PASSWORD_CANNOT_BE_EMPTY("Password cannot be empty"),
    INVALID_PASSWORD_CHARACTERS("Invalid password characters. Latin letters A-Z, a-z and numbers 0-9 are acceptable"),
    USER_EXISTS_CODE("USER_EXISTS");

    private final String value;

    InfoMessage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
