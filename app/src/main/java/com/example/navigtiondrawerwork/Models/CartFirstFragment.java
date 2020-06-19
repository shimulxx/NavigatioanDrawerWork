package com.example.navigtiondrawerwork.Models;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigtiondrawerwork.R;
import com.example.navigtiondrawerwork.Utils;

import java.util.ArrayList;

public class CartFirstFragment extends Fragment implements CartRecViewAdapter.GetTotalPrice,
        CartRecViewAdapter.DeleteCartItem {
    private static final String TAG = "CartFirstFragment";

    private TextView textPrice, textnoOfItem;
    private RecyclerView recyclerView;
    private Button btnNext;
    private CartRecViewAdapter cartRecViewAdapter;
    private Utils utils;

    private double totalPrice = 0;
    private ArrayList<Integer> items;

    @Override
    public void onDeletingResult(GroceryItem groceryItem) {
        Log.d(TAG, "onDeletingResult: attempting to delete + " + groceryItem.toString());
        ArrayList<Integer> itemIds = new ArrayList<>();
        itemIds.add(groceryItem.getId());
        ArrayList<GroceryItem> items = utils.getItemsById(itemIds);

        if(items.size() > 0){
            ArrayList<Integer> newItemsIds = utils.deleteCartItem(items.get(0));
            if(newItemsIds.size() == 0){
                btnNext.setVisibility(View.GONE);
                btnNext.setEnabled(false);
                recyclerView.setVisibility(View.GONE);
                textnoOfItem.setVisibility(View.VISIBLE);
            } else{
                btnNext.setVisibility(View.VISIBLE);
                btnNext.setEnabled(true);
                recyclerView.setVisibility(View.VISIBLE);
                textnoOfItem.setVisibility(View.GONE);
            }
            ArrayList<GroceryItem> newItems = utils.getItemsById(newItemsIds);
            this.items = newItemsIds;
            cartRecViewAdapter.setGroceryItems(newItems);
        }
    }

    @Override
    public void onGettingTotalPriceResult(double price) {
        Log.d(TAG, "onGettingTotalPriceResult: " + price);
        textPrice.setText(String.valueOf(price));
        this.totalPrice = price;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_cart, container, false);
        initViews(view);
        items = new ArrayList<>();
        utils = new Utils(getActivity());
        initRecViews();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Orders orders = new Orders();
                orders.setTotalPrice(totalPrice);
                orders.setGroceryItems(items);
                Bundle bundle = new Bundle();
                bundle.putParcelable("orders", orders);
                CartSecondFragment cartSecondFragment = new CartSecondFragment();
                cartSecondFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.in, R.anim.out)
                        .replace(R.id.fragment_container_frameLayout_cart,cartSecondFragment).commit();
            }
        });
        return view;
    }
    private void initRecViews(){
        Log.d(TAG, "initRecViews: started");
        cartRecViewAdapter = new CartRecViewAdapter(this);
        recyclerView.setAdapter(cartRecViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArrayList<Integer> integerArrayListIds = utils.getCartItems();
        if(integerArrayListIds != null){

            ArrayList<GroceryItem> groceryItems = utils.getItemsById(integerArrayListIds);

            if(groceryItems.size() == 0){
                btnNext.setVisibility(View.GONE);
                btnNext.setEnabled(false);
                recyclerView.setVisibility(View.GONE);
                textnoOfItem.setVisibility(View.VISIBLE);
            } else{
                btnNext.setVisibility(View.VISIBLE);
                btnNext.setEnabled(true);
                recyclerView.setVisibility(View.VISIBLE);
                textnoOfItem.setVisibility(View.GONE);
            }
            this.items = integerArrayListIds;
            cartRecViewAdapter.setGroceryItems(groceryItems);
        }
    }
    private void initViews(View view){
        Log.d(TAG, "initViews: started");
        textPrice = (TextView)view.findViewById(R.id.textSumCart);
        recyclerView = (RecyclerView)view.findViewById(R.id.reviewsRecView_cart_list);
        btnNext = (Button)view.findViewById(R.id.btnNextCart);
        textnoOfItem = (TextView)view.findViewById(R.id.textNoItemListCart);
    }
}
