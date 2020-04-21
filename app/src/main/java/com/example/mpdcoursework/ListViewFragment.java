package com.example.mpdcoursework;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/*

Name: Peter Lunardi
Student ID: S1636120

 */

public class ListViewFragment extends Fragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    RadioButton incidentsBtn;
    RadioButton currentBtn;
    RadioButton plannedBtn;
    RadioButton checkedButton;

    RadioGroup mRadioGroup;

    View view;

    private String incidentsSource = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String plannedSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String currentSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_view_fragment, container, false);

        mRadioGroup = view.findViewById(R.id.radioGroup);

        incidentsBtn = (RadioButton) view.findViewById(R.id.incidentsBtn);
        incidentsBtn.setOnClickListener(this);

        currentBtn = (RadioButton) view.findViewById(R.id.currentBtn);
        currentBtn.setOnClickListener(this);

        plannedBtn = (RadioButton) view.findViewById(R.id.plannedBtn);
        plannedBtn.setOnClickListener(this);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        UpdateList();

        return view;
    }

    @Override
    public void onClick(View aview) {

        if (aview == incidentsBtn)
        {
            new ProcessUrlAsync(mRecyclerViewAdapter).execute(incidentsSource);
            checkedButton = incidentsBtn;
        }
        if (aview == currentBtn)
        {
            new ProcessUrlAsync(mRecyclerViewAdapter).execute(currentSource);
            checkedButton = currentBtn;
        }
        if (aview == plannedBtn)
        {
            new ProcessUrlAsync(mRecyclerViewAdapter).execute(plannedSource);
            checkedButton = plannedBtn;
        }
    }

    public RecyclerViewAdapter getmRecyclerViewAdapter()
    {
        return mRecyclerViewAdapter;
    }

    public void setmRecyclerViewAdapter(RecyclerViewAdapter adapter) { mRecyclerViewAdapter = adapter; }

    public void UpdateList()
    {
        if(checkedButton != null)
        {
            if(checkedButton.getId() == incidentsBtn.getId())
            {
                new ProcessUrlAsync(mRecyclerViewAdapter).execute(incidentsSource);

                Log.e("TAG", "Incidents button is selected");
            }
            else if(checkedButton.getId() == currentBtn.getId())
            {
                new ProcessUrlAsync(mRecyclerViewAdapter).execute(currentSource);

                Log.e("TAG", "current button is selected");
            }
            else if(checkedButton.getId() == plannedBtn.getId())
            {
                new ProcessUrlAsync(mRecyclerViewAdapter).execute(plannedSource);

                Log.e("TAG", "planned button is selected");
            }
            else
                return;
        }
        else return;
    }



}
