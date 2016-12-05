package anh2772.slenderman;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jason on 11/7/2016.
 */

/**
 * Manages music.
 */
public class MusicManager {

    private MediaPlayer player; // music player for the slenderman music
    private Activity main;
    private Integer i; // for keeping track of static animation
    private ImageView staticImage; // static animation view
    private Game g; // game object, allows us to get access to game attributes and properties

    public MusicManager(Activity a, Game g){
        // initialize variables
        this.main = a;
        staticImage = (ImageView) main.findViewById(R.id.staticImage);
        this.g = g;
    }

    public void startGameMusic(){
        // start the overall creepy game music
        player = MediaPlayer.create(this.main, R.raw.music);
        player.setLooping(true);
        player.start();
    }

    // does the static noise and animation
    public void startStaticNoise(final Timer timer){
        i = 0;

        // play static noise
        player.stop();
        player = MediaPlayer.create(main.getApplicationContext(), R.raw.staticnoise);
        player.setLooping(false);
        player.start();
        ImageView user = (ImageView) g.findViewById(R.id.person);
        user.setVisibility(View.INVISIBLE);

        // play the static animation in the view
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // end static noise and animation
                        if (i >= 10) {
                            player.stop();
                            timer.cancel();
                            timer.purge();
                            staticImage.setBackgroundColor(Color.TRANSPARENT);
                            ImageView user = (ImageView) g.findViewById(R.id.person);
                            user.setVisibility(View.VISIBLE);
                            Toast.makeText(main.getApplicationContext(), "RUN!", Toast.LENGTH_LONG).show();
                            startGameMusic();
                            return;
                        }
                        // switch between colors
                        if (i % 2 == 0) {
                            staticImage.setBackgroundColor(Color.WHITE);
                        } else {
                            staticImage.setBackgroundColor(Color.BLACK);
                        }
                        i++;
                    }
                });
            }
        }, 80,80);
    }

    // end of game noise and animation
    public void startScreamingNoise(final Timer timer){
        i = 0;

        // play screaming noise
        player.stop();
        player = MediaPlayer.create(main.getApplicationContext(), R.raw.scream);
        player.setLooping(false);
        player.start();
        ImageView user = (ImageView) g.findViewById(R.id.person);
        user.setVisibility(View.INVISIBLE);

        // play the static animation
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // end screaming, animation, and game
                        if (i >= 30) {
                            timer.cancel();
                            timer.purge();
                            player.stop();
                            g.endGame();
                            Toast.makeText(main.getApplicationContext(), "You died...", Toast.LENGTH_LONG).show();
                            return;
                        }
                        // switch between colors
                        if (i % 2 == 0) {
                            staticImage.setBackgroundColor(Color.WHITE);
                        } else {
                            staticImage.setBackgroundColor(Color.BLACK);
                        }
                        i++;
                    }
                });
            }
        }, 80,80);
    }
}
