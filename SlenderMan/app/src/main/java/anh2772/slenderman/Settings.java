package anh2772.slenderman;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by AndyHecht on 11/8/2016.
 */

public class Settings extends AppCompatActivity {

    TextView settingsLabel;
    Switch augmentedReality;
    Switch easyMode;
    Boolean easy;
    Boolean ar;
    String diff;
    MediaPlayer player;
    ImageView staticImage;
    int i;

    String difficultyType[] = { "Easy", "Medium", "Hard"};
    ArrayAdapter<String> adapterDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(myToolbar);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }

        // Get the Intent that called for this Activity to open
        Intent activityThatCalled = getIntent();

        //play static
        player = new MediaPlayer();
        staticImage = (ImageView)findViewById(R.id.staticImage);

        // Get the data that was sent
        Bundle callingBundle = activityThatCalled.getBundleExtra("callingSetting");
        if( callingBundle != null ) {
            easy = callingBundle.getBoolean("easy");
            ar = callingBundle.getBoolean("ar");
            System.out.println("easy = " + this.easy + ", ar = "+ this.ar);
        }

//        settingsLabel = (TextView)findViewById(R.id.settingsLabel);
//        SpannableString content = new SpannableString("Settings");
//        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
//        settingsLabel.setText(content);

        augmentedReality = (Switch) findViewById(R.id.augmentedReality);
        easyMode = (Switch) findViewById(R.id.easyMode);

        updateSwitches();

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
