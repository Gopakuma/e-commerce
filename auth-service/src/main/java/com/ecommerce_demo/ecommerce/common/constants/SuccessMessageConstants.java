package com.ecommerce_demo.ecommerce.common.constants;

public final class SuccessMessageConstants {

    private SuccessMessageConstants() {} // Prevent instantiation

    // User & Account Success
    public static final String USER_REGISTERED = "User registration completed successfully.";
    public static final String LOGIN_SUCCESSFUL = "Authentication successful.";
    public static final String PASSWORD_RESET_SUCCESS = "Password has been updated successfully.";
    public static final String PROFILE_UPDATED = "Profile details updated successfully.";
    public static final String ACCOUNT_DEACTIVATED = "Account has been successfully deactivated.";
    public static final String TOKEN_GENERATION_SUCCESS = "Token generated successfully";

    // Product & Catalog Success
    public static final String PRODUCT_CREATED = "Product added to catalog successfully.";
    public static final String PRODUCT_UPDATED = "Product details updated successfully.";
    public static final String PRODUCT_DELETED = "Product permanently removed from catalog.";

    // Cart & Order Success
    public static final String ITEM_ADDED_TO_CART = "Item added to your shopping cart.";
    public static final String CART_UPDATED = "Shopping cart quantities adjusted successfully.";
    public static final String ORDER_PLACED = "Order placed successfully. Thank you for your purchase.";
    public static final String ORDER_CANCELLED = "Your order has been successfully cancelled.";

    // Payment Success
    public static final String PAYMENT_PROCESSED = "Payment transaction completed successfully.";
}
