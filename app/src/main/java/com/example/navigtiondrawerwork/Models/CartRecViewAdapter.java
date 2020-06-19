package com.example.navigtiondrawerwork.Models;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigtiondrawerwork.R;

import java.util.ArrayList;

public class CartRecViewAdapter extends RecyclerView.Adapter<CartRecViewAdapter.ViewHolder>{
    private static final String TAG = "CartRecViewAdapter";

    private ArrayList<GroceryItem> groceryItems = new ArrayList<>();

    public interface GetTotalPrice{
        void onGettingTotalPriceResult(double price);
    }

    public interface DeleteCartItem {
        void onDeletingResult(GroceryItem groceryItem);
    }

    private Fragment fragment;

    private GetTotalPrice getTotalPrice;

    private DeleteCartItem deleteCartItem;

    public CartRecViewAdapter() {
    }

    public CartRecViewAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_rec_view_list_item,parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: started");
        holder.textName.setText(groceryItems.get(position).getName());
        holder.textPrice.setText(String.valueOf(groceryItems.get(position).getPrice()));

        deleteCartItem = (DeleteCartItem) fragment;

        holder.textDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity())
                        .setTitle("Delete Item ?")
                        .setMessage("Are you sure you want to delete " + groceryItems.get(position).getName() + " ?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteCartItem.onDeletingResult(groceryItems.get(position));
                            }
                        });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    private void calculatePrice(){
        Log.d(TAG, "calculatePrice: started");

        try{
            getTotalPrice = (GetTotalPrice)fragment;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        double totelPrice = 0;
        for(GroceryItem groceryItem: groceryItems){
            totelPrice += groceryItem.getPrice();
        }
        getTotalPrice.onGettingTotalPriceResult(totelPrice);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textName, textPrice, textDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.textNameCartItemsList);
            textPrice = (TextView)itemView.findViewById(R.id.textPriceCartListItems);
            textDelete = (TextView)itemView.findViewById(R.id.btnDeleteCartListItems);
        }
    }
    public void setGroceryItems(ArrayList<GroceryItem> groceryItems) {
        this.groceryItems = groceryItems;
        calculatePrice();
        notifyDataSetChanged();
    }
}
