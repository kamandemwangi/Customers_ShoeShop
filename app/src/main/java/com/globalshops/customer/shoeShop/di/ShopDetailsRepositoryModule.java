package com.globalshops.customer.shoeShop.di;

import android.content.Context;

import com.globalshops.customer.shoeShop.repositories.ShopDetailsRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ShopDetailsRepositoryModule {

    @Singleton
    @Provides
    public ShopDetailsRepository providesShopDetails(FirebaseAuth auth, FirebaseFirestore db, FirebaseStorage storage, @ApplicationContext Context context){
        return new ShopDetailsRepository(auth, db, storage, context);
    }
}
