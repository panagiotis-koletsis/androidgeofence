package gr.hua.dit.android.geofencetserpes.sqLite;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {GeoData.class},version = 1)
public abstract class GeoDataDatabase extends RoomDatabase {
    public abstract GeoDataDao GeoDataDao();

}
