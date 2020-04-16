package com.example.mpdcoursework;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;

public class ProcessUrlAsync extends AsyncTask<String, Void, ArrayList<RoadTrafficItem>> {

    private String result;
    private ArrayList<RoadTrafficItem> items;
    private RecyclerViewAdapter recyclerViewAdapter;
    private GoogleMap googleMap;
    boolean usingMaps;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    ProcessUrlAsync(RecyclerViewAdapter rva)
    {
        recyclerViewAdapter = rva;
    }

    ProcessUrlAsync(GoogleMap map)
    {
        googleMap = map;
        usingMaps = true;
    }

    @Override
    protected ArrayList<RoadTrafficItem> doInBackground(String... strings) {

        try {
            URL aurl = new URL(strings[0]);
            URLConnection yc = aurl.openConnection();
            yc.connect();
            InputStream stream = yc.getInputStream();
            ArrayList<RoadTrafficItem> trafficItems = parseIncident(stream);
            stream.close();
            return trafficItems;

        }
        catch (IOException ae)
        {
            Log.e("MyTag", "IOException");
            return null;
        }

    }


    ArrayList<RoadTrafficItem> parseIncident(InputStream stream)
    {
        RoadTrafficItem item = null;
        ArrayList<RoadTrafficItem> trafficItems = null;
        boolean processingItem = false;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( stream, null );

            int eventType = xpp.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                if(eventType == XmlPullParser.START_DOCUMENT)
                {
                    System.out.println("Start of document");
                } else if(eventType == XmlPullParser.START_TAG)
                {
                    if(xpp.getName().equalsIgnoreCase("channel"))
                    {
                        trafficItems = new ArrayList<RoadTrafficItem>();
                    } else if(xpp.getName().equalsIgnoreCase("item"))
                    {
                        //we've found an incident item!
                        System.out.println("FOUND AN ITEM");
                        processingItem = true;
                        item = new RoadTrafficItem();
                    } else if(xpp.getName().equalsIgnoreCase("title"))
                    {
                        //found the title of the incident!
                        if(processingItem)
                            item.setTitle(xpp.nextText());
                    } else if(xpp.getName().equalsIgnoreCase("description"))
                    {
                        //found the description for the incident
                        if(processingItem)
                            item.setDescription(xpp.nextText());
                    } else if(xpp.getName().equalsIgnoreCase("link"))
                    {
                        if(processingItem)
                            item.setLink(xpp.nextText());
                    } else if(xpp.getName().equalsIgnoreCase("point"))
                    {
                        if(processingItem)
                        {
                            String[] coordinates = xpp.nextText().split(" ");
                            float latitude = Float.parseFloat(coordinates[0]);
                            float longitude = Float.parseFloat(coordinates[1]);
                            item.setLatitude(latitude);
                            item.setLongitude(longitude);
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG)
                {
                    if(xpp.getName().equalsIgnoreCase("item"))
                    {
                        //finished parsing an incident item!
                        processingItem = false;
                        trafficItems.add(item);
                    } else if (xpp.getName().equalsIgnoreCase("channel"))
                    {
                        int size = trafficItems.size();
                    }
                }

                eventType = xpp.next();
            }

        } catch (XmlPullParserException ae1)
        {
            Log.e("MyTag", "Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("MyTag", "IO error");
        }

        return trafficItems;
    }

    @Override
    protected void onPostExecute(ArrayList<RoadTrafficItem> trafficItems) {
        super.onPostExecute(trafficItems);
        if(usingMaps)
        {
            for(RoadTrafficItem rti : trafficItems)
            {
                LatLng pos = new LatLng(rti.getLatitude(), rti.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(pos).title(rti.getTitle()));
            }
        }
        else
        {
            recyclerViewAdapter.SetItemList(trafficItems);
        }
    }


}
