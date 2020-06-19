package com.example.navigtiondrawerwork.Models;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.navigtiondrawerwork.R;
import com.example.navigtiondrawerwork.Utils;

import java.util.ArrayList;

public class ShowItemByCategoryActivity extends AppCompatActivity {

    private static final String TAG = "ShowItemByCategoryActiv";

    private TextView textName;
    private RecyclerView recyclerView;
    private GroceryItemAdapter groceryItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item_by_category);
        
        initViews();

        groceryItemAdapter = new GroceryItemAdapter(this);
        recyclerView.setAdapter(groceryItemAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        try{
            Intent intent = getIntent();
            String category = intent.getStringExtra("category");
            Utils utils = new Utils(this);
            ArrayList<GroceryItem> groceryItems = utils.getItemByCategory(category);
            groceryItemAdapter.setGroceryItems(groceryItems);
            textName.setText(category);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }
    private void initViews(){
        Log.d(TAG, "initViews: starrted");
        textName = (TextView)findViewById(R.id.textCategory);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewShowItemByCat);
    }
}
