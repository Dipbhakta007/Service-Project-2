package com.example.user.seviceproject2;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    static final String STATE_USER="user";
    private String mUser;


    // UI Components

    private static TextView mTextView;
    private static Button mButton;

    private static TextView mTextView2;
    private static Button mButton2;

    private MutableLiveData<Boolean> mIsProgressUpdating=new MutableLiveData<>();
    private MutableLiveData<MyService.MyBinder>mBinder= new MutableLiveData<>();

    private MutableLiveData<Boolean> mIsProgressUpdating2=new MutableLiveData<>();
    private MutableLiveData<MyService2.MyBinder> mBinder2= new MutableLiveData<>();



    // Vars
    MyService mService;
    MyService2 mService2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        mTextView = (TextView) findViewById(R.id.text_view);
        mButton = (Button) findViewById(R.id.toggle_updates);

        mTextView2 = (TextView) findViewById(R.id.text_view2);
        mButton2 = (Button) findViewById(R.id.toggle_updates2);



        setObservers();
        setObservers2();


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleUpdates();
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleUpdates2();
            }
        });



    }



    private void toggleUpdates(){
        if(mService != null){
            if(mService.getProgress() == mService.getMaxValue()){
                mService.resetTask();
                mButton.setText("Start");
            }
            else{
                if(mService.getIsPaused()){
                    mService.unPausePretendLongRunningTask();
                    setIsProgressBarUpdating(true);
                }
                else{
                    mService.pausePretendLongRunningTask();
                    setIsProgressBarUpdating(false);
                }
            }

        }
    }

    private void toggleUpdates2(){
        if(mService2 != null){
            if(mService2.getProgress() == mService2.getMaxValue()){
                mService2.resetTask();
                mButton2.setText("Start");
            }
            else{
                if(mService2.getIsPaused()){
                    mService2.unPausePretendLongRunningTask();
                    setIsProgressBarUpdating2(true);
                }
                else{
                    mService2.pausePretendLongRunningTask();
                    setIsProgressBarUpdating2(false);
                }
            }

        }
    }



    private void setObservers() {
        getBinder().observe(this, new Observer<MyService.MyBinder>() {

            @Override
            public void onChanged(@Nullable MyService.MyBinder myBinder) {

                if (myBinder == null) {
                    Log.d(TAG, "onChanged: unbound from service");
                } else {
                    Log.d(TAG, "onChanged: bound to service.");
                    mService = myBinder.getService();

                    if(mService.getIsPaused()){
                        mButton.setText("Start");
                    }
                    else{
                        mButton.setText("Pause");
                    }
                }
            }

        });

        getIsProgressUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean aBoolean) {
                final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(getIsProgressUpdating().getValue()){
                            if(getBinder().getValue() != null){ // meaning the service is bound
                                if(mService.getProgress() == mService.getMaxValue()){
                                    setIsProgressBarUpdating(false);
                                }

                                String progress = String.valueOf(mService.getProgress());
                                if(mService.getProgress()<10){
                                    mTextView.setText("0"+progress);
                                }
                                else mTextView.setText(progress);
                            }

                            handler.postDelayed(this, 100 );
                        }
                        else{

                            handler.removeCallbacks(this);
                        }
                    }
                };

                // control what the button shows
                if(aBoolean){
                    mButton.setText("Pause");
                    handler.postDelayed(runnable, 100);

                }
                else{
                    if(mService.getProgress() == mService.getMaxValue()){
                        mButton.setText("Restart");
                    }
                    else{
                        mButton.setText("Start");
                    }
                }
            }
        });


    }

    private void setObservers2() {
        getBinder2().observe(this, new Observer<MyService2.MyBinder>() {

            @Override
            public void onChanged(@Nullable MyService2.MyBinder myBinder) {
                if (myBinder == null) {
                    Log.d(TAG, "onChanged: unbound from service");
                } else {
                    Log.d(TAG, "onChanged: bound to service.");
                    mService2 = myBinder.getService();

                    if(mService2.getIsPaused()){
                        mButton2.setText("Start");
                    }
                    else{
                        mButton2.setText("Pause");
                    }
                }
            }

        });

        getIsProgressUpdating2().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean aBoolean) {
                final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(getIsProgressUpdating2().getValue()){

                            if(getBinder2().getValue() != null){ // meaning the service is bound
                                if(mService2.getProgress() == mService2.getMaxValue()){
                                    setIsProgressBarUpdating2(false);
                                }

                                String progress = String.valueOf(mService2.getProgress());
                                if(mService2.getProgress()<10){
                                    mTextView2.setText("0"+progress);
                                }
                                else mTextView2.setText(progress);
                            }

                            handler.postDelayed(this, 100 );
                        }
                        else{

                            handler.removeCallbacks(this);
                        }
                    }
                };

                // control what the button shows
                if(aBoolean){
                    mButton2.setText("Pause");
                    handler.postDelayed(runnable, 100);

                }
                else{
                    if(mService2.getProgress() == mService2.getMaxValue()){
                        mButton2.setText("Restart");
                    }
                    else{
                        mButton2.setText("Start");
                    }
                }
            }
        });


    }

    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: Connected to service");
            MyService.MyBinder binder= (MyService.MyBinder) iBinder;
            mBinder.postValue(binder);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBinder.postValue(null);
        }
    };

    private ServiceConnection serviceConnection2=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: Connected to service");
            MyService2.MyBinder binder2= (MyService2.MyBinder) iBinder;
            mBinder2.postValue(binder2);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBinder2.postValue(null);
        }
    };

    public LiveData<Boolean> getIsProgressUpdating(){
        return mIsProgressUpdating;
    }

    public LiveData<Boolean> getIsProgressUpdating2(){
        return mIsProgressUpdating2;
    }

    public LiveData<MyService.MyBinder>getBinder(){
        return mBinder;
    }

    public LiveData<MyService2.MyBinder>getBinder2(){
        return mBinder2;
    }



    public ServiceConnection getServiceConnection(){
        return serviceConnection;
    }

    public ServiceConnection getServiceConnection2(){
        return serviceConnection2;
    }



    public void setIsProgressBarUpdating(boolean isUpdating){
        mIsProgressUpdating.postValue(isUpdating);
    }

    public void setIsProgressBarUpdating2(boolean isUpdating){
        mIsProgressUpdating2.postValue(isUpdating);
    }






    @Override
    protected void onResume(){
        super.onResume();
        startService2();
        startService();

    }



    @Override
    protected void onStop() {
        super.onStop();
        if(getBinder() != null){
            unbindService(getServiceConnection());
        }

        if(getBinder2() != null){
            unbindService(getServiceConnection2());
        }


    }


    private void startService(){
        Intent serviceIntent= new Intent(this, MyService.class);
        startService(serviceIntent);
        bindService();
    }

    private void bindService(){
        Intent serviceIntent= new Intent(this, MyService.class);
        bindService(serviceIntent,getServiceConnection(),BIND_AUTO_CREATE);
    }

    private void startService2(){
        Intent serviceIntent2= new Intent(this, MyService2.class);
        startService(serviceIntent2);

        bindService2();
    }

    private void bindService2(){
        Intent serviceIntent2= new Intent(this, MyService2.class);
        bindService(serviceIntent2,getServiceConnection2(),BIND_AUTO_CREATE);
    }
}