package fi.metropolia.busdata.sensoridata.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import fi.metropolia.busdata.sensoridata.DataContainer;
import fi.metropolia.busdata.sensoridata.R;

/**§
 * Created by alm on 12/28/16.
 */

public class GPSService extends Service implements LocationListener {

    private LocationManager locationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Log.e("GPSService / start", "pending");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        // Android studio halusi laittaa permissio chekin
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("GPSTracker", "permission failed for accessing the location!");
            return START_STICKY;
        }
        Log.e("GPS ok", "on create");
        // dummy values to test that the init was ok
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);


        return START_STICKY;

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e("GPSTracker","provider disabled");
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e("GPSTracker","provider enabled");
        Toast.makeText(getBaseContext(), "Gps is turned on! ", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e("GPSTracker","status changed");
        // TODO Auto-generated method stub
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("GPSTracker","location changed");
        DataContainer.setLocation(location);
    }

    @Override
    public void onDestroy() {
        Log.e("GPS","ended!");
        super.onDestroy();

    }

}
