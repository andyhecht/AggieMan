package anh2772.slenderman;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
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

        // Get the Intent that called for this Activity to open
        Intent activityThatCalled = getIntent();

        //play static
        player = new MediaPlayer();
        staticImage = (ImageView)findViewById(R.id.staticImage);

        // Get the data that was sent
        Bundle callingBundle = activityThatCalled.getExtras();
        if( callingBundle != null ) {
            //            String extra = callingBundle.getString("callingActivity");
            //            text.setText(extra);
        }

        settingsLabel = (TextView)findViewById(R.id.settingsLabel);
        SpannableString content = new SpannableString("Settings");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        settingsLabel.setText(content);

        augmentedReality = (Switch) findViewById(R.id.augmentedReality);
        easyMode = (Switch) findViewById(R.id.easyMode);

        augmentedReality.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getApplicationContext(), "Augmented Reality checked", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Augmented Reality unchecked", Toast.LENGTH_LONG).show();
                }
            }
        });
        easyMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getApplicationContext(), "Easy Mode checked", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Easy Mode unchecked", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
