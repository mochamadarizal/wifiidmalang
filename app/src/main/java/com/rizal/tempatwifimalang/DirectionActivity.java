package com.rizal.tempatwifimalang;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.List;

public class DirectionActivity extends FragmentActivity implements OnMyLocationChangeListener
{
    private final String	URL	= "http://maps.googleapis.com/maps/api/directions/json?";
    private LatLng			start;
    private LatLng			end;
    private String			nama;

    private GoogleMap		map;
    private JSONHelper		json;
    private ProgressDialog	pDialog;
    private List<LatLng>	listDirections;
    boolean status = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        status = checkInternetConnection();

        if(status) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_direction);
            json = new JSONHelper();
            setupMapIfNeeded();

            Bundle b = getIntent().getExtras();
            if (b != null) {
                start = new LatLng(b.getDouble(MainActivity.KEY_LAT_ASAL), b.getDouble(MainActivity.KEY_LNG_ASAL));
                end = new LatLng(b.getDouble(MainActivity.KEY_LAT_TUJUAN), b.getDouble(MainActivity.KEY_LNG_TUJUAN));
                nama = b.getString(MainActivity.KEY_NAMA);
            }

            new AsyncTaskDirection().execute();

        }else{

    super.onCreate(savedInstanceState);
    setContentView(R.layout.error_con);
}



    }

    private void setupMapIfNeeded()
    {
        if (map == null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentManager
                    .findFragmentById(R.id.mapsdirections);
            map = supportMapFragment.getMap();

            if (map != null)
            {
                setupMap();
            }
        }

    }


    public boolean checkInternetConnection()
    {

        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null)
        {
            NetworkInfo[] inf = connectivity.getAllNetworkInfo();
            if (inf != null)
                for (int i = 0; i < inf.length; i++)
                    if (inf[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    private void setupMap()
    {
        map.setMyLocationEnabled(true);
        map.setOnMyLocationChangeListener(this);
        moveToMyLocation();
    }

    private void moveToMyLocation()
    {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 15));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_direction, menu);
        return true;
    }

    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        int resCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (resCode != ConnectionResult.SUCCESS)
        {
            GooglePlayServicesUtil.getErrorDialog(resCode, this, 1);
        }
    }

    private class AsyncTaskDirection extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            String uri = URL
                    + "origin=" + start.latitude + "," + start.longitude
                    + "&destination=" + end.latitude + "," + end.longitude
                    + "&sensor=true&units=metric";

            JSONObject jObject = json.getJSONFromURL(uri);
            listDirections = json.getDirection(jObject);

            return null;
        }

        @Override
        protected void onPreExecute()
        {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(DirectionActivity.this);
            pDialog.setMessage("Loading....");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void result)
        {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();
            gambarDirection();
        }

    }

    public void gambarDirection()
    {
        PolylineOptions line = new PolylineOptions().width(3).color(Color.BLUE);
        for (int i = 0; i < listDirections.size(); i++)
        {
            line.add(listDirections.get(i));
        }
        map.addPolyline(line);

        // tambah marker di posisi end
        map.addMarker(new MarkerOptions()
                .position(end)
                .title(nama));
    }

    @Override
    public void onMyLocationChange(Location location)
    {
        Toast.makeText(this, "Lokasi berubah ke " + location.getLatitude() + "," + location.getLongitude(),
                Toast.LENGTH_SHORT).show();

    }
}

