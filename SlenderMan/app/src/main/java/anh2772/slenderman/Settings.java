package anh2772.slenderman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by AndyHecht on 11/8/2016.
 *
 * activity that allows the user to edit the settings of the game.
 */

public class Settings extends AppCompatActivity {

    Switch augmentedReality; // switch to turn on or off augmented reality
    Switch easyMode; // switch to turn on or off easy mode
    Boolean easy; // true if easy mode on
    Boolean ar; // true if augmented reality on

    // different modes of difficulty (easy mode is what we have right now)
    String difficultyType[] = { "Easy", "Medium", "Hard"};
    ArrayAdapter<String> adapterDifficulty; // adapter for managing the different difficulty modes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(myToolbar);

        // allow the user to go back to the previous activity that called this one
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }

        // Get the Intent that called for this Activity to open
        Intent activityThatCalled = getIntent();

        // Get the data that was sent
        Bundle callingBundle = activityThatCalled.getBundleExtra("callingSetting");
        if( callingBundle != null ) {
            // initialize variables to reflect the game's current setting values
            easy = callingBundle.getBoolean("easy");
            ar = callingBundle.getBoolean("ar");
            System.out.println("easy = " + this.easy + ", ar = "+ this.ar);
        }

        augmentedReality = (Switch) findViewById(R.id.augmentedReality);
        easyMode = (Switch) findViewById(R.id.easyMode);

        // update the setting switches to reflect the game's current setting values
        updateSwitches();

        // if ar switch clicked on, update the ar variable
        augmentedReality.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    ar = true;
                    Toast.makeText(getApplicationContext(), "Augmented Reality checked", Toast.LENGTH_LONG).show();
                }else{
                    ar = false;
                    Toast.makeText(getApplicationContext(), "Augmented Reality unchecked", Toast.LENGTH_LONG).show();
                }
            }
        });

        // if easy mode switch is clicked on, update the easy mode variable
        easyMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    easy = true;
                    Toast.makeText(getApplicationContext(), "Easy Mode checked", Toast.LENGTH_LONG).show();
                }else{
                    easy = false;
                    Toast.makeText(getApplicationContext(), "Easy Mode unchecked", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("item = " + item.getItemId() + ", " + android.R.id.home);
        switch (item.getItemId()) {
            // action with ID action_settings was selected
            case android.R.id.home:

                // if back button pressed, return to the activity that called this one
                System.out.println("pushing home...");
                Intent intent = new Intent();
                Bundle extras = new Bundle();
                extras.putBoolean("easy", this.easy);
                extras.putBoolean("ar", this.ar);
                intent.putExtra("intent", 0);
                intent.putExtra("settings", extras);
                intent.putExtras(extras);
                setResult(RESULT_OK, intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // update the setting switches to reflect the game's current setting values
    public void updateSwitches(){
        if(this.ar){
            this.augmentedReality.setChecked(true);
        }else{
            this.augmentedReality.setChecked(false);
        }

        if(this.easy){
            this.easyMode.setChecked(true);
        }else{
            this.easyMode.setChecked(false);
        }
    }
}
