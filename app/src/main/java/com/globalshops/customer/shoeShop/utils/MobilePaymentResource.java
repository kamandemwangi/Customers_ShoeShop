package com.globalshops.customer.shoeShop.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MobilePaymentResource<T> {

    @NonNull
    public final PaymentStatus status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;


    public MobilePaymentResource(@NonNull PaymentStatus status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> MobilePaymentResource<T> success(@Nullable T data){
        return new MobilePaymentResource<>(PaymentStatus.SUCCESS, data, null);
    }

    public static <T> MobilePaymentResource<T> error(@NonNull String message, @Nullable T data){
        return new MobilePaymentResource<>(PaymentStatus.ERROR, data, message);
    }

    public static <T> MobilePaymentResource<T> loading(@Nullable T data){
        return new MobilePaymentResource<>(PaymentStatus.LOADING, data, null);
    }
    public enum PaymentStatus{
        SUCCESS, ERROR, LOADING
    }
}














