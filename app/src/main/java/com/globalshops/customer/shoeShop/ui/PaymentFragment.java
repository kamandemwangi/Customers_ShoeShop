package com.globalshops.customer.shoeShop.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.globalshops.customer.shoeShop.R;
import com.globalshops.customer.shoeShop.models.MobilePayment;
import com.globalshops.customer.shoeShop.models.MobilePaymentAuthorization;
import com.globalshops.customer.shoeShop.models.Shoe;
import com.globalshops.customer.shoeShop.utils.Constants;
import com.globalshops.customer.shoeShop.utils.InputValidation;
import com.globalshops.customer.shoeShop.utils.MobilePaymentResource;
import com.globalshops.customer.shoeShop.viewmodels.MobilePaymentsViewModel;
import com.globalshops.customer.shoeShop.viewmodels.ShoeViewModel;
import com.globalshops.customer.shoeShop.viewmodels.ShopDetailsViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class PaymentFragment extends Fragment implements View.OnClickListener {
    private MaterialTextView orderStatusTextView;
    private MaterialTextView totalsTextView;
    private TextInputLayout phoneNumberTextInputLayout;
    private MaterialButton payButton;

    private ShoeViewModel viewModel;
    private ShopDetailsViewModel shopDetailsViewModel;
    private MobilePaymentsViewModel paymentsViewModel;
    private String shopId;
    private String orderNumber;


    @Inject
    FirebaseAuth auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ShoeViewModel.class);
        shopDetailsViewModel = new ViewModelProvider(this).get(ShopDetailsViewModel.class);
        paymentsViewModel = new ViewModelProvider(this).get(MobilePaymentsViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderStatusTextView = view.findViewById(R.id.payment_fragment_order_status);
        totalsTextView = view.findViewById(R.id.payment_fragment_totals);
        phoneNumberTextInputLayout = view.findViewById(R.id.payment_fragment_phone_textField);
        payButton = view.findViewById(R.id.payment_fragment_pay_button);
        observeTotals();
        observeOrderStatus();
        addClickListeners();

    }

    private void observeTotals(){
        viewModel.getShoppingBagTotals().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble != null){
                    totalsTextView.setText("Ksh. "+String.valueOf(aDouble));
                }else {
                    totalsTextView.setText("Loading...");
                }
            }
        });
    }

    private void observeOrderStatus(){
        shopDetailsViewModel.getSelectedShopId().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null){
                    viewModel.getShoppingBagShoes().observe(getViewLifecycleOwner(), new Observer<List<Shoe>>() {
                        @Override
                        public void onChanged(List<Shoe> shoes) {
                            if (shoes != null){
                                for (Shoe shoe : shoes){
                                    String orderNumber = shoe.getOrderNumber();
                                    viewModel.getShoeOrderStatus(s, orderNumber).observe(getViewLifecycleOwner(), new Observer<Shoe>() {
                                        @Override
                                        public void onChanged(Shoe shoe) {
                                            if (shoe != null){
                                                String orderStatus = shoe.getOrderStatus();

                                                if (orderStatus == null){
                                                    orderStatusTextView.setText("Pending approve, please wait");
                                                }else if (orderStatus.equals("accepted")){
                                                    orderStatusTextView.setText("Order approved");
                                                    payButton.setEnabled(true);
                                                }else {
                                                    orderStatusTextView.setText("Order declined");
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });

                }
            }
        });

    }
    private void observeAccessToken(){
//        String userId = auth.getCurrentUser().getUid();
//        MobilePayment mobilePaymentUserId = new MobilePayment(userId);
//        paymentsViewModel.postUserId(mobilePaymentUserId);

        String phoneNumber = "254"+phoneNumberTextInputLayout.getEditText().getText().toString().trim();
        Long mpesaPhoneNumber = Long.parseLong(phoneNumber);
        MobilePayment mobilePayment = new MobilePayment(174379, "MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMjExMDMxMDgzNjEx",
                "20211031083611", "CustomerPayBillOnline",
                1, mpesaPhoneNumber, 174379, mpesaPhoneNumber, "https://us-central1-shoeshop-be287.cloudfunctions.net/mPesaCallbackUrl", "Practices", "Payment of x");
     paymentsViewModel.getAccessToken().observe(getViewLifecycleOwner(), new Observer<MobilePaymentResource<MobilePaymentAuthorization>>() {
         @Override
         public void onChanged(MobilePaymentResource<MobilePaymentAuthorization> mobilePaymentAuthorizationMobilePaymentResource) {
             switch (mobilePaymentAuthorizationMobilePaymentResource.status){
                 case LOADING:{
                     Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();
                     break;
                 }
                 case SUCCESS:{
                     if (mobilePaymentAuthorizationMobilePaymentResource.data != null){
                         String accessToken = "Bearer "+mobilePaymentAuthorizationMobilePaymentResource.data.getAccess_token();
                         paymentsViewModel.invokeMobilePayments(mobilePayment, accessToken);
                     }
                     break;

                 }
                 case ERROR:{
                     Toast.makeText(getContext(), mobilePaymentAuthorizationMobilePaymentResource.message, Toast.LENGTH_SHORT).show();
                     break;
                 }
             }
         }
     });
    }

    private void observePayments(){
        paymentsViewModel.observePayments().observe(getViewLifecycleOwner(), new Observer<MobilePaymentResource<MobilePayment>>() {
            @Override
            public void onChanged(MobilePaymentResource<MobilePayment> mobilePaymentMobilePaymentResource) {
                switch (mobilePaymentMobilePaymentResource.status){
                    case LOADING:{
                        payButton.setEnabled(false);
                        break;
                    }
                    case SUCCESS:{
                        payButton.setEnabled(false);
                        break;
                    }
                    case ERROR:{
                        Toast.makeText(getContext(), mobilePaymentMobilePaymentResource.message, Toast.LENGTH_SHORT).show();
                        payButton.setEnabled(false);
                        break;
                    }
                }
            }
        });
    }
    private void observeUserId(){
        paymentsViewModel.observeUserId().observe(getViewLifecycleOwner(), new Observer<MobilePaymentResource<MobilePayment>>() {
            @Override
            public void onChanged(MobilePaymentResource<MobilePayment> mobilePaymentMobilePaymentResource) {
                switch (mobilePaymentMobilePaymentResource.status){
                    case ERROR:{
                        Toast.makeText(getContext(), mobilePaymentMobilePaymentResource.message, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case SUCCESS:{
                        Toast.makeText(getContext(), "userId sent", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.payment_fragment_pay_button:{
                String phoneNumber = phoneNumberTextInputLayout.getEditText().getText().toString().trim();
                if (InputValidation.isValidPhoneNumber(phoneNumber, phoneNumberTextInputLayout)){
                    return;
                }else {
                    paymentsViewModel.invokeGetAccessToken(Constants.BASIC_AUTH);
                    observeAccessToken();
                    observePayments();
                    //observeUserId();
                }
                break;
            }
        }
    }
    private void addClickListeners(){
        payButton.setOnClickListener(this);
    }
}