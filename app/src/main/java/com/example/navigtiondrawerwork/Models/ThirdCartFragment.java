package com.example.navigtiondrawerwork.Models;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.navigtiondrawerwork.R;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ThirdCartFragment extends Fragment {
    private static final String TAG = "ThirdCartFragment";

    private TextView textPrice, textShippingDetails;
    private Button buttonBack, buttonFinished;
    private RadioGroup radioGrouppaymentMethod;

    private Orders inCommingOrders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third_cart,container,false);

        initViews(view);

        Bundle bundle = getArguments();
        try{
            inCommingOrders = bundle.getParcelable("orders");
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        if(inCommingOrders != null){
            textPrice.setText(String.valueOf(inCommingOrders.getTotalPrice()));
            String finalString = "Items:\n\tAddress: " + inCommingOrders.getAddeess() + "\n\t" +
                    "Email: " + inCommingOrders.getEmail() + "\n\t" +
                    "Zip code: " + inCommingOrders.getZipCode() + "\n\t" +
                    "Phone Number: " + inCommingOrders.getPhoneNumber();
            textShippingDetails.setText(finalString);
            buttonFinished.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoPayment();
                }
            });
            buttonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goBack();
                }
            });
        }

        return view;
    }

    private void goBack(){
        Log.d(TAG, "goBack: started");
        Orders orders = new Orders();
        orders.setTotalPrice(inCommingOrders.getTotalPrice());
        orders.setGroceryItems(inCommingOrders.getGroceryItems());
        Bundle bundle = new Bundle();
        bundle.putParcelable("orders",orders);
        CartSecondFragment cartSecondFragment = new CartSecondFragment();
        cartSecondFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.out, R.anim.in)
                .replace(R.id.fragment_container_frameLayout_cart, cartSecondFragment)
                .commit();
    }

    private void gotoPayment(){
        Log.d(TAG, "gotoPayment: started");
        RadioButton radioButton = getActivity().findViewById(radioGrouppaymentMethod.getCheckedRadioButtonId());
        inCommingOrders.setPaymentMethod(radioButton.getText().toString());
        inCommingOrders.setSuccess(true);

       final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                //.baseUrl("http://172.16.100.5:3000/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitClient retrofitClient = retrofit.create(RetrofitClient.class);
        Call<Orders> call = retrofitClient.gotoFakePayment(inCommingOrders);
        call.enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                Log.d(TAG, "onResponse: " + response.code());
                /*if(!response.isSuccessful()){
                    return;
                }*/
                gotoPaymentResult(response.body());
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {
                Log.d(TAG, "onFailure: t: " + t.getMessage());
            }
        });
    }

    private void gotoPaymentResult(Orders orders){
        Log.d(TAG, "gotoPaymentResult: started");
        if(orders.isSuccess()){
            SuccessfulPaymentFragment successfulPaymentFragment = new SuccessfulPaymentFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("orders", orders);
            successfulPaymentFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.in, R.anim.out)
                    .replace(R.id.fragment_container_frameLayout_cart, successfulPaymentFragment)
                    .commit();
        }else{
            FailedPaymentFragment failedPaymentFragment = new FailedPaymentFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("orders", orders);
            failedPaymentFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.in, R.anim.out)
                    .replace(R.id.fragment_container_frameLayout_cart, failedPaymentFragment)
                    .commit();
        }
    }

    private void initViews(View view){
        Log.d(TAG, "initViews: started");
        textPrice = (TextView)view.findViewById(R.id.textPriceThirdCart);
        textShippingDetails = (TextView)view.findViewById(R.id.textShippingDetailThirdCart);

        buttonBack = (Button)view.findViewById(R.id.buttonBackThirdCart);
        buttonFinished = (Button)view.findViewById(R.id.buttonFinishThirdCart);

        radioGrouppaymentMethod = (RadioGroup)view.findViewById(R.id.rgPaymentMethodThirdCart);
    }
}
