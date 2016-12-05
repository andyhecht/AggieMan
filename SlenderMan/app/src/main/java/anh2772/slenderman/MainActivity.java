package anh2772.slenderman;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/*
 * Main activity for the app - displays splashview and allows user to play game or update settings
 */
public class MainActivity extends AppCompatActivity {

    //<https://pixabay.com/en/walkers-autumn-fog-man-human-mood-486583/>
    Button startGame; // new game button
    Button continueGame; // continue game button
    private boolean hasBeenStarted = false; // true if game has started
    private int i;
    private MediaPlayer player; // music player
    ImageView staticImage; // for static animation
    private Boolean ar; // true if augmented reality is enabled in settings
    private Boolean easy; // true if easy mode is enabled in settings

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialized setting variables
        ar = false;
        easy = false;

        startGame = (Button)findViewById(R.id.startGame);
        continueGame = (Button)findViewById(R.id.continueGame);

        // if new game button clicked, start new game
        startGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hasBeenStarted = true;
                playGame();
            }
        });

        // if continue game button clicked, do nothing unless game has already been started
        continueGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(hasBeenStarted){
                    playGame();
                }else{
                    Toast.makeText(getApplicationContext(), "Please start a new game.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // initialize music player and static animation
        staticImage = (ImageView)findViewById(R.id.staticImage);
        player = new MediaPlayer();
    }

    public void playGame(){

        Intent intent = new Intent();

        // true if augmented reality disabled in settings
        if (!ar) {
            // play 2d SlenderMan
            intent = new Intent(getApplicationContext(), Warning.class);
            Bundle extras = new Bundle();
            extras.putBoolean("easy", this.easy);
            intent.putExtra("game", extras);
        }else{
            // play ar SlenderMan
            intent = new Intent(getApplicationContext(), Augmented.class);
        }

        startActivity(intent);
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

                // if settings button clicked, go to the Settings activity
                startStaticNoise(new Timer());
                Intent intent = new Intent(getApplicationContext(),Settings.class);
                Bundle extras = new Bundle();
                extras.putBoolean("easy", this.easy);
                extras.putBoolean("ar", this.ar);
                intent.putExtra("callingSetting", extras);
                final int result = 1;
                startActivityForResult(intent, result);
                break;
        }

        return true;
    }

    // static animation
    public void startStaticNoise(final Timer timer){
        i = 0;

        // plays static noise
        player.stop();
        player = MediaPlayer.create(getApplicationContext(), R.raw.staticnoise);
        player.setLooping(false);
        player.start();

        // performs the actual static animation
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    // switches back and forth between colors
                    @Override
                    public void run() {
                        if (i >= 10) {
                            player.stop();
                            timer.cancel();
                            timer.purge();
                            staticImage.setBackgroundColor(Color.TRANSPARENT);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras;
        Integer call = data.getIntExtra("intent", 0);
        System.out.println("call = " + call);

        // true if returning from setting activity
        if(call == 0){
            // update setting variables
            extras = data.getBundleExtra("settings");
            easy = extras.getBoolean("easy", false);
            ar = extras.getBoolean("ar", false);
            System.out.println("easy = " + this.easy + ", ar = "+ this.ar);
        }else if(call == 1){
            // game has ended, so do nothing for now.
            extras = data.getBundleExtra("game_over");
            System.out.println("Exited game.");
        }
    }


}

