package fi.metropolia.busdata.sensoridata;

import android.content.Intent;
import android.icu.util.GregorianCalendar;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    // alustetaan syötettävä ID
    EditText appIdEdit;
    public String valueAppID;
    EditText devIdEdit;
    public String valueDevID;
    EditText msgEdit;
    public String valueMSG;
    private GPSTracker gps;

    // alustetaan sending while-looppia varten
    public boolean sending = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);

        Log.e("Main / onCreate", "ok");
    }

    // Thread suoritetaan Käynnistä-nappia painettaessa
    public class MyThread extends Thread {
        public void run(){
            while(sending) {
                Log.e("Run OK", "ok");
                // haetaan App_Id arvo käyttöliittymästä
                appIdEdit = (EditText) findViewById(R.id.edit_appid);
                valueAppID = appIdEdit.getText().toString();
                // haetaan Dev_id arvo käyttöliittymästä
                devIdEdit = (EditText) findViewById(R.id.edit_devid);
                valueDevID = appIdEdit.getText().toString();
                // haetaan msg arvo käyttöliittymästä
                msgEdit = (EditText) findViewById(R.id.edit_msg);
                valueMSG = msgEdit.getText().toString();
                try {
                    // suoritetaan AsyncT
                    AsyncT asyncT = new AsyncT();
                    asyncT.execute();
                    // odotetaan sekunti
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // luodaan timestamp
    public static String getTimeStamp() {
        Time now = new Time();
        now.setToNow();
        return now.format("%d.%m.%Y %H:%M:%S");
    }

    // sammutetaan lähetys
    public void close(View view) {
        Toast.makeText(getBaseContext(), "Sammutettu", Toast.LENGTH_SHORT).show();
        sending = false;
    }

    // käynnistetään lähetys
    public void sendData(View view) {
        Log.e("Main / sendData", "start");

        Log.e("***Init GPSTracker***", "in process");
        Intent intent = new Intent(this, GPSTracker.class);
        startActivity(intent);
        Log.e("********************", "ok");

        Toast.makeText(getBaseContext(), "Lähetys päällä", Toast.LENGTH_SHORT).show();
        sending = true;
        MyThread loop = new MyThread();
        loop.start();
    }

    // https://trinitytuts.com/post-json-data-to-server-in-android/
    // http://stackoverflow.com/questions/31552242/sending-http-post-request-with-android
    /* Inner class to get response */
    class AsyncT extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://busdata.metropolia.fi:80/bussidata"); // YOUR_SERVICE_URL
            post.addHeader("Content-Type", "application/json; charset=UTF-8");

            String latLonStr = DataContainer.getGPS().toString();

            try {
                // luodaan JSON-objekti, eli mitä lähetetään
                JSONObject jsonobj = new JSONObject();

                jsonobj.put("APP_ID", valueAppID);
                jsonobj.put("DEV_ID", valueDevID);
                jsonobj.put("viesti", valueMSG);
                jsonobj.put("nopeus", "15");
                jsonobj.put("GPS", latLonStr);
                jsonobj.put("TimeStamp", getTimeStamp());

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