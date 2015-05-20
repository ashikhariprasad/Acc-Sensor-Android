package com.sjsu.proj.accsensor;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Ashik on 4/13/2015.
 */
public class SoundDetector {

    static final private double CONST = 0.6;

    private MediaRecorder mRecorder = null;
    private double mConst = 0.0;
    double refAmplitude = 1;

    public void start() {
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
        }
    }

    public void stop() {
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
