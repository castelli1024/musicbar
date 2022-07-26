package com.android.riccardo.myapplication.musictext;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.riccardo.myapplication.R;
import com.android.riccardo.myapplication.containers.FillUsersSpinnerContainer;
import com.android.riccardo.myapplication.misc.DBArtist;
import com.android.riccardo.myapplication.misc.DBUser;
import com.android.riccardo.myapplication.services.EditUserService;
import com.android.riccardo.myapplication.services.FillUsersSpinnerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SelectArtistActivity extends AppCompatActivity {
    List<DBUser> usersList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(0xff4caf50));
        }
        setContentView(R.layout.select_artists_activity);

        InputStream is = this.getResources().openRawResource(R.raw.top_ranked_artists);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        LinearLayout layout = (LinearLayout) this.findViewById(R.id.artists_scroll_layout);
        final ArrayList<CheckBox> boxes = new ArrayList<CheckBox>();
        final ArrayList<DBArtist> artistList = new ArrayList<DBArtist>();


        try {
            br.readLine();
            String line = br.readLine();
            while(line != null) {
                String[] splittedLine = line.split(",");
                String artist = splittedLine[2].replace(",", "\0");

                CheckBox box = new CheckBox(this);
                box.setText(artist);
                box.setTextSize(20);
                layout.addView(box);
                boxes.add(box);

                DBArtist toAdd = new DBArtist(Integer.parseInt(splittedLine[0]), artist, Integer.parseInt(splittedLine[1]));
                artistList.add(toAdd);
                line = br.readLine();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        final Spinner userSpinner = (Spinner) this.findViewById(R.id.userSpinner);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        FillUsersSpinnerContainer container = new FillUsersSpinnerContainer(this, progressDialog, userSpinner);
        FillUsersSpinnerService fillUsersSpinnerService = new FillUsersSpinnerService();
        fillUsersSpinnerService.execute(container);

        Button confirmSelectionButton = (Button) this.findViewById(R.id.confirmArtistsSelectionButton);
        confirmSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DBUser user = usersList.get(userSpinner.getSelectedItemPosition());
                List<DBArtist> selectedArtists = getSelectedArtists(boxes, artistList);
                user.setArtists(selectedArtists);

                ProgressDialog progressDialog = new ProgressDialog(SelectArtistActivity.this);
                progressDialog.setMessage("Please Wait");
                //progressDialog.show();

                EditUserService editUserService = new EditUserService(SelectArtistActivity.this, progressDialog);
                editUserService.execute(user);

            }
        });

    }

    private List<DBArtist> getSelectedArtists(ArrayList<CheckBox> boxes, ArrayList<DBArtist> artistList) {
        ArrayList<DBArtist> selectedArtists = new ArrayList<>();

        for(int i = 0; i < boxes.size(); i++) {
            CheckBox box = boxes.get(i);

            if(box.isChecked()) {
                selectedArtists.add(artistList.get(i));
            }
        }

        return selectedArtists;
    }

    public void setUsersList(List<DBUser> usersList) {
        this.usersList = usersList;
    }
}

