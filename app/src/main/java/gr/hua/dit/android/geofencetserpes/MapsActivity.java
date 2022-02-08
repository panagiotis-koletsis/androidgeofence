package gr.hua.dit.android.geofencetserpes;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import gr.hua.dit.android.geofencetserpes.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int GEOFENCING_FINE_LOCATION_PERMISSION_CODE = 3;
    private static final float GEOFENCE_RADIUS = 200;
    private static final int ENABLE_MYLOCATION_FINE_LOCATION_PERMISSION_CODE =4 ;
    private static final int BACKGROUND_LOCATION_REQUEST_CODE = 5;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    GeofencingClient geofencingClient;
    private final List<Geofence> geofenceList =new ArrayList<>();
    private PendingIntent geofencePendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //add api client interface
        geofencingClient = LocationServices.getGeofencingClient(MapsActivity.this);


    }

    private void addGeofences() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GEOFENCING_FINE_LOCATION_PERMISSION_CODE);
            return;
        }
        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
        .addOnSuccessListener(MapsActivity.this, unused -> {
            Log.d("GeofencingClient","Geofence succesfully added!");
        })
        .addOnFailureListener(MapsActivity.this,unused -> {
            Log.d("GeofencingClient","Geofence addition failed!");
        });
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        //initial trigger enter exit
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case GEOFENCING_FINE_LOCATION_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addGeofences();
                }
                break;
            case ENABLE_MYLOCATION_FINE_LOCATION_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                }
                break;
            case BACKGROUND_LOCATION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"You can add geofences",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in home and move the camera
        LatLng home = new LatLng(37.97534238174084, 23.760987199821525);
        mMap.addMarker(new MarkerOptions().position(home).title("Marker in home"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, 15));


        mMap.setOnMapLongClickListener(latLng -> {

            mMap.clear();
            geofenceList.add(new Geofence.Builder()
                    .setCircularRegion(latLng.latitude, latLng.longitude, GEOFENCE_RADIUS)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .setRequestId("Geofence_ID")
                    .build());

            CircleOptions circleOptions = new CircleOptions();
            circleOptions.radius(GEOFENCE_RADIUS);
            circleOptions.center(latLng);
            circleOptions.fillColor(Color.argb(100,100,100,100));
            mMap.addCircle(circleOptions);
            if (Build.VERSION.SDK_INT >= 29){
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    addGeofences();
                }else{
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},BACKGROUND_LOCATION_REQUEST_CODE);
                    }
//                    } else {
//                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},BACKGROUND_LOCATION_REQUEST_CODE);
//                    }
                        addGeofences();
                }
            }

            //addGeofences();
        });
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ENABLE_MYLOCATION_FINE_LOCATION_PERMISSION_CODE);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}