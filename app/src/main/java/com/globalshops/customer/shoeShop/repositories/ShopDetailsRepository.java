package com.globalshops.customer.shoeShop.repositories;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.globalshops.customer.shoeShop.models.ShopDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopDetailsRepository {
    private static final String TAG = "ShopDetailsRepository";
    private List<String> shopsUidS = new ArrayList<>();
    private List<ShopDetails> shopDetailsList = new ArrayList<>();
    private MediatorLiveData<List<ShopDetails>> shopDetailsMediatorLiveData = new MediatorLiveData<>();
    private MediatorLiveData<String> shopIdLivedata = new MediatorLiveData<>();
    private MediatorLiveData<ShopDetails> _shopDetails = new MediatorLiveData<>();
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private Context context;

    public ShopDetailsRepository(FirebaseAuth auth, FirebaseFirestore db, FirebaseStorage storage, Context context) {
        this.auth = auth;
        this.db = db;
        this.storage = storage;
        this.context = context;
    }

    public LiveData<List<ShopDetails>> getShopDetails(){
        db.collection("utils")
                .document("shops_uid")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            if (shopsUidS.size() > 0){
                                shopsUidS.clear();
                            }
                            DocumentSnapshot documentSnapshot = task.getResult();
                           shopsUidS = (List<String>)documentSnapshot.getData().get("shopsUidList");

                           if (shopDetailsList.size() > 0){
                               shopDetailsList.clear();
                           }
                           for (int i = 0; i < shopsUidS.size(); i++){
                               db.collection("shops")
                                       .document(shopsUidS.get(i))
                                       .collection("shop_info")
                                       .document(shopsUidS.get(i))
                                       .get()
                                       .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                               if (task.isSuccessful()){
                                                   ShopDetails shopDetails = task.getResult().toObject(ShopDetails.class);
                                                   shopDetailsList.add(shopDetails);
                                                   shopDetailsMediatorLiveData.postValue(shopDetailsList);
                                                   Log.d(TAG, "onComplete: "+shopDetailsList.get(0).getName());
                                               }
                                           }
                                       }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                   }
                               });
                           }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return shopDetailsMediatorLiveData;
    }

    public void addSelectedShopId(Map<String, Object> shopId){
        db.collection("utils")
                .document("selected_shopId")
                .set(shopId);
    }
    public LiveData<String> getSelectedShopId(){
        db.collection("utils")
                .document("selected_shopId")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.getData() != null){
                            String shopId = value.getString("shopId");
                            shopIdLivedata.postValue(shopId);
                        }
                    }
                });
        return shopIdLivedata;
    }

    public LiveData<ShopDetails> getSingleShopDetails(String shopId){
        db.collection("shops")
                .document(shopId)
                .collection("shop_info")
                .document(shopId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            ShopDetails shopDetails = task.getResult().toObject(ShopDetails.class);
                            _shopDetails.postValue(shopDetails);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return _shopDetails;
    }
}
