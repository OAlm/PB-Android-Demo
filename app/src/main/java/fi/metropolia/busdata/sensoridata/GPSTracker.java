package fi.metropolia.busdata.sensoridata;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;
import android.util.Log;

/**
 * Created by jasu on 14/11/2016.
 */

// http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/
// http://techlovejump.com/android-gps-location-manager-tutorial/

public class GPSTracker extends Activity implements LocationListener {

    public String valueGPS;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("GPSTracker / onCreate", "pending");
        setContentView(R.layout.activity_send_data);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        // Android studio halusi laittaa permissio chekin
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("GPSTracker", "permission failed for accessing the location!");
            return;
        }
        Log.e("GPS ok", "ok");
        // dummy values to test that the init was ok
        DataContainer.setGPS(-10.0, -10.0);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        DataContainer.setGPS(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getBaseContext(), "Gps is turned on! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("GPS permission","Permission granted!");
                    Toast.makeText(getBaseContext(), "GPS enabled", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("GPS permission","Permission not granted!");
                    Toast.makeText(getBaseContext(), "GPS disabled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}