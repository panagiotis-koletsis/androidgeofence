package gr.hua.dit.android.geofencetserpes.sqLite;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GeoDataDao {

    @Query("SELECT * FROM GEODATA")
    public List<GeoData> getAllGeoData();

//    @Query("SELECT * FROM GEODATA")
//    public Cursor getAllGeoData1();

    @Insert
    public void insertGeoData(GeoData geoData);
}
