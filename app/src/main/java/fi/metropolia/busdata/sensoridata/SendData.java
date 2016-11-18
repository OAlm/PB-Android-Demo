package fi.metropolia.busdata.sensoridata;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.text.format.Time;

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

public class SendData extends AppCompatActivity {

    // alustetaan syötettävä ID
    EditText mEdit;
    public String valueID;

    // alustetaan sending while-looppia varten
    public boolean sending = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);
    }

    // Thread suoritetaan Käynnistä-nappia painettaessa
    public class MyThread extends Thread {
        public void run(){
            while(sending == true) {
                // haetaan id arvo käyttöliittymästä
                mEdit = (EditText) findViewById(R.id.edit_id);
                valueID = mEdit.getText().toString();
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
        String timeStamp = now.format("%d.%m.%Y %H:%M:%S");
        return timeStamp;
    }

    // sammutetaan lähetys
    public void close(View view) {
        sending = false;
        return;
    }

    // käynnistetään lähetys
    public void sendData(View view) {
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

                jsonobj.put("ID", valueID);
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