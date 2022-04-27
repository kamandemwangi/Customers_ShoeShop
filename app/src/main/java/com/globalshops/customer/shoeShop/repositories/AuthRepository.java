package com.globalshops.customer.shoeShop.repositories;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.globalshops.customer.shoeShop.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthRepository {
    private MediatorLiveData<Boolean> isAccountCreated = new MediatorLiveData<>();
    private MediatorLiveData<Boolean> isAuthenticated = new MediatorLiveData<>();
    private MediatorLiveData<User> _user = new MediatorLiveData<>();
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Context context;

    public AuthRepository(FirebaseAuth auth, FirebaseFirestore db, Context context) {
        this.auth = auth;
        this.db = db;
        this.context = context;
    }

    public LiveData<Boolean> createAccount(String email, String password, User user){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            isAccountCreated.postValue(true);
                            String userId = task.getResult().getUser().getUid();
                            user.setUserId(userId);
                            addUserToDb(user, userId);
                            Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return isAccountCreated;
    }

    private void addUserToDb(User user, String doc){
        db.collection("customers")
                .document(doc)
                .set(user);
    }
    public LiveData<Boolean> logIn(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            isAuthenticated.postValue(true);
                            Toast.makeText(context, "Logged In", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return isAuthenticated;
    }

    public LiveData<User> getUserDetails(){
        db.collection("customers")
                .document(auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            User user = task.getResult().toObject(User.class);
                            _user.postValue(user);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return _user;
    }

    public void logOut(){
        auth.signOut();
    }
}
