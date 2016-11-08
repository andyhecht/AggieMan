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

    private Activity a;
    private Fog fog;

    public Controls(Activity a){
        this.a = a;
        this.fog = (Fog) this.a.findViewById(R.id.fog);
        this.fog.invalidate();
    }

    public void updateFog(Integer orientation){
        this.fog.updateFogPosition(orientation);
        this.fog.invalidate();
    }
}
