package anh2772.slenderman;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jason on 11/7/2016.
 */
public class MusicManager {

    private MediaPlayer player; // music player for the slenderman music
    private Activity main;
    private Integer i;
    private ImageView staticImage;
    private Game g;

    public MusicManager(Activity a, Game g){
        this.main = a;
        staticImage = (ImageView) main.findViewById(R.id.staticImage);
        this.g = g;
    }

    public void startGameMusic(){
        player = MediaPlayer.create(this.main, R.raw.music);
        player.setLooping(true);
        player.start();
    }

    public void startStaticNoise(final Timer timer){
        i = 0;

        player.stop();
        player = MediaPlayer.create(main.getApplicationContext(), R.raw.staticnoise);
        player.setLooping(false);
        player.start();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (i >= 10) {
                            player.stop();
                            timer.cancel();
                            timer.purge();
                            staticImage.setBackgroundColor(Color.TRANSPARENT);
                            Toast.makeText(main.getApplicationContext(), "RUN!", Toast.LENGTH_LONG).show();
                            startGameMusic();
                            return;
                        }
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

    public void startScreamingNoise(final Timer timer){
        i = 0;

        player.stop();
        player = MediaPlayer.create(main.getApplicationContext(), R.raw.scream);
        player.setLooping(false);
        player.start();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (i >= 30) {
                            timer.cancel();
                            timer.purge();
                            player.stop();
                            g.endGame();
                            Toast.makeText(main.getApplicationContext(), "You died...", Toast.LENGTH_LONG).show();
                            return;
                        }
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
