package ichou.domotically;

/**
 * Created by HP I7 on 08/02/2017.
 */

public class Arduino {
    String icon;
    String deviceName;
    String deviceMAC;

    public Arduino(String deviceMAC, String deviceName,String icon) {
        this.deviceMAC = deviceMAC;
        this.deviceName = deviceName;
        this.icon = icon;
    }

    public String getDeviceMAC() {
        return deviceMAC;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getIcon() {
       return icon;
    }

    public void setDeviceMAC(String deviceMAC) {
        this.deviceMAC = deviceMAC;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


}
