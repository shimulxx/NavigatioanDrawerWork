package com.example.navigtiondrawerwork.Models;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.navigtiondrawerwork.R;
import com.example.navigtiondrawerwork.Utils;

import java.util.ArrayList;

public class GroceryItemActivity extends AppCompatActivity implements AddReviewDialog.AddReview{
    private static final String TAG = "GroceryItemActivity";

    private TextView txtName, txtPrice, txtDescriptoin, txtAvailability;
    private ImageView itemImage;
    private Button btnAddtoCart;

    private ImageView firstFilledStar,secondFilledStar, thirdFilledStar, firstEmptyStar, secondEmptyStar, thirdEmptyStar;
    private RecyclerView reviewRecyclearView;

    private RelativeLayout addReviewRelLayout;

    private GroceryItem incommingGroceryItem;

    private ReviewsAdapter reviewsAdapter;

    private Utils utils;

    private int curRate = 0;

    private TrackUserTime trackUserTimeService;
    private boolean isBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackUserTime.LocalBinder localBinder = (TrackUserTime.LocalBinder) service;
            trackUserTimeService = localBinder.getService();
            isBound = true;
            trackUserTimeService.setGroceryItem(incommingGroceryItem);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
       Intent intent = new Intent(this, TrackUserTime.class);
       bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isBound)
           unbindService(serviceConnection);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_item);

        initViews();

        utils = new Utils(this);

        Intent intent = getIntent();
        try{
            incommingGroceryItem = intent.getParcelableExtra("groceryItem");
            this.curRate = incommingGroceryItem.getRate();
            changeVisibility(curRate);
            utils.increaseUserPoint(incommingGroceryItem, 1);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        setViewsVal();
    }
    private void setViewsVal(){
        Log.d(TAG, "setViewsVal: started");
        txtName.setText(incommingGroceryItem.getName());
        txtPrice.setText(String.valueOf(incommingGroceryItem.getPrice() + "$"));
        txtAvailability.setText(String.valueOf(incommingGroceryItem.getAvailableAmount() + " number(s) available"));
        txtDescriptoin.setText(incommingGroceryItem.getDescription());

        Glide.with(this)
                .asBitmap()
                .load(incommingGroceryItem.getImageUrl())
                .into(itemImage);

        btnAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.addItemToCart(incommingGroceryItem.getId());
                Intent intent = new Intent(GroceryItemActivity.this, CartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        handleStarSituation();

        //TODO: Handle star condition
        reviewsAdapter = new ReviewsAdapter();
        reviewRecyclearView.setAdapter(reviewsAdapter);
        reviewRecyclearView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Review> reviews = utils.getReviewItems(incommingGroceryItem.getId());
        if(reviews != null){
            reviewsAdapter.setReviews(reviews);
        }
        addReviewRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Show dialog
                AddReviewDialog addReviewDialog = new AddReviewDialog();
                Bundle bundle = new Bundle();
                bundle.putParcelable("groceryItem", incommingGroceryItem);
                addReviewDialog.setArguments(bundle);
                addReviewDialog.show(getSupportFragmentManager(),"Add review dialog");
            }
        });

    }
    private void handleStarSituation(){
        Log.d(TAG, "handleStarSituation: started");
        
        firstEmptyStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabase(1);
                changeVisibility(1);
                changeUserPoint(1);
                /*if(checkIfRateHasChanged(1)){
                    updateDatabase(1);
                    changeVisibility(1);
                }*/
            }
        });
        secondEmptyStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabase(2);
                changeVisibility(2);
                changeUserPoint(2);
                /*if(checkIfRateHasChanged(2)){
                    updateDatabase(2);
                    changeVisibility(2);
                }*/
            }
        });
        thirdEmptyStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateDatabase(3);
               changeVisibility(3);
               changeUserPoint(3);
                /*if(checkIfRateHasChanged(3)){
                    updateDatabase(3);
                    changeVisibility(3);
                }*/
            }
        });

        firstFilledStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabase(1);
                changeVisibility(1);
                changeUserPoint(1);
                /*if(checkIfRateHasChanged(1)){
                    updateDatabase(1);
                    changeVisibility(1);
                }*/
            }
        });
        secondFilledStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabase(2);
                changeVisibility(2);
                changeUserPoint(2);
                /*if(checkIfRateHasChanged(2)){
                    updateDatabase(2);
                    changeVisibility(2);
                }*/
            }
        });
        thirdFilledStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabase(3);
                changeVisibility(3);
                changeUserPoint(3);
                /*if(checkIfRateHasChanged(3)){
                    updateDatabase(3);
                    changeVisibility(3);
                }*/
            }
        });
    }
    private void changeUserPoint(int stars){
        Log.d(TAG, "changeUserPoint: started");
        utils.increaseUserPoint(incommingGroceryItem, (stars - curRate) * 2);
    }
    private void updateDatabase(int newRate){
        Log.d(TAG, "updateDatabase: started");
        utils.updateTheRate(incommingGroceryItem, newRate);
    }
    private boolean checkIfRateHasChanged(int newRate){
        Log.d(TAG, "checkIfRateHasChanged: started");
        if(newRate == curRate)
            return false;
        else
            return true;
    }
    private void changeVisibility(int newRate){
        Log.d(TAG, "changeVisibility: started");
        switch(newRate){
            case 0:
                firstFilledStar.setVisibility(View.GONE);
                secondFilledStar.setVisibility(View.GONE);
                thirdFilledStar.setVisibility(View.GONE);
                firstEmptyStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.VISIBLE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                break;
            case 1:
                firstFilledStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.GONE);
                thirdFilledStar.setVisibility(View.GONE);
                firstEmptyStar.setVisibility(View.GONE);
                secondEmptyStar.setVisibility(View.VISIBLE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                break;
            case 2:
                firstFilledStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                firstEmptyStar.setVisibility(View.GONE);
                secondEmptyStar.setVisibility(View.GONE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                break;
            case 3:
                firstFilledStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.VISIBLE);
                firstEmptyStar.setVisibility(View.GONE);
                secondEmptyStar.setVisibility(View.GONE);
                thirdEmptyStar.setVisibility(View.GONE);
                break;
            default:
                firstFilledStar.setVisibility(View.GONE);
                secondFilledStar.setVisibility(View.GONE);
                thirdFilledStar.setVisibility(View.GONE);
                firstEmptyStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.VISIBLE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
        }
    }
    private void initViews(){
        Log.d(TAG, "initViews: started");

        txtName = (TextView)findViewById(R.id.textName);
        txtPrice = (TextView)findViewById(R.id.textPriceGroceryItem);
        txtDescriptoin = (TextView)findViewById(R.id.txtDesc);
        txtAvailability = (TextView)findViewById(R.id.txtAvailability);

        itemImage = (ImageView)findViewById(R.id.itemImage);

        btnAddtoCart = (Button)findViewById(R.id.btnAddtoCart);

        firstFilledStar = (ImageView)findViewById(R.id.firstFilledStar);
        secondFilledStar = (ImageView)findViewById(R.id.secondFilledStar);
        thirdFilledStar = (ImageView)findViewById(R.id.thirdFilledStar);

        firstEmptyStar = (ImageView)findViewById(R.id.firstEmptyStar);
        secondEmptyStar =(ImageView)findViewById(R.id.secondEmptyStar);
        thirdEmptyStar = (ImageView)findViewById(R.id.thirdEmptyStar);

        reviewRecyclearView = (RecyclerView)findViewById(R.id.reviewsRecView);

        addReviewRelLayout = (RelativeLayout)findViewById(R.id.addReviewRelLayout);
    }

    @Override
    public void onAddReviewResult(Review review) {
        Log.d(TAG, "onAddReviewResult: we are adding " + review.toString());
        utils.addReview(review);
        utils.increaseUserPoint(incommingGroceryItem, 3);
        ArrayList<Review> reviews = utils.getReviewItems(review.getGroceryItemId());
        if(reviews != null){
            reviewsAdapter.setReviews(reviews);
        }
    }
}
