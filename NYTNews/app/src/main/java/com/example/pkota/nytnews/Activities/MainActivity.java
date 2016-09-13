package com.example.pkota.nytnews.Activities;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pkota.nytnews.R;
import com.example.pkota.nytnews.retrofit.NewsAPI;
import com.example.pkota.nytnews.retrofit.NewsApiInterface;
import com.example.pkota.nytnews.utils.NavDrawerItem;
import com.example.pkota.nytnews.utils.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<News> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    private static final String TAG = "MainActivity";


    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

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

        // display the first navigation drawer view on app launch
        displayView(0);

    }
    public void setRecycler(Call<News> call) {
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                Log.d(TAG, "heylo: " + response);
                List<News> movies = response.body().getResults();
                Log.d(TAG, "Number of movies received: " + movies.size());
                recyclerView.setAdapter(new CustomList(movies));
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.d(TAG, "Number of movies received: " + t.toString());
            }
        });
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        NewsApiInterface apiService =
                NewsAPI.getClient().create(NewsApiInterface.class);
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                setRecycler(apiService.getAllNews());
                break;
            case 1:
                setRecycler(apiService.getSportsNews());
                break;
            case 2:
                setRecycler(apiService.getHealthNews());
                break;
            case 3:
                setRecycler(apiService.getBusinessNews());
                break;
            case 4:
                setRecycler(apiService.getArtNews());
                break;
            default:
                break;
        }

        getSupportActionBar().setTitle(title);
        // set the toolbar title

    }
}
