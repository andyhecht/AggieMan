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
 */

public class NoteList extends AppCompatActivity {
    private DynamicAdapter notesAdapter;
    private Integer[] noteIds;
    private Integer noteNum;
    protected LinearLayoutManager rv_layout_mgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list_layout);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.note_list_toolbar);
        setSupportActionBar(myToolbar);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }

        noteIds = new Integer[]{R.drawable.page_1, R.drawable.page_2,R.drawable.page_3,
                R.drawable.page_4, R.drawable.page_5, R.drawable.page_6, R.drawable.page_7,
                R.drawable.page_8};

        // Get the Intent that called for this Activity to open
        Intent activityThatCalled = getIntent();

        // Get the data that was sent
        Bundle callingBundle = activityThatCalled.getBundleExtra("note_list");
        if( callingBundle != null ) {
            noteNum = callingBundle.getInt("num", 0);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.note_list_rv);
        notesAdapter = new DynamicAdapter();

        rv_layout_mgr = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rv_layout_mgr);
        recyclerView.setAdapter(notesAdapter);

        updateNotesAdapter();

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

    private void updateNotesAdapter(){
        for(int i = 0; i < noteNum; i++){
            notesAdapter.add(noteIds[i]);
        }
        notesAdapter.notifyDataSetChanged();
    }
}
