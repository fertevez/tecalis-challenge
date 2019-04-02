package fecasado.citius.usc.tecalischallenge;

public class SensorData {

    private long timestamp;
    private float accx;
    private float accy;
    private float accz;
    private float gyrox;
    private float gyroy;
    private float gyroz;
    private float lat;
    private float lng;

    public SensorData(long timestamp, float accx, float accy, float accz, float gyrox, float gyroy, float gyroz) {
        this.timestamp = timestamp;
        this.accx = accx;
        this.accy = accy;
        this.accz = accz;
        this.gyrox = gyrox;
        this.gyroy = gyroy;
        this.gyroz = gyroz;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float getAccx() {
        return accx;
    }

    public float getAccy() {
        return accy;
    }

    public float getAccz() {
        return accz;
    }

    public float getGyrox() {
        return gyrox;
    }

    public float getGyroy() {
        return gyroy;
    }

    public float getGyroz() {
        return gyroz;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }
}
