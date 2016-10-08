package com.example.zw.location;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class MainActivity extends Activity {
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent("com.zw.service.LOCAL_START_SERVICE");
        intent.setPackage("com.example.zw.location");
        startService(intent);

        Location location = GPSLocation.getInstance(this).getLocation(LocationManager.PASSIVE_PROVIDER);
        if (location != null) {
            locationChangeListener.onChange(location);
        }
        startListener();

    }

    private void startListener() {
        GPSLocation.getInstance(this).removeAllChangeListener();
        GPSLocation.getInstance(this).addChangeListener(locationChangeListener);
        GPSLocation.getInstance(this).useGPS();
        GPSLocation.getInstance(this).startListener();
    }

    private LocationChangeListener locationChangeListener = new LocationChangeListener() {
        @Override
        public void onChange(Location location) {
            textview= (TextView) findViewById(R.id.textview);
            textview.setText("经度："+location.getLatitude()+"\n纬度："+location.getLongitude()+"\n海拔："+location.getAltitude());
        }
    };

}