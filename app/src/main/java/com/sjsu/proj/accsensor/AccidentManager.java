package com.sjsu.proj.accsensor;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Ashik on 4/19/2015.
 */
public class AccidentManager implements Runnable {

    final Context mContext;
    final Handler mHandler;
    float accThreshold = 10.0f;
    DataObject dObj;
    Runnable accRun,soundRun;

    public AccidentManager(Context m, Handler h,DataObject db,Runnable accRun, Runnable soundRun){
        this.mContext = m;
        this.mHandler = h;
        this.dObj = db;
        this.accRun = accRun;
        this.soundRun = soundRun;
    }

    @Override
    public void run() {
        Log.d("AccelerationManager", "run()");

        Thread accelerationThread = new Thread(new AccelerationDetector(mContext,dObj));
        accelerationThread.start();
        while(true) {
            Log.d("AccelerationManager : ", Float.toString(dObj.getAcceleration()));
            mHandler.post(accRun);
        }
    }
}
