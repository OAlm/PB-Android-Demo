package fi.metropolia.busdata.sensoridata;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/*
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);
    }

    /*
    * Method joka käynnistyy kun Käynnistä-nappia painetaan.
    * Suorittaa AsyncT classin 1s välein.
    */
    public void sendData(View view) {
        // Tämä looppiin:

        // haetaan id arvo käyttöliittymästä
        mEdit = (EditText) findViewById(R.id.edit_id);
        valueID = mEdit.getText().toString();

        // suoritetaan AsyncT
        AsyncT asyncT = new AsyncT();
        asyncT.execute();
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

            try {
                // luodaan JSON-objekti, eli mitä lähetetään
                JSONObject jsonobj = new JSONObject();

                jsonobj.put("ID", valueID);
                jsonobj.put("nopeus", "10");

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