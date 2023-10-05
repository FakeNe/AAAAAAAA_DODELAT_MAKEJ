package com.example.aaaaaaaa_dodelat_makej;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.aaaaaaaa_dodelat_makej.a.adapters.DataAdapter;
import com.example.aaaaaaaa_dodelat_makej.a.adapters.TeamAdapter;
import com.example.aaaaaaaa_dodelat_makej.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private File file = null;

    private String groupID = "groupID";

    private String[] players = {"Martin","Michal","Filip","Petr"};
    private String loggedPlayer = "Filip";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        file = new File(getFilesDir(),groupID+".json");

        RecyclerView list = binding.list;
        ListAdapter listAdapter = new ListAdapter();
        list.setAdapter(listAdapter);

        listAdapter.add(new Game(1958321,new String[]{"Michal","Martin"}, new String[]{"Petr","Filip"}, new Game.Data("Pivo",18,21)));

        listAdapter.add(new Game(1978321,new String[]{"Petr","Martin"}, new String[]{"Petr","Filip"}, new Game.Data("Uklid",21,17)));

//        //INIT
//        JSONObject object = readData();
//
//        JSONArray games;
//        try {
//            if((games = object.getJSONArray("games")) == null) {
//                games = new JSONArray();
//            }
//            listAdapter.addArr(games);
//        } catch (JSONException e) {
//            Log.e("dataInit",e.toString());
//        }
//
//        //REMOVE
//        TextView textView = findViewById(R.id.text);
//        textView.setText(object.toString());
        //END REMOVE


    }

    private void bottomDialog(HashMap<String,Integer> playerNames, String[] dataNames){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet);

        RecyclerView team1 = dialog.findViewById(R.id.team1);
        TeamAdapter teamAdapter1 = new TeamAdapter();
        team1.setAdapter(teamAdapter1);

        RecyclerView team2 = dialog.findViewById(R.id.team2);
        TeamAdapter teamAdapter2 = new TeamAdapter();
//        teamAdapter2.setTeam(true);
        team2.setAdapter(teamAdapter2);

        //EDIT MORE TEAMS
        String[] names = new String[playerNames.size()];
        playerNames.keySet().toArray(names);

        for(int i = 0; i < playerNames.size(); i++){
            if(playerNames.get(names[i]) == 1){
                teamAdapter1.add(names[i]);
            }else{
                teamAdapter2.add(names[i]);
            }
        }
        //END EDIT MORE TEAMS

        SwitchCompat toggle = dialog.findViewById(R.id.toggle);

        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,names);

        AutoCompleteTextView autoCompleteTextView = dialog.findViewById(R.id.nameInput);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);
        autoCompleteTextView.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(textView.getText().toString().isEmpty()){
                textView.setError("Please fill in name.");
                return true;
            }
            if(i == EditorInfo.IME_ACTION_DONE){
                if(!toggle.isChecked()){
                    teamAdapter1.add(textView.getText().toString());
                }else{
                    teamAdapter2.add(textView.getText().toString());
                }
                textView.setText("");
                return false;
            }
            return false;
        });

        RecyclerView data = dialog.findViewById(R.id.data);
        DataAdapter dataAdapter = new DataAdapter(null);

        for (String dataName : dataNames) {
            dataAdapter.add(dataName);
        }
        data.setAdapter(dataAdapter);

        Button save = dialog.findViewById(R.id.save);
        save.setOnClickListener(view -> {
            boolean noError = true;
            if(teamAdapter1.getItemCount() == 0 || teamAdapter2.getItemCount() == 0){
                noError = false;
                Toast.makeText(MainActivity.this,"Need people in team.",Toast.LENGTH_SHORT).show();
            }
            for(int i = 0; i < dataAdapter.getItemCount(); i++){
                DataAdapter.DataHolder holder = (DataAdapter.DataHolder) data.findViewHolderForAdapterPosition(i);
//                if(holder.team1Val.getText().toString().isEmpty()){
//                    noError = false;
//                    holder.team1Val.setError("Fill in.");
//                }
//                if(holder.team2Val.getText().toString().isEmpty()){
//                    noError = false;
//                    holder.team2Val.setError("Fill in.");
//                }
            }

            if(noError){
                parseToJSON();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void parseToJSON(){

    }

    private void saveData(JSONObject o) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(o.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject readData(){
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

}