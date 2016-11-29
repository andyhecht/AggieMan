package anh2772.slenderman;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by AndyHecht on 11/10/2016.
 */
public class Augmented extends AppCompatActivity implements SensorEventListener {

    //    private MusicManagerAugmented mm;
    private Camera mCamera = null;
    private CameraView mCameraView = null;
    private ImageView sman;
    private ImageView note;
    private int noteCount = 0;
    private MediaPlayer player; // music player for the slenderman music
    private int i;
    private Timer t;
    private Timer tt;
    private ImageView staticImage;
    private int count = 0;
    int ii = 0;
    double time = 10000;
    private int nextSman = 1600;
    private boolean showSman = false;
    private Random rr;
    private int rand;
    private Random r;
    private int random;

    // record the compass picture angle turned
    private float currentDegree = 0f;

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

        Intent activityThatCalled = getIntent();

        sman = (ImageView) findViewById(R.id.sman);
        sman.setBackgroundResource(R.drawable.slenderman);

        note = (ImageView) findViewById(R.id.note);
        note.setBackgroundResource(R.drawable.notes);
        note.setVisibility(View.INVISIBLE);

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
//                            System.out.println("!!! ii == time !!!");
//                            sman.setImageAlpha(255);
//                            sman.setVisibility(View.VISIBLE);
                        System.out.println("time elapsed:" + nextSman);
//                            time = time + nextSman;
                    }
                });
            }
        }, nextSman, 600000);
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

        if (noteCount == 10) {
            Toast.makeText(getApplicationContext(), "You won!", Toast.LENGTH_LONG).show();
            endGame();
            return;
        }

        if (nextSman <= 10000) {
//            t = new Timer();
//            startStatic(t);
//            player.stop();
//            player.release();
            player = MediaPlayer.create(getApplicationContext(), R.raw.scream);
            player.setLooping(false);
            player.start();

            Toast.makeText(getApplicationContext(), "You died...", Toast.LENGTH_LONG).show();
            endGame();
            return;
        }

        if (degree >= rand && (degree <= rand + 5)) {
            note.setImageAlpha(255);
            note.setVisibility(View.VISIBLE);
        } else {
            note.setVisibility(View.INVISIBLE);
        }

//        System.out.println("Random #:  " + random +  " and showSman: " + showSman);

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
        currentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    public void endGame() {
        System.out.println("ended");
        tt.cancel();
        t.cancel();
        tt.purge();
        t.purge();
        player.stop();
        Toast.makeText(getApplicationContext(), "GAME ENDED!", Toast.LENGTH_LONG).show();
        // end intent/activity
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
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
