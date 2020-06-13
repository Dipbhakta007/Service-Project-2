package com.example.user.seviceproject2;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.app.Notification;

import static com.example.user.seviceproject2.Notification.CHANNEL_ID;

/**
 * Created by USER on 6/12/2020.
 */

public class MyService extends Service {

    private static final String TAG= "MyService";
    private IBinder mBinder=new MyBinder();
    private Handler mHandler;
    private int mProgress,mMaxValue;
    private boolean mIsPaused;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler=new Handler();
        mProgress=0;
        mMaxValue=5000000;
        mIsPaused=true;

        Intent notificationIntent=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,
                0,notificationIntent,0);

        Notification notification= new NotificationCompat.Builder(this,CHANNEL_ID).setContentTitle("Service")
                .setContentText("The service is running.")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1,notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Intent notificationIntent=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,
                0,notificationIntent,0);

        Notification notification= new NotificationCompat.Builder(this,CHANNEL_ID).setContentTitle("Service")
                .setContentText("The service is running.")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1,notification);

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder{
        MyService getService(){
            return MyService.this;
        }
    }

    public void startPretendLongRunningTask(){
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                if(mProgress>=mMaxValue || mIsPaused){
                    Log.d(TAG,"run: removing callbacks");
                    mHandler.removeCallbacks(this);
                    pausePretendLongRunningTask();
                }
                else{
                    Log.d(TAG,"run: progress- "+mProgress);
                    mHandler.postDelayed(this,1000);
                    mProgress+=1;


                }


            }
        };
        mHandler.postDelayed(runnable,1000);
    }

    public void pausePretendLongRunningTask() {
        mIsPaused=true;
    }
    public void unPausePretendLongRunningTask() {
        mIsPaused=false;
        startPretendLongRunningTask();
    }

    public Boolean getIsPaused(){
        return mIsPaused;
    }

    public int getProgress(){
        return mProgress;
    }

    public int getMaxValue(){
        return mMaxValue;
    }

    public void resetTask(){
        mProgress = 0;
    }


}
