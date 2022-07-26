package com.android.riccardo.myapplication.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.riccardo.myapplication.musictext.MainActivity;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class CreateUserService extends AsyncTask<String, Void, Void> {
    private Activity activity;
    private ProgressDialog progressDialog;

    public CreateUserService(Activity activity, ProgressDialog progressDialog) {
        this.activity = activity;
        this.progressDialog = progressDialog;
    }

    @Override
    protected Void doInBackground(String... users_names) {
        String userName = users_names[0];
        try {
            URL url = new URL("http://10.0.2.2:8080/user?user_name=" + userName);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            final int response = connection.getResponseCode();

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (connection.getResponseCode() == 201) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("User created!");
                            builder.setMessage("User successfully created");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    startMainActivity();
                                }
                            });

                            if (progressDialog != null)
                                progressDialog.dismiss();
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (connection.getResponseCode() == 400) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("User not created!");
                            builder.setMessage("Username too short or already existent");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    startMainActivity();
                                }
                            });

                            if (progressDialog != null)
                                progressDialog.dismiss();
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (connection.getResponseCode() == 500) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("User NOT created!");
                            builder.setMessage("Internal Server Error");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    startMainActivity();
                                }
                            });

                            if (progressDialog != null)
                                progressDialog.dismiss();
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }catch (IOException e) {
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

    private void startMainActivity() {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    private void setListView(ArrayList<String> values, Activity activity, ListView listView) {
        String[] valuesArray = toStringArray(values);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, valuesArray);
        listView.setAdapter(adapter);
    }

    private String[] toStringArray(ArrayList<String> list) {
        String[] toReturn = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            toReturn[i] = list.get(i);
        }
        return toReturn;
    }


}

