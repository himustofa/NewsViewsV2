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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.newsviews.R;
import com.apps.newsviews.adapter.MyAdapter;
import com.apps.newsviews.history.HistoryModel;
import com.apps.newsviews.history.HistoryService;
import com.apps.newsviews.model.ArticleModel;
import com.apps.newsviews.model.ResponseModel;
import com.apps.newsviews.retrofit.RetrofitClient;
import com.apps.newsviews.utility.ConstantKey;
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
    private AutoCompleteTextView searchAuto;
    private DatePicker picker;
    private ImageButton numberButton, dateButton, refreshButton;
    private Button go;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<ArticleModel> mArticleList;
    private ProgressDialog mProgress;

    private HistoryService historyService;
    private ArrayList<HistoryModel> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //context = this;
        mProgress = new ProgressDialog(this);
        mProgress.setMessage(HomeActivity.this.getString(R.string.progress));
        mProgress.show();

        this.historyService = new HistoryService(this); //To get from service

        mArticleList = new ArrayList<>();
        try {
            historyList = (ArrayList) historyService.getAllData();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false); //LinearLayoutManager.VERTICAL
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        //numberButton = (ImageButton) findViewById(R.id.number_button);
        dateButton = (ImageButton) findViewById(R.id.date_button);
        refreshButton = (ImageButton) findViewById(R.id.refresh_button);
        itemSearch = (EditText) findViewById(R.id.search_button);
        searchAuto = (AutoCompleteTextView) findViewById(R.id.search_button);
        go = (Button) findViewById(R.id.search_go);


        //====================================================| Number API Call
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = searchAuto.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    Intent intent = new Intent(HomeActivity.this, DisplayActivity.class);
                    intent.putExtra(ConstantKey.NUMBER_API_RESULT_KEY, value);
                    startActivity(intent);
                    //finish();

                    long data = HomeActivity.this.historyService.addData(new HistoryModel(value));
                    if (data > 0){
                        Toast.makeText(getApplicationContext(),"Saved successfully", Toast.LENGTH_SHORT).show();
                    }
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

        //====================================================| History
        try {
            ArrayList<String> arr = new ArrayList<>();
            for(HistoryModel obj : historyList){
                arr.add(obj.getHistoryItem());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arr); //new String[] {"Belgium", "France", "Italy", "Germany", "Spain"}
            searchAuto.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mArticleList.clear();

        //====================================================| API
        //Call<ResponseModel> bbc = RetrofitClient.getInstance().getApi().getBbcNews(ConstantKey.SOURCE, ConstantKey.API);
        Call<ResponseModel> apple = RetrofitClient.getInstance().getApi().getNews(ConstantKey.COIN, ConstantKey.DATE, ConstantKey.SORT, ConstantKey.API);
        apple.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.body().getStatus().equals("ok")) {
                    Log.d(TAG, "APPLE: "+new Gson().toJson(response.body().getArticles()));
                    //mAdapter = new RecyclerAdapter(HomeActivity.this, response.body().getArticles());
                    ArrayList<ArticleModel> list = response.body().getArticles();
                    for(int i=0; i<list.size(); i++) {
                        if (i%2 == 0) {
                            mArticleList.add(new ArticleModel(ArticleModel.ItemType.ONE_ITEM, list.get(i).getAuthor(), list.get(i).getTitle(), list.get(i).getDescription(), list.get(i).getUrl(), list.get(i).getUrlToImage(), list.get(i).getPublishedAt(), list.get(i).getContent()));
                        } else {
                            mArticleList.add(new ArticleModel(ArticleModel.ItemType.TWO_ITEM, list.get(i).getAuthor(), list.get(i).getTitle(), list.get(i).getDescription(), list.get(i).getUrl(), list.get(i).getUrlToImage(), list.get(i).getPublishedAt(), list.get(i).getContent()));
                        }
                    }

                    mAdapter = new MyAdapter(HomeActivity.this, mArticleList);
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerView.setAdapter(mAdapter);
                    mProgress.dismiss();
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                retrofitOnFailure();
                alertDialog(t.getMessage());
            }
        });


    }

    private void retrofitOnFailure() {
        for (int i=0; i<=20; i++) {
            if (i%2 == 0) {
                mArticleList.add(new ArticleModel(ArticleModel.ItemType.ONE_ITEM,"BBC News", "Australian jihadist stripped of citizenship", "Neil Prakash, currently in jail in Turkey, appeared in propaganda videos for the Islamic State group.", "http://www.bbc.co.uk/news/world-australia-46706710", "https://ichef.bbci.co.uk/news/1024/branded_news/E73D/production/_104979195_mediaitem89600714.jpg", "2018-12-29T03:40:23Z", "Image caption Neil Prakash appeared in IS propaganda videos Australia's most wanted jihadist, Neil Prakash, has been stripped of his Australian citizenship."));
            } else {
                mArticleList.add(new ArticleModel(ArticleModel.ItemType.TWO_ITEM,"BBC News", "Indonesian volcano's lost stature", "Satellite images indicate Anak Krakatau has now lost over two-thirds of its height and volume.", "http://www.bbc.co.uk/news/science-environment-46707731", "https://ichef.bbci.co.uk/news/1024/branded_news/13C5B/production/_104978908_051355469.jpg", "2018-12-29T01:13:37Z", "Image copyright Reuters Image caption Radar satellites are one of the few ways of assessing the volcano currently The scale of the dramatic collapse of the Indonesian volcano that led to last Saturday's devastating tsunami in the Sunda Strait is becoming clea…"));
            }
            mAdapter = new MyAdapter(HomeActivity.this, mArticleList);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapter);
            mProgress.dismiss();
        }
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
