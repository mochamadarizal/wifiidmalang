package com.rizal.tempatwifimalang;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;


public class MainFirst extends TabActivity {
TabHost th;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_first);
        th =(TabHost) findViewById(android.R.id.tabhost);


        TabHost.TabSpec spec = th.newTabSpec("Tab1");
        spec.setContent(new Intent(this, MainActivity.class));
        spec.setIndicator("Maps Online");
        th.addTab(spec);

        TabHost.TabSpec spec1 = th.newTabSpec("Tab2");
        spec1.setContent(new Intent(this, DataMain.class));
        spec1.setIndicator("Data Locations");
        th.addTab(spec1);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            th.setCurrentTab(1);
        }
    }


}
