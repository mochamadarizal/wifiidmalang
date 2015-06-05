package com.rizal.tempatwifimalang;

import android.content.Intent;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class DataMain extends ListActivity {
     Intent intent;
        TextView wifiId,nama;
        DBController controller = new DBController(this);
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_data_main);
            ArrayList<HashMap<String, String>> wifiList =  controller.getAllWifi();
            if(wifiList.size()!=0) {
                ListView lv = getListView();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        nama = (TextView) view.findViewById(R.id.nama);


                        String valwifiId = nama.getText().toString();
                        Intent  objIndent = new Intent(getApplicationContext(),
                                EditData.class);
                        objIndent.putExtra("nama", valwifiId);
                        startActivity(objIndent);



                    }
                });
                ListAdapter adapter =
                        new SimpleAdapter( DataMain.this,wifiList, R.layout.view_wifi_entry, new String[]
                                {"nama","alamat"}, new int[]
                                {R.id.nama,R.id.alamat});
                setListAdapter(adapter);
            }
        }




}
