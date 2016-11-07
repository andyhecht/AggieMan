package anh2772.slenderman;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by AndyHecht on 10/23/2016.
 */
public class PlayGame extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    TextView text;
    Integer collectedNotesCount; // # of notes collected so far
    private Marker uMarker; // marker of the user - has position of user
    private Marker sMarker; // marker of slenderman - has position of slenderman
    private Handler sHandler; // handler for moving slenderman
    private MediaPlayer player; // music player for the slenderman music
    private Double sDist; // distance slenderman is from user
    private Float zoom; // current zoom on googlemap
    protected GoogleMap gMap; // google map
    private Fog fog;
    private Integer direction;
    private Boolean changeDirection;
    private Marker[] notes;
    private int i;
    private Timer t;
    private ImageView staticImage;

    // runnable for slenderman movement - moves random position every 2 seconds
    private Runnable sR = new Runnable() {
        @Override public void run() {
            randomizeSlenderMan();
            if(sHandler != null) {
                sHandler.postDelayed(this, 2000);
            }
        }
    };

    // when user movement button held down, move every .1 second until user releases button
    // http://stackoverflow.com/questions/10511423/android-repeat-action-on-pressing-and-holding-a-button
    private View.OnTouchListener tl = new View.OnTouchListener() {

        Handler h = new Handler();
        View view;
        private Runnable r = new Runnable() {
            @Override public void run() {
                switch (view.getId()) {
                    case R.id.move_up:
                        updateMarker(1);
                        break;
                    case R.id.move_down:
                        updateMarker(3);
                        break;
                    case R.id.move_left:
                        updateMarker(0);
                        break;
                    case R.id.move_right:
                        updateMarker(2);
                        break;
                    default:
                        break;
                }
                h.postDelayed(this, 100);
            }
        };


        // if user is pressing button, check if pressing down or releasing - stop timer if released
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            this.view = v;
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                if (h != null) return true;
                h = new Handler();
                h.postDelayed(r, 100);
            } else{
                if (h == null) return true;
                h.removeCallbacks(r);
                h = null;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // initialize variables
        sDist = 0.02;
        this.zoom = 18.0f;
        collectedNotesCount = 0;
        text = (TextView)findViewById(R.id.text);
        sHandler = new Handler();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        this.direction = 0;
        this.changeDirection = true;
        notes = new Marker[8];

        this.fog = (Fog) findViewById(R.id.fog);
        this.fog.invalidate();

        //initialize imageView as transparent
        staticImage = (ImageView) findViewById(R.id.staticImage);

        // create map
        mapFragment.getMapAsync(this);

        // set user movement buttons to listen for touches/presses
        setMovementTouchListeners();

        // Get the Intent that called for this Activity to open
        Intent activityThatCalled = getIntent();

        // Get the data that was sent
        Bundle callingBundle = activityThatCalled.getExtras();
        if( callingBundle != null ) {
        //            String extra = callingBundle.getString("callingActivity");
        //            text.setText(extra);
        }

        player = MediaPlayer.create(this, R.raw.music);
        player.setLooping(true);
        player.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }

    // do nothing if long click
    @Override
    public void onMapLongClick(LatLng latLng) {
        System.out.println("Long clicking.");
    }

    // create map and start game.
    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.gMap = googleMap;

        this.gMap.getUiSettings().setScrollGesturesEnabled(false);
        this.gMap.getUiSettings().setZoomGesturesEnabled(false);

        // create listeners for map
        this.gMap.setOnMapLongClickListener(this);
        this.gMap.setOnMarkerClickListener(this);
        this.gMap.setMyLocationEnabled(true);
        // http://stackoverflow.com/questions/13756261/how-to-get-the-current-location-in-google-maps-android-api-v2
        GoogleMap.OnMyLocationChangeListener locCL = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                LatLng sLoc = new LatLng(loc.latitude - sDist, loc.longitude - sDist);
                uMarker.setPosition(loc);
                sMarker.setPosition(sLoc);
                if(gMap != null){
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, zoom));
                }
            }
        };
        this.gMap.setOnMyLocationChangeListener(locCL);

        // starting position of user - Austin Texas pretty much
        LatLng start = new LatLng(30.286303, -97.737115);

        // starting position of slenderman - next to user a distance away
        LatLng slenderPos = new LatLng(start.latitude - sDist, start.longitude - sDist);

        // add user and slenderman to map as markers
        this.uMarker = gMap.addMarker(new MarkerOptions().position(start).title
                ("Marker in Austin").icon(BitmapDescriptorFactory.fromBitmap(resizeIcon("person", 100, 100))));
        this.sMarker = gMap.addMarker(new MarkerOptions().position(slenderPos).title
                ("Marker of slenderman").icon(BitmapDescriptorFactory.fromBitmap(resizeIcon("slenderman", 100, 100))));

        // zoom on user position
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, this.zoom));

        // populate map with notes at randomized positions
        for(int i = 0; i < 8; i++){
            notes[i] = setRandomNote(0.01);
            notes[i].setVisible(false);
        }

        for(int i = 0; i < 8; i++){
            Marker fake = setRandomNote(0.01);
            fake.setVisible(false);
        }

        // start slenderman movement
        sHandler.postDelayed(sR,2000);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        // true if clicked on a note
        if(!this.uMarker.equals(marker) && !this.sMarker.equals(marker)) {

            Double distance = Math.pow(Math.pow(marker.getPosition().latitude - uMarker.getPosition().latitude, 2)
                    + Math.pow(marker.getPosition().longitude - uMarker.getPosition().longitude, 2), 0.5);

            Boolean real = false;

            for(int i = 0; i < notes.length; i++){
                if(notes[i].equals(marker)){
                    real = true;
                    break;
                }
            }

            // true if note is close enough to collect
            if(real && distance <= 0.000413 ) {
                // remove note update counter
                marker.setVisible(false);
                collectedNotesCount += 1;
                Toast.makeText(this, "NOTE COLLECTED!", Toast.LENGTH_SHORT).show();

                // if all notes collected, end game - you win
                if(collectedNotesCount == 10){
                    Toast.makeText(this, "YOU WIN!", Toast.LENGTH_SHORT).show();
                    endGame();
                }
            } else if(distance <= 0.000413){
                Integer note = 0;
                for(int i = 0; i < notes.length; i++) {
                    if(marker.getPosition().latitude == notes[i].getPosition().latitude &&
                            marker.getPosition().longitude == notes[i].getPosition().longitude){
                        real = true;
                        note = i;
                        break;
                    }
                }
                marker.setVisible(false);
                if(real){
                    notes[note].setVisible(true);
                    Toast.makeText(this, "FOUND NOTE!", Toast.LENGTH_SHORT).show();
                }
            } else{
                // note is too far away - let them know how far away it is.
                Toast.makeText(this, "Note too far away to collect. Distance : "
                        + (distance - 0.000213), Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }


    // resize marker icons so they are uniform size
    // http://stackoverflow.com/questions/14851641/change-marker-size-in-google-maps-api-v2
    public Bitmap resizeIcon(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    // user pressed + button - zoom in
    public void zoomInPressed(View view) {
        this.gMap.animateCamera(CameraUpdateFactory.zoomIn());
        zoom += 1;
    }

    // user pressed - button - zoom out
    public void zoomOutPressed(View view) {
        this.gMap.animateCamera(CameraUpdateFactory.zoomOut());
        zoom -= 1;
    }

    // update user position based on button pressed
    public void updateMarker(int direction){
        Double latitude = 0.0;
        Double longitude = 0.0;
        if (direction < 0 || direction >= 4){
            return;
        }

        if(this.direction != direction){
            changeDirection = true;
        }

        if (direction == 0){
            // left
            this.direction = 0;
            longitude = -0.00005;
        }
        else if (direction == 1){
            // up
            this.direction = 1;
            latitude = 0.00005;
        }
        else if (direction == 2){
            // right
            this.direction = 2;
            longitude = 0.00005;
        }
        else {
            // down
            this.direction = 3;
            latitude = -0.00005;
        }

        if(changeDirection){
            updateFog(this.direction);
            randomizeSlenderMan();
            changeDirection = false;
        }

        // update User position to new position
        LatLng loc = new LatLng(uMarker.getPosition().latitude + latitude, uMarker.getPosition().longitude + longitude);
        this.uMarker.setPosition(loc);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, this.zoom));
    }

    // randomize slenderman's position - but slowly randomize slenderman closer to the user
    private void randomizeSlenderMan(){

        Random rand = new Random();

        // randomizes whether position or negative offset
        int sign = rand.nextInt(3) + 1;
        if(sign == 1 || sign == 2){
            sign = -1;
        } else{
            sign = 1;
        }

        // randomly adds or subtracts distance between slenderman and user - odds are in favor of
        // subtracting
        sDist += ((rand.nextInt(20))*sign)*0.00008;

        // new position of slenderman
        Double latitude = uMarker.getPosition().latitude + rand.nextDouble()*sDist*sign;
        Double longitude = uMarker.getPosition().longitude + rand.nextDouble()*sDist*sign;
        LatLng sLoc = new LatLng(latitude, longitude);

        // update position of slenderman
        this.sMarker.setPosition(sLoc);

        // if slenderman is close enough to user, kill user and end game
        if(getDistanceBetweenItandU(sMarker) < 0.0001 || sDist < 0) {
            sDist = 0.0;

            i = 0;
            t = new Timer();
            scheduleTimer(t);
        } else if(getDistanceBetweenItandU(sMarker) < 0.001){
            t = new Timer();
            signalEnemyIsNear(t);
        }

        System.out.println("sDist = " + sDist);
    }

    private void scheduleTimer(final Timer timer){
        i = 0;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if( i == 5) {
                            player.stop();
                            player = MediaPlayer.create(getApplicationContext(), R.raw.scream);
                            player.setLooping(false);
                            player.start();
                        }
                        if (i >= 30) {
                            timer.cancel();
                            timer.purge();
                            Toast.makeText(getApplicationContext(), "You died...", Toast.LENGTH_LONG).show();
                            endGame();
                            return;
                        }
                        if(i%2 == 0) {
                            staticImage.setBackgroundColor(Color.WHITE);
                        }else{
                            staticImage.setBackgroundColor(Color.BLACK);
                        }
                        i++;
                    }
                });
            }
        }, 80,80);
    }

    private void signalEnemyIsNear(final Timer timer){
        i = 0;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if( i == 0) {
                            player.stop();
                            player = MediaPlayer.create(getApplicationContext(), R.raw.staticnoise);
                            player.setLooping(false);
                            player.start();
                        }
                        if (i >= 10) {
                            player.stop();
                            timer.cancel();
                            timer.purge();
                            staticImage.setBackgroundColor(Color.TRANSPARENT);
                            Toast.makeText(getApplicationContext(), "RUN!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(i%2 == 0) {
                            staticImage.setBackgroundColor(Color.WHITE);
                        }else{
                            staticImage.setBackgroundColor(Color.BLACK);
                        }
                        i++;
                    }
                });
            }
        }, 80,80);
    }

    // set up touch listeners of the movement buttons
    private void setMovementTouchListeners(){
        ImageView moveUpBut = (ImageView) findViewById(R.id.move_up);
        ImageView moveDownBut = (ImageView) findViewById(R.id.move_down);
        ImageView moveLeftBut = (ImageView) findViewById(R.id.move_left);
        ImageView moveRightBut = (ImageView) findViewById(R.id.move_right);
        moveUpBut.setOnTouchListener(tl);
        moveDownBut.setOnTouchListener(tl);
        moveLeftBut.setOnTouchListener(tl);
        moveRightBut.setOnTouchListener(tl);
    }

    // will place a note randomly on the map given limit (the distance from the user)
    private Marker setRandomNote(Double limit){
        Random rand = new Random();

        // randomizes add or subtract from user position
        int sign = rand.nextInt(3) + 1;
        if(sign == 1 || sign == 2){
            sign = -1;
        } else{
            sign = 1;
        }

        // position of note
        Double latitude = uMarker.getPosition().latitude + rand.nextDouble()*limit*sign;
        Double longitude = uMarker.getPosition().longitude + rand.nextDouble()*limit*sign;

        gMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon
                (BitmapDescriptorFactory.fromBitmap(resizeIcon("circle", 100, 100))));

        // add note to map
        return gMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon
                (BitmapDescriptorFactory.fromBitmap(resizeIcon("notes", 75, 75))));
    }

    // calculates distance from user to marker m.
    public Double getDistanceBetweenItandU(Marker m){
        return Math.pow(Math.pow(m.getPosition().latitude - uMarker.getPosition().latitude, 2)
                + Math.pow(m.getPosition().longitude - uMarker.getPosition().longitude, 2), 0.5);
    }

    // end the game, you're finished, hasta luego.
    public void endGame(){
        // stop slenderman movement
        if(sHandler != null) sHandler.removeCallbacks(sR);
        sHandler = null;

        // stop music
        player.stop();

        // end intent/activity
        finish();
    }

    private void updateFog(Integer orientation){
        this.fog.updateFogPosition(orientation);
        this.fog.invalidate();
    }
}
