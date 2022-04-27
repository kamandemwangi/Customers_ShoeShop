package com.globalshops.customer.shoeShop.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;


import com.globalshops.customer.shoeShop.models.MobilePayment;
import com.globalshops.customer.shoeShop.models.MobilePaymentAuthorization;
import com.globalshops.customer.shoeShop.network.PayableMobileMoney;
import com.globalshops.customer.shoeShop.utils.MobilePaymentResource;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class MobilePaymentsViewModel extends ViewModel {
    private static final String TAG = "MobilePaymentsViewModel";
    public PayableMobileMoney mobileMoney;
    private MediatorLiveData<MobilePaymentResource<MobilePayment>> mediatorLiveData = new MediatorLiveData<>();
    private MediatorLiveData<MobilePaymentResource<MobilePaymentAuthorization>> authorizationMediatorLiveData = new MediatorLiveData<>();
    private MediatorLiveData<MobilePaymentResource<MobilePayment>> mobilePaymentUserIdLiveData = new MediatorLiveData<>();

    @Inject
    public MobilePaymentsViewModel(PayableMobileMoney mobileMoney) {
        this.mobileMoney = mobileMoney;
    }

    public void invokeGetAccessToken(String authorization){
        authorizationMediatorLiveData.setValue(MobilePaymentResource.loading(null));
        final LiveData<MobilePaymentResource<MobilePaymentAuthorization>> source = LiveDataReactiveStreams.fromPublisher(
                mobileMoney.getAccessToken(authorization)
                .onErrorReturn(new Function<Throwable, MobilePaymentAuthorization>() {
                    @NonNull
                    @Override
                    public MobilePaymentAuthorization apply(@NonNull Throwable throwable) throws Exception {
                        MobilePaymentAuthorization error = new MobilePaymentAuthorization();
                        error.setAccess_token("-1");
                        //used expiry to pass error message
                        error.setExpiry_in(throwable.getMessage());
                        return error;
                    }
                }).map(new Function<MobilePaymentAuthorization, MobilePaymentResource<MobilePaymentAuthorization>>() {
                    @NonNull
                    @Override
                    public MobilePaymentResource<MobilePaymentAuthorization> apply(@NonNull MobilePaymentAuthorization mobilePaymentAuthorization) throws Exception {
                        if (mobilePaymentAuthorization.getAccess_token().equals("-1")){
                            return MobilePaymentResource.error(mobilePaymentAuthorization.getExpiry_in(), null);
                        }
                        Log.d(TAG, "apply: "+mobilePaymentAuthorization.getAccess_token());
                        return MobilePaymentResource.success(mobilePaymentAuthorization);
                    }
                }).subscribeOn(Schedulers.io())
        );

        authorizationMediatorLiveData.addSource(source, new Observer<MobilePaymentResource<MobilePaymentAuthorization>>() {
            @Override
            public void onChanged(MobilePaymentResource<MobilePaymentAuthorization> mobilePaymentAuthorization) {
                authorizationMediatorLiveData.setValue(mobilePaymentAuthorization);
                authorizationMediatorLiveData.removeSource(source);
            }
        });
    }
    public void invokeMobilePayments(MobilePayment mobilePayment, String authorization){
        MobilePaymentResource.loading(null);
        final LiveData<MobilePaymentResource<MobilePayment>> source = LiveDataReactiveStreams.fromPublisher(
                mobileMoney.getPaymentPushSTK(mobilePayment, authorization)
                        .onErrorReturn(new Function<Throwable, MobilePayment>() {
                            @NonNull
                            @Override
                            public MobilePayment apply(@NonNull Throwable throwable) throws Exception {
                                MobilePayment errorMobilePay = new MobilePayment();
                                errorMobilePay.setPassword(throwable.getLocalizedMessage());
                                errorMobilePay.setAmount(-1);
                                return errorMobilePay;
                            }
                        }).map(new Function<MobilePayment, MobilePaymentResource<MobilePayment>>() {
                    @NonNull
                    @Override
                    public MobilePaymentResource<MobilePayment> apply(@NonNull MobilePayment mobilePayment) throws Exception {
                        if (mobilePayment.getAmount() == -1){
                            return MobilePaymentResource.error("Error while processing payments: "+mobilePayment.getPassword(), null);
                        }
                        return MobilePaymentResource.success(mobilePayment);
                    }
                }).subscribeOn(Schedulers.io())
        );
        mediatorLiveData.addSource(source, new Observer<MobilePaymentResource<MobilePayment>>() {
            @Override
            public void onChanged(MobilePaymentResource<MobilePayment> mobilePayment) {
                mediatorLiveData.setValue(mobilePayment);
                mediatorLiveData.removeSource(source);
            }
        });
    }

    public void postUserId(MobilePayment mobilePayment){
        MobilePaymentResource.loading(null);
        final LiveData<MobilePaymentResource<MobilePayment>> source = LiveDataReactiveStreams.fromPublisher(
                mobileMoney.postUserUID("https://us-central1-shoeshop-be287.cloudfunctions.net/mPesaCallbackUrl", mobilePayment)
                .onErrorReturn(new Function<Throwable, MobilePayment>() {
                    @NonNull
                    @Override
                    public MobilePayment apply(@NonNull Throwable throwable) throws Exception {
                         MobilePayment error = new MobilePayment();
                         error.setPassword(throwable.getLocalizedMessage());
                         error.setAmount(-1);
                         return error;
                    }
                }).map(new Function<MobilePayment, MobilePaymentResource<MobilePayment>>() {
                    @NonNull
                    @Override
                    public MobilePaymentResource<MobilePayment> apply(@NonNull MobilePayment mobilePayment) throws Exception {
                        if (mobilePayment.getAmount() == -1){
                            return  MobilePaymentResource.error(mobilePayment.getPassword(), null);
                        }
                        return MobilePaymentResource.success(mobilePayment);
                    }
                }).subscribeOn(Schedulers.io())
        );
        mobilePaymentUserIdLiveData.addSource(source, new Observer<MobilePaymentResource<MobilePayment>>() {
            @Override
            public void onChanged(MobilePaymentResource<MobilePayment> mobilePaymentMobilePaymentResource) {
                mobilePaymentUserIdLiveData.setValue(mobilePaymentMobilePaymentResource);
                mobilePaymentUserIdLiveData.removeSource(source);
            }
        });
    }

    public LiveData<MobilePaymentResource<MobilePaymentAuthorization>> getAccessToken(){
        return authorizationMediatorLiveData;
    }

    public LiveData<MobilePaymentResource<MobilePayment>> observePayments(){
        return mediatorLiveData;
    }

    public LiveData<MobilePaymentResource<MobilePayment>> observeUserId(){
        return mobilePaymentUserIdLiveData;
    }

}
