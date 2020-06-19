package com.example.navigtiondrawerwork;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigtiondrawerwork.Models.CartActivity;
import com.example.navigtiondrawerwork.Models.GroceryItem;
import com.example.navigtiondrawerwork.Models.GroceryItemAdapter;
import com.example.navigtiondrawerwork.Models.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";

    private BottomNavigationView bottomNavigationView;
    private RecyclerView newItemRecview, popularItemRecView, suggestedItemRecView;
    private GroceryItemAdapter newItemAdapter, popularItemAdapter, suggestedItemAdapter;
    private Utils utils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initViews(view);

        initBottomNavigation();

        utils = new Utils(getActivity());
        utils.initDatabase();

        initRecViewsAdapterAndSetAdapter();

        //setRecAdapterValues(); not necessary

        return view;
    }

    private void initBottomNavigation() {
        Log.d(TAG, "initBottomNavigation: started");
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cartBottomNavigation:
                        Intent cartIntent = new Intent(getActivity(), CartActivity.class);
                        cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(cartIntent);
                        break;
                    case R.id.search:
                        //TODO:: FIX this
                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    case R.id.home:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void initViews(View view) {
        Log.d(TAG, "initViews: started");
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation_view);
        newItemRecview = (RecyclerView) view.findViewById(R.id.newItemRecView);
        popularItemRecView = (RecyclerView) view.findViewById(R.id.popularItemRecView);
        suggestedItemRecView = (RecyclerView) view.findViewById(R.id.suggestedItemRecView);
    }
    private void initRecViewsAdapterAndSetAdapter(){
        Log.d(TAG, "initRecViews: started");
        newItemAdapter = new GroceryItemAdapter(getActivity());
        popularItemAdapter = new GroceryItemAdapter(getActivity());
        suggestedItemAdapter = new GroceryItemAdapter(getActivity());

        newItemRecview.setAdapter(newItemAdapter);
        popularItemRecView.setAdapter(popularItemAdapter);
        suggestedItemRecView.setAdapter(suggestedItemAdapter);

        newItemRecview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        popularItemRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        suggestedItemRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        //setRecAdapterValues(); not necessary
    }
    private void setRecAdapterValues(){
        ArrayList<GroceryItem> newGroceryItems = utils.getAllGroceryItems();

        Comparator<GroceryItem> groceryNewItemComparator = new Comparator<GroceryItem>() {
            @Override
            public int compare(GroceryItem o1, GroceryItem o2) {
                return o1.getId() - o2.getId();
            }
        };

        Comparator<GroceryItem> reverseNewItemComparator = Collections.reverseOrder(groceryNewItemComparator);
        Collections.sort(newGroceryItems, reverseNewItemComparator);

        newItemAdapter.setGroceryItems(newGroceryItems);

        ArrayList<GroceryItem> popularGroceryItems = utils.getAllGroceryItems();

        Comparator<GroceryItem> popularityComparator = new Comparator<GroceryItem>() {
            @Override
            public int compare(GroceryItem o1, GroceryItem o2) {
                return compareByPopularity(o1,o2);
            }
        };

        Comparator<GroceryItem> reversePopularityComparator = Collections.reverseOrder(popularityComparator);
        Collections.sort(popularGroceryItems, reversePopularityComparator);
        popularItemAdapter.setGroceryItems(popularGroceryItems);

        ArrayList<GroceryItem> suggestedGroceryItems = utils.getAllGroceryItems();
        Comparator<GroceryItem> suggestedGroceryItemsComparator = new Comparator<GroceryItem>() {
            @Override
            public int compare(GroceryItem o1, GroceryItem o2) {
                return o1.getUserPoint() - o2.getUserPoint();
            }
        };
        Comparator<GroceryItem> suggestedReverseGroceryItemComparator = Collections.reverseOrder(suggestedGroceryItemsComparator);
        Collections.sort(suggestedGroceryItems, suggestedReverseGroceryItemComparator);
        suggestedItemAdapter.setGroceryItems(suggestedGroceryItems);
    }

    @Override
    public void onResume() {
        setRecAdapterValues();
        super.onResume();
    }

    private int compareByPopularity(GroceryItem groceryItem1, GroceryItem groceryItem2){
        Log.d(TAG, "compareByPopularity: started");
        if(groceryItem1.getPopularityPoint() > groceryItem2.getPopularityPoint())
            return 1;
        else if(groceryItem1.getPopularityPoint() < groceryItem2.getPopularityPoint())
            return -1;
        else
            return 0;
    }
}
