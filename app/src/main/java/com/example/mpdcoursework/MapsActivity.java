package com.example.mpdcoursework;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    View mView;

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

        UpdateMap();

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


        LatLng startPoint = new LatLng(55.866487, -4.250019);
        mMap.addMarker(new MarkerOptions().position(startPoint).draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 6.3f));

    }

    @Override
    public void onClick(View aview) {
        mMap.clear();
        if (aview == incidentsBtn)
        {
            new ProcessUrlAsync(mMap).execute(incidentsSource);
            checkedButton = incidentsBtn;
        }
        if (aview == currentBtn)
        {
            new ProcessUrlAsync(mMap).execute(currentSource);
            checkedButton = currentBtn;
        }
        if (aview == plannedBtn)
        {
            new ProcessUrlAsync(mMap).execute(plannedSource);
            checkedButton = plannedBtn;
        }
    }

    public void UpdateMap()
    {
        if(checkedButton != null)
        {
            if(checkedButton.getId() == incidentsBtn.getId())
            {
                new ProcessUrlAsync(mMap).execute(incidentsSource);

                Log.e("TAG", "Incidents button is selected");
            }
            else if(checkedButton.getId() == currentBtn.getId())
            {
                new ProcessUrlAsync(mMap).execute(currentSource);

                Log.e("TAG", "current button is selected");
            }
            else if(checkedButton.getId() == plannedBtn.getId())
            {
                new ProcessUrlAsync(mMap).execute(plannedSource);

                Log.e("TAG", "planned button is selected");
            }
            else
                return;
        }
        else return;
    }
}
