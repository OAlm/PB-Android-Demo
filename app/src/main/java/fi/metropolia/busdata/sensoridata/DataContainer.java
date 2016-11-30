package fi.metropolia.busdata.sensoridata;

import java.util.Arrays;
import java.util.List;
import android.util.Log;

/**
 * Created by jasu on 17/11/2016.
 */

public class DataContainer {

    // GPS: format <lat, lon>
    public static final Double[] UNDEFINEDCOORDINATES = {-1.0,-1.0};
    private static List<Double> gpsCoordinates = Arrays.asList(UNDEFINEDCOORDINATES);
    public static void setGPS(Double lat, Double lon) {
        Log.e("setGPS", "Values: "+lat+","+lon);
        Double[] arr = {lat, lon};
        gpsCoordinates = Arrays.asList(arr);
    }

    public static List<Double> getGPS(){
        Log.e("GPSTracker", "Values: "+gpsCoordinates);
        return gpsCoordinates;
    }

    // Mitä tekee?
    public static void main(String[]args){
        setGPS(0.1,0.1);
        System.out.println(getGPS());
    }

    // Gyroscope
    public static final float[] UNDEFINEDGYROAXISES = {1f,1f};
    private static List<float[]> gyroAxises = Arrays.asList(UNDEFINEDGYROAXISES);
    public static void setGyro(float gyroAxisX, float gyroAxisY) {
        Log.e("Gyro", "Values: "+gyroAxisX+","+gyroAxisY);
        float[] arr = {gyroAxisX, gyroAxisY};
        gyroAxises = Arrays.asList(arr);
    }

    public static List<float[]> getGyro() {
        Log.e("GMS / Gyro", "Values: " + gyroAxises);
        return gyroAxises;
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

    // Step Counter
    public static final float UNDEFINEDSTEPS = 0;
    private static float steps = UNDEFINEDSTEPS;
    public static void setStepCount(float stepCount) {steps = stepCount;
    }

    public static float getStepCount(){
        Log.e("GMS / Steps", "Value: "+steps);
        return steps;
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
    public static int UNDEFINEDBATLEVEL = 100;
    private static int battery = UNDEFINEDBATLEVEL;
    public static void setBatLevel(int betLevel) {
        battery = betLevel;
    }

    public static int getBatLevel(){
        Log.e("BatteryTracker", "Level: "+battery);
        return battery;
    }
}