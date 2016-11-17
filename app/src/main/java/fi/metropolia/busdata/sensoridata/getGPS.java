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
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by jasu on 14/11/2016.
 */

// http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/
// http://techlovejump.com/android-gps-location-manager-tutorial/

public class getGPS extends Activity implements LocationListener {

    public String valueGPS;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Android studio halusi laittaa permissio chekin
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
    }

    @Override
    public void onLocationChanged(Location location) {

        valueGPS = "Lat: " + location.getLatitude() + "Long: " + location.getLongitude();
        Toast.makeText(getBaseContext(), valueGPS, Toast.LENGTH_LONG).show();
        DataContainer.setGPS(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(getBaseContext(), "Gps is turned on!! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}


