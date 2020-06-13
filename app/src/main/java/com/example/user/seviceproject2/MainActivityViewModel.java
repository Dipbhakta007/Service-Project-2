package com.example.user.seviceproject2;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by USER on 6/12/2020.
 */

public class MainActivityViewModel extends ViewModel{

    private static final String TAG= "MainActivityViewModel";

    private MutableLiveData<Boolean>mIsProgressUpdating=new MutableLiveData<>();
    private MutableLiveData<MyService.MyBinder>mBinder= new MutableLiveData<>();

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

    public LiveData<Boolean>getIsProgressUpdating(){
        return mIsProgressUpdating;
    }

    public LiveData<MyService.MyBinder>getBinder(){
        return mBinder;
    }

    public ServiceConnection getServiceConnection(){
        return serviceConnection;
    }



    public void setIsProgressBarUpdating(boolean isUpdating){
        mIsProgressUpdating.postValue(isUpdating);
    }

}
