package anh2772.slenderman;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by jason on 11/8/2016.
 *
 * Parent class for managing a player in the game.
 */
public class Player {

    protected Activity a;
    protected GoogleMap gMap; // google map of the game
    protected LatLng p; // position of the player
    protected Game g; // game object
    protected Marker m; // marker of the player

    public Player(Activity a, GoogleMap gMap, LatLng p, Game g){
        // initialize variables
        this.a = a;
        this.gMap = gMap;
        this.p = p;
        this.g = g;
    }


    // returns the marker of the player
    public Marker getMarker(){
        return this.m;
    }

    // creates a marker of the player and adds the marker ot the google map.
    protected void createMarker(String title, String image){
        this.m = gMap.addMarker(new MarkerOptions().position(p).title
                (title).icon(BitmapDescriptorFactory.fromBitmap
                (resizeIcon(image, 100, 100))));
    }

    // resize marker icons so they are uniform size
    // http://stackoverflow.com/questions/14851641/change-marker-size-in-google-maps-api-v2
    private Bitmap resizeIcon(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(this.a.getResources(),
                this.a.getResources().getIdentifier(iconName, "drawable", this.a.getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

}
