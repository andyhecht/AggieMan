package anh2772.slenderman;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;
import java.util.Timer;

/**
 * Created by jason on 11/8/2016.
 */
public class SlenderMan {

    private Activity a;
    private GoogleMap gMap;
    private Marker sMarker; // marker of slenderman - has position of slenderman
    private LatLng p;
    private Double sDist; // distance slenderman is from user
    private User user;
    private MusicManager mm;
    private Handler sHandler; // handler for moving slenderman

    // runnable for slenderman movement - moves random position every 2 seconds
    private Runnable sR = new Runnable() {
        @Override public void run() {
            randomizeSlenderMan();
            if(sHandler != null) {
                sHandler.postDelayed(this, 2000);
            }
        }
    };

    public SlenderMan(Activity a, GoogleMap gMap, LatLng p, Game g){
        this.a = a;
        this.gMap = gMap;
        this.p = p;
        this.sDist = 0.02;
        sHandler = new Handler();
        createMarker();
    }

    private void createMarker(){
        this.sMarker = gMap.addMarker(new MarkerOptions().position(p).title
                ("Marker of slenderman").icon(BitmapDescriptorFactory.fromBitmap
                (resizeIcon("slenderman", 100, 100))));
    }

    public void setManagers(User user, MusicManager mm){
        this.user = user;
        this.mm = mm;
    }

    // resize marker icons so they are uniform size
    // http://stackoverflow.com/questions/14851641/change-marker-size-in-google-maps-api-v2
    private Bitmap resizeIcon(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(this.a.getResources(),
                this.a.getResources().getIdentifier(iconName, "drawable", this.a.getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    public Marker getMarker(){
        return sMarker;
    }

    // randomize slenderman's position - but slowly randomize slenderman closer to the user
    public void randomizeSlenderMan(){

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
        Double latitude = this.user.getMarker().getPosition().latitude + rand.nextDouble()*sDist*sign;
        Double longitude = this.user.getMarker().getPosition().longitude + rand.nextDouble()*sDist*sign;
        LatLng sLoc = new LatLng(latitude, longitude);

        // update position of slenderman
        sMarker.setPosition(sLoc);

        // if slenderman is close enough to user, kill user and end game
        if(getDistanceBetweenItandU(sMarker) < 0.0001 || sDist < 0) {
            sDist = 0.0;

            endGame();

        } else if(getDistanceBetweenItandU(sMarker) < 0.001){
            this.mm.startStaticNoise(new Timer());
        }

        System.out.println("sDist = " + sDist);
    }

    // calculates distance from user to marker m.
    private Double getDistanceBetweenItandU(Marker m){
        return Math.pow(Math.pow(m.getPosition().latitude - this.user.getMarker().getPosition().latitude, 2)
                + Math.pow(m.getPosition().longitude - this.user.getMarker().getPosition().longitude, 2), 0.5);
    }

    public void stopHandler(){
        // stop slenderman movement
        if(sHandler != null) sHandler.removeCallbacks(sR);
        sHandler = null;
    }

    public void startHandler(){
        // start slenderman movement
        sHandler.postDelayed(sR,2000);
    }

    private void endGame(){
        // stop music
        this.mm.startScreamingNoise(new Timer());
    }

}
