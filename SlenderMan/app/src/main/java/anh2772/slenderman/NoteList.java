package anh2772.slenderman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jason on 12/4/2016.
 *
 * Activity for displaying all of the user's gathered notes in a recycler view
 */

public class NoteList extends AppCompatActivity {

    private DynamicAdapter notesAdapter; // dynamic adapter for managing the rows of the rv
    private Integer[] noteIds; // array of note resource ids
    private Integer noteNum; // number of notes collected
    protected LinearLayoutManager rv_layout_mgr; // recycler view layout manager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list_layout);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.note_list_toolbar);
        setSupportActionBar(myToolbar);

        // enable back button to go back to game
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }

        // initialize note resource ids
        noteIds = new Integer[]{R.drawable.page_1, R.drawable.page_2,R.drawable.page_3,
                R.drawable.page_4, R.drawable.page_5, R.drawable.page_6, R.drawable.page_7,
                R.drawable.page_8};

        // Get the Intent that called for this Activity to open
        Intent activityThatCalled = getIntent();

        // Get the data that was sent
        Bundle callingBundle = activityThatCalled.getBundleExtra("note_list");
        if( callingBundle != null ) {
            // get the number of notes collected
            noteNum = callingBundle.getInt("num", 0);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.note_list_rv);

        // create dynamic adapter
        notesAdapter = new DynamicAdapter();

        // update the recycler view ot use adapter
        rv_layout_mgr = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rv_layout_mgr);
        recyclerView.setAdapter(notesAdapter);

        // update the adapter to have all of the notes the user contains
        updateNotesAdapter();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("item = " + item.getItemId() + ", " + android.R.id.home);
        switch (item.getItemId()) {
            case android.R.id.home:
                // go back to the game
                this.finish();
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    // adds the note resource ids to the adapter to display the notes' images
    private void updateNotesAdapter(){
        for(int i = 0; i < noteNum; i++){
            notesAdapter.add(noteIds[i]);
        }
        // render the changes
        notesAdapter.notifyDataSetChanged();
    }
}
