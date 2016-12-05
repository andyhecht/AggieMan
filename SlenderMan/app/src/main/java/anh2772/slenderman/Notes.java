package anh2772.slenderman;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

/**
 * Created by jason on 11/7/2016.
 */
public class Notes {

    private Marker[] notes;
    private Activity a;
    private GoogleMap gMap;
    private Marker uMarker;
    private Marker sMarker;
    private Integer collectedNotesCount;
    private Integer maxNotes;
    private Integer[] noteIDs;

    public Notes(Activity a, GoogleMap gMap, Marker uMarker, Marker sMarker, Integer maxNotes){
        this.notes = new Marker[8];
        this.a = a;
        this.gMap = gMap;
        this.uMarker = uMarker;
        this.sMarker = sMarker;
        this.collectedNotesCount = 0;
        this.maxNotes = maxNotes;
        noteIDs = new Integer[]{R.drawable.page_1, R.drawable.page_2,R.drawable.page_3,
                R.drawable.page_4, R.drawable.page_5, R.drawable.page_6, R.drawable.page_7,
                R.drawable.page_8};
    }

    public void generateNotes(){
        // populate map with notes at randomized positions
        for(int i = 0; i < this.maxNotes; i++){
            notes[i] = setRandomNote(0.01);
            notes[i].setVisible(false);
        }

        for(int i = 0; i < this.maxNotes; i++){
            Marker fake = setRandomNote(0.01);
            fake.setVisible(false);
        }

        Button notesBut = (Button) a.findViewById(R.id.notes);
        notesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(a.getApplicationContext(), NoteList.class);
                Bundle extras = new Bundle();
                extras.putInt("num", collectedNotesCount);
                intent.putExtra("note_list", extras);
                a.startActivity(intent);
            }
        });
    }

    // will place a note randomly on the map given limit (the distance from the user)
    private Marker setRandomNote(Double limit){
        Random rand = new Random();

        // randomizes add or subtract from user position
        int sign = rand.nextInt(3) + 1;
        if(sign == 1 || sign == 2){
            sign = -1;
        } else{
            sign = 1;
        }

        // position of note
        Double latitude = uMarker.getPosition().latitude + rand.nextDouble()*limit*sign;
        Double longitude = uMarker.getPosition().longitude + rand.nextDouble()*limit*sign;

        gMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon
                (BitmapDescriptorFactory.fromBitmap(resizeIcon("circle", 100, 100))));

        // add note to map
        return gMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon
                (BitmapDescriptorFactory.fromBitmap(resizeIcon("notes", 75, 75))));
    }

    public Boolean checkNotes(Marker marker){
        Boolean real = false;

        for(int i = 0; i < notes.length; i++){
            if(notes[i].equals(marker)){
                real = true;
                break;
            }
        }
        return real;
    }

    public Integer revealIfNoteExists(Marker marker){
        Integer note = 0;
        Boolean real = false;

        for(int i = 0; i < notes.length; i++) {
            if(marker.getPosition().latitude == notes[i].getPosition().latitude &&
                    marker.getPosition().longitude == notes[i].getPosition().longitude){
                real = true;
                note = i;
                break;
            }
        }

        marker.setVisible(false);
        if(real){
            notes[note].setVisible(true);
            Toast.makeText(a, "FOUND NOTE!", Toast.LENGTH_SHORT).show();
        }
        return note;
    }

    // resize marker icons so they are uniform size
    // http://stackoverflow.com/questions/14851641/change-marker-size-in-google-maps-api-v2
    public Bitmap resizeIcon(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(a.getResources(), a.getResources().getIdentifier(iconName, "drawable", a.getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    public Boolean onClick(Marker marker){
        // true if clicked on a note
        if(!this.uMarker.equals(marker) && !this.sMarker.equals(marker)) {

            Double distance = Math.pow(Math.pow(marker.getPosition().latitude - uMarker.getPosition().latitude, 2)
                    + Math.pow(marker.getPosition().longitude - uMarker.getPosition().longitude, 2), 0.5);

            Boolean real = checkNotes(marker);

            // true if note is close enough to collect
            if(real && distance <= 0.000413 ) {
                // remove note update counter
                marker.setVisible(false);
                collectedNotesCount += 1;

                Intent intent = new Intent(a.getApplicationContext(), LargeNoteDisplay.class);
                Bundle extras = new Bundle();
                extras.putInt("id", noteIDs[collectedNotesCount-1]);
                extras.putInt("num", collectedNotesCount);
                intent.putExtra("note", extras);
                a.startActivity(intent);

                Toast.makeText(a, "NOTE COLLECTED!", Toast.LENGTH_SHORT).show();

                // if all notes collected, end game - you win
                if(collectedNotesCount == 10){
                    Toast.makeText(a, "YOU WIN!", Toast.LENGTH_SHORT).show();
                    return true;
                }
            } else if(distance <= 0.000413){
                revealIfNoteExists(marker);
            } else{
                // note is too far away - let them know how far away it is.
                Toast.makeText(a, "Note too far away to collect. Distance : "
                        + (distance - 0.000213), Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

}
