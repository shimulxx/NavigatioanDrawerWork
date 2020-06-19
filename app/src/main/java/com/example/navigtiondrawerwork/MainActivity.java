package com.example.navigtiondrawerwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;


import com.example.navigtiondrawerwork.Models.CartActivity;
import com.example.navigtiondrawerwork.Models.LicenseDialog;
import com.example.navigtiondrawerwork.Models.ShowAllCategoriesDialog;
import com.example.navigtiondrawerwork.Models.ShowItemByCategoryActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ShowAllCategoriesDialog.SelectCategory {

    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed);
        //drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_frameLayout, new MainFragment());
        fragmentTransaction.commit();
    }

    private void initViews(){
        Log.d(TAG, "initViews: started");
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        navigationView = (NavigationView)findViewById(R.id.navigationDrawer);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cartNavigationDrawer:
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.categories:
                ShowAllCategoriesDialog showAllCategoriesDialog = new ShowAllCategoriesDialog();
                showAllCategoriesDialog.show(getSupportFragmentManager(), "All categories");
                break;
            case R.id.aboutUs:
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("About")
                        .setMessage("Developed by MUSTAFA HAMIM")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO: Something
                            }
                        });
                builder.create().show();
                break;
            case R.id.licenses:
                LicenseDialog licenseDialog = new LicenseDialog();
                licenseDialog.show(getSupportFragmentManager(), "License Dialog");
                break;
            case R.id.terms:
                AlertDialog.Builder termsBuilder = new AlertDialog.Builder(this)
                        .setTitle("Terms of use")
                        .setMessage("No extra terms\n enjoy :)")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO: Something
                            }
                        });
                termsBuilder.create().show();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onSelectCategoryResult(String category) {
        Log.d(TAG, "onSelectCategoryResult: " + category);
        Intent intent = new Intent(this, ShowItemByCategoryActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}
