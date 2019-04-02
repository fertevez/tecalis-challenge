package fecasado.citius.usc.tecalischallenge;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity implements SensorSubscriberInterface {
    private String LOGTAG = "MainActivity";

    private final static short DIM = 7; // dimension of the chess board 7x7
    private final static String BASE_URL = "http://www.thomas-bayer.com/sqlrest/CUSTOMER/"; // customers data
    private final static short SENSOR_FREQ = 3; // take sensor data every 3 seconds

    private Square[] chessBoard = new Square[DIM * DIM]; // chess board

    private ArrayList<SensorData> sensorReadings; // sensor measurements
    SensorDataCollector dataCollector; // sensor data reader
    LocationManager locationManager; // GPS location manager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create chess board
        createLayoutDynamically();

        // Start reading sensors
        sensorReadings = new ArrayList<>();
        dataCollector = new SensorDataCollector(this, this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        dataCollector.registerListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataCollector.unregisterListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataCollector.registerListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataCollector.unregisterListeners();
    }

    /**
     * Create chess board dinamically
     */
    private void createLayoutDynamically() {
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.my_linear_layout);
        // Get square size based on display width
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int squareDim = dm.widthPixels / (DIM);

        for (int i = 0; i < DIM; i++) {
            // Create a new linear layout
            LinearLayout ll = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            parentLayout.setOrientation(VERTICAL);
            parentLayout.addView(ll, params);

            for (int j = 0; j < DIM; j++) {
                final int btnId = j + DIM * (i);
                int color;
                if ((btnId & 1) == 0) {
                    color = R.color.darkSquare;
                } else {
                    color = R.color.lightSquare;
                }

                // Create a new square
                final Square square = new Square(this, "" + btnId, color, squareDim);
                ll.addView(square);
                chessBoard[btnId] = square;

                // Define behaviour when it is clicked
                square.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!chessBoard[btnId].isActivated()) {
                            RetrieveXML getXML = new RetrieveXML(BASE_URL + btnId, btnId,
                                    chessBoard[btnId], MainActivity.super.getApplicationContext());
                            getXML.execute();
                        }
                    }
                });
            }
        }
    }


    /**
     * Save data to a .txt file
     */
    public void saveToTXT(View view) {
        if (isStoragePermissionGranted()) {
            try {
                File root = new File(Environment.getExternalStorageDirectory(), "TecalisData");
                if (!root.exists()) {
                    root.mkdirs();
                }
                File file = new File(root, System.currentTimeMillis() + ".txt");
                FileWriter writer = new FileWriter(file);

                writer.write("###############\n");
                writer.write("## CUSTOMERS ##\n");
                writer.write("###############\n");
                for (Square s : chessBoard) {
                    if (s.isActivated()) {
                        writer.write("> " + s.getCustomer().toString() + "\n\n");
                    }
                }
                writer.write("\n\n\n");
                writer.write("###############\n");
                writer.write("##  SENSORS  ##\n");
                writer.write("###############\n");
                writer.write("timestamp, lat, lng, gyrox, gyroy, gyroz, accx, accy, accz\n");
                for (SensorData s : sensorReadings) {
                    Log.d("AAA",  "--"  + s.getTimestamp());
                    writer.write(s.getTimestamp() + ", "
                            + s.getLat() + ", " + s.getLng() + ", "
                            + s.getGyrox() + ", " + s.getGyroy() + ", " + s.getGyroz() + ", "
                            + s.getAccx() + ", " + s.getAccy() + ", " + s.getAccz() + "\n");
                }

                writer.flush();
                writer.close();
            } catch (IOException e) {
                Log.e(LOGTAG, "File write failed: " + e.toString());
                Toast.makeText(this, "File write failed.", Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(this, "Data has been saved!", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Check if storage permission was granted
     */
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(LOGTAG, "Permission is granted");
                return true;
            } else {
                Log.v(LOGTAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(LOGTAG, "Permission is granted");
            return true;
        }
    }

    /**
     * Check if storage permission was granted
     */
    public boolean isLocationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                Log.v(LOGTAG, "Permission is granted");
                return true;
            } else {
                Log.v(LOGTAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(LOGTAG, "Permission is granted");
            return true;
        }
    }


    /**
     * Get sensor data
     */
    @Override
    public void onSensorDataChanged(SensorData sensorData) {
        // first reading
        if (sensorReadings.isEmpty()) {
            // get last known location
            Location location = getLastKnownLocation();
            if(location != null) {
                sensorData.setLat((float)location.getLatitude());
                sensorData.setLng((float)location.getLongitude());
                sensorReadings.add(sensorData);
            }
        } else { // check if it is time to save another reading
            // calculate time difference
            long timeDiff = sensorData.getTimestamp() - sensorReadings.get(sensorReadings.size() - 1).getTimestamp();
            // convert to seconds
            timeDiff = timeDiff / 1000000000;
            if (timeDiff >= SENSOR_FREQ) {
                // get last known location
                Location location = getLastKnownLocation();
                sensorData.setLat((float)location.getLatitude());
                sensorData.setLng((float)location.getLongitude());
                sensorReadings.add(sensorData);
            }
        }
    }


    @SuppressLint("MissingPermission")
    private Location getLastKnownLocation() {
        Location networkLocation = null;
        if (isLocationPermissionGranted())
            networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        return networkLocation;
    }
}
