package anh2772.slenderman;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by jason on 12/4/2016.
 */

public class LargeNoteDisplay  extends AppCompatActivity {

    private Integer noteId = 0;
    private String noteTitle = "Page ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.largenote_layout);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.large_note_toolbar);
        setSupportActionBar(myToolbar);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }

        // Get the Intent that called for this Activity to open
        Intent activityThatCalled = getIntent();

        // Get the data that was sent
        Bundle callingBundle = activityThatCalled.getBundleExtra("note");
        if( callingBundle != null ) {
            noteId = callingBundle.getInt("id",0);
            noteTitle += callingBundle.getInt("num", 1);
            System.out.println("note id = " + noteId);
        }

        ImageView nIV = (ImageView) findViewById(R.id.large_note_image);
        TextView nt = (TextView) findViewById(R.id.large_note_text);

        nIV.setImageResource(noteId);
        nt.setText(noteTitle);

        Button exit = (Button) findViewById(R.id.large_note_but);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("item = " + item.getItemId() + ", " + android.R.id.home);
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }
}
