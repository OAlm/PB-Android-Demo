package fi.metropolia.busdata.sensoridata;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by jasu on 21/11/2016.
 */

public class GetMotionSensors extends Activity implements SensorEventListener {

    public enum SensorType {STEPCOUNTER, GYRO, ACCELERATOR}

    // https://developer.android.com/guide/topics/sensors/sensors_overview.html

    // GYRO variables
    private float timestamp;

    public Sensor newSensor(SensorType sensor) {
        Log.e("MotionSenrorit", "HALOO???");

        SensorManager mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if(sensor == SensorType.GYRO) {
            return mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        } else if(sensor == SensorType.ACCELERATOR) {
            return mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        } else if(sensor == SensorType.STEPCOUNTER) {
            return mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        } else {
            // FAIL
            return mSensorManager.getDefaultSensor(0);
        }

    }

    // https://developer.android.com/reference/android/hardware/SensorManager.html
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            if (timestamp != 0) {
                DataContainer.setGyro(event.values[0], event.values[1]);
            }
        } else if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            DataContainer.setAcceleration(event.values[0], event.values[1]);
        } else if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            DataContainer.setStepCount(event.values[0]);
        }else {
            Log.e("onSensorChanged", "FAIL");
        }
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
*/
}