package com.apps.newsviews;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.apps.newsviews.activity.LoginActivity;
import com.apps.newsviews.model.ArticleModel;
import com.apps.newsviews.model.ResponseModel;
import com.apps.newsviews.retrofit.RetrofitClient;
import com.apps.newsviews.utility.ConstantKey;
import com.apps.newsviews.adapter.RecyclerAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Navigation
    private ActionBarDrawerToggle toggle;

    private static final String TAG = "HomeActivity";
    private Activity context;
    private SharedPreferences preferences;
    private boolean isLoggedIn;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<ArticleModel> articleList;
    private ProgressDialog progress;

    //private static final String API_KEY = "9688fe5fdd5148c4bcee81fcf94e1837";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this;
        progress = new ProgressDialog(this);

        //===============================================| RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.news_recycler);
        //final LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        articleList = new ArrayList<>();

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

    @Override
    protected void onStart() {
        super.onStart();

        progress.setMessage(context.getString(R.string.progress));
        progress.show();

        articleList.clear();

        Call<ResponseModel> call = RetrofitClient.getInstance().getApi().getNews("bitcoin", "2018-11-25", "publishedAt", "9688fe5fdd5148c4bcee81fcf94e1837");
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.body().getStatus().equals("ok")) {
                    ArrayList<ArticleModel> list = response.body().getArticles();
                    if(list.size()>0) {
                        //for (int i=0; i<articleModels.size(); i++) {}
                        for (ArticleModel m: list) {
                            //Log.d(TAG, "Author: "+m.getAuthor());
                            articleList.add(new ArticleModel(m.getAuthor(),m.getTitle(),m.getDescription(),m.getUrl(),m.getUrlToImage(),m.getPublishedAt(),m.getContent()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                alertDialog(t.getMessage());
            }
        });


        /*for (int i=0; i<=10; i++) {
            articleList.add(new ArticleModel("Name", "Author", "Title", "Description", "Url", "UrlToImage", "PublishedAt", "Content"));
        }*/
        adapter = new RecyclerAdapter(getApplicationContext(), articleList);
        recyclerView.setAdapter(adapter);

        progress.dismiss();
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
