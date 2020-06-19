package com.example.navigtiondrawerwork.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.navigtiondrawerwork.Utils;

import java.util.ArrayList;

public class Orders implements Parcelable {
    private int id;
    private ArrayList<Integer> groceryItems;
    private String addeess;
    private String email;
    private String phoneNumber;
    private String zipCode;
    private String paymentMethod;
    private boolean success;
    private double totalPrice;

    public Orders() {
    }

    public Orders(ArrayList<Integer> groceryItems, String addeess, String email, String phoneNumber, String zipCode, String paymentMethod, boolean success, double totalPrice) {
        this.id = Utils.getOrderId();
        this.groceryItems = groceryItems;
        this.addeess = addeess;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
        this.paymentMethod = paymentMethod;
        this.success = success;
        this.totalPrice = totalPrice;
    }


    protected Orders(Parcel in) {
        id = in.readInt();
        addeess = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        zipCode = in.readString();
        paymentMethod = in.readString();
        success = in.readByte() != 0;
        totalPrice = in.readDouble();
    }

    public static final Creator<Orders> CREATOR = new Creator<Orders>() {
        @Override
        public Orders createFromParcel(Parcel in) {
            return new Orders(in);
        }

        @Override
        public Orders[] newArray(int size) {
            return new Orders[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getGroceryItems() {
        return groceryItems;
    }

    public void setGroceryItems(ArrayList<Integer> groceryItems) {
        this.groceryItems = groceryItems;
    }

    public String getAddeess() {
        return addeess;
    }

    public void setAddeess(String addeess) {
        this.addeess = addeess;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", groceryItems=" + groceryItems +
                ", addeess='" + addeess + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", success=" + success +
                ", totalPrice=" + totalPrice +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(addeess);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeString(zipCode);
        dest.writeString(paymentMethod);
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeDouble(totalPrice);
    }
}

