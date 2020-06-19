package com.example.navigtiondrawerwork.Models;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.navigtiondrawerwork.Utils;

public class TrackUserTime extends Service {

    private static final String TAG = "TrackUserTime";
    private IBinder binder = new LocalBinder();
    private int seconds = 0;
    private GroceryItem groceryItem;
    private boolean shouldFinished = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: started");
        trackTime();
        return binder;
    }
    public class LocalBinder extends Binder{
        TrackUserTime getService(){
            return TrackUserTime.this;
        }
    }
    private void trackTime(){
        Log.d(TAG, "trackTime: started");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!shouldFinished){
                    try {
                        Thread.sleep(1000);
                        ++seconds;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void setGroceryItem(GroceryItem groceryItem) {
        this.groceryItem = groceryItem;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: started");
        shouldFinished = true;
        Utils utils = new Utils(this);
        int minutes = seconds / 60;
        utils.increaseUserPoint(groceryItem, minutes * 2);
    }
}
