package fi.metropolia.busdata.sensoridata;

import java.util.Arrays;
import java.util.List;

import android.location.Location;
import android.util.Log;

/**
 * Created by jasu on 17/11/2016.
 */

public class DataContainer {

    //  --------
    //  LOCATION
    //  --------
    private static Location location = null;
    private static GyroData gyro = null;
    private static Float stepCount = null;

    public static boolean locationDefined() {
        return location != null;
    }

    public static void setLocation(Location loc) {
        Log.e("DC.setLocation", loc.getLatitude()+","+loc.getLongitude());
        location = loc;
    }
    public static Location getLocation() {
        return location;
    }

    //  ---------
    //  GYROSCOPE
    //  ---------
    public static boolean gyroDefined() {
        return gyro != null;
    }

    public static void setGyro(GyroData g) {
        Log.e("Gyro", g.toString());
        gyro = g;
    }

    public static GyroData getGyro() {
        return gyro;
    }

    // ---------
    // STEPCOUNT
    // ---------
    public static boolean stepCountDefined() {
        return stepCount != null;
    }

    public static void setStepCount(float stepCount) {
        stepCount = stepCount;
    }

    public static float getStepCount(){
        Log.e("GMS / Steps", "Value: "+stepCount);
        return stepCount;
    }


    // Linear acceleration
    public static final float[] UNDEFINEDAXISES = {1f,1f};
    private static List<float[]> accAxises = Arrays.asList(UNDEFINEDAXISES);
    public static void setAcceleration(float accAxisX, float accAxisY) {
        Log.e("setAcceleration", "Values: "+accAxisX+","+accAxisY);
        float[] arr = {accAxisX, accAxisY};
        accAxises = Arrays.asList(arr);
    }

    public static List<float[]> getAcc(){
        Log.e("GMS / Acceleration", "Values: "+accAxises);
        // -> E/GMS / Acceleration: Values: [[F@21c02a0] ???
        return accAxises;
    }
    // Noise meter
    public static int UNDEFINEDNOISE = 0;
    private static int noise = UNDEFINEDNOISE;
    public static void setNoise(int noiseMeter) {
        noise = noiseMeter;
    }

    public static int getNoise(){
        Log.e("NoiseTracker", "Value: "+noise);
        return noise;
    }

    // Battery level
    public static int UNDEFINEDBATLEVEL = 1;
    private static int battery = UNDEFINEDBATLEVEL;
    public static void setBatLevel(int batLevel) {
        battery = batLevel;
    }

    public static int getBatLevel(){
        Log.e("BatteryTracker", "Level: "+battery);
        return battery;
    }
}