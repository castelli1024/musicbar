package com.android.riccardo.myapplication.musictext;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.riccardo.myapplication.R;
import com.android.riccardo.myapplication.containers.FillUsersSpinnerContainer;
import com.android.riccardo.myapplication.misc.DBOsservazione;
import com.android.riccardo.myapplication.misc.DBRequestSugg;
import com.android.riccardo.myapplication.misc.DBSuggerimento;
import com.android.riccardo.myapplication.misc.DBTag;
import com.android.riccardo.myapplication.misc.DBUser;
import com.android.riccardo.myapplication.misc.ObservationTemplate;
import com.android.riccardo.myapplication.services.FillUsersSpinnerService;
import com.android.riccardo.myapplication.services.SuggestService;

import java.util.ArrayList;
import java.util.List;

public class InsertObservationActivity extends AppCompatActivity {
    List<ObservationTemplate> observationTemplateList = null;
    List<Spinner> spinnerList;
    List<DBUser> usersList = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(0xff4caf50));
        }
        setContentView(R.layout.insert_observations_activity);

        final Spinner userSpinner = (Spinner) this.findViewById(R.id.userSpinnerObs);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        FillUsersSpinnerContainer container = new FillUsersSpinnerContainer(this, progressDialog, userSpinner);
        FillUsersSpinnerService fillUsersSpinnerService = new FillUsersSpinnerService();
        fillUsersSpinnerService.execute(container);

        spinnerList = new ArrayList<>();
        LinearLayout layout = (LinearLayout) this.findViewById(R.id.observationsScrollLayout);

        observationTemplateList = constructObservationTemplate();

        for (ObservationTemplate observationTemplate : observationTemplateList) {
            LinearLayout toAdd = constructLayout(observationTemplate);
            layout.addView(toAdd);
        }

        Button confirmObservationsButton = (Button) this.findViewById(R.id.confirmObservationsButton);
        confirmObservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog1 = new ProgressDialog(InsertObservationActivity.this);
                progressDialog1.setMessage("Please wait");
                progressDialog1.show();

                List<DBOsservazione> observationsList = constructObservationsList();
                DBUser user = usersList.get(userSpinner.getSelectedItemPosition());
                DBRequestSugg requestSugg = new DBRequestSugg(user.getId(), observationsList);

                SuggestService suggestService = new SuggestService(progressDialog, InsertObservationActivity.this);
                suggestService.execute(requestSugg);
            }
        });

    }


    private List<DBOsservazione> constructObservationsList() {
        List<DBOsservazione> toReturn = new ArrayList<>();
        for (int i = 0; i < spinnerList.size(); i++) {
            int chosenIndex = spinnerList.get(i).getSelectedItemPosition();
            String name = observationTemplateList.get(i).getRealName();
            String value = observationTemplateList.get(i).getRealValues().get(chosenIndex);
            toReturn.add(new DBOsservazione(name, value));
        }

        return toReturn;
    }


    private LinearLayout constructLayout(ObservationTemplate observationTemplate) {
        LinearLayout layoutToAdd = new LinearLayout(this);
        layoutToAdd.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(this);
        textView.setText(observationTemplate.getNome());
        textView.setTextSize(20);

        String[] options = toStringArray(observationTemplate.getValues());
        Spinner spinner = new Spinner(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setScrollBarSize(20);
        layoutToAdd.addView(textView);
        layoutToAdd.addView(spinner);
        spinnerList.add(spinner);
        return layoutToAdd;

    }

    private String[] toStringArray(List<String> values) {
        String[] toReturn = new String[values.size()];
        for (int i = 0; i < values.size(); i++) {
            toReturn[i] = values.get(i);
        }
        return toReturn;
    }

    private List<ObservationTemplate> constructObservationTemplate() {
        List<ObservationTemplate> templateList = new ArrayList<ObservationTemplate>();
        /*
        x;temperatura;min5:5_15:15_25:maj25;;0.05:0.2:0.35:0.4
        x;condizioni_meteo;Temporale:Nebbia:Coperto:Variabile:Pioggia:Sereno;;0.05:0.03:0.1:0.35:0.12:0.35
        x;mezzo;Auto:Moto:Bici:Mezzo_pubblico;;0.35:0.45:0.05:0.15
        x;km_to_run;min10:10_30:30_50:50_100:maj100;;0.35:0.35:0.2:0.05:0.05
        x;momento_viaggio;Giorno_feriale:Giorno_festivo:Notte_feriale:Notte_festivo;;0.325:0.175:0.325:0.175
         */
        String name = "Temperatura";
        String realName = "Temperatura";
        List<String> values = new ArrayList<String>();
        List<String> realValues = new ArrayList<String>();
        values.add("Minore 5°C");
        realValues.add("min5");
        values.add("Tra 5°C e 15°C");
        realValues.add("5_15");
        values.add("Tra 15°C e 25°C");
        realValues.add("15_25");
        values.add("Maggiore 25°C");
        realValues.add("maj25");
        templateList.add(new ObservationTemplate(name, realName, values, realValues));


        String name2 = "Condizioni meteo";
        String realName2 = "Condizioni_meteo";
        List<String> values2 = new ArrayList<String>();
        List<String> realValues2 = new ArrayList<String>();
        values2.add("Temporale");
        realValues2.add("Temporale");
        values2.add("Nebbia");
        realValues2.add("Nebbia");
        values2.add("Coperto");
        realValues2.add("Coperto");
        values2.add("Variabile");
        realValues2.add("Variabile");
        values2.add("Pioggia");
        realValues2.add("Pioggia");
        values2.add("Sereno");
        realValues2.add("Sereno");
        templateList.add(new ObservationTemplate(name2, realName2, values2, realValues2));


        String name3 = "Mezzo";
        String realName3 = "Mezzo";
        List<String> values3 = new ArrayList<String>();
        List<String> realValues3 = new ArrayList<String>();
        values3.add("Auto");
        realValues3.add("Auto");
        values3.add("Moto");
        realValues3.add("Moto");
        values3.add("Bici");
        realValues3.add("Bici");
        values3.add("Mezzo pubblico");
        realValues3.add("Mezzo_pubblico");
        templateList.add(new ObservationTemplate(name3, realName3, values3, realValues3));


        String name4 = "Km da percorrere";
        String realName4 = "Km_to_run";
        List<String> values4 = new ArrayList<String>();
        List<String> realValues4 = new ArrayList<String>();
        values4.add("Meno di 10");
        realValues4.add("min10");
        values4.add("Tra 10 e 30");
        realValues4.add("10_30");
        values4.add("Tra 30 e 50");
        realValues4.add("30_50");
        values4.add("Tra 50 e 100");
        realValues4.add("50_100");
        values4.add("Più di 100");
        realValues4.add("maj100");
        templateList.add(new ObservationTemplate(name4, realName4, values4, realValues4));

        //momento_viaggio;Giorno_feriale:Giorno_festivo:Notte_feriale:Notte_festivo
        String name5 = "Momento viaggio";
        String realName5 = "Momento_viaggio";
        List<String> values5 = new ArrayList<String>();
        List<String> realValues5 = new ArrayList<String>();
        values5.add("Giorno feriale");
        realValues5.add("Giorno_feriale");
        values5.add("Giorno festivo");
        realValues5.add("Giorno_festivo");
        values5.add("Notte feriale");
        realValues5.add("Notte_feriale");
        values5.add("Notte festivo");
        realValues5.add("Notte_festivo");
        templateList.add(new ObservationTemplate(name5, realName5, values5, realValues5));


        return templateList;
    }


    public void startShowSuggestionActivity(DBSuggerimento suggerimento) {
        Intent intent = new Intent(this, ShowSuggestionsActivity.class);
        String pkg = this.getPackageName();
        List<DBTag> tags = suggerimento.getTagSuggeriti();
        int[] tagsArray = new int[tags.size()];
        for (int i = 0; i < tags.size(); i++) {
            tagsArray[i] = tags.get(i).getId();
        }
        intent.putExtra(pkg + ".tags", tagsArray);
        this.startActivity(intent);
    }


    public void setUsersList(List<DBUser> usersList) {
        this.usersList = usersList;
    }
}
