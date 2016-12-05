package anh2772.slenderman;

/**
 * Created by jason on 12/4/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by jason on 10/11/2016.
 */
public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.DynamicViewHolder>{

    // note resource Ids
    private ArrayList<Integer> notes;

    // adapter constructor
    public DynamicAdapter (){
        notes = new ArrayList<>();
    }

    // view holder, manages data for each row
    public class DynamicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView note; // note picture
        TextView text; // note title text view
        View container; // row
        Integer noteId; // not resource Id
        Integer noteNum; // note title
        private final Context context;

        // viewholder constructor
        public DynamicViewHolder(View view) {
            super(view);

            // initializations
            container = view;
            note = (ImageView) view.findViewById(R.id.note_row_image);
            text = (TextView) view.findViewById(R.id.note_row_text);
            noteId = 0;
            noteNum = 0;
            context = view.getContext();
        }

        @Override
        public void onClick(View v) {

            // if clicked, enlarge the note in a new view
            Intent intent = new Intent(v.getContext(), LargeNoteDisplay.class);
            Bundle extras = new Bundle();
            extras.putInt("id", noteId);
            extras.putInt("num", noteNum);
            intent.putExtra("note", extras);
            v.getContext().startActivity(intent);
        }

    }

    @Override
    public DynamicAdapter.DynamicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_row, parent, false);

        // create view holder and set on click listener for the note row
        DynamicViewHolder dvh = new DynamicViewHolder(itemView);
        itemView.setOnClickListener(dvh);

        // return view holder
        return dvh;
    }

    @Override
    public void onBindViewHolder(DynamicAdapter.DynamicViewHolder holder, int position) {

        // updates the view holder at this position
        Integer noteId = notes.get(position);
        String noteTitle = "Page " + (position + 1);
        holder.noteId = noteId;
        holder.noteNum = position + 1;
        holder.text.setText(noteTitle);
        holder.note.setImageResource(noteId);
    }

    // returns number of notes collected
    @Override
    public int getItemCount() {
        return notes.size();
    }

    // removes the note at this location
    public void remove(int position){

        // remove listview at position
        notes.remove(position);

        // update the UI
        notifyDataSetChanged();
    }

    // adds a note to the note Id array
    public void add(Integer noteId){
        this.notes.add(noteId);
    }
}
