package com.rizal.tempatwifimalang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;


public class EditData extends Activity {
    EditText txtNama,txtEditAlamat, keterangan;
    DBController controller = new DBController(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        txtNama = (EditText) findViewById(R.id.txtNama);
        txtEditAlamat = (EditText) findViewById(R.id.txtEditAlamat);
        keterangan = (EditText) findViewById(R.id.keterangan);
        Intent objIntent = getIntent();
        String nama = objIntent.getStringExtra("nama");
        Log.d("Reading: ", "Reading all Wifi..");
        HashMap<String, String> wifiList = controller.getWifiInfo(nama);

        if(wifiList.size()!=0) {
            txtNama.setText(wifiList.get("nama"));
            txtEditAlamat.setText(wifiList.get("alamat"));
            keterangan.setText(wifiList.get("keterangan"));
        }

        Button button= (Button) findViewById(R.id.btnEdit);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                HashMap<String, String> queryValues =  new  HashMap<String, String>();

                Intent objIntent = getIntent();
                String nama1 = objIntent.getStringExtra("nama");
                txtNama = (EditText) findViewById(R.id.txtNama);
                txtEditAlamat = (EditText) findViewById(R.id.txtEditAlamat);
                keterangan = (EditText) findViewById(R.id.keterangan);
                queryValues.put("nama", txtNama.getText().toString());
                queryValues.put("alamat", txtEditAlamat.getText().toString());
                queryValues.put("keterangan", keterangan.getText().toString());
                queryValues.put("nama1", nama1);
                controller.updateWifi(queryValues);
                callHomeActivity();
            }
        });
        Button button1= (Button) findViewById(R.id.btnDelete);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent objIntent = getIntent();
                String nama = objIntent.getStringExtra("nama");
                controller.deleteWifi(nama);
                callHomeActivity();


            }
        });
    }
    public void callHomeActivity() {

        Intent objIntent = new Intent(this, MainFirst.class);
        String Data = "aksi";
        objIntent.putExtra("actions", Data);
        startActivity(objIntent);
    }
}