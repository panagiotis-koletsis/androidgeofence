package gr.hua.dit.android.geofencetserpes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;


import androidx.room.Room;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.sql.Timestamp;
import java.util.List;

import gr.hua.dit.android.geofencetserpes.sqLite.GeoData;
import gr.hua.dit.android.geofencetserpes.sqLite.GeoDataDao;
import gr.hua.dit.android.geofencetserpes.sqLite.GeoDataDatabase;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private String TAG ="GeofenceBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e("BroadcastReceiver", errorMessage);
            return;
        }

            GeoDataDatabase database = Room.databaseBuilder(context.getApplicationContext(),GeoDataDatabase.class,"GeoData_DB").build();
            GeoDataDao geoDataDao = database.GeoDataDao();
            int transitionType = geofencingEvent.getGeofenceTransition();

            Location location = geofencingEvent.getTriggeringLocation();
            int transitionTypeDb = geofencingEvent.getGeofenceTransition();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            //Toast.makeText(context, ""+timestamp.getTime()+"", Toast.LENGTH_SHORT).show();



        if (geofencingEvent.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_ENTER ){
            Log.d("BroadcastReceiver","entered in geofence");
            Toast.makeText(context,"enter",Toast.LENGTH_SHORT).show();
            new Thread(()->{
                GeoData geoData = new GeoData(location.getLatitude(),location.getLongitude(),"enter",timestamp.getTime());
                geoDataDao.insertGeoData(geoData);
//                List<GeoData> geoD = geoDataDao.getAllGeoData();
//                for(int i=0; i<geoD.size();i++){
//                    Log.d(TAG,"database"+geoD.get(i).getLat()+geoD.get(i).getLng()+geoD.get(i).getAction()+geoD.get(i).getTimestamp());
//
//                }

            }).start();
        }
        if (geofencingEvent.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_EXIT ){
            Log.d("BroadcastReceiver","Exited in geofence");
            Toast.makeText(context,"exit",Toast.LENGTH_SHORT).show();
            new Thread(()->{
                GeoData geoData = new GeoData(location.getLatitude(),location.getLongitude(),"exit",timestamp.getTime());
                geoDataDao.insertGeoData(geoData);
//                    List<GeoData> geoD = geoDataDao.getAllGeoData();
//                    for(int i=0; i<geoD.size();i++){
//                        Log.d(TAG,"database"+geoD.get(i).getLat()+geoD.get(i).getLng()+geoD.get(i).getAction()+geoD.get(i).getTimestamp());
//
//                    }

            }).start();
        }
    }
}