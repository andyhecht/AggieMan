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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jason on 10/11/2016.
 */
public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.DynamicViewHolder>{

    // reddit meta data
    private ArrayList<Integer> notes;

    // adapter constructor
    public DynamicAdapter (){
        notes = new ArrayList<>();
    }

    public class DynamicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // metadata
        ImageView note;
        TextView text;
        View container;
        Integer noteId;
        Integer noteNum;
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

        // create view holder and set on click listener for One Post
        DynamicViewHolder dvh = new DynamicViewHolder(itemView);
        itemView.setOnClickListener(dvh);

        // return view holder
        return dvh;
    }

    @Override
    public void onBindViewHolder(DynamicAdapter.DynamicViewHolder holder, int position) {

        // updates listview ImageView and TextView
        Integer noteId = notes.get(position);
        String noteTitle = "Page " + (position + 1);
        holder.noteId = noteId;
        holder.noteNum = position + 1;
        holder.text.setText(noteTitle);
        holder.note.setImageResource(noteId);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void remove(int position){

        // remove listview at position
        notes.remove(position);

        // update the UI
        notifyDataSetChanged();
    }

    public void add(Integer noteId){
        this.notes.add(noteId);
    }
}
