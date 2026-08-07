package com.ecommerce_demo.ecommerce.common.constants;

public final class ErrorMessageConstants {

    private ErrorMessageConstants() {
    } // Prevent instantiation

    public static final String USER_NOT_FOUND = "User account not found.";
    public static final String INVALID_CREDENTIALS = "Invalid username or password.";
    public static final String EMAIL_EXISTS = "Email address is already registered.";
    public static final String USERNAME_EXISTS = "Username is already taken.";
    public static final String UUID_EXISTS = "The requested UUID is invalid or already taken.";
    public static final String ACCOUNT_DISABLED = "Your account has been deactivated. Please contact support.";
    public static final String TOKEN_EXPIRED = "Session has expired. Please log in again.";
    public static final String REFRESH_TOKEN_EXPIRED = "Refresh Token has expired. Please log in again.";
    public static final String TOKEN_INVALID = "Invalid Token type.";
    public static final String USER_REGISTRATION_FAILED = "User registration failed.";
    public static final String INVALID_NEW_PASSWORD = "New password should different from Old password.";
    public static final String PASSWORD_RESET_FAILED = "Password Reset Failed.";
    public static final String SERVER_ERROR = "Something went wrong.";
    public static final String ACCESS_DENIED = "You do not have permission to perform this action.";

}
