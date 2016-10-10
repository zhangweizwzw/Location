package com.example.zw.location;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private Timer mTimer;
    private MyTimerTask mTimerTask;

    private Handler handler  = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                goLocation();
            }
        }
    };

    private void goLocation() {
        GPSLocation.getInstance(this).startListener();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        if (mTimer != null) {
            if (mTimerTask != null) {
                mTimerTask.cancel();  //将原任务从队列中移除
            }
        }
        mTimer = new Timer(true);

        mTimerTask = new MyTimerTask();  // 新建一个任务
        mTimer.schedule(mTimerTask, 3000);

//        timer=new Timer();
//        timer.schedule(task, 0, 10*1000);
    }

    //任务
//    private TimerTask task = new TimerTask() {
//        public void run() {
//            Message msg = new Message();
//            msg.what = 1;
//            handler.sendMessage(msg);
//        }
//    };

    class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        }

    }
}
