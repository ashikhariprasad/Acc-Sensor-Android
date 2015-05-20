package com.sjsu.proj.accsensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by Ashik on 4/20/2015.
 */
public class AccelerationDetector implements Runnable,SensorEventListener{

    Context mContext;
    SensorManager mSensorManager;
    Sensor mSensor;
    float[] gravity = new float[3];
    float[] linear_acc = new float[3];
    float acceleration;
    float accThreshold = 10.0f;
    DataObject dob;

    public AccelerationDetector(Context c, DataObject dob){
        this.mContext = c;
        this.dob = dob;
    }

    @Override
    public void run() {
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), mSensorManager.SENSOR_DELAY_GAME);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Log.d("AccelerationDetector","run()");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Log.d("AccelerationDetector","onSensorChanged()");
        final float alpha = 0.8f;

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        linear_acc[0] = event.values[0] - gravity[0];
        linear_acc[1] = event.values[1] - gravity[1];
        linear_acc[2] = event.values[2] - gravity[2];

        if(linear_acc[0] > linear_acc[1] && linear_acc[0] > linear_acc[2])
            acceleration = linear_acc[0];
        else if(linear_acc[1] > linear_acc[0] && linear_acc[1] > linear_acc[2])
            acceleration = linear_acc[1];
        else if(linear_acc[2] > linear_acc[0] && linear_acc[2] > linear_acc[1])
            acceleration = linear_acc[2];

        //Log.d("AccelerationDetector",Float.toString(acceleration));
        if(acceleration > accThreshold) {
            dob.updateAcceleration(acceleration);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
