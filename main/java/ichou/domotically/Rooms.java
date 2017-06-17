package ichou.domotically;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HP I7 on 01/05/2017.
 */

public class Rooms {
    @SerializedName("humidity")
    public float humidity;
    @SerializedName("temperature")
    public float temperature;
    @SerializedName("surface")
    public float surface;
    @SerializedName("room_name")
    public String room_name;
    @SerializedName("volume")
    public float volume;




}
