package com.example.navigtiondrawerwork;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.navigtiondrawerwork.Models.GroceryItem;
import com.example.navigtiondrawerwork.Models.Review;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Utils {
    private static final String TAG = "Utils";

    public static final String DATABASE_NAME = "fake_database";
    private static int ID = 0;
    private static int ORDER_ID = 0;
    ArrayList<GroceryItem> groceryItems = new ArrayList<>();

    private Context context;

    public Utils(Context context) {
        this.context = context;
    }

    public void updateTheRate(GroceryItem groceryItem, int newRate){
        Log.d(TAG, "updateTheRate: started");
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<GroceryItem>>(){}.getType();
        ArrayList<GroceryItem> items = gson.fromJson(sharedPreferences.getString("allgroceryitems",null),type);
        if(items != null){
            ArrayList<GroceryItem> newItems = new ArrayList<>();
            for(GroceryItem i: items){
                if(i.getId() == groceryItem.getId()){
                    i.setRate(newRate);
                }
                newItems.add(i);
            }
            //groceryItems = newItems;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("allgroceryitems",gson.toJson(newItems));
            editor.commit();
        }
    }

    public static int getID(){
        ++ID;
        return ID;
    }

    public static int getOrderId(){
        ++ORDER_ID;
        return ORDER_ID;
    }

    public boolean addReview(Review review){
        Log.d(TAG, "addReview: started");
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<GroceryItem>>(){}.getType();
        ArrayList<GroceryItem> groceryItemsInternal  = gson.fromJson(sharedPreferences.getString("allgroceryitems", ""), type);

        if(null != groceryItemsInternal){
            ArrayList<GroceryItem> newGroceryItems = new ArrayList<>();
            for(GroceryItem groceryItem: groceryItemsInternal){
                if(groceryItem.getId() == review.getGroceryItemId()){
                    ArrayList<Review> reviews = groceryItem.getReviews();
                    reviews.add(review);
                    groceryItem.setReviews(reviews);
                }
                newGroceryItems.add(groceryItem);
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("allgroceryitems", gson.toJson(newGroceryItems));
            editor.commit();
            return true;
        }
        return false;
    }

    public ArrayList<Review> getReviewItems(int id){
        Log.d(TAG, "getReviewItems: started");
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<GroceryItem>>(){}.getType();
        ArrayList<GroceryItem> groceryItems = gson.fromJson(sharedPreferences.getString("allgroceryitems",""),type);
        if(groceryItems != null){
            for(GroceryItem groceryItem : groceryItems){
                if(groceryItem.getId() == id)
                    return groceryItem.getReviews();
            }
        }
        return null;
    }

    public void initDatabase(){
        Log.d(TAG, "initDatabase: started");
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        Type type = new TypeToken<ArrayList<GroceryItem>>(){}.getType();
        ArrayList<GroceryItem> possiblegroceryItems = gson.fromJson(sharedPreferences.getString("allgroceryitems",""), type);
        if(possiblegroceryItems == null){
            initGroceryItems();
        }
    }

    public ArrayList<GroceryItem> getAllGroceryItems(){
        Log.d(TAG, "getAllGroceryItems: started");
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<GroceryItem>>(){}.getType();
        ArrayList<GroceryItem> groceryItems = gson.fromJson(sharedPreferences.getString("allgroceryitems", null),type);
        return  groceryItems;
    }

    public ArrayList<GroceryItem> searchForItems(String text){
        Log.d(TAG, "searchForItems: started");

        Gson gson = new Gson();

        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);

        Type type = new TypeToken<ArrayList<GroceryItem>>(){}.getType();

        ArrayList<GroceryItem> allItems = gson.fromJson(sharedPreferences.getString("allgroceryitems", null),type);
        ArrayList<GroceryItem> searchItems = new ArrayList<>();

        if(allItems != null){
            for(GroceryItem item: allItems){
                if(item.getName().equalsIgnoreCase(text)){
                    searchItems.add(item);
                }

                String[] splittedString = item.getName().split(" ");
                for(int i = 0; i <splittedString.length; ++i){
                    if(splittedString[i].equalsIgnoreCase(text)){
                        boolean doesExist = false;
                        for(GroceryItem searchItem : searchItems){
                            if(searchItem.equals(item)){
                                doesExist =true;
                            }
                        }

                        if(!doesExist)
                            searchItems.add(item);
                    }
                }
            }
        }
        return searchItems;
    }

    private void initGroceryItems(){
        Log.d(TAG, "initGroceryItems: started");
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        GroceryItem iceCream = new GroceryItem("Ice cream", "produced from fresh milk",
                "https://images.unsplash.com/photo-1497034825429-c343d7c6a68f?ixlib=rb-1.2.1&w=1000&q=80",
                "Food",10,20);

        groceryItems.add(iceCream);
        groceryItems.add(new GroceryItem("Cheese", "best cheese possible",
                "https://cdn11.bigcommerce.com/s-7c08qbh/images/stencil/1280x1280/products/225/913/Idiazabal_web_edited_db__24833.1432909422.jpg?c=2&imbypass=on",
                "Food",3, 3.5));
        groceryItems.add(new GroceryItem("Cucumber","fresh item",
                "https://images.pexels.com/photos/2329440/pexels-photo-2329440.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
                "Vegetables",10,2.5));
        groceryItems.add(new GroceryItem("Cocacola","Tasty drink",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f6/15-09-26-RalfR-WLC-0098.jpg/800px-15-09-26-RalfR-WLC-0098.jpg",
                "Soft Drink",30,5.5));
        groceryItems.add(new GroceryItem("Spaghetti","easy to cook and meal",
                "https://www.errenskitchen.com/wp-content/uploads/2015/02/Quick-Easy-Spaghetti-Bolognese2-1.jpg",
                "Food",20,1.5));
        groceryItems.add(new GroceryItem("Chicken Nugget","tasty food which usually can eat 4 person",
                "https://food.fnr.sndimg.com/content/dam/images/food/fullset/2013/9/12/1/FN_Picky-Eaters-Chicken-Nuggets_s4x3.jpg.rend.hgtvcom.826.620.suffix/1383770571120.jpeg",
                "Food",5,10));
        groceryItems.add(new GroceryItem("Clear Shampoo","you won not experience hair fall with this",
                "https://images-na.ssl-images-amazon.com/images/I/51R7SariONL.jpg",
                "Hygiene",10,8.5));
        groceryItems.add(new GroceryItem("Axe body spray","is hot and sweaty ! any more !",
                "https://images-na.ssl-images-amazon.com/images/I/81ciFYVrsUL._SL1500_.jpg",
                "Hygiene",15, 20));
        groceryItems.add(new GroceryItem("Kleenex","soft and famous !",
                "https://images-na.ssl-images-amazon.com/images/I/81UPIKA7TqL._AC_SL1500_.jpg",
                "Hygiene",50,2.5));

        String finalstring = gson.toJson(groceryItems);
        editor.putString("allgroceryitems", finalstring);
        editor.commit();
    }

    public void addItemToCart(int id){
        Log.d(TAG, "addItemToCart: started");
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
        ArrayList<Integer> cartItems = gson.fromJson(sharedPreferences.getString("cartItems", null), type);
        if(cartItems == null){
            cartItems = new ArrayList<>();
        }
        cartItems.add(id);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cartItems", gson.toJson(cartItems));
        editor.commit();
    }

    public ArrayList<String> getThreecategories(){
        Log.d(TAG, "getThreecategories: started");
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<GroceryItem>>(){}.getType();
        ArrayList<GroceryItem> groceryItems = gson.fromJson(sharedPreferences.getString("allgroceryitems", null),type);

        ArrayList<String> categories = new ArrayList<>();

        if(groceryItems != null){
            for(int i = 0; i < groceryItems.size(); ++i){
                if(categories.size() < 3){
                    boolean doesExist = false;
                    for(String s: categories){
                        if(groceryItems.get(i).getCategory().equals(s)){
                            doesExist = true;
                        }
                    }
                    if(!doesExist)
                        categories.add(groceryItems.get(i).getCategory());
                }
            }
        }
        return categories;
    }

    public ArrayList<String> getAllcategories(){
        Log.d(TAG, "getThreecategories: started");
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<GroceryItem>>(){}.getType();
        ArrayList<GroceryItem> groceryItems = gson.fromJson(sharedPreferences.getString("allgroceryitems", null),type);

        ArrayList<String> categories = new ArrayList<>();

        if(groceryItems != null){
            for(int i = 0; i < groceryItems.size(); ++i){
                boolean doesExist = false;
                for(String s: categories){
                    if(groceryItems.get(i).getCategory().equals(s)){
                        doesExist = true;
                    }
                }
                if(!doesExist)
                    categories.add(groceryItems.get(i).getCategory());
            }
        }
        return categories;
    }

    public ArrayList<GroceryItem> getItemByCategory(String category){
        Log.d(TAG, "getItemByCategory: started");
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<GroceryItem>>(){}.getType();
        ArrayList<GroceryItem> groceryItems = gson.fromJson(sharedPreferences.getString("allgroceryitems", null),type);
        ArrayList<GroceryItem> newGroceryItems = new ArrayList<>();
        if(groceryItems != null){
            for(GroceryItem groceryItem : groceryItems){
                if(groceryItem.getCategory().equals(category)){
                    newGroceryItems.add(groceryItem);
                }
            }
        }
        return newGroceryItems;
    }
    
    public ArrayList<Integer> getCartItems(){
        Log.d(TAG, "getCartItems: started");

        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
        ArrayList<Integer> groceryCartItems = gson.fromJson(sharedPreferences.getString("cartItems", null),type);
        return groceryCartItems;
    }

    public ArrayList<GroceryItem> getItemsById(ArrayList<Integer> ids){
        Log.d(TAG, "getItemsById: started");
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<GroceryItem>>(){}.getType();
        ArrayList<GroceryItem> groceryCartItems = gson.fromJson(sharedPreferences.getString("allgroceryitems", null),type);
        ArrayList<GroceryItem> resultItems = new ArrayList<>();

        if(groceryCartItems != null){
            for(int id:ids){
                for(GroceryItem groceryItem: groceryCartItems){
                    if(groceryItem.getId() == id){
                        resultItems.add(groceryItem);
                        break;
                    }
                }
            }
        }
        return resultItems;
    }

    public ArrayList<Integer> deleteCartItem(GroceryItem item){
        Log.d(TAG, "deleteCartItem: started");

        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
        ArrayList<Integer> integerArrayList = gson.fromJson(sharedPreferences.getString("cartItems", null),type);
        ArrayList<Integer> newItems = new ArrayList<>();
        if(integerArrayList != null){
            for(int i: integerArrayList){
                if(item.getId() != i){
                    newItems.add(i);
                }
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("cartItems",gson.toJson(newItems));
            editor.commit();
        }
        return newItems;
    }

    public void removeCartItems(){
        Log.d(TAG, "removeCartItems: started");
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("cartItems");
        editor.apply();
    }

    public void addPopularitypoint(ArrayList<Integer> integerArrayList){
        Log.d(TAG, "addPopularitypoint: started");
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<GroceryItem>>(){}.getType();
        ArrayList<GroceryItem> groceryCartItems = gson.fromJson(sharedPreferences.getString("allgroceryitems", null),type);
        ArrayList<GroceryItem> resultItems = new ArrayList<>();

        for(GroceryItem groceryItem: groceryCartItems){
            for(int j: integerArrayList){
                if(groceryItem.getId() == j){
                    groceryItem.setPopularityPoint(groceryItem.getPopularityPoint() + 1);
                }
            }
            resultItems.add(groceryItem);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("allgroceryitems", gson.toJson(resultItems));
        editor.apply();
    }

    public void increaseUserPoint(GroceryItem groceryItem, int points){
        Log.d(TAG, "increaseUserPoint: started to increase user point for " + groceryItem.getName() + " for " + points);

        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<GroceryItem>>(){}.getType();
        ArrayList<GroceryItem> groceryItems = gson.fromJson(sharedPreferences.getString("allgroceryitems", null),type);

        if(groceryItems != null){
            ArrayList<GroceryItem> newItems = new ArrayList<>();
            for(GroceryItem item : groceryItems){
                if(item.getId() == groceryItem.getId()){
                    item.setUserPoint(item.getUserPoint() + points);
                }
                newItems.add(item);
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("allgroceryitems", gson.toJson(newItems));
            editor.commit();
        }
    }
}
