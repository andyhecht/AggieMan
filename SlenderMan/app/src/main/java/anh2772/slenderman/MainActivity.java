package anh2772.slenderman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //<https://pixabay.com/en/walkers-autumn-fog-man-human-mood-486583/>
    Button startGame;
    Button continueGame;
    private boolean hasBeenStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    Toast.makeText(getApplicationContext(), "Please start a game.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    public void playGame(){
        Intent intent = new Intent(getApplicationContext(),PlayGame.class);
//        Bundle myExtras = new Bundle();
//        myExtras.putString("callingActivity", "haaaaaaaaaaaaay");
//        intent.putExtras(myExtras);
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
                Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }


}

