package com.example.aaaaaaaa_dodelat_makej.a;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.aaaaaaaa_dodelat_makej.R;
import com.example.aaaaaaaa_dodelat_makej.a.adapters.DataAdapter;
import com.example.aaaaaaaa_dodelat_makej.a.adapters.MainAdapter;
import com.example.aaaaaaaa_dodelat_makej.a.adapters.TeamAdapter;
import com.example.aaaaaaaa_dodelat_makej.a.graphs.BlockGraph;
import com.example.aaaaaaaa_dodelat_makej.databinding.GroupAddDialogBinding;
import com.example.aaaaaaaa_dodelat_makej.databinding.GroupMainBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class GroupActivity extends AppCompatActivity {

    private String groupID = "groupID";
    private String userID = "userID";

    private GroupMainBinding binding;

    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GroupMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<String> headers = new ArrayList<>();
        headers.add("Pivo");
        headers.add("Uklid");

        HashMap<String,Integer> colors = new HashMap<>();
        colors.put("Martin",Color.CYAN);
        colors.put("Michal",Color.RED);
        colors.put("Petr",Color.YELLOW);
        colors.put("Filip",Color.GREEN);

        mainAdapter = new MainAdapter("Filip");

        mainAdapter.add(new MainAdapter.DataType(0,0, null,null,"Graph Test", MainAdapter.DataType.TYPE_GRAPH,System.currentTimeMillis(),colors));

        try{
            JSONObject object = readJSON();
            JSONArray jsonArray = new JSONArray();
            if(object.has("games")){
                jsonArray = object.getJSONArray("games");
            }
            for(int i = 0; i < jsonArray.length();i++){
                mainAdapter.add(new Gson().fromJson((String) jsonArray.get(i), MainAdapter.DataType.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        binding.mainView.setAdapter(mainAdapter);

        binding.mainAdd.setOnClickListener(view -> {

            HashMap<String,Integer> players = new HashMap<>();
            players.put("Martin",1);
            players.put("Filip",1);
            players.put("Michal",2);
            players.put("Petr",2);

            bottomAddDialog(players,headers);
        });

    }


    private void bottomAddDialog(HashMap<String,Integer> players, ArrayList<String> dataTitles){
        BottomSheetDialog dialog = new BottomSheetDialog(GroupActivity.this, R.style.BottomSheetDialogTheme);
        GroupAddDialogBinding binding = GroupAddDialogBinding.inflate(LayoutInflater.from(GroupActivity.this));

        dialog.getBehavior().setPeekHeight(300);
        dialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        TeamAdapter teamAdapter1 = new TeamAdapter();
        TeamAdapter teamAdapter2 = new TeamAdapter();
        teamAdapter2.team2(true);

        binding.team1.setAdapter(teamAdapter1);
        binding.team2.setAdapter(teamAdapter2);

        String[] names = new String[players.size()];
        players.keySet().toArray(names);

        for(int i = 0; i < players.size(); i++){
            if(players.get(names[i]) == 1){
                teamAdapter1.add(names[i]);
            }else{
                teamAdapter2.add(names[i]);
            }
        }

        binding.date.setText(DateFormat.getDateInstance().format(new Date(System.currentTimeMillis())));
        binding.calendar.setOnClickListener(view -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(GroupActivity.this, (datePicker, i, i1, i2) -> binding.date.setText(i2+". "+(i1+1)+". "+i),c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
        });

        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(GroupActivity.this, R.layout.support_simple_spinner_dropdown_item,names);
        binding.nameInput.setThreshold(1);
        binding.nameInput.setAdapter(autoCompleteAdapter);
        binding.nameInput.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(textView.getText().toString().isEmpty()){
                textView.setError("Napis tam aspon neco.");
                return true;
            }
            if(i == EditorInfo.IME_ACTION_DONE){
                if(!binding.toggle.isChecked()){
                    teamAdapter1.add(textView.getText().toString());
                }else{
                    teamAdapter2.add(textView.getText().toString());
                }
                textView.setText("");
                return false;
            }
            return false;
        });
        binding.nameInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = (String) adapterView.getItemAtPosition(i);
                if(!binding.toggle.isChecked()){
                    teamAdapter1.add(s);
                }else{
                    teamAdapter2.add(s);
                }
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(adapterView.getApplicationWindowToken(),0);
                binding.nameInput.setText("");
            }
        });

        DataAdapter dataAdapter = new DataAdapter(dataTitles);
        binding.data.setAdapter(dataAdapter);

        binding.save.setOnClickListener(view -> {
            boolean noError = true;
            if(teamAdapter1.getItemCount() == 0 || teamAdapter2.getItemCount() == 0){
                noError = false;
                Toast.makeText(GroupActivity.this,"Potrebuju cokoliv v timu",Toast.LENGTH_SHORT).show();
            }
            if(dataAdapter.getItemCount() == 0){
                noError = false;
                Toast.makeText(GroupActivity.this,"Potrebuji nejakou activitu", Toast.LENGTH_SHORT).show();
            }
            for(int i = 0; i < dataAdapter.getItemCount(); i++){
                DataAdapter.DataHolder holder = (DataAdapter.DataHolder) binding.data.findViewHolderForAdapterPosition(i);
                if(holder.binding.team1Val.getText().toString().isEmpty()){
                    noError = false;
                    holder.binding.team1Val.setError("Potrebuju cislo myslim");
                }
                if(holder.binding.team2Val.getText().toString().isEmpty()){
                    noError = false;
                    holder.binding.team2Val.setError("Potrebuju cislo myslim");
                }
            }

            if(noError){
                long time = 0;
                try {
                    time = DateFormat.getDateInstance().parse(binding.date.getText().toString()).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for(int i = 0; i < dataAdapter.getItemCount(); i++) {
                    DataAdapter.DataHolder holder = (DataAdapter.DataHolder) binding.data.findViewHolderForAdapterPosition(i);
                    MainAdapter.DataType dataType = new MainAdapter.DataType(
                            Integer.parseInt(holder.binding.team1Val.getText().toString()), Integer.parseInt(holder.binding.team2Val.getText().toString()),
                            teamAdapter1.getArray(), teamAdapter2.getArray(),
                            holder.binding.title.getText().toString(), MainAdapter.DataType.TYPE_LIST,
                            time,
                            null
                    );
                    mainAdapter.add(dataType);
                    addToJSON(dataType);
                }
                dialog.dismiss();
            }
        });


        dialog.setContentView(binding.getRoot());
        dialog.show();
    }

    private void addToJSON(MainAdapter.DataType dataType){
        try {
            JSONObject object = readJSON();
            JSONArray array = new JSONArray();
            if(object.has("games")){
                array = object.getJSONArray("games");
            }
            array.put(new Gson().toJson(dataType));
            object.put("games",array);

            saveJSON(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject readJSON(){
        File file = new File(getFilesDir(),"data.json");
        if(!file.exists()){
            return new JSONObject();
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null){
                builder.append(line);
                line = reader.readLine();
            }
            reader.close();
            return new JSONObject(builder.toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private void saveJSON(JSONObject o) {
        try {
            File file = new File(getFilesDir(),"data.json");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(o.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
