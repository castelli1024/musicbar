package com.android.riccardo.myapplication.musictext;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.riccardo.myapplication.R;
import com.android.riccardo.myapplication.services.CreateUserService;

public class CreateUserActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(0xff4caf50));
        }

        setContentView(R.layout.create_user_layout);

        final EditText insertUser = (EditText) this.findViewById(R.id.insertedUsername);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");

        Button confirmUser = (Button) this.findViewById(R.id.confirmUserButton);
        confirmUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name = insertUser.getText().toString();

                CreateUserService backgroundTask =
                        new CreateUserService(CreateUserActivity.this, progressDialog);
                backgroundTask.execute(name);

            }
        });



    }




}
