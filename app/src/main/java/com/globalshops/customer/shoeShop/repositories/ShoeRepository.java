package com.globalshops.customer.shoeShop.repositories;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.globalshops.customer.shoeShop.models.Shoe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoeRepository {
    private List<Shoe> shoeList = new ArrayList<>();
    private MediatorLiveData<List<Shoe>> source = new MediatorLiveData<>();
    private MediatorLiveData<Shoe> singleShoeLiveData = new MediatorLiveData<>();
    private MediatorLiveData<Boolean> isAddedToBag = new MediatorLiveData<>();
    private MediatorLiveData<Shoe> _placedOrder = new MediatorLiveData<>();
    private MediatorLiveData<Double> _totals = new MediatorLiveData<>();
    private MediatorLiveData<List<Shoe>> shoppingBagShoeListLiveData = new MediatorLiveData<>();
    private List<Shoe> shoppingBagShoeList = new ArrayList<>();
    private MediatorLiveData<Boolean> isOrderAdded = new MediatorLiveData<>();
    private MediatorLiveData<Shoe> shoeOrderStatus = new MediatorLiveData<>();
    private MediatorLiveData<List<Shoe>> _openOrders = new MediatorLiveData<>();
    private List<Shoe> openOrdersList = new ArrayList<>();
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Context context;

    public ShoeRepository(FirebaseAuth auth, FirebaseFirestore db, Context context) {
        this.auth = auth;
        this.db = db;
        this.context = context;
    }

    public LiveData<List<Shoe>> getShoes(String shopId){
        db.collection("shops")
                .document(shopId)
                .collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (shoeList.size() > 0){
                                shoeList.clear();
                            }
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                Shoe shoe = snapshot.toObject(Shoe.class);
                              shoeList.add(shoe);
                              source.postValue(shoeList);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return source;
    }

    public LiveData<Shoe> getSingleShoe(String shopId, String productId){
        db.collection("shops")
                .document(shopId)
                .collection("products")
                .document(productId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Shoe shoe = task.getResult().toObject(Shoe.class);
                            singleShoeLiveData.postValue(shoe);
                        }
                    }
                });
        return singleShoeLiveData;
    }

    public LiveData<Boolean> addToBag(Shoe shoe, String orderNumber){
        String userId = auth.getCurrentUser().getUid();
        HashMap<String, Integer> totals = new HashMap<>();
        totals.put("totals", Integer.parseInt(shoe.getTotal()));
        db.collection("customers")
                .document(userId)
                .collection("shopping_bag")
                .document(orderNumber)
                .set(shoe, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            isAddedToBag.postValue(true);

                            db.collection("customers")
                                    .document(userId)
                                    .collection("customers_utils")
                                    .document("totals")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot snapshot = task.getResult();
                                            if (snapshot.getData() == null){
                                                db.collection("customers")
                                                        .document(userId)
                                                        .collection("customers_utils")
                                                        .document("totals")
                                                        .set(totals);
                                            }else {
                                                db.collection("customers")
                                                        .document(userId)
                                                        .collection("customers_utils")
                                                        .document("totals")
                                                        .update("totals", FieldValue.increment(Long.parseLong(shoe.getShoePriceTag())));
                                            }

                                        }
                                    });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return isAddedToBag;
    }

    public void decrementDbShoeQuantity(String shopId, String productId, Map<String, String> oldValue, Map<String, String> newValue, String arrayValue, String shoesListField, String field){
        db.collection("shops")
                .document(shopId)
                .collection("products")
                .document(productId)
                .update(field, FieldValue.arrayRemove(oldValue))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            db.collection("shops")
                                    .document(shopId)
                                    .collection("products")
                                    .document(productId)
                                    .update(field, FieldValue.arrayUnion(newValue))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                shoeSizesListUpdate(shopId, productId,arrayValue, shoesListField);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void shoeSizesListUpdate(String shopId, String productId, String arrayValue, String field){
        db.collection("shops")
                .document(shopId)
                .collection("products")
                .document(productId)
                .update(field, FieldValue.arrayRemove(arrayValue))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            db.collection("shops")
                                    .document(shopId)
                                    .collection("products")
                                    .document(productId)
                                    .update(field, FieldValue.arrayUnion(arrayValue))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    public LiveData<List<Shoe>> getShoppingBagShoes(){
        if (shoppingBagShoeList.size() > 0){
            shoppingBagShoeList.clear();
        }
        String userId = auth.getCurrentUser().getUid();
        db.collection("customers")
                .document(userId)
                .collection("shopping_bag")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null){
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                                Shoe shoe = snapshot.toObject(Shoe.class);
                                shoppingBagShoeList.add(shoe);
                                shoppingBagShoeListLiveData.postValue(shoppingBagShoeList);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return shoppingBagShoeListLiveData;
    }
    public void deleteShoppingBagSingleShoe(String orderNumber, String shoePriceTag){
        String userId = auth.getCurrentUser().getUid();
        db.collection("customers")
                .document(userId)
                .collection("shopping_bag")
                .document(orderNumber)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    updateTotals(userId, shoePriceTag);
                    Toast.makeText(context, "shoe removed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void emptyShoppingBag(String orderNumber){
        String userId = auth.getCurrentUser().getUid();
        db.collection("customers")
                .document(userId)
                .collection("shopping_bag")
                .document(orderNumber)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            deleteTotals();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteTotals(){
        String userId = auth.getCurrentUser().getUid();
        db.collection("customers")
                .document(userId)
                .collection("customers_utils")
                .document("totals")
                .delete();
    }

    public void updateTotals(String userId, String shoePriceTag){
        db.collection("customers")
                .document(userId)
                .collection("customers_utils")
                .document("totals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            double totals = task.getResult().getDouble("totals");
                            totals = totals - Double.parseDouble(shoePriceTag);

                            db.collection("customers")
                                    .document(userId)
                                    .collection("customers_utils")
                                    .document("totals")
                                    .update("totals", totals);
                        }
                    }
                });

    }
    public LiveData<Shoe> getPlaceOrder(String productId){
        String userId = auth.getCurrentUser().getUid();
        db.collection("customers")
                .document(userId)
                .collection("shopping_bag")
                .document(productId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (value != null && value.exists()) {
                            Shoe shoe = value.toObject(Shoe.class);
                            _placedOrder.postValue(shoe);
                        }
                    }
                });
        return _placedOrder;
    }

    public LiveData<Double> getShoppingBagTotals(){
        String userId = auth.getCurrentUser().getUid();
        db.collection("customers")
                .document(userId)
                .collection("customers_utils")
                .document("totals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().getData() != null){
                            double totals = task.getResult().getDouble("totals");
                            _totals.postValue(totals);
                        }
                    }
                });
        return _totals;
    }

    public LiveData<Boolean> addOrdersToShops(String shopId, String orderNumber, Shoe shoe){
        db.collection("shops")
                .document(shopId)
                .collection("orders")
                .document(orderNumber)
                .set(shoe, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            isOrderAdded.postValue(true);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return isOrderAdded;
    }


    public void updatePlaceOrder(String quantity, String total, String productId){
        String userId = auth.getCurrentUser().getUid();
        db.collection("customers")
                .document(userId)
                .collection("shopping_bag")
                .document(productId)
                .update("quantity",quantity, "total", total)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            db.collection("customers")
                                    .document(userId)
                                    .collection("shopping_bag")
                                    .document("totals")
                                    .update("totals", FieldValue.increment(Long.parseLong(total)));
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public LiveData<Shoe> getOrderStatus(String shopId, String orderId){
        db.collection("shops")
                .document(shopId)
                .collection("orders")
                .document(orderId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.getData() != null){
                            Shoe shoe = value.toObject(Shoe.class);
                            shoeOrderStatus.postValue(shoe);
                        }
                    }
                });
        return shoeOrderStatus;
    }

    public LiveData<List<Shoe>> getOpenOrders(){
        String userId = auth.getCurrentUser().getUid();

        db.collection("customers")
                .document(userId)
                .collection("open_orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (openOrdersList.size() > 0){
                            openOrdersList.clear();
                        }
                        for (QueryDocumentSnapshot snapshot : task.getResult()){
                            Shoe shoe = snapshot.toObject(Shoe.class);
                            openOrdersList.add(shoe);
                            _openOrders.postValue(openOrdersList);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return _openOrders;

    }
}
