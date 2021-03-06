package com.example.mpdcoursework;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/*

Name: Peter Lunardi
Student ID: S1636120

 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TabLayout.BaseOnTabSelectedListener {
    Button incidentsBtn;
    Button currentBtn;
    Button plannedBtn;

    private String incidentsSource = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String plannedSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String currentSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";

    TabLayout mTabLayout;
    int tabPos = 0;

    private ListViewFragment listView;
    private MapsActivity mapView;
    private Fragment mFrag;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = findViewById(R.id.tabLayout);
        mTabLayout.addOnTabSelectedListener(this);

        listView = new ListViewFragment();
        mapView = new MapsActivity();

        listView.setmRecyclerViewAdapter(new RecyclerViewAdapter());

        mFrag = listView;

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragContainer, mFrag);
        transaction.commit();

        listView.getmRecyclerViewAdapter().setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mapView.setSpecificItem(listView.getmRecyclerViewAdapter().clickedItem(position));
                mTabLayout.getTabAt(1).select();
            }
        });
    }

    @Override
    public void onClick(View aview)
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(tabPos == 0)
                {
                    listView.getmRecyclerViewAdapter().getFilter().filter(newText);
                }
                else if (tabPos == 1)
                {
                    mapView.getFilter().filter(newText);
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            mFrag = listView;
            mapView.setSpecificItem(null);
            tabPos = 0;
        } else if (tab.getPosition() == 1) {
            mFrag = mapView;
            tabPos = 1;
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragContainer, mFrag);
        transaction.commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
