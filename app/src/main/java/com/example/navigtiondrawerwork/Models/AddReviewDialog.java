package com.example.navigtiondrawerwork.Models;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.navigtiondrawerwork.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddReviewDialog extends DialogFragment {
    private static final String TAG = "AddReviewDialog";

    private EditText editTextName, editTextReview;
    private TextView textItemName, textWarning;
    private Button btnAddReview;

    private int groceryItemId = 0;

    public interface  AddReview{
        void onAddReviewResult(Review review);
    }
    private AddReview addReview;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_review, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Add Review")
                .setView(view);

        initViews(view);

        Bundle bundle = getArguments();
        try {
            GroceryItem groceryItem = bundle.getParcelable("groceryItem");
            textItemName.setText(groceryItem.getName());
            this.groceryItemId = groceryItem.getId();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReview();
            }
        });
        return builder.create();
    }
    private void addReview(){
        Log.d(TAG, "addReview: started");
        if(validateData()){
            String name = editTextName.getText().toString();
            String reviewText = editTextReview.getText().toString();
            String date = getCurDate();

            Review review = new Review(groceryItemId, name, date, reviewText);

            try{
                addReview = (AddReview)getActivity();
                addReview.onAddReviewResult(review);
                dismiss();
            }catch (ClassCastException e){
                e.printStackTrace();
            }
        }else{
            textWarning.setVisibility(View.VISIBLE);
        }

    }
    private boolean validateData(){
        if(editTextName.getText().toString().equals(""))
            return false;
        if(editTextReview.getText().toString().equals(""))
            return false;
        return true;
    }
    private String getCurDate(){
        Log.d(TAG, "getCurDate: started");
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(date);
    }
    private void initViews(View view){
        editTextName = (EditText)view.findViewById(R.id.editTextName);
        editTextReview = (EditText)view.findViewById(R.id.editTextReview);

        textItemName = (TextView) view.findViewById(R.id.textItemName);
        textWarning = (TextView)view.findViewById(R.id.textWarning);

        btnAddReview = (Button)view.findViewById(R.id.btnAdd);
    }
}
