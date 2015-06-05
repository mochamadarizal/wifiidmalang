package com.rizal.tempatwifimalang;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.List;



public class MainActivity extends FragmentActivity implements OnInfoWindowClickListener
{
	private GoogleMap			map;
	private JSONHelper			json;
	private ProgressDialog		pDialog;
	private LatLng myLocation;

	private List<TempatWifi> listTempatWifi;
	private final String		URL_API		= "http://rizal29.freeiz.com/WS/index.php";

	public static final String	KEY_NAMA	= "nama";
	public static final String	KEY_ALAMAT	= "alamat";
	public static final String	KEY_LAT_TUJUAN	= "lat_tujuan";
	public static final String	KEY_LNG_TUJUAN	= "lng_tujuan";
	public static final String	KEY_LAT_ASAL	= "lat_asal";
	public static final String	KEY_LNG_ASAL	= "lng_asal";
	boolean status = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		status = checkInternetConnection();

		if(status){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			json = new JSONHelper();
			new AsynTaskMain().execute();
			setupMapIfNeeded();
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
			SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.maps);
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
		map.setOnInfoWindowClickListener(this);
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
		getMenuInflater().inflate(R.menu.menu_main, menu);
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

	private class AsynTaskMain extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected void onPostExecute(Void result)
		{
			// TODO Auto-generated method stub
			pDialog.dismiss();
			runOnUiThread(new Runnable()
			{

				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					for (int i = 0; i < listTempatWifi.size(); i++)
					{

						map.addMarker(new MarkerOptions()
								.position(new LatLng(listTempatWifi.get(i).getLat(),
										listTempatWifi.get(i).getLng()))
								.title(listTempatWifi.get(i).getNama())
								.snippet(listTempatWifi.get(i).getAlamat()));

					}
				}
			});

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Loading....");
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			// TODO Auto-generated method stub

			JSONObject jObject = json.getJSONFromURL(URL_API);
			listTempatWifi = json.getTempatMakanAll(jObject);
			return null;
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker)
	{
		// marker id -> m0, m1, m2 dst..
		String id = marker.getId();
		id = id.substring(1);

		myLocation = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());

		if (myLocation != null)
		{
			Bundle bundle = new Bundle();
			bundle.putString(KEY_NAMA, listTempatWifi.get(Integer.parseInt(id)).getNama());
			bundle.putString(KEY_ALAMAT, listTempatWifi.get(Integer.parseInt(id)).getAlamat());
			bundle.putDouble(KEY_LAT_TUJUAN, marker.getPosition().latitude);
			bundle.putDouble(KEY_LNG_TUJUAN, marker.getPosition().longitude);
			bundle.putDouble(KEY_LAT_ASAL, myLocation.latitude);
			bundle.putDouble(KEY_LNG_ASAL, myLocation.longitude);

			Intent i = new Intent(MainActivity.this, InfoTempatWifiActivity.class);
			i.putExtras(bundle);
			startActivity(i);

		} else
		{
			Toast.makeText(this, "Tidak dapat menemukan lokasi anda ", Toast.LENGTH_LONG).show();
		}
	}
}
