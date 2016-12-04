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

public class MainActivity extends AppCompatActivity {

    //<https://pixabay.com/en/walkers-autumn-fog-man-human-mood-486583/>
    Button startGame;
    Button continueGame;
    private boolean hasBeenStarted = false;
    private int i;
    private MediaPlayer player;
    ImageView staticImage;
    private Boolean ar;
    private Boolean easy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ar = false;
        easy = false;

        startGame = (Button)findViewById(R.id.startGame);
        continueGame = (Button)findViewById(R.id.continueGame);

        startGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hasBeenStarted = true;
                playGame();
            }
        });
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

        staticImage = (ImageView)findViewById(R.id.staticImage);
        player = new MediaPlayer();
    }

    public void playGame(){
        Intent intent = new Intent();
        if (!ar) {
            intent = new Intent(getApplicationContext(), Game.class);
            Bundle extras = new Bundle();
            extras.putBoolean("easy", this.easy);
            intent.putExtra("game", extras);
        }else{
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

    public void startStaticNoise(final Timer timer){
        i = 0;

        player.stop();
        player = MediaPlayer.create(getApplicationContext(), R.raw.staticnoise);
        player.setLooping(false);
        player.start();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
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
        if(call == 0){
            extras = data.getBundleExtra("settings");
            easy = extras.getBoolean("easy", false);
            ar = extras.getBoolean("ar", false);
            System.out.println("easy = " + this.easy + ", ar = "+ this.ar);
        }else if(call == 1){
            extras = data.getBundleExtra("game");
            System.out.println("Exited game.");
        }
    }


}

