package com.example.zw.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created at zw
 * 定位
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class GPSLocation {
    private final String TAG = "GPSLocation";
    private Context mContext;
    private LocationManager locationManager;
    private List<LocationChangeListener> listeners;
    private static GPSLocation gpsLocation;
    private Location location = null;

    /*定位参数*/
    /*默认使用gps定位*/
    private boolean isGPS = true;
    private long minTime = 1000;
    private long minDistance = 0;

    public static GPSLocation getInstance(Context mContext) {
        if (gpsLocation == null)
            gpsLocation = new GPSLocation(mContext);
        return gpsLocation;
    }

    public GPSLocation(Context mContext) {
        gpsLocation = this;
        this.mContext = mContext;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        listeners = new ArrayList<>();
    }


    public Location getLocation(String provider) {
        //判断GPS是否正常启动
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.i(TAG,"请开启GPS导航...");
            Toast.makeText(mContext, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            //返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
            return null;
        }


        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Toast.makeText(mContext, "缺少访问地理位置的权限", Toast.LENGTH_SHORT).show();
            return null;
        }
        return locationManager.getLastKnownLocation(provider);
    }

    public Location getLocation() {
        //判断GPS是否正常启动
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(mContext, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            //返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
            return null;
        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Toast.makeText(mContext, "缺少访问地理位置的权限", Toast.LENGTH_SHORT).show();
            return null;
        }
        String bestProvider = locationManager.getBestProvider(getCriteria(), true);
        return locationManager.getLastKnownLocation(bestProvider);
    }

    public void startListener() {
        //判断GPS是否正常启动
      if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.i(TAG,"缺少访问地理位置的权限");
            Toast.makeText(mContext, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            //返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
            return;
        }

        System.out.println("<------------------过了10秒------------------>");
        //获得位置服务的管理对象
        LocationManager locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS获取定位的位置数据
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        updateToNewLocation(location);
        /**服务管理对象的监听器*/
        //参数1：定位的方式   参数2：监听更新间隔时间(ms)  参数3：监听更新的距离(m) 参数4：监听的方法
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000, 10, new LocationListener() {
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            public void onProviderEnabled(String provider) {
            }
            public void onProviderDisabled(String provider) {
            }
            public void onLocationChanged(Location location)
            {
                updateToNewLocation(location);
            }
        });
    }

    private void updateToNewLocation(Location location) {
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            for (LocationChangeListener listener : listeners)
                listener.onChange(location);
//            LocationChangeListener.onChange(location);
//            Point p1=new Point(longitude,latitude);
//            Point p2 = (Point) GeometryEngine.project(p1 ,SpatialReference.create(4326),map.getSpatialReference());
//            Graphic graphic = new Graphic(p2, new SimpleMarkerSymbol(Color.BLUE, 25, SimpleMarkerSymbol.STYLE.CIRCLE));
//            graphicsLayer.addGraphic(graphic);
//            Point wgsPoint=new Point(longitude,latitude);
//            Point mapPoint2 = (Point) GeometryEngine.project(wgsPoint ,SpatialReference.create(4326),map.getSpatialReference());
//            points.add(mapPoint2);
//            drawPolyline();
        }
    }

    /*停止监听*/
    public void stopListener() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Log.i(TAG,"缺少访问地理位置的权限");
            Toast.makeText(mContext, "缺少访问地理位置的权限", Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

    private GpsStatus.Listener gpsListtener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int i) {

        }
    };


    private LocationListener locationListener = new LocationListener() {
        /**
         * 位置信息变化时触发
         */
        @Override
        public void onLocationChanged(Location location) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Log.i(TAG, "时间：" + simpleDateFormat.format(new Date(location.getTime())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i(TAG, "经度：" + location.getLongitude());
            Log.i(TAG, "纬度：" + location.getLatitude());
            Log.i(TAG, "海拔：" + location.getAltitude());
            Log.i(TAG, "速度：" + location.getSpeed());
            for (LocationChangeListener listener : listeners)
                listener.onChange(location);
        }

        /**
         * GPS状态变化时触发
         */
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            switch (i) {
                //GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    Log.i(TAG, "当前GPS状态为可见状态");
                    break;
                //GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    Log.i(TAG, "当前GPS状态为服务区外状态");
                    break;
                //GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.i(TAG, "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * GPS开启时触发
         */
        @Override
        public void onProviderEnabled(String provider) {
        }

        /**
         * GPS禁用时触发
         */
        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    /**
     * 返回查询条件
     *
     * @return
     */
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(true);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(true);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    /*添加监听*/
    public boolean addChangeListener(LocationChangeListener listener) {
        return listeners.add(listener);
    }

    public boolean removeChangeListener(LocationChangeListener listener) {
        return listeners.remove(listener);
    }

    public void removeAllChangeListener() {
        listeners.clear();
    }

    public long getMinTime() {
        return minTime;
    }

    public void setMinTime(long minTime) {
        this.minTime = minTime;
    }

    public long getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(long minDistance) {
        this.minDistance = minDistance;
    }

    /*使用gps定位*/
    public void useGPS() {
        isGPS = true;
    }

    /*使用网络定位*/
    public void useNetWork() {
        isGPS = false;
    }
}
