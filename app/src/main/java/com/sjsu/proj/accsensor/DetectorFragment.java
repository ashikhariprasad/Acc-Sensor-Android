package com.sjsu.proj.accsensor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.sjsu.proj.accsensor.UserManager.AsycCallHelp;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashik on 4/1/2015.
 */
public class DetectorFragment extends Fragment {

    TextView outputX;
    TextView outPutSound;
    Switch alarmSwitch;
    TextView alertText;
    TextView alertCountDown;
    TextView location;
    TextView slideLeft;
    String alertResponse;
    String[] gpsResponse;
    Intent gpsIntent;
    Intent soundIntent;
    boolean sentAlert = false;
    String[] gpsData;
    String alertSound;
    String alertAcceleration;
    long timeOut = 15000;
    MyTimer timer = new MyTimer(timeOut,1000);
    boolean stopThread = false;

    String userId;Handler mainHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null){
            userId = extras.getString("EXTRA_USER_ID");
        }
        stopThread = true;
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_detector,parent,false);
        outputX = (TextView) v.findViewById(R.id.accX);
        outPutSound = (TextView) v.findViewById(R.id.soundValue);
        alarmSwitch = (Switch)v.findViewById(R.id.alarmSwitch);
        alertText = (TextView)v.findViewById(R.id.alert);
        alertCountDown = (TextView)v.findViewById(R.id.countDown);
        location = (TextView)v.findViewById(R.id.location);
        slideLeft = (TextView)v.findViewById(R.id.slideLeft);

        soundIntent = new Intent(getActivity(), SoundAlarmService.class);

        mainHandler = new Handler(){
            public void handleMessage(Message messages) {
                alertResponse = messages.getData().getString("ALERT");
                if(alertResponse != null){

                    alertResponse = null;
                    alarmSwitch.setVisibility(View.VISIBLE);
                    alarmSwitch.setChecked(true);
                    alertText.setVisibility(View.VISIBLE);
                    alertCountDown.setVisibility(View.VISIBLE);
                    slideLeft.setVisibility(View.VISIBLE);
                    outputX.setText(alertAcceleration);
                    outPutSound.setText(alertSound);
                    alertResponse = null;
                    timer.start();

                    getActivity().startService(soundIntent);

                    MyGPSResultReceiver receiver = new MyGPSResultReceiver(null);
                    gpsIntent = new Intent(getActivity(),GPSLocation.class);
                    gpsIntent.putExtra("Receiver",receiver);
                    getActivity().startService(gpsIntent);

                }
                //gpsResponse = messages.getData().getStringArray("GPS");
                //if(gpsResponse != null){
                //    location.setText("Latitude: "+gpsResponse[0]+
                //            " Longitude: "+gpsResponse[1]+
                //            " Country: "+gpsResponse[2]+
                //            " City: "+gpsResponse[3]+
                //            " Postal Code: "+gpsResponse[4]+
                //            " AddressLine: "+gpsResponse[5]);
                //    alertResponse = null;
                //}
            }

        };

        Thread accelerationThread = new Thread(new SenseAccident(getActivity(),mainHandler));
        accelerationThread.start();

        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    getActivity().stopService(soundIntent);
                    timer.cancel();
                    alertText.setVisibility(View.INVISIBLE);
                    alertCountDown.setVisibility(View.INVISIBLE);
                    alarmSwitch.setVisibility(View.INVISIBLE);
                    slideLeft.setVisibility(View.INVISIBLE);
                    location.setVisibility(View.INVISIBLE);
                    sentAlert = false;
                }
            }
        });

        return v;
    }
    @Override
    public void onResume() {
        stopThread = true;
        super.onResume();
    }

    @Override
    public void onPause(){
        stopThread = false;
        mainHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    class MyTimer extends CountDownTimer {
        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
            alertCountDown.setText(""+millisUntilFinished/1000);
            if(millisUntilFinished / 1000 == 1){
                getActivity().stopService(soundIntent);
            }
        }

        public void onFinish() {
            alertText.setVisibility(View.GONE);
            alertCountDown.setText("Contacting Emergency Services");
            location.setVisibility(View.VISIBLE);
            location.setText("Latitude: "+gpsData[0]+" Longitude: "+gpsData[1]+
                    " Country: "+gpsData[2]+" City: "+gpsData[3]+
                    " Postal Code: "+gpsData[4]+" AddressLine: "+gpsData[5]);
            sentAlert = false;
            AsycCallHelp callHelp = new AsycCallHelp(getActivity());
            callHelp.execute(new String[]{gpsData[0],gpsData[1],gpsData[2],gpsData[3],
                    gpsData[4],gpsData[5],userId});
        }
    }

    class UpdateGPSUI implements Runnable{

        public UpdateGPSUI(String[] data){
            gpsData = data;
        }
        @Override
        public void run() {
            location.setVisibility(View.VISIBLE);
            location.setText("Latitude: "+gpsData[0]+" Longitude: "+gpsData[1]+
                    " Country: "+gpsData[2]+" City: "+gpsData[3]+
                    " Postal Code: "+gpsData[4]+" AddressLine: "+gpsData[5]);
            sentAlert = false;
        }
    }

    class MyGPSResultReceiver extends ResultReceiver{

        public MyGPSResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(resultCode == 100){
                getActivity().stopService(gpsIntent);
                gpsData = resultData.getStringArray("GPS");
                //getActivity().runOnUiThread(new UpdateGPSUI(resultData.getStringArray("GPS")));
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    //This class is computes the instantaneous decibel value. Runs on a separate thread.
    public class ReviseSoundDetect implements Runnable{
        static final private double CONST = 0.6;
        private MediaRecorder mRecorder = null;
        private double mConst = 0.0;
        double refAmplitude = 1;
        double currentDb = 0.0;
        Handler handler;

        public ReviseSoundDetect(Handler h){
            this.handler = h;
        }

        @Override
        public void run() {
            if (mRecorder == null) {
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile("/dev/null");
                try {
                    mRecorder.prepare();
                } catch (IOException e) {
                    Log.e("Error using recorder", e.toString());
                }
                mRecorder.start();
                mConst = 0.0;
                while(stopThread){
                    currentDb = soundDb();
                    if(currentDb > 80.0 && handler != null){
                        Log.d("SOUND = ",Double.toString(currentDb));
                        Bundle bundle = new Bundle();
                        Message message = handler.obtainMessage();
                        bundle.putString("ALERT","SOUND");
                        message.setData(bundle);
                        handler.sendMessage(message);
                        //outPutSound.setText(Double.toString(currentDb));
                        alertSound = Double.toString(currentDb);
                        try {
                            // thread to sleep for 1000 milliseconds
                            Thread.sleep(300);
                        } catch (Exception e) {
                            Log.e("Sound Thread Exception:", "Error sleeping sound thread");
                        }
                    }
                }
                stop();
            }
        }
        public void stop() {
            Log.d("Sound Detector","Stopping Sound Thread");
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            }
        }
        public double getAmplitude() {
            if (mRecorder != null)
                return  (mRecorder.getMaxAmplitude());
            else
                return 0;
        }
        public double getAmplitudeEMA() {
            double amp = getAmplitude();
            mConst = CONST * amp + (1.0 - CONST) * mConst;
            return mConst;
        }
        public double soundDb(){
            return  20 * Math.log10(getAmplitudeEMA() / refAmplitude);
        }
    }

    //----------------------------------------------------------------------------------------------
    //Acceleration Event Listener.
    public class SenseAccident extends Thread{
        SensorManager mSensorManager;
        Sensor mSensor;
        Map<String,Long> tStamp = new HashMap<String,Long>();
        Context mContext;
        Handler mHandler;
        AccelerationEventListener accelerationListener;

        public SenseAccident(Context c, Handler h){
            this.mContext = c;
            this.mHandler = h;
        }

        public void run(){
            tStamp.put("ACCELERATION_TIME",(long)10000);
            tStamp.put("SOUND_TIME",(long)0);
            mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER );
            Looper.prepare();

            Handler handler = new Handler(){
                public void handleMessage(Message message){
                    String response = message.getData().getString("ALERT");
                    Long time = System.currentTimeMillis();
                    if(response.equalsIgnoreCase("ACCELERATION")){
                        Log.d("MESSAGE : ","ACCELERATION ALERT");
                        Log.d("TIME : ",Long.toString(time));
                        tStamp.put("ACCELERATION_TIME",time);

                    }
                    if(response.equalsIgnoreCase("SOUND")){
                        Log.d("MESSAGE : ","SOUND ALERT");
                        Log.d("TIME : ",Long.toString(time));
                        tStamp.put("SOUND_TIME",time);
                    }

                    if(Math.abs(tStamp.get("ACCELERATION_TIME") - tStamp.get("SOUND_TIME")) < 500 && !sentAlert){
                        if(mHandler != null) {
                            sentAlert = true;
                            Log.d("ALERT : ", "!!!!SOUND ALARM!!!");
                            Bundle bundle = new Bundle();
                            Message alertMessage = mHandler.obtainMessage();
                            bundle.putString("ALERT", "SOS");
                            alertMessage.setData(bundle);
                            mHandler.sendMessage(alertMessage);
                            //Thread GPS = new Thread(new GPSLocation(mContext,mHandler));
                            //GPS.start();
                        }
                        else{
                            mSensorManager.unregisterListener(accelerationListener,mSensor);
                            this.removeCallbacksAndMessages(null);
                        }
                    }
                }
            };

            Thread soundThread = new Thread(new ReviseSoundDetect(handler));
            soundThread.start();

            accelerationListener = new AccelerationEventListener(handler);
            mSensorManager.registerListener(accelerationListener,mSensor,SensorManager.SENSOR_DELAY_UI,handler);
            Looper.loop();

        }
    }
    public class AccelerationEventListener implements SensorEventListener{

        float[] gravity = new float[3];
        float[] linear_acc = new float[3];
        private float accelerationThreshold = 10.0f;
        float acceleration;
        Handler handler;

        public AccelerationEventListener(Handler h){
            this.handler = h;
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

            if(linear_acc[0] > accelerationThreshold || linear_acc[1] > accelerationThreshold || linear_acc[2] > accelerationThreshold) {
                if (linear_acc[0] > linear_acc[1] && linear_acc[0] > linear_acc[2])
                    acceleration = linear_acc[0];
                else if (linear_acc[1] > linear_acc[0] && linear_acc[1] > linear_acc[2])
                    acceleration = linear_acc[1];
                else if (linear_acc[2] > linear_acc[0] && linear_acc[2] > linear_acc[1])
                    acceleration = linear_acc[2];
                Log.d("ACCELERATION = ",Float.toString(acceleration));

                //outputX.setText(alertAcceleration);
                alertAcceleration = Float.toString(acceleration);
                if(handler != null) {
                    Bundle bundle = new Bundle();
                    Message message = handler.obtainMessage();
                    bundle.putString("ALERT", "ACCELERATION");
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

}