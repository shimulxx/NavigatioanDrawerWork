package com.example.navigtiondrawerwork.Models;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.navigtiondrawerwork.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

public class CartSecondFragment extends Fragment {
    private static final String TAG = "CartSecondFragment";

    private EditText editTextAddress, editTextZipCode, editTextPhoneNumber, editTextEmail;
    private Button buttonBack, buttonNext;

    private RelativeLayout parent, addressRelLayout, emailRelLayout, phoneNumberRelLayout, zipCodeRelLayout;
    private NestedScrollView nestedScrollView;

    private Orders inCommingorders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_cart, container, false);

        initViews(view);

        Bundle bundle = getArguments();
        if(bundle != null){
            inCommingorders = bundle.getParcelable("orders");
        }

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.out, R.anim.in)
                        .replace(R.id.fragment_container_frameLayout_cart, new CartFirstFragment()).commit();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateData()){
                    passData();
                } else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                            .setTitle("Error")
                            .setMessage("Please fill all the fields")
                            .setPositiveButton(Html.fromHtml("<font color='#C71585'>Dismiss</font>"), (dialog, which) -> {

                            });
                    builder.create().show();
                }
            }
        });

        initRelLayouts();

        return view;
    }

    private void passData(){
        Log.d(TAG, "passData: started");
        Bundle bundle = new Bundle();
        bundle.putParcelable("orders", inCommingorders);

        ThirdCartFragment thirdCartFragment = new ThirdCartFragment();
        thirdCartFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.in, R.anim.out)
                .replace(R.id.fragment_container_frameLayout_cart, thirdCartFragment)
                .commit();
    }

    private boolean validateData(){
        Log.d(TAG, "onCreateView: started");
        if(editTextAddress.getText().toString().equals(""))
            return false;
        if(editTextEmail.getText().toString().equals(""))
            return false;
        if(editTextZipCode.getText().toString().equals(""))
            return false;
        if(editTextPhoneNumber.getText().toString().equals(""))
            return false;
        inCommingorders.setAddeess(editTextAddress.getText().toString());
        inCommingorders.setEmail(editTextEmail.getText().toString());
        inCommingorders.setZipCode(editTextZipCode.getText().toString());
        inCommingorders.setPhoneNumber(editTextPhoneNumber.getText().toString());
        return true;
    }

    private void initViews(View view) {
        Log.d(TAG, "initViews: started");
        editTextAddress = (EditText) view.findViewById(R.id.editTextAddressSecondCart);
        editTextZipCode = (EditText) view.findViewById(R.id.editTextZipCodeSecondCart);
        editTextPhoneNumber = (EditText) view.findViewById(R.id.editTextPhoneNumberSecondCart);
        editTextEmail = (EditText) view.findViewById(R.id.editTexteMailSecondCart);
        buttonBack = (Button) view.findViewById(R.id.buttonBackSecondCart);
        buttonNext = (Button) view.findViewById(R.id.buttonNextSecondCart);

        parent = (RelativeLayout)view.findViewById(R.id.parentFragmentSecondCart);
        addressRelLayout = (RelativeLayout)view.findViewById(R.id.addressRelLayoutSecondCart);
        emailRelLayout = (RelativeLayout) view.findViewById(R.id.eMailRelLayoutSecondCart);
        phoneNumberRelLayout = (RelativeLayout)view.findViewById(R.id.phoneNumberRelLayoutSecondCart);
        zipCodeRelLayout = (RelativeLayout)view.findViewById(R.id.zipCodeRelLayoutSecondCart);

        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollviewSecondCart);
    }

    private void closeKeyBoard(){
        View view = getActivity().getCurrentFocus();
        if(view != null){
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void initRelLayouts(){
        Log.d(TAG, "initRelLayouts: started");
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
            }
        });
        addressRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
            }
        });
        emailRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
            }
        });
        phoneNumberRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
            }
        });
        zipCodeRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
            }
        });
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                closeKeyBoard();
            }
        });

    }
}
