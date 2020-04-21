package com.example.mpdcoursework;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/*

Name: Peter Lunardi
Student ID: S1636120

 */

public class MapsActivity extends Fragment implements OnMapReadyCallback, View.OnClickListener, Filterable {

    private GoogleMap mMap;
    View mView;
    private ArrayList<RoadTrafficItem> roadTrafficItems;
    private ArrayList<RoadTrafficItem> roadTrafficItemsFull;
    private RoadTrafficItem specificItem;

    private String incidentsSource = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String plannedSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String currentSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";

    RadioButton incidentsBtn;
    RadioButton currentBtn;
    RadioButton plannedBtn;
    RadioButton checkedButton;

    RadioGroup mRadioGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_maps, container, false);

        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);

        mRadioGroup = mView.findViewById(R.id.radioGroup);

        incidentsBtn = (RadioButton) mView.findViewById(R.id.incidentsBtn);
        incidentsBtn.setOnClickListener(this);

        currentBtn = (RadioButton) mView.findViewById(R.id.currentBtn);
        currentBtn.setOnClickListener(this);

        plannedBtn = (RadioButton) mView.findViewById(R.id.plannedBtn);
        plannedBtn.setOnClickListener(this);

        //UpdateMap();

        return mView;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            Marker currentShown;

            public boolean onMarkerClick(Marker marker) {
                if (marker.equals(currentShown)) {
                    marker.hideInfoWindow();
                    currentShown = null;
                } else {
                    marker.showInfoWindow();
                    currentShown = marker;
                }
                return true;
            }
        });

        //UpdateMap();

        LatLng startPoint = new LatLng(55.866487, -4.250019);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 6.3f));

        if (specificItem != null)
        {
            mMap.clear();
            LatLng pos = new LatLng(specificItem.getLatitude(), specificItem.getLongitude());
            mMap.addMarker(new MarkerOptions().position(pos).title(specificItem.getTitle()).snippet(specificItem.getDescription()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 2.3f));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 14));
        }
    }

    public void setSpecificItem(RoadTrafficItem rti)
    {
        specificItem = rti;
    }

    public void setItemList(ArrayList<RoadTrafficItem> items)
    {
        roadTrafficItems = items;
        roadTrafficItemsFull = new ArrayList<RoadTrafficItem>(items);
        showMarkers();
    }

    @Override
    public void onClick(View aview) {
        mMap.clear();
        if (aview == incidentsBtn)
        {
            new ProcessUrlAsync(this).execute(incidentsSource);
            checkedButton = incidentsBtn;
        }
        if (aview == currentBtn)
        {
            new ProcessUrlAsync(this).execute(currentSource);
            checkedButton = currentBtn;
        }
        if (aview == plannedBtn)
        {
            new ProcessUrlAsync(this).execute(plannedSource);
            checkedButton = plannedBtn;
        }
    }

    private void showMarkers()
    {
        mMap.clear();
        for(RoadTrafficItem rti : roadTrafficItems)
        {
            LatLng pos = new LatLng(rti.getLatitude(), rti.getLongitude());
            if(rti.getDelayTime() < 2)
            {
                mMap.addMarker(new MarkerOptions().position(pos).title(rti.getTitle()).snippet(rti.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }
            else if (rti.getDelayTime() <= 7 && rti.getDelayTime() > 2)
            {
                mMap.addMarker(new MarkerOptions().position(pos).title(rti.getTitle()).snippet(rti.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            }
            else if (rti.getDelayTime() > 7)
            {
                mMap.addMarker(new MarkerOptions().position(pos).title(rti.getTitle()).snippet(rti.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }
    }

    public void UpdateMap()
    {
        if(checkedButton != null)
        {
            if(checkedButton.getId() == incidentsBtn.getId())
            {
                new ProcessUrlAsync(this).execute(incidentsSource);

                Log.e("TAG", "Incidents button is selected");
            }
            else if(checkedButton.getId() == currentBtn.getId())
            {
                new ProcessUrlAsync(this).execute(currentSource);

                Log.e("TAG", "current button is selected");
            }
            else if(checkedButton.getId() == plannedBtn.getId())
            {
                new ProcessUrlAsync(this).execute(plannedSource);

                Log.e("TAG", "planned button is selected");
            }
            else
                return;
        }
        else return;
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<RoadTrafficItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(roadTrafficItemsFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                OUTER_LOOP:
                for (RoadTrafficItem item : roadTrafficItemsFull)
                {
                    for (String date : item.getDates())
                    {
                        if(date.contains(filterPattern))
                        {
                            filteredList.add(item);
                            continue OUTER_LOOP;
                        }
                    }

                    if(item.getTitle().toLowerCase().contains(filterPattern)
                            || item.getNumericalStartDate(item.getStartDate()).contains(filterPattern)
                            || item.getNumericalEndDate(item.getEndDate()).contains(filterPattern)
                    )
                    {
                        filteredList.add(item);
                    }
                }
            }

            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, @NonNull FilterResults results) {
            if(roadTrafficItems != null)
            {
                roadTrafficItems.clear();
                roadTrafficItems.addAll((ArrayList)results.values);
                showMarkers();
            }
        }
    };
}
