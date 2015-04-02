package com.sjsu.proj.accsensor;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ssrinidhi on 4/1/2015.
 */
public class DetectorFragment extends Fragment implements SensorEventListener{

    SensorManager mSensorManager;
    Sensor mSensor;
    TextView outputX;
    TextView outputY;
    TextView outputZ;

    float[] gravity = new float[3];
    float[] linear_acc = new float[3];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_detector,parent,false);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        outputX = (TextView) v.findViewById(R.id.accX);
        outputY = (TextView) v.findViewById(R.id.accY);
        outputZ = (TextView) v.findViewById(R.id.accZ);

        return v;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        final float alpha = 0.8f;

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        linear_acc[0] = event.values[0] - gravity[0];
        linear_acc[1] = event.values[1] - gravity[1];
        linear_acc[2] = event.values[2] - gravity[2];

        if(linear_acc[0] > 10 || linear_acc[1] > 10 || linear_acc[2] > 10){
            outputX.setText("Acceleration x:"+Float.toString(linear_acc[0]));
            outputY.setText("Acceleration y:"+Float.toString(linear_acc[1]));
            outputZ.setText("Acceleration z:"+Float.toString(linear_acc[2]));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), mSensorManager.SENSOR_DELAY_GAME);

    }
}
