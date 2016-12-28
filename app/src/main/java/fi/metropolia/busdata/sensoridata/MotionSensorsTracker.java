package fi.metropolia.busdata.sensoridata;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by jasu on 21/11/2016.
 */

public class MotionSensorsTracker extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mGyroscope;
    private Sensor mAcceleration;
    private Sensor mStepCounter;

    public enum SensorType {STEPCOUNTER, GYRO, ACCELERATOR}

    // https://developer.android.com/guide/topics/sensors/sensors_overview.html
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mStepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Log.e("Motion sensors", "onCreate");
    }

    /*
    public Sensor newSensor(SensorType sensor) {
        Log.e("MotionSenrorit", "HALOO???");
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if(sensor == SensorType.GYRO) {
            return mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null){
                Log.e("Success!", "There's a GYROSCOPE.");
            }
            else {
                Log.e("Failure!", "No GYROSCOPE.");
            }
            return mGyroscope;
        } else if(sensor == SensorType.ACCELERATOR) {
            return mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        } else if(sensor == SensorType.STEPCOUNTER) {
            return mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        } else {
            // FAIL
            return mSensorManager.getDefaultSensor(0);
        }
    }
    */

    // https://developer.android.com/reference/android/hardware/SensorManager.html
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.e("Motion sensors", "accuracy changed");
    }

    private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp = 0; // TODO, move

    @Override
    public final void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            if (timestamp != 0) { // TODO check
                final float dT = (event.timestamp - timestamp) * NS2S;
                float axisX = event.values[0];
                float axisY = event.values[1];
                float axisZ = event.values[2];

                GyroData g = new GyroData(dT, axisX, axisY, axisZ);
                DataContainer.setGyro(g);
            }
            timestamp = event.timestamp;
        } else if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            DataContainer.setAcceleration(event.values[0], event.values[1]);
        } else if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            DataContainer.setStepCount(event.values[0]);
        }else {
            Log.e("onSensorChanged", "FAIL");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}