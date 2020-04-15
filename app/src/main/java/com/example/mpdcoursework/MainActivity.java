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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    Button incidentsBtn;
    Button currentBtn;
    Button plannedBtn;

    private ArrayList<RoadTrafficItem> incidents;

    private String incidentsSource = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String plannedSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String currentSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        incidents = new ArrayList<RoadTrafficItem>();

        incidentsBtn = (Button) findViewById(R.id.incidentsBtn);
        incidentsBtn.setOnClickListener(this);

        currentBtn = (Button) findViewById(R.id.currentBtn);
        currentBtn.setOnClickListener(this);

        plannedBtn = (Button) findViewById(R.id.plannedBtn);
        plannedBtn.setOnClickListener(this);

        recyclerViewAdapter = new RecyclerViewAdapter();

        mRecyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(recyclerViewAdapter);
    }


    @Override
    public void onClick(View aview)
    {
        if (aview == incidentsBtn)
        {
            incidentsBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.CornflowerBlue, null)));
            currentBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.LightGrey, null)));
            plannedBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.LightGrey, null)));
            new ProcessUrlAsync(recyclerViewAdapter).execute(incidentsSource);
        }
        if (aview == currentBtn)
        {
            currentBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.CornflowerBlue, null)));
            plannedBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.LightGrey, null)));
            incidentsBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.LightGrey, null)));
            new ProcessUrlAsync(recyclerViewAdapter).execute(currentSource);
        }
        if (aview == plannedBtn)
        {
            plannedBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.CornflowerBlue, null)));
            currentBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.LightGrey, null)));
            incidentsBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.LightGrey, null)));
            new ProcessUrlAsync(recyclerViewAdapter).execute(plannedSource);
        }
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
                recyclerViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
