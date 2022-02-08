package gr.hua.dit.android.geofencetserpes;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import gr.hua.dit.android.geofencetserpes.sqLite.GeoData;
import gr.hua.dit.android.geofencetserpes.sqLite.GeoDataDao;
import gr.hua.dit.android.geofencetserpes.sqLite.GeoDataDatabase;

public class GeoDataProvider extends ContentProvider {
    private UriMatcher uriMatcher;
    private static final String AUTHORITY = "gr.hua.dit.android.geofencetserpes";
    private static final String PATH = "GEODATA";

    @Override
    public boolean onCreate() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,PATH,1);
        //uriMatcher.addURI(AUTHORITY,PATH+"/#",2);

        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        GeoDataDatabase database = Room.databaseBuilder(getContext(),GeoDataDatabase.class,"GeoData_DB").build();
        GeoDataDao geoDataDao = database.GeoDataDao();
        List<GeoData> geoDataList = new ArrayList<>();
        //Cursor cursor = null;
        switch(uriMatcher.match(uri)) {
            case 1:
                //select * from GEODATA;
                //Cursor cursor;
                //cursor =  geoDataDao.getAllGeoData1() ;
                geoDataList = geoDataDao.getAllGeoData();
                break;
//          case 2:
//                //select * from GEODATA where ROWID=?;
//                break;


        }
        return (Cursor) geoDataList;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
