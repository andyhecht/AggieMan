package anh2772.slenderman;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.gms.location.FusedLocationProviderApi;

import java.util.Random;

/**
 * Created by AndyHecht on 10/23/2016.
 */
public class PlayGame extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    TextView text;

    private Marker uMarker;
    private Marker sMarker;
    private Double sDist;
    private Float zoom;

    protected GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        sDist = 0.001;
        this.zoom = 18.0f;

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        text = (TextView)findViewById(R.id.text);

        // Get the Intent that called for this Activity to open
        Intent activityThatCalled = getIntent();

        // Get the data that was sent
        Bundle callingBundle = activityThatCalled.getExtras();
        if( callingBundle != null ) {
        //            String extra = callingBundle.getString("callingActivity");
        //            text.setText(extra);
        }
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

    @Override
    public void onMapLongClick(LatLng latLng) {
        double lat = latLng.latitude;
        double lon = latLng.longitude;

        String positionText = "(" + lat + ", " + lon + ")";

//        MarkerOptions marker = new MarkerOptions().position(latLng).title(positionText);
//        gMap.addMarker(marker);
        uMarker.setPosition(latLng);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.gMap = googleMap;

        this.gMap.setOnMapLongClickListener(this);
        this.gMap.setOnMarkerClickListener(this);
        this.gMap.setMyLocationEnabled(true);

        LatLng start = new LatLng(30.286303, -97.737115);

//        LatLng slenderPos = new LatLng(30.286320, -97.737159);
        LatLng slenderPos = new LatLng(start.latitude - sDist, start.longitude - sDist);

        this.uMarker = gMap.addMarker(new MarkerOptions().position(start).title
                ("Marker in Austin").icon(BitmapDescriptorFactory.fromBitmap(resizeIcon("person", 100, 100))));
        this.sMarker = gMap.addMarker(new MarkerOptions().position(slenderPos).title
                ("Marker of slenderman").icon(BitmapDescriptorFactory.fromBitmap(resizeIcon("slenderman", 100, 100))));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, this.zoom));


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
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setVisible(false);

        return true;
    }

    // http://stackoverflow.com/questions/14851641/change-marker-size-in-google-maps-api-v2
    public Bitmap resizeIcon(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    public void zoomInPressed(View view) {
        this.gMap.animateCamera(CameraUpdateFactory.zoomIn());
        zoom += 1;
    }

    public void zoomOutPressed(View view) {
        this.gMap.animateCamera(CameraUpdateFactory.zoomOut());
        zoom -= 1;
    }

    public void moveLeft(View view){
        updateMarker(0);
    }
    public void moveUp(View view){
        updateMarker(1);
    }
    public void moveRight(View view){
        updateMarker(2);
    }
    public void moveDown(View view){
        updateMarker(3);
    }

    public void updateMarker(int direction){
        Double latitude = 0.0;
        Double longitude = 0.0;
        if (direction < 0 || direction >= 4){
            return;
        }
        else if (direction == 0){
            longitude = -0.00005;
        }
        else if (direction == 1){
            latitude = 0.00005;
        }
        else if (direction == 2){
            longitude = 0.00005;
        }
        else {
            latitude = -0.00005;
        }

        LatLng loc = new LatLng(uMarker.getPosition().latitude + latitude, uMarker.getPosition().longitude + longitude);
        randomizeSlenderMan();
        LatLng sLoc = new LatLng(loc.latitude - sDist, loc.longitude - sDist);
        this.uMarker.setPosition(loc);
        this.sMarker.setPosition(sLoc);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, this.zoom));
    }

    private void randomizeSlenderMan(){
        Random rand = new Random();
        int sign = rand.nextInt(3) + 1;
        if(sign == 1 || sign == 2){
            sign = -1;
        } else{
            sign = 1;
        }
        sDist += ((rand.nextInt(20))*sign)*0.000003;
        if(sDist < 0) {
            sDist = 0.0;
        }
        System.out.println("sDist = " + sDist);
    }
}
