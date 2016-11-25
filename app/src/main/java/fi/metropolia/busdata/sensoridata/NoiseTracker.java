package fi.metropolia.busdata.sensoridata;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by jasu on 24/11/2016.
 */

public class NoiseTracker extends Activity {

    private MediaRecorder mRecorder = null;

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
                e.printStackTrace();
            }
            mRecorder.start();
            Log.e("NoiseTracker", "mRecorder.start ok");
        }
    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public int getAmplitude() {
        if (mRecorder != null) {
            DataContainer.setNoise(mRecorder.getMaxAmplitude());
        } else {
            return 0;
        }
        return 0;
    }
}