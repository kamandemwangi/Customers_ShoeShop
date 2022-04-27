package com.globalshops.customer.shoeShop.di;

import com.globalshops.customer.shoeShop.network.PayableMobileMoney;
import com.globalshops.customer.shoeShop.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class RetrofitModule {

    @Singleton
    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .create();
    }

    @Singleton
    @Provides
    public Retrofit.Builder provideRetrofit(Gson gson) {
        return new Retrofit
                .Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Constants.BASE_URL_1);
    }

    @Singleton
    @Provides
    public PayableMobileMoney provideMpesa(Retrofit.Builder retrofit) {
        return retrofit.build()
                .create(PayableMobileMoney.class);
    }


}