package com.globalshops.customer.shoeShop.di;

import android.content.Context;

import com.globalshops.customer.shoeShop.repositories.AuthRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AuthRepositoryModule {

    @Singleton
    @Provides
    public AuthRepository provideAuthRepository(FirebaseAuth auth, FirebaseFirestore db, @ApplicationContext Context context){
        return new AuthRepository(auth, db, context);
    }
}
