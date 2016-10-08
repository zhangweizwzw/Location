package com.example.zw.location;

import android.location.Location;

/**
 * Created at 陈 on 2016/9/14.
 * 地理位置改变监听
 * @author cwf
 * @email 237142681@qq.com
 */
public interface LocationChangeListener {
    public void onChange(Location location);
}
