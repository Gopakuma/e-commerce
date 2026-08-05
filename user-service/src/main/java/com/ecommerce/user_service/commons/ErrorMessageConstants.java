package com.ecommerce.user_service.commons;

public class ErrorMessageConstants {
    private ErrorMessageConstants(){}

    public static final String INVALID_CREDENTIALS = "Invalid username or password.";
    public static final String ACCESS_DENIED = "You do not have permission to perform this action.";
    public static final String UUID_CREATION_FAILED = "Failed to create a new UUID.";
    public static final String USER_NOT_FOUND = "User account not found.";
    public static final String USER_UPDATE_FAILED = "Failed to update the User.";
    public static final String SERVER_ERROR = "Something went wrong.";

}
