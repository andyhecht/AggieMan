package anh2772.slenderman;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by AndyHecht on 11/10/2016.
 */
public class Augmented extends AppCompatActivity implements SensorEventListener, LocationListener {

    private Camera mCamera = null;
    private CameraView mCameraView = null;
    private ImageView sman;
    private ImageView note;
    private int noteCount = 0;
    private MediaPlayer player; // music player for the slenderman music
    private int i;
    private Timer t;
    private Timer tt;
    private Timer ttt;
    private ImageView staticImage;
    private int count = 0;
    int ii = 0;
    double time = 10000;
    private int nextSman = 16000;
    private boolean showSman = false;
    private Random rr;
    private int rand;
    private Random r;
    private int random;
    private boolean noteReady = true;
    private LocationManager locationManager;
    private Location startingLocation;
    private Location currentLocation;
    private boolean isLocationOn;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private LocationListener locationListener;


    // The minimum distance to change Updates in meters
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = (float)0.5; // 0.5 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000; // 1 second


    // device sensor manager
    private SensorManager mSensorManager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.augmented_layout);

        isLocationOn = false;
        currentLocation = getLocation2();
        System.out.println("LOCATION: " + currentLocation.getLatitude() + " and " + currentLocation.getLongitude());
        startingLocation = currentLocation;

        Intent activityThatCalled = getIntent();

        sman = (ImageView) findViewById(R.id.sman);
        sman.setBackgroundResource(R.drawable.slenderman);

        note = (ImageView) findViewById(R.id.note);
        note.setBackgroundResource(R.drawable.notes_small);

        noteCount = 0;
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (note.isShown()) {
                    noteCount++;
                    if (noteCount == 1) {
                        Toast.makeText(getApplicationContext(), noteCount + " NOTE COLLECTED!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), noteCount + " NOTES COLLECTED!", Toast.LENGTH_SHORT).show();
                    }

                    rr = new Random(); //move note
                    rand = rr.nextInt(355);

                }
            }
        });

        staticImage = (ImageView) findViewById(R.id.staticImage);

        if(player != null)player.stop();
        if(player != null)player.release();
        player = MediaPlayer.create(getApplicationContext(), R.raw.music);
        player.setLooping(true);
        player.start();

        tt = new Timer();
        slenderManTimer(tt);

        rr = new Random(); //set first note
        rand = rr.nextInt(355);


        r = new Random(); //set slenderman
        random = r.nextInt(320);

        try {
            mCamera = Camera.open();//you can use open(int) to use different cameras
            Camera.Parameters parameters = mCamera.getParameters();
            System.out.println("ROTATING!!!!");
            parameters.set("orientation", "portrait");
            parameters.setRotation(90);
            mCamera.setParameters(parameters);
            System.out.println("SET DISPLAY ORIENTATION!!!!");
            mCamera.setDisplayOrientation(180);
        } catch (Exception e) {
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if (mCamera != null) {
            mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
            FrameLayout camera_view = (FrameLayout) findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout
        }

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //btn to close the application
//        ImageButton imgClose = (ImageButton)findViewById(R.id.imgClose);
//        imgClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.exit(0);
//            }
//        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onLocationChanged(final Location location) {
        //your code here
        System.out.println("LOCATION CHANGED");
        currentLocation = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public Location getLocation2() {
        Location location = currentLocation;
        try {
            locationManager = (LocationManager) getApplicationContext()
                    .getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                Log.d("GPS Enabled", "GPS Enabled");
                if (locationManager != null) {
                    isLocationOn = true;
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    private void startStatic(final Timer timer) {
        i = 0;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (i >= 30) {
                            timer.cancel();
                            timer.purge();
                            showSman = false;
                            staticImage.setVisibility(View.INVISIBLE);
                            count = 0;

                            r = new Random(); //reset slenderman
                            random = r.nextInt(320);

                            player.stop();
                            player.release();
                            player = MediaPlayer.create(getApplicationContext(), R.raw.music);
                            player.setLooping(true);
                            player.start();
                            return;
                        }
                        if (i % 2 == 0) {
                            staticImage.setBackgroundColor(Color.WHITE);
                        } else {
                            staticImage.setBackgroundColor(Color.BLACK);
                        }
                        i++;
                    }
                });
            }
        }, 80, 80);
    }

    private void slenderManTimer(final Timer timer) {

        int cnt = 0;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showSman = true;
                        System.out.println("time elapsed:" + nextSman);
                    }
                });
            }
        }, nextSman, 600000);
    }

    private void noteTimer(final Timer timer) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noteReady = true;
                    }
                });
            }
        }, 10000, 600000);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        if(isLocationOn){
            float distance = startingLocation.distanceTo(currentLocation);
            System.out.println("Distance: " + distance);
        }

        if (noteCount == 10) {
            Toast.makeText(getApplicationContext(), "You won!", Toast.LENGTH_LONG).show();
            endGame();
            return;
        }

        if (nextSman <= 10000) {
            player = MediaPlayer.create(getApplicationContext(), R.raw.scream);
            player.setLooping(false);
            player.start();

            Toast.makeText(getApplicationContext(), "You died...", Toast.LENGTH_LONG).show();
            endGame();
            return;
        }

        if (noteReady && (degree >= rand) && (degree <= rand + 5)) {
            noteReady = false;

            ttt.cancel();
            ttt.purge();
            ttt = new Timer();
            noteTimer(ttt);

            note.setImageAlpha(255);
            note.setVisibility(View.VISIBLE);
        } else {
            note.setVisibility(View.INVISIBLE);
        }

        if (degree >= random && (degree <= random + 40) && showSman) {
            sman.setImageAlpha(255);
            sman.setVisibility(View.VISIBLE);
            if (count == 0) {
                count++;

                staticImage.setVisibility(View.VISIBLE);

                player.stop();
                player.release();
                player = MediaPlayer.create(getApplicationContext(), R.raw.staticnoise);
                player.setLooping(false);
                player.start();

                i = 0;
                t = new Timer();
                startStatic(t);

                tt.cancel();
                tt.purge();
                tt = new Timer();
                nextSman = (int) ((0.90) * nextSman);
                slenderManTimer(tt);
            }
        } else {
            sman.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    public void endGame() {
        System.out.println("ended");
        if(t != null){
            t.cancel();
            t.purge();
        }
        if(tt != null){
            tt.cancel();
            tt.purge();
        }
        if(ttt != null){
            ttt.cancel();
            ttt.purge();
        }
        player.stop();
        Toast.makeText(getApplicationContext(), "GAME ENDED!", Toast.LENGTH_LONG).show();
//        // end intent/activity
//        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Augmented Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://anh2772.slenderman/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Augmented Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://anh2772.slenderman/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
