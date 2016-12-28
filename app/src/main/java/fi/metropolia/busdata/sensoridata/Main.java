package fi.metropolia.busdata.sensoridata;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.text.format.Time;
import android.widget.Toast;

/**
* org.apache.http pois käytöstä
* build.gradleen lisätty: useLibrary "org.apache.http.legacy"
* -> toimii, mutta deprecated varoituksia
*/
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import fi.metropolia.busdata.sensoridata.service.GPSService;

public class Main extends AppCompatActivity {

    // alustetaan syötettävä data
    EditText appIdEdit;
    public String valueAppID;
    EditText devIdEdit;
    public String valueDevID;
    EditText msgEdit;
    public String valueMSG;

    // alustetaan muut muuttujat
    //private GPSTracker gps;

    private Intent gpsTracker;
    private Intent motionTracker;

    public boolean sending = true;
    private boolean locationEnabled;
    private boolean audioEnabled;
    private boolean deviceEnabled;
    private boolean motionEnabled;

    private void initCheckBoxes() {
        this.locationEnabled = false;
        this.audioEnabled = false;
        this.deviceEnabled = false;
        this.motionEnabled = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Main","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);
        initCheckBoxes();
        Log.e("Main / onCreate", "ok");
    }

    // Thread suoritetaan Käynnistä-nappia painettaessa
    public class MyThread extends Thread {
        public void run(){
            Log.e("Main","sending");
            while(sending) {
                // Haetaan App_Id, Dev_ID ja msg arvot käyttöliittymästä
                appIdEdit = (EditText) findViewById(R.id.edit_appid);
                valueAppID = appIdEdit.getText().toString();
                devIdEdit = (EditText) findViewById(R.id.edit_devid);
                valueDevID = devIdEdit.getText().toString();
                msgEdit = (EditText) findViewById(R.id.edit_msg);
                    valueMSG = msgEdit.getText().toString();
                try {
                    // Suoritetaan AsyncT, ja odotetaan sekunti
                    AsyncT asyncT = new AsyncT();
                    asyncT.execute();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Katsotaan checkboxit
    public void onCheckboxClicked(View view) {

        // TODO: intent can carry extra values with putExtra, see
        // https://developer.android.com/training/basics/firstapp/starting-activity.html
        Log.e("Checkbox", "clicked");
        boolean status = ((CheckBox) view).isChecked();

        int checkBox = view.getId();
        if (checkBox == R.id.checkbox_device) {
            this.deviceEnabled = status;
            Log.e("Checkbox device", "" + status);
        } else if (checkBox == R.id.checkbox_audio) {
            this.audioEnabled = status;
            Log.e("Checkbox audio", "" + status);
        } else if (checkBox == R.id.checkbox_location) {
            this.locationEnabled = status;
            Log.e("Checkbox location", "" + status);
            if(this.locationEnabled) {
                startGPS();
            } else {
                stopGPS();
            }
            //gpsTracker = new Intent(this, GPSTracker.class);
            //gpsTracker = new Intent(this, GPSTracker.class);
            //startActivity(gpsTracker);

            Log.e("gpstracker init ok!", "ok");

        } else if (checkBox == R.id.checkbox_mSensors) {
            this.motionEnabled = status;
            Log.e("Checkbox motion", "" + status);

            Log.e("***Init MotionTrckr***", "in process");
            motionTracker = new Intent(this, MotionSensorsTracker.class);
            startActivity(motionTracker);
            Log.e("motiontracker init ok!", "ok");

        }
        Log.e("Main","CheckBox leave");
    }

    // GPS Service
    public void startGPS() {
        startService(new Intent(getBaseContext(), GPSService.class));
    }

    // Method to stop the service
    public void stopGPS() {
        stopService(new Intent(getBaseContext(), GPSService.class));
    }

    // luodaan timeStamp
    public static String getTimeStamp() {
        Log.e("Main","getTimeStamp");
        Time now = new Time();
        now.setToNow();
        return now.format("%d.%m.%Y %H:%M:%S");}

    // sammutetaan lähetys
    public void close(View view) {
        Log.e("Main","close");
        Toast.makeText(getBaseContext(), "Sammutettu", Toast.LENGTH_SHORT).show();
        sending = false;
    }

    // käynnistetään lähetys
    public void sendData(View view) {
        Log.e("Main","sendData");
        Toast.makeText(getBaseContext(), "Käynnissä", Toast.LENGTH_SHORT).show();
        sending = true;
        MyThread loop = new MyThread();
        loop.start();
    }

    // https://trinitytuts.com/post-json-data-to-server-in-android/
    // http://stackoverflow.com/questions/31552242/sending-http-post-request-with-android
    // Inner class to get response
    class AsyncT extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            Log.e("Main","background");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://busdata.metropolia.fi:80/bussidata");
            post.addHeader("Content-Type", "application/json; charset=UTF-8");

            int noiseStr = DataContainer.getNoise();
            int batteryStr = DataContainer.getBatLevel();

            try {
                // Luodaan JSON-objekti(t), eli mitä lähetetään
                JSONObject messageContainer = new JSONObject();
                messageContainer.put("app_id", valueAppID);
                messageContainer.put("dev_id", valueDevID);
                messageContainer.put("timeStamp", getTimeStamp());
                if (locationEnabled && DataContainer.locationDefined()) {

                    Location locationData = DataContainer.getLocation();
                    String latlonStr = "["+locationData.getLatitude()+", "
                                            +locationData.getLongitude()+"]";

                    JSONObject location = new JSONObject();
                    location.put("coordinates", latlonStr);
                    location.put("speed", locationData.getSpeed()); //example value
                    //location.put("heading",locationData.); //example value
                    location.put("altitude", locationData.getAltitude()); //example value
                    messageContainer.put("location", location);
                } else {
                    System.out.println("loc: "+ DataContainer.locationDefined());
                    Log.e("Main / location","off");
                }
                if (audioEnabled) {
                    JSONObject audio = new JSONObject();
                    audio.put("maxDecibel", noiseStr);
                    messageContainer.put("audio", audio);
                } else {
                    Log.e("Main / audio","off");
                }

                // TODO: how to recognize different evens
                if (motionEnabled && DataContainer.gyroDefined())  {
                    JSONObject mSensors = new JSONObject();
                    String gyroStr = DataContainer.getGyro().getJsonString();
                    //String accStr = DataContainer.getAcc().toString();
                    //float stepsStr = DataContainer.getStepCount();

                    //mSensors.put("acceleration", accStr);
                    //mSensors.put("stepCounter", stepsStr);
                    mSensors.put("gyro", gyroStr);
                    messageContainer.put("motionsensors", mSensors);
                } else {
                    System.out.println("mot: "+ DataContainer.gyroDefined());
                    Log.e("Main / motionsensors","off");
                }
                if (deviceEnabled) {
                    JSONObject device = new JSONObject();
                    device.put("battery", batteryStr);
                    device.put("msg", valueMSG);
                    device.put("storage", "32/64"); //example value
                    messageContainer.put("device", device);
                } else {
                    Log.e("Main / device","off");
                }

                StringEntity se = new StringEntity(messageContainer.toString());
                Log.e("mainToPost", "mainToPost" + messageContainer.toString());
                post.setEntity(se);

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(post);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}