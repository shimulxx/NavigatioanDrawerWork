package com.example.navigtiondrawerwork.Models;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.navigtiondrawerwork.R;


import java.util.ArrayList;

public class GroceryItemAdapter extends RecyclerView.Adapter<GroceryItemAdapter.ViewHolder>{
    private static final String TAG = "GroceryItemAdapter";

    private ArrayList<GroceryItem> groceryItems = new ArrayList<>();

    private Context context;

    public GroceryItemAdapter() {
    }

    public GroceryItemAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grocery_rec_view_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.name.setText(groceryItems.get(position).getName());
        holder.price.setText(String.valueOf(groceryItems.get(position).getPrice()));
        Glide.with(context)
                .asBitmap()
                .load(groceryItems.get(position).getImageUrl())
                .into(holder.image);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: navigate to another activity
                Intent intent = new Intent(context, GroceryItemActivity.class);
                intent.putExtra("groceryItem", groceryItems.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView name;
        private TextView price;
        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.itemImage);
            name = (TextView)itemView.findViewById(R.id.textName);
            price = (TextView)itemView.findViewById(R.id.textPrice);
            parent = (CardView)itemView.findViewById(R.id.parent);
        }
    }

    public void setGroceryItems(ArrayList<GroceryItem> groceryItems) {
        this.groceryItems = groceryItems;
        notifyDataSetChanged();
    }
}
