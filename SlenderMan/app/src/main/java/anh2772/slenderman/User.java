package anh2772.slenderman;

import android.app.Activity;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jason on 11/7/2016.
 */

/**
 * User Player - controls and metadata of User
 */
public class User extends Player{

    private Float zoom; // current zoom on googlemap
    private Integer direction; // direction the user is facing
    private Boolean changeDirection; // true if the user has changed directions
    private Controls c; // flashlight and tools of the user
    private SlenderMan sm; // slenderman object

    // when user movement button held down, move every .1 second until user releases button
    // http://stackoverflow.com/questions/10511423/android-repeat-action-on-pressing-and-holding-a-button
    private View.OnTouchListener tl = new View.OnTouchListener() {

        Handler h = new Handler();
        View view;
        private Runnable r = new Runnable() {
            @Override public void run() {
                // move the user in specified direction
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
        super(a, gMap, p, g);

        // initialize variables
        this.direction = 0;
        this.zoom = 18.0f;
        this.changeDirection = true;

        // add user to the game
        createMarker("Marker in Austin", "person");
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                (this.m.getPosition(), this.zoom));

        // hide marker so we can render a static image
        this.m.setVisible(false);

        // set user movement buttons to listen for touches/presses
        setMovementTouchListeners();
    }

    // add slenderman and flashlight/tools to user, needs to be called after User is created
    public void setManagers(SlenderMan sm, Controls c){
        this.c = c;
        this.sm = sm;
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

        // true if direction value is out of scope
        if (direction < 0 || direction >= 4){
            return;
        }

        // true if there is a change of direction
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

        // update User position to new position, also update direction of flashlight
        LatLng loc = new LatLng(this.m.getPosition().latitude + latitude, this.m.getPosition().longitude + longitude);
        this.m.setPosition(loc);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, this.zoom));
    }

    // update the zoom on google maps
    public void updateZoom(Integer zoom){
        this.zoom += zoom;
    }


}
