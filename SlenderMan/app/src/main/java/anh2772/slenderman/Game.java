package anh2772.slenderman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by AndyHecht on 10/23/2016.
 */
public class Game extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    protected GoogleMap gMap; // google map

    private MusicManager mm;
    private Notes notes;
    private User user;
    private SlenderMan sm;
    private Controls c;
    private Boolean easy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // initialize variables
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        // create map
        mapFragment.getMapAsync(this);

        // Get the Intent that called for this Activity to open
        Intent activityThatCalled = getIntent();

        // Get the data that was sent
        Bundle callingBundle = activityThatCalled.getBundleExtra("game");
        if( callingBundle != null ) {
            this.easy = callingBundle.getBoolean("easy");
        }

        this.mm = new MusicManager(this, this);
        this.mm.startGameMusic();
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

        Integer noteCount = 8;

        if (this.easy){
            noteCount = 5;
        }

        // for tracking the movement of the user with their phone GPS.
        // http://stackoverflow.com/questions/13756261/how-to-get-the-current-location-in-google-maps-android-api-v2
//        GoogleMap.OnMyLocationChangeListener locCL = new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//                LatLng sLoc = new LatLng(loc.latitude - sDist, loc.longitude - sDist);
//                uMarker.setPosition(loc);
//                sMarker.setPosition(sLoc);
//                if(gMap != null){
//                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, zoom));
//                }
//            }
//        };
//        this.gMap.setOnMyLocationChangeListener(locCL);

        // starting position of user - Austin Texas pretty much
        LatLng start = new LatLng(30.286303, -97.737115);

        // starting position of slenderman - next to user a distance away
        LatLng slenderPos = new LatLng(start.latitude - 0.02, start.longitude - 0.02);

        // add user and slenderman to map as markers
        this.user = new User(this, this.gMap, start, this);
        this.sm = new SlenderMan(this, this.gMap, slenderPos, this);

        // add user tools in game
        this.c = new Controls(this);

        // add players' game dependencies
        this.user.setManagers(this.sm, this.c);
        this.sm.setManagers(this.user, this.mm);

        // populate map with notes at randomized positions
        this.notes = new Notes(this, this.gMap, this.user.getMarker(), this.sm.getMarker());
        this.notes.generateNotes(noteCount);

        // start slenderman movement
        this.sm.startHandler();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Boolean isWinner = this.notes.onClick(marker);
        if(isWinner){
            endGame();
        }
        return true;
    }

    // user pressed + button - zoom in
    public void zoomInPressed(View view) {
        this.gMap.animateCamera(CameraUpdateFactory.zoomIn());
        this.user.updateZoom(1);
    }

    // user pressed - button - zoom out
    public void zoomOutPressed(View view) {
        this.gMap.animateCamera(CameraUpdateFactory.zoomOut());
        this.user.updateZoom(-1);
    }

    // end the game, you're finished, hasta luego.
    public void endGame(){
        this.sm.stopHandler();

        // end intent/activity
        setResult(RESULT_OK);
        finish();
    }
}