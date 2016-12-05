package anh2772.slenderman;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by jason on 12/4/2016.
 */

public class Warning  extends AppCompatActivity {

    private Boolean easy;
    private Boolean warned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warning_layout);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }

        warned = false;

        // Get the Intent that called for this Activity to open
        Intent activityThatCalled = getIntent();

        // Get the data that was sent
        Bundle callingBundle = activityThatCalled.getBundleExtra("game");
        if( callingBundle != null ) {
            easy = callingBundle.getBoolean("easy");
        }

        Button exit = (Button) findViewById(R.id.warning_exit_but);
        Button play = (Button) findViewById(R.id.warning_play_but);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(warned) {
                    Intent intent = new Intent(getApplicationContext(), Game.class);
                    Bundle extras = new Bundle();
                    extras.putBoolean("easy", easy);
                    intent.putExtra("game", extras);
                    final int result = 1;
                    startActivityForResult(intent, result);
                }else{
                    warned = true;
                    TextView instructions = (TextView) findViewById(R.id.warning_text);
                    instructions.setText(getResources().getString(R.string.instructions));
                    LinearLayout instr_images = (LinearLayout) findViewById(R.id.instr_images_ll);
                    instr_images.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("item = " + item.getItemId() + ", " + android.R.id.home);
        switch (item.getItemId()) {
            // action with ID action_settings was selected
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent();
        Bundle extras = new Bundle();
        intent.putExtra("intent", 1);
        intent.putExtra("game_over", extras);
        intent.putExtras(extras);
        setResult(RESULT_OK);
        finish();
    }
}
