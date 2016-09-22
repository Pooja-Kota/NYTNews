package com.example.pkota.nytnews.Activities;

/**
 * Created by pkota on 13-09-2016.
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pkota.nytnews.R;
import com.example.pkota.nytnews.retrofit.NewsAPI;
import com.example.pkota.nytnews.retrofit.NewsApiInterface;
import com.example.pkota.nytnews.utils.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private static final String TAG = "MainActivity";
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private CustomList customListAdapter;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.recycler_fragment);
        drawerFragment.setUp(R.id.recycler_fragment, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        initProgressDialog();
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setItemViewCacheSize(0);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        // display the first navigation drawer view on app launch
        displayView(0);
    }

    public void setRecycler(Call<News> call)
    {
        showProgressDialog();
        call.enqueue(new Callback<News>() {

            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if((response.body() != null)) {
                    List<News> news = response.body().getResults();
                    dismissProgressDialog();
                    customListAdapter = new CustomList(news, getApplicationContext());
                    recyclerView.setAdapter(customListAdapter);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.d(TAG, "Number of news received: " + t.toString());
            }
        });
    }

    @Override
    public void onDrawerItemSelected(View view, int position)
    {
        displayView(position);
    }

    private void displayView(int position) {
        NewsApiInterface apiService =
                NewsAPI.getClient().create(NewsApiInterface.class);
        String title = getString(R.string.app_name);

        switch (position) {
            case 0:
                setRecycler(apiService.getAllNews());
                title = getString(R.string.title_home);
                break;
            case 1:
                setRecycler(apiService.getSportsNews());
                title = getString(R.string.title_sports);
                break;
            case 2:
                setRecycler(apiService.getHealthNews());
                title = getString(R.string.title_health);
                break;
            case 3:
                setRecycler(apiService.getBusinessNews());
                title = getString(R.string.title_business);
                break;
            case 4:
                setRecycler(apiService.getAllArtNews());
                title = getString(R.string.title_art);
                break;
            default:
                break;
        }
        // set the toolbar title
        getSupportActionBar().setTitle(title);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
       String drawerSelectedTitle = (String) getSupportActionBar().getTitle();
        if(drawerSelectedTitle.equalsIgnoreCase("home"))
        {
            drawerSelectedTitle = "all";
        }

        switch(item.getItemId()){
            case R.id.All:
                setRecycler(menuItemsSelection(drawerSelectedTitle,"all.json"));
                break;

            case R.id.oneDay:
                setRecycler(menuItemsSelection(drawerSelectedTitle,"24.json"));
                break;

            case R.id.twoDay:
                setRecycler(menuItemsSelection(drawerSelectedTitle,"48.json"));
                break;

            case R.id.threeDay:
                setRecycler( menuItemsSelection(drawerSelectedTitle,"72.json"));
                break;
            default:
                break;
        }
        return true;

    }

    public Call<News>  menuItemsSelection(String title,String selectID)
    {
        NewsApiInterface apiService =
                NewsAPI.getClient().create(NewsApiInterface.class);
        String pathParam = title+"/"+selectID;
        Log.d(TAG,pathParam);
           return apiService.getSpecificNews(title,selectID);
    }

    private void initProgressDialog() {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);
    }


    public void showProgressDialog() {

        try {
            if (mProgressDialog == null)
                initProgressDialog();
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();
        }
        catch (Exception e) { }
    }
    public void dismissProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        } catch (Exception e) {

        } finally {
            this.mProgressDialog = null;
        }
    }
}
