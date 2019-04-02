package fecasado.citius.usc.tecalischallenge;


/**
 * You should implement this interface if you want to be notified of updates on sensor readings
 */
public interface SensorSubscriberInterface {
    void onSensorDataChanged(SensorData sensorData);
}
