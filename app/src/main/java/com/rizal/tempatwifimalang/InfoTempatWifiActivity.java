package com.rizal.tempatwifimalang;
/*
 * Pratama Nur Wijaya (c) 2013
 *
 * Project       : Tempat Makan Jogja
 * Filename      : InfoTempatMakanActivity.java
 * Creation Date : Apr 7, 2013 time : 1:47:27 PM
 *
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class InfoTempatWifiActivity extends Activity implements OnClickListener
{
    DBController controller = new DBController(this);
    TextView nama1;
    TextView alamat1;
    private TextView	tvNama;
    private TextView	tvAlamat;
    private Button		btnGetDirection;

    private LatLng		lokasiTujuan;
    private LatLng		lokasiAwal;
    private String		nama;
    private String		alamat;
    boolean status = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        status = checkInternetConnection();

        if(status) {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_info_tempat_wifi);

            initialize();

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                nama = bundle.getString(MainActivity.KEY_NAMA);
                alamat = bundle.getString(MainActivity.KEY_ALAMAT);
                lokasiTujuan = new LatLng(bundle.getDouble(MainActivity.KEY_LAT_TUJUAN),
                        bundle.getDouble(MainActivity.KEY_LNG_TUJUAN));
                lokasiAwal = new LatLng(bundle.getDouble(MainActivity.KEY_LAT_ASAL),
                        bundle.getDouble(MainActivity.KEY_LNG_ASAL));
            }

            setTeksView();
            nama1 = (TextView) findViewById(R.id.namaTempatMakan);
            alamat1 = (TextView) findViewById(R.id.alamatTempatMakan);
            Button button= (Button) findViewById(R.id.btnAdd);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    boolean wifiList = controller.cekData(nama1.getText().toString());
                    if(wifiList){
                        callHomeActivity();
                    }else {
                        HashMap<String, String> queryValues = new HashMap<String, String>();
                        queryValues.put("nama", nama1.getText().toString());
                        queryValues.put("alamat", alamat1.getText().toString());
                        controller.addWifi(queryValues);
                        callHomeActivity1();
                    }


                }


            });
        }else{

    super.onCreate(savedInstanceState);
    setContentView(R.layout.error_con);
            }
    }

    private void callHomeActivity1() {
        Toast.makeText(this, "Data Berhasil Di Simpan", Toast.LENGTH_LONG).show();
        Intent objIntent = new Intent(this, MainFirst.class);
        String Data = "aksi";
        objIntent.putExtra("actions", Data);
        startActivity(objIntent);
    }

    private void callHomeActivity() {
        Toast.makeText(this, "Data Gagal Disimpan,Data Sudah Ada", Toast.LENGTH_LONG).show();

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


    private void setTeksView()
    {
        tvNama.setText(nama);
        tvAlamat.setText(alamat);
    }

    private void initialize()
    {
        tvAlamat = (TextView) findViewById(R.id.alamatTempatMakan);
        tvNama = (TextView) findViewById(R.id.namaTempatMakan);
        btnGetDirection = (Button) findViewById(R.id.btnDirection);
        btnGetDirection.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Bundle bundle = new Bundle();
        bundle.putDouble(MainActivity.KEY_LAT_ASAL, lokasiAwal.latitude);
        bundle.putDouble(MainActivity.KEY_LNG_ASAL, lokasiAwal.longitude);
        bundle.putDouble(MainActivity.KEY_LAT_TUJUAN, lokasiTujuan.latitude);
        bundle.putDouble(MainActivity.KEY_LNG_TUJUAN, lokasiTujuan.longitude);
        bundle.putString(MainActivity.KEY_NAMA, nama);

        Intent intent = new Intent(this, DirectionActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

}

