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
import android.provider.ContactsContract;
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
    // The minimum distance to change Updates in meters
    final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.e("GPSService onBind", "called");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Log.e("GPSService onStart", "pending");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("GPSService onStart", "permission failed for accessing the location!");
            //return START_STICKY;
        } else {
            Log.e("GPSService onStart", "permission succeeded");
        }
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        Log.e("GPS service", "created!");

        if(locationManager!=null){
            Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location!=null){
                Log.e("LAT", ""+location.getLatitude());
                Log.e("LON", ""+location.getLongitude());

            }
        } else {
            Log.e("locman","is null");
        }

        return START_STICKY;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e("GPSservice","provider disabled");
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e("GPSservice","provider enabled");
        Toast.makeText(getBaseContext(), "Gps is turned on! ", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e("GPSservice","status changed");
        // TODO Auto-generated method stub
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("GPSservice","location changed");
        DataContainer.setLocation(location);
    }

    @Override
    public void onDestroy() {
        Log.e("GPSservice","closed (onDestroy)");
        super.onDestroy();

    }

}
