package fi.metropolia.busdata.sensoridata;

/**
 * Created by alm on 12/8/16.
 */

public class GyroData {

    private float timeStampDeltaRotation = -0.0f;
    private float axisX = -0.0f;
    private float axisY = -0.0f;
    private float axisZ = -0.0f;

    public GyroData(float timeStampDeltaRotation,
                    float axisX,
                    float axisY,
                    float axisZ) {

        this.timeStampDeltaRotation = timeStampDeltaRotation;
        this.axisX = axisX;
        this.axisY = axisY;
        this.axisZ = axisZ;
    }

    public float getAxisX() {
        return axisX;
    }

    public float getAxisY() {
        return axisY;
    }

    public float getAxisZ() {
        return axisZ;
    }

    public float getTimeStampDeltaRotation() {
        return timeStampDeltaRotation;
    }
    public String getJsonString() {
        return "{'dT': '"+this.timeStampDeltaRotation+"',"+
                "'X': '"+this.axisX+"',"+
                "'Y': '"+this.axisY+"',"+
                "'Z':  '"+this.axisZ+"'}";
    }

    public String toString() {
        return "dT "+this.timeStampDeltaRotation+"\n"+
               "X  "+this.axisX+"\n"+
               "Y  "+this.axisY+"\n"+
               "Z  "+this.axisZ;
    }

}
