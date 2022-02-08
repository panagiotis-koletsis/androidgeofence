package gr.hua.dit.android.geofencetserpes.sqLite;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "GEODATA")
public class GeoData {
    @ColumnInfo(name ="LAT")
    private double lat;

    @ColumnInfo(name = "LNG")
    private double lng;

    @ColumnInfo(name ="ACTION")
    private String action;

    @PrimaryKey
    @ColumnInfo(name = "TIMESTAMP")
    private long timestamp;

    public GeoData(double lat, double lng, String action, long timestamp) {
        this.lat = lat;
        this.lng = lng;
        this.action = action;
        this.timestamp = timestamp;
    }

    public GeoData() {

    }
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
