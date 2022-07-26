package com.android.riccardo.myapplication.musictext;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.riccardo.myapplication.R;
import com.android.riccardo.myapplication.misc.Track;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ShowSuggestionsActivity extends AppCompatActivity {
    private String[] maxArtistsNumber = {"3", "5", "8", "10", "15"};
    private String[] maxTracksNumber = {"3", "5", "8", "10", "15"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(0xff4caf50));
        }
        setContentView(R.layout.show_suggestions_layout);

        final List<Track> trackList = new ArrayList<>();
        Intent intent = this.getIntent();
        String pkg = this.getPackageName();

        final ListView suggestList = (ListView) this.findViewById(R.id.suggest_list);
        final Spinner maxTracks = (Spinner) this.findViewById(R.id.max_tracks_spinner);
        final Spinner maxArtists = (Spinner) this.findViewById(R.id.max_artists_spinner);
        setSpinners(maxArtists, maxTracks);

        maxTracks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ProgressDialog progressDialog = new ProgressDialog(ShowSuggestionsActivity.this);
                progressDialog.setMessage("Please Wait");
                progressDialog.show();

                int selectedMaxTracks = Integer.parseInt(maxTracks.getSelectedItem().toString());
                int selectedMaxArtists = Integer.parseInt(maxArtists.getSelectedItem().toString());

                List<Track> filteredTracks = filterSuggestions(trackList, selectedMaxArtists, selectedMaxTracks);
                List<String> valuesPrintTracks = new ArrayList<>();
                for (Track track : filteredTracks) {
                    valuesPrintTracks.add("Name: " + track.getName() + "\n Artist: " + track.getArtist());
                }
                setListView(valuesPrintTracks, suggestList);

                progressDialog.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        maxArtists.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ProgressDialog progressDialog = new ProgressDialog(ShowSuggestionsActivity.this);
                progressDialog.setMessage("Please wait");
                progressDialog.show();

                int selectedMaxTracks = Integer.parseInt(maxTracks.getSelectedItem().toString());
                int selectedMaxArtists = Integer.parseInt(maxArtists.getSelectedItem().toString());

                List<Track> filteredTracks = filterSuggestions(trackList, selectedMaxArtists, selectedMaxTracks);
                List<String> valuesPrintArtists = new ArrayList<String>();

                for (Track track : filteredTracks) {
                    valuesPrintArtists.add("Name: " + track.getName() + "\n Artist: " + track.getArtist());
                }
                setListView(valuesPrintArtists, suggestList);

                progressDialog.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button mainMenu = (Button) this.findViewById(R.id.back_main_menu);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
            }
        });

        int[] tags = (int[]) intent.getSerializableExtra(pkg + ".tags");


        InputStream is = this.getResources().openRawResource(R.raw.track_artist_tags);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {
            String line = br.readLine();
            line = br.readLine();
            String r;
            while (line != null) {
                String[] splittedLine = line.split(";");
                if (splittedLine[4].length() >= 1) {
                    int matchedTags = 0;
                    String[] d = splittedLine[4].split(",");
                    for (int t : tags) {
                        matchedTags = 0;
                        for(String s : d) {
                            if(s.contains("\"")) {
                                r = s.replaceAll("\"", "");
                            } else r = s;

                            if(Integer.parseInt(r) == t) {
                                matchedTags++;
                            }

                        }
                    }
                    if (matchedTags > 0) {
                        double rank = (double) matchedTags / tags.length;
                        trackList.add(new Track(splittedLine[3].replaceAll("\"", ""),
                                splittedLine[1].replaceAll("\"", ""), rank));
                    }
                }

                line = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> valuesPrint = new ArrayList<>();
        for (Track track : trackList) {
            valuesPrint.add("Name: " + track.getName() +
                    "\n Artist: " + track.getArtist());
        }
        setListView(valuesPrint, suggestList);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    private void setListView(List<String> values, ListView listView) {
        String[] valuesArray = toStringArray(values);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, valuesArray);
        listView.setAdapter(adapter);

    }

    private String[] toStringArray(List<String> list) {
        String[] toReturn = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            toReturn[i] = list.get(i);
        }
        return toReturn;
    }


    private List<Track> filterSuggestions(List<Track> trackList, int maxArtists, int maxTracks) {
        List<Track> filteredTracks = new ArrayList<>();
        int temp_tracks = 0;
        int temp_artists = 0;

        for (int i = 0; i < trackList.size(); i++) {
            if (temp_tracks < maxTracks && temp_artists < maxArtists) {
                filteredTracks.add(trackList.get(i));
                temp_tracks++;
            }
            if (i > 0) {
                if (!(trackList.get(i - 1).getArtist()).equals(trackList.get(i).getArtist())) {
                    temp_tracks = 0;
                    temp_artists++;
                }
            }
        }
        return filteredTracks;
    }

    private void setSpinners(Spinner maxArtists, Spinner maxTracks) {
        ArrayAdapter<String> artistsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, maxArtistsNumber);
        maxArtists.setAdapter(artistsAdapter);
        ArrayAdapter<String> tracksAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, maxTracksNumber);
        maxTracks.setAdapter(tracksAdapter);

    }
}
