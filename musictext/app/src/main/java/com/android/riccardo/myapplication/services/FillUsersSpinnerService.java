package com.android.riccardo.myapplication.services;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.riccardo.myapplication.containers.FillUsersSpinnerContainer;
import com.android.riccardo.myapplication.misc.DBUser;
import com.android.riccardo.myapplication.musictext.InsertObservationActivity;
import com.android.riccardo.myapplication.musictext.SelectArtistActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FillUsersSpinnerService extends AsyncTask<FillUsersSpinnerContainer, Void, Void> {

    @Override
    protected Void doInBackground(FillUsersSpinnerContainer... params) {
        final FillUsersSpinnerContainer container = params[0];
        List<DBUser> userList = null;

        try {
            URL url = new URL("http://10.0.2.2:8080/listusers");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();

            while ((line = in.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }

            in.close();

            String answer = response.toString();
            JsonParser parser = new JsonParser();
            JsonArray jsonElements = (JsonArray) parser.parse(answer);
            userList = parseJson(jsonElements);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (userList != null) {
            final Activity activity = container.getActivity();
            if (activity instanceof SelectArtistActivity) {
                final SelectArtistActivity callingActivity = (SelectArtistActivity) activity;
                callingActivity.setUsersList(userList);
            }

            if (activity instanceof InsertObservationActivity) {
                final InsertObservationActivity callingActivity = (InsertObservationActivity) activity;
                callingActivity.setUsersList(userList);
            }

            final List<DBUser> finalUserList = userList;

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String[] names = toStringArray(finalUserList);
                    Spinner spinner = container.getSpinner();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_spinner_item, names);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    container.getProgressDialog().dismiss();

                }
            });
        }


        return null;
    }

    private String[] toStringArray(List<DBUser> toParse) {
        String[] toReturn = new String[toParse.size()];
        for (int i = 0; i < toParse.size(); i++) {
            toReturn[i] = toParse.get(i).getName();
        }

        return toReturn;
    }

    private List<DBUser> parseJson(JsonArray jsonElements) {
        List<DBUser> users = new ArrayList<>();
        for (int i = 0; i < jsonElements.size(); i++) {
            Gson gson = new Gson();
            DBUser user = gson.fromJson(jsonElements.get(i), DBUser.class);
            users.add(user);
        }

        return users;
    }
}
