package fi.metropolia.busdata.sensoridata;

import android.content.Intent;
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

public class Main extends AppCompatActivity {

    // alustetaan syötettävä data
    EditText appIdEdit;
    public String valueAppID;
    EditText devIdEdit;
    public String valueDevID;
    EditText msgEdit;
    public String valueMSG;

    // alustetaan muut muuttujat
    private GPSTracker gps;
    public boolean onoffLocation;
    public boolean onoffAudio;
    public boolean onoffMSensors;
    public boolean onoffDevice;
    public boolean sending = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);
        // Log.e("Main / onCreate", "ok");
    }

    // Thread suoritetaan Käynnistä-nappia painettaessa
    public class MyThread extends Thread {
        public void run(){
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
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.checkbox_location:
                if(checked) {
                    onoffLocation = true;
                } else {
                    onoffLocation = false;
                }
            case R.id.checkbox_audio:
                if(checked) {
                    onoffAudio = true;
                } else {
                    onoffAudio = false;
                }
            case R.id.checkbox_mSensors:
                if(checked) {
                    onoffMSensors = true;
                } else {
                    onoffMSensors = false;
                }
            case R.id.checkbox_device:
                if(checked) {
                    onoffDevice = true;
                } else {
                    onoffDevice = false;
                }
                break;
        }
    }

    // luodaan timeStamp
    public static String getTimeStamp() {
        Time now = new Time();
        now.setToNow();
        return now.format("%d.%m.%Y %H:%M:%S");}

    // sammutetaan lähetys
    public void close(View view) {
        Toast.makeText(getBaseContext(), "Sammutettu", Toast.LENGTH_SHORT).show();
        sending = false;
    }

    // käynnistetään lähetys
    public void sendData(View view) {
        Log.e("***Init GPSTracker***", "in process");
        Intent intent = new Intent(this, GPSTracker.class);
        startActivity(intent);
        Log.e("********************", "ok");
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
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://busdata.metropolia.fi:80/bussidata");
            post.addHeader("Content-Type", "application/json; charset=UTF-8");

            // Haetaan muuttujia DataContainer luokasta
            String latLonStr = DataContainer.getGPS().toString();
            String gyroStr = DataContainer.getGyro().toString();
            String accStr = DataContainer.getAcc().toString();
            float stepsStr = DataContainer.getStepCount();
            int noiseStr = DataContainer.getNoise();
            int batteryStr = DataContainer.getBatLevel();

            try {
                // Luodaan JSON-objekti(t), eli mitä lähetetään
                JSONObject jsonobj = new JSONObject();
                JSONObject location = new JSONObject();
                JSONObject audio = new JSONObject();
                JSONObject mSensors = new JSONObject();
                JSONObject device = new JSONObject();

                jsonobj.put("app_id", valueAppID);
                jsonobj.put("dev_id", valueDevID);
                jsonobj.put("timeStamp", getTimeStamp());
                if (onoffLocation) {
                    location.put("coordinates", latLonStr);
                    location.put("speed", "15"); //example value
                    location.put("heading", "0"); //example value
                    location.put("altitude", "0"); //example value
                    jsonobj.put("location", location);
                } else {
                    Log.e("Main / location","off");
                }
                if (onoffAudio) {
                    audio.put("maxDecibel", noiseStr);
                    jsonobj.put("audio", audio);
                } else {
                    Log.e("Main / audio","off");
                }
                if (onoffMSensors) {
                    mSensors.put("acceleration", accStr);
                    mSensors.put("stepCounter", stepsStr);
                    jsonobj.put("motionsensors", mSensors);
                } else {
                    Log.e("Main / motionsensors","off");
                }
                if (onoffDevice) {
                    device.put("battery", batteryStr);
                    device.put("msg", valueMSG);
                    device.put("storage", "32/64"); //example value
                    jsonobj.put("device", device);
                } else {
                    Log.e("Main / device","off");
                }

                StringEntity se = new StringEntity(jsonobj.toString());
                Log.e("mainToPost", "mainToPost" + jsonobj.toString());
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