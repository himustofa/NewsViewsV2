package com.apps.newsviews.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.newsviews.R;
import com.apps.newsviews.model.ArticleModel;
import com.apps.newsviews.model.ResponseModel;
import com.apps.newsviews.retrofit.RetrofitClient;
import com.apps.newsviews.utility.ConstantKey;
import com.apps.newsviews.adapter.RecyclerAdapter;
import com.apps.newsviews.utility.SharedPrefManager;
import com.google.gson.Gson;

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
    private String userEmail;
    private EditText itemSearch;
    private DatePicker picker;
    private ImageButton numberButton, dateButton, refreshButton;
    private Button go;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<ArticleModel> mArticleList;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //context = this;
        mProgress = new ProgressDialog(this);
        mProgress.setMessage(HomeActivity.this.getString(R.string.progress));
        mProgress.show();

        mArticleList = new ArrayList<>();

        //===============================================| Getting SharedPreferences
        userEmail = SharedPrefManager.getInstance(HomeActivity.this).getSharedPrefEmail();

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
        TextView navUserName = (TextView)hView.findViewById(R.id.nav_header_user);
        if (!TextUtils.isEmpty(userEmail)) {navUserName.setText(userEmail);}
        navigationView.setNavigationItemSelectedListener(HomeActivity.this);
        //separator Item Decoration
        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(HomeActivity.this,DividerItemDecoration.VERTICAL));


        //===============================================| RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.news_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false); //LinearLayoutManager.VERTICAL
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //====================================================| Custom adapter search
        /*itemSearch = (EditText) findViewById(R.id.customer_search);
        itemSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                int textlength = cs.length();
                ArrayList<ArticleModel> tempArrayList = new ArrayList<ArticleModel>();
                for(ArticleModel c: mArticleList){
                    if (textlength <= c.getAuthor().length()) {
                        if (c.getAuthor().toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                mAdapter = new RecyclerAdapter(HomeActivity.this, mArticleList);
                mRecyclerView.setAdapter(mAdapter);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });*/

        //numberButton = (ImageButton) findViewById(R.id.number_button);
        dateButton = (ImageButton) findViewById(R.id.date_button);
        refreshButton = (ImageButton) findViewById(R.id.refresh_button);
        itemSearch = (EditText) findViewById(R.id.search_button);
        go = (Button) findViewById(R.id.search_go);

        //====================================================| Number API Call
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = itemSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    Intent intent = new Intent(HomeActivity.this, DisplayActivity.class);
                    intent.putExtra(ConstantKey.NUMBER_API_RESULT_KEY, value);
                    startActivity(intent);
                    //finish();
                }
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDate();
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    //====================================================| Call API and Displaying data in RecyclerAdapter
    @Override
    protected void onStart() {
        super.onStart();
        /*for (int i=0; i<=100; i++) {
            mArticleList.add(new ArticleModel("Author", "Title", "Description", "https://www.google.com/", "http://freakonomics.com/wp-content/uploads/2016/05/PC-Games-300x225.jpg", "PublishedAt", "Content"));
        }*/

        mArticleList.clear();

        Call<ResponseModel> call = RetrofitClient.getInstance().getApi().getNews(ConstantKey.COIN, ConstantKey.DATE, ConstantKey.SORT, ConstantKey.API);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.body().getStatus().equals("ok")) {
                    ArrayList<ArticleModel> list = response.body().getArticles();
                    Log.d(TAG, new Gson().toJson(response.body().getArticles()));
                    if(list.size() > 0) {
                        mAdapter = new RecyclerAdapter(HomeActivity.this, response.body().getArticles());
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        mRecyclerView.setAdapter(mAdapter);
                        mProgress.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                alertDialog(t.getMessage());
            }
        });

    }

    //====================================================| Search using date
    private void searchDate() {
        picker = new DatePicker(this);
        int curYear = picker.getYear();
        int curMonth = picker.getMonth()+1;
        int curDayOfMonth = picker.getDayOfMonth();
        DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                itemSearch.setText((month+1)+"/"+dayOfMonth); //dayOfMonth+"/"+(month+1)+"/"+year
            }
        }, curYear, curMonth, curDayOfMonth);
        pickerDialog.show();
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
            finish();
        }
        if (menuItem.getItemId() == R.id.about_id) {
            aboutMe();
        }
        if (menuItem.getItemId() == R.id.exit_id) {
            SharedPrefManager.getInstance(HomeActivity.this).removeSharedPref();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
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
