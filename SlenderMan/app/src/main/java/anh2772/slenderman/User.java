package anh2772.slenderman;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by jason on 11/7/2016.
 */
public class User{

    private Activity a;
    private GoogleMap gMap;
    private LatLng p;
    private Marker uMarker; // marker of the user - has position of user
    private Float zoom; // current zoom on googlemap
    private Integer direction;
    private Boolean changeDirection;
    private Controls c;
    private Game g;
    private SlenderMan sm;

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

    public User(Activity a, GoogleMap gMap, LatLng p, Game g){
        this.a = a;
        this.gMap = gMap;
        this.zoom = 18.0f;
        this.p = p;
        this.direction = 0;
        this.changeDirection = true;
        this.g = g;
        createMarker();
    }

    public Marker getMarker(){
        return this.uMarker;
    }

    public void createMarker(){

        this.uMarker = gMap.addMarker(new MarkerOptions().position(p).title
                ("Marker in Austin").icon(BitmapDescriptorFactory.fromBitmap
                (resizeIcon("person", 100, 100))));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                (this.uMarker.getPosition(), this.zoom));

        // set user movement buttons to listen for touches/presses
        setMovementTouchListeners();
    }

    // resize marker icons so they are uniform size
    // http://stackoverflow.com/questions/14851641/change-marker-size-in-google-maps-api-v2
    private Bitmap resizeIcon(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(this.a.getResources(),
                this.a.getResources().getIdentifier(iconName, "drawable", this.a.getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    // set up touch listeners of the movement buttons
    private void setMovementTouchListeners(){
        ImageView moveUpBut = (ImageView) this.a.findViewById(R.id.move_up);
        ImageView moveDownBut = (ImageView) this.a.findViewById(R.id.move_down);
        ImageView moveLeftBut = (ImageView) this.a.findViewById(R.id.move_left);
        ImageView moveRightBut = (ImageView) this.a.findViewById(R.id.move_right);
        moveUpBut.setOnTouchListener(tl);
        moveDownBut.setOnTouchListener(tl);
        moveLeftBut.setOnTouchListener(tl);
        moveRightBut.setOnTouchListener(tl);
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
            this.c.updateFog(this.direction);
            this.sm.randomizeSlenderMan();
            changeDirection = false;
        }

        // update User position to new position
        LatLng loc = new LatLng(this.uMarker.getPosition().latitude + latitude, this.uMarker.getPosition().longitude + longitude);
        this.uMarker.setPosition(loc);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, this.zoom));
    }

    public void setManagers(SlenderMan sm, Controls c){
        this.c = c;
        this.sm = sm;
    }

    public void updateZoom(Integer zoom){
        this.zoom += zoom;
    }


}
