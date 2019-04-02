package fecasado.citius.usc.tecalischallenge;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Sensor data collector
 */
public class SensorDataCollector implements SensorEventListener {

    private float[] lastAccData = null;
    private float[] lastGyroData = null;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyrometer;

    private SensorSubscriberInterface SensorSubscriberInterface;

    public SensorDataCollector(Context context, SensorSubscriberInterface SensorSubscriberInterface){
        this.SensorSubscriberInterface = SensorSubscriberInterface;

        sensorManager =(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyrometer = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

    }

    public void registerListeners(){
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyrometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void unregisterListeners(){
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            lastAccData = sensorEvent.values;
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            lastGyroData = sensorEvent.values;
        }

        // Send data to subscriber only if we have new measures of both sensors
        if (lastGyroData != null && lastAccData != null) {
            SensorData data = new SensorData(sensorEvent.timestamp,
                    lastAccData[0], lastAccData[1], lastAccData[2],
                    lastGyroData[0], lastGyroData[1], lastGyroData[2]);

            SensorSubscriberInterface.onSensorDataChanged(data);
            lastAccData = null;
            lastGyroData = null;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // do nothing
    }

}
