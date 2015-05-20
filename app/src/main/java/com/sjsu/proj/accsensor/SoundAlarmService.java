package com.sjsu.proj.accsensor;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class SoundAlarmService extends Service {
    MediaPlayer player;
    public SoundAlarmService() {
    }
    public void onCreate(){
        super.onCreate();
        Log.d("Alarm Service", "Service Started!");
        player = MediaPlayer.create(this,R.raw.emergency_alarm);
        player.setLooping(true);
    }
    public int onStartCommand(Intent intent, int flags, int startId){
        player.start();
        Log.d("Alarm Service", "Media Player started!");
        return 1;
    }
    public void onStop(){
        player.stop();
        player.release();
    }

    public void onPause(){
        player.stop();
        player.release();
    }
    public void onDestroy(){
        player.stop();
        player.release();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
