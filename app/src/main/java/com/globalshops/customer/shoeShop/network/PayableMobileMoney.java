package com.globalshops.customer.shoeShop.network;

import com.globalshops.customer.shoeShop.models.MobilePayment;
import com.globalshops.customer.shoeShop.models.MobilePaymentAuthorization;
import com.globalshops.customer.shoeShop.utils.Constants;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface PayableMobileMoney {
    @GET("/oauth/v1/generate?grant_type=client_credentials")
    Flowable<MobilePaymentAuthorization> getAccessToken(@Header("Authorization") String authorization);

    @Headers("Content-Type: application/json")
    @POST("/mpesa/stkpush/v1/processrequest")
    Flowable<MobilePayment> getPaymentPushSTK(@Body MobilePayment mobilePayment, @Header("Authorization") String authorization);

    @POST()
    Flowable<MobilePayment> postUserUID(@Url String url, @Body MobilePayment mobilePayment);

}
