package co.com.ancas.playground.sec05.enums;

public enum Messages {
    MESSAGE_ERROR_NAME_REQUIRED("Name is required"),
    MESSAGE_ERROR_USER_NOT_FOUND("User with id %s not found"),
    MESSAGE_ERROR_EMAIL_REQUIRED("Email is required");

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
