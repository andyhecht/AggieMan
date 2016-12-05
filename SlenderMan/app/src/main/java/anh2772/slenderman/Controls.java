package anh2772.slenderman;

import android.app.Activity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Random;
import java.util.Timer;

/**
 * Created by jason on 11/7/2016.
 */

/**
 * Controls tools User has in game. - Flashlight.
 */
public class Controls {

    // declare Controls variables
    private Activity a;
    private Fog fog; // makes it seem like the user is using a flashlight

    public Controls(Activity a){
        this.a = a;

        // get fog object and render it
        this.fog = (Fog) this.a.findViewById(R.id.fog);
        this.fog.invalidate();
    }

    // update the direction the flashlight is facing
    public void updateFog(Integer orientation){
        this.fog.updateFogPosition(orientation);
        this.fog.invalidate();
    }
}
