package com.android.riccardo.myapplication.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import com.android.riccardo.myapplication.misc.DBUser;

public class EditUserService extends AsyncTask<DBUser, Void, Void> {
    private Activity activity;
    private ProgressDialog progressDialog;

    public EditUserService(Activity activity, ProgressDialog progressDialog) {
        this.activity = activity;
        this.progressDialog = progressDialog;
    }

    @Override
    protected Void doInBackground(DBUser... usr) {
        DBUser users = usr[0];
        final ArrayList nodes = new ArrayList<String>();
        try {
            //create connection
            URL url = new URL("http://10.0.2.2:8080/user");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //java to json
            Gson gsonObject = new Gson();
            String userJSON;
            userJSON = gsonObject.toJson(users);

            //send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(userJSON);
            wr.flush();
            wr.close();

            //get response
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = br.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            br.close();


            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("User Modified!");
                    try {
                        if(connection.getResponseCode() == 200) {
                            builder.setMessage("User successfully modified");
                        }
                        if(connection.getResponseCode() >= 400 && connection.getResponseCode() < 500) {
                            builder.setMessage("Error 400");
                        }
                        if(connection.getResponseCode() >= 500) {
                            builder.setMessage("Error 500");
                        }
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        if(progressDialog != null) progressDialog.dismiss();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
