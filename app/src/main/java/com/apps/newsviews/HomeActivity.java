package com.apps.newsviews;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.apps.newsviews.activity.LoginActivity;
import com.apps.newsviews.utility.ConstantKey;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Navigation
    private ActionBarDrawerToggle toggle;

    private static final String TAG = "HomeActivity";
    private Activity context;
    private SharedPreferences preferences;
    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //===============================================| Getting SharedPreferences
        preferences = getSharedPreferences(ConstantKey.USER_LOGIN_KEY, MODE_PRIVATE);
        isLoggedIn = preferences.getBoolean(ConstantKey.USER_IS_LOGGED_KEY, false);

        //====================================| Custom Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            //toolbar.getBackground().setAlpha(200);
        }

        //====================================================| To Display Navigation Bar
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_id);
        toggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_id);
        View hView =  navigationView.getHeaderView(0);
        CircleImageView navUserPhoto = (CircleImageView)hView.findViewById(R.id.nav_header_photo);
        navUserPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_mk_logo));
        //TextView navUserName = (TextView)hView.findViewById(R.id.nav_header_user);
        //navUserName.setText("Anonymous");
        navigationView.setNavigationItemSelectedListener(HomeActivity.this);
        //separator Item Decoration
        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(HomeActivity.this,DividerItemDecoration.VERTICAL));
    }

    //====================================================| Back press disabled and OptionsMenu
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //====================================================| Navigation
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.home_id) {
            startActivity(new Intent(HomeActivity.this, HomeActivity.class));
        }
        if (menuItem.getItemId() == R.id.about_id) {
            aboutMe();
        }
        if (menuItem.getItemId() == R.id.exit_id) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }
        return false;
    }

    //====================================================| Alert Message
    public void alertDialog(String msg) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).show();
    }

    //====================================================| About
    public void aboutMe() {
        new AlertDialog.Builder(HomeActivity.this)
                .setTitle(R.string.alert_about)
                .setMessage(HomeActivity.this.getString(R.string.apps_details))
                .setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Toast.makeText(getApplicationContext(), "Thank You", Toast.LENGTH_SHORT);
                    }
                }).show();
    }
}
