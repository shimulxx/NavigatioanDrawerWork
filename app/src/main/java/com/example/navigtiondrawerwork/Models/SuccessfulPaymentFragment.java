package com.example.navigtiondrawerwork.Models;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.navigtiondrawerwork.MainActivity;
import com.example.navigtiondrawerwork.R;
import com.example.navigtiondrawerwork.Utils;

import java.util.ArrayList;

public class SuccessfulPaymentFragment extends Fragment {
    private static final String TAG = "SuccessfulPaymentFragme";

    private Button butonGoBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success_payment, container, false);

        Utils utils = new Utils(getActivity());
        utils.removeCartItems();

        Bundle bundle = getArguments();
        if(bundle != null){
            Orders orders = bundle.getParcelable("orders");
            ArrayList<Integer> itemIds = orders.getGroceryItems();
            ArrayList<GroceryItem> groceryItems = utils.getItemsById(itemIds);
            for(GroceryItem groceryItem: groceryItems){
                ArrayList<GroceryItem> groceryItemsSameCategory;
                groceryItemsSameCategory = utils.getItemByCategory(groceryItem.getCategory());
                for(GroceryItem j: groceryItemsSameCategory){
                    utils.increaseUserPoint(j, 4);
                }
            }
            utils.addPopularitypoint(itemIds);
        }

        butonGoBack = (Button)view.findViewById(R.id.buttonGoBackSuccessFragment);

        butonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        return view;
    }
}
