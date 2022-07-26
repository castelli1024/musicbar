package com.android.riccardo.myapplication.musictext;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.riccardo.myapplication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();

        if(actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(0x3b5998));
        }

        setContentView(R.layout.main_menu_layout);

        Button createUser = (Button) this.findViewById(R.id.createUserButton);
        createUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreateUserActivity();
            }
        });

        Button modifyUser = (Button) this.findViewById(R.id.modifyUserButton);
        modifyUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startSelectArtistActivity();
            }
        });

        Button startSuggest = (Button) this.findViewById(R.id.suggestButton);
        startSuggest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startInsertObservationActivity();
            }
        });



    }

    private void startSelectArtistActivity() {
        Intent intent = new Intent(this, SelectArtistActivity.class);
        this.startActivity(intent);
    }


    private void startCreateUserActivity() {
        Intent intent = new Intent(this, CreateUserActivity.class);
        this.startActivity(intent);
    }

    private void startInsertObservationActivity() {
        Intent intent = new Intent(this, InsertObservationActivity.class);
        this.startActivity(intent);
    }

}
