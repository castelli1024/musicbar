package com.android.riccardo.myapplication.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.android.riccardo.myapplication.misc.DBRequestSugg;
import com.android.riccardo.myapplication.misc.DBSuggerimento;
import com.android.riccardo.myapplication.musictext.InsertObservationActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SuggestService extends AsyncTask<DBRequestSugg, Void, Void> {

    private  ProgressDialog progressDialog;
    private  Activity activity;

    public SuggestService(ProgressDialog progressDialog, Activity activity) {
        this.progressDialog = progressDialog;
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(DBRequestSugg... params) {
        DBRequestSugg dbRequestSugg = params[0];
        DBSuggerimento suggerimento = null;
        try {
            URL url = new URL("http://10.0.2.2:8080/suggest");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(15000);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            Gson gsonObject = new Gson();
            String dbRequestJson = gsonObject.toJson(dbRequestSugg);


            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(dbRequestJson);
            wr.flush();
            wr.close();

            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            String answer = "";

            while(line != null) {
                answer += line;
                line = br.readLine();
            }
            br.close();

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject) parser.parse(answer);
            Gson gson = new Gson();
            suggerimento = gson.fromJson(jsonObject, DBSuggerimento.class);
            final DBSuggerimento suggerimentoFinale = suggerimento;


            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    if(activity instanceof InsertObservationActivity) {
                        InsertObservationActivity insertObservationActivity = (InsertObservationActivity) activity;
                        insertObservationActivity.startShowSuggestionActivity(suggerimentoFinale);
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
