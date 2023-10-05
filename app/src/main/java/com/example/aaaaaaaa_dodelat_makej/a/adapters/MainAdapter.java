package com.example.aaaaaaaa_dodelat_makej.a.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aaaaaaaa_dodelat_makej.R;
import com.example.aaaaaaaa_dodelat_makej.a.graphs.BlockGraph;
import com.example.aaaaaaaa_dodelat_makej.databinding.GroupGraphBinding;
import com.example.aaaaaaaa_dodelat_makej.databinding.GroupListBinding;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

    private ArrayList<DataType> array = new ArrayList<>();
    private String user;

    public MainAdapter(String user){
        this.user = user;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case DataType.TYPE_GRAPH:
                return new MainHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.group_graph,parent,false));
            case DataType.TYPE_LIST:
                return new MainHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list,parent,false));
        }
        return new MainHolder(new TextView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        switch (array.get(position).type){
            case DataType.TYPE_GRAPH:
                graphBinding(holder.itemView,array.get(position));
                break;
            case DataType.TYPE_LIST:
                listBinding(holder.itemView,array.get(position));
                break;
            default:
                ((TextView)holder.itemView).setText("Unsupported type.");
        }
    }

    private void graphBinding(View view, DataType dataType){
        GroupGraphBinding binding = GroupGraphBinding.bind(view);

        binding.title.setText(dataType.title);

        GraphAdapter adapter = new GraphAdapter();

        HashMap<String,HashMap<String,Double>> arr = new HashMap<>();

        int numOfGames = 0;
        HashMap<String,Double> maxScore = new HashMap<>();
        HashMap<String,Double> percent = new HashMap<>();

        for (DataType type : array){
            if(type.type == DataType.TYPE_LIST){
                HashMap<String,Double> map;
                if((map=arr.get(type.title))==null){
                    map = new HashMap<>();
                }

                int v1 = 0, v2 = 0;

                for(String s:type.team1){
                    double n = 0;
                    if(map.containsKey(s)){
                        n = map.get(s);
                    }
                    n += type.score1;
                    v1 = type.score1;
                    map.put(s,n);
                }
                for(String s:type.team2){
                    double n = 0;
                    if(map.containsKey(s)){
                        n = map.get(s);
                    }
                    n += type.score2;
                    v2 = type.score2;
                    map.put(s,n);
                }

                int max = Math.max(v1,v2);
                double tmp = 0;
                if(maxScore.containsKey(type.title)){
                    tmp = maxScore.get(type.title);
                }
                tmp += max;
                maxScore.put(type.title,tmp);

                arr.put(type.title,map);
                numOfGames++;
            }
        }

        for(String s:arr.keySet()){
            ArrayList<BlockGraph.DataModel> a = new ArrayList<>();

            HashMap<String,Double> m = arr.get(s);

            for(String q:m.keySet()){
                a.add(new BlockGraph.DataModel(q,(m.get(q)/maxScore.get(s))*100, dataType.colors.get(q)+"")); //ERROR: color exception
            }

            adapter.add(a,s+" n: "+(numOfGames/2)+" max: " + maxScore.get(s));
        }

        binding.list.setOnFlingListener(null);
        new PagerSnapHelper().attachToRecyclerView(binding.list);
        binding.list.setAdapter(adapter);
    }

    private void listBinding(View view, DataType dataType){
        GroupListBinding binding = GroupListBinding.bind(view);

        boolean absent = true;

        binding.title.setText(dataType.title);
        binding.date.setText(DateFormat.getDateInstance().format(new Date(dataType.date)));

        binding.team1.removeAllViews();
        binding.team2.removeAllViews();

        for(String s:dataType.team1){
            TextView textView = new TextView(binding.team1.getContext());
            textView.setText(s);
            textView.setTextColor(binding.team1.getContext().getColor(R.color.absent));
            if(s.equals(user)){
                textView.setTextColor(binding.team1.getContext().getColor(R.color.white));
                absent = false;
            }
            binding.team1.addView(textView);
        }
        for(String s:dataType.team2){
            TextView textView = new TextView(binding.team2.getContext());
            textView.setText(s);
            textView.setTextColor(binding.team1.getContext().getColor(R.color.absent));
            if(s.equals(user)){
                textView.setTextColor(binding.team1.getContext().getColor(R.color.white));
                absent = false;
            }
            binding.team2.addView(textView);
        }
        binding.score1.setText(dataType.score1+"");
        binding.score2.setText(dataType.score2+"");

        //TODO: OPTION win by lowest score or win by highest score
        if(dataType.score1 > dataType.score2){
            if(dataType.team1.contains(user)){
                //win
                binding.state.setText("Výhra");
                binding.title.setTextColor(binding.title.getContext().getColor(R.color.win));
                binding.getRoot().setBackground(AppCompatResources.getDrawable(binding.getRoot().getContext(),R.drawable.group_list_bg_win));
            }else if(dataType.team2.contains(user)){
                //lose
                binding.state.setText("Prohra");
                binding.title.setTextColor(binding.title.getContext().getColor(R.color.lose));
                binding.getRoot().setBackground(AppCompatResources.getDrawable(binding.getRoot().getContext(),R.drawable.group_list_bg_lose));
            }else{
                //absent
                binding.state.setText("Chyběl");
                binding.title.setTextColor(binding.title.getContext().getColor(R.color.absent));
                binding.getRoot().setBackground(AppCompatResources.getDrawable(binding.getRoot().getContext(),R.drawable.group_list_bg_absent));
            }
        }else if(dataType.score1 < dataType.score2){
            if(dataType.team1.contains(user)){
                //lose
                binding.state.setText("Prohra");
                binding.title.setTextColor(binding.title.getContext().getColor(R.color.lose));
                binding.getRoot().setBackground(AppCompatResources.getDrawable(binding.getRoot().getContext(),R.drawable.group_list_bg_lose));
            }else if(dataType.team2.contains(user)){
                //win
                binding.state.setText("Výhra");
                binding.title.setTextColor(binding.title.getContext().getColor(R.color.win));
                binding.getRoot().setBackground(AppCompatResources.getDrawable(binding.getRoot().getContext(),R.drawable.group_list_bg_win));
            }else{
                //absent
                binding.state.setText("Chyběl");
                binding.title.setTextColor(binding.title.getContext().getColor(R.color.absent));
                binding.getRoot().setBackground(AppCompatResources.getDrawable(binding.getRoot().getContext(),R.drawable.group_list_bg_absent));
            }
        }else{
            //draw
            if(!absent) {
                binding.state.setText("Remíza");
                binding.title.setTextColor(binding.title.getContext().getColor(R.color.tie));
                binding.getRoot().setBackground(AppCompatResources.getDrawable(binding.getRoot().getContext(), R.drawable.group_list_bg_tie));
            }else{
                //absent
                binding.state.setText("Chyběl");
                binding.title.setTextColor(binding.title.getContext().getColor(R.color.absent));
                binding.getRoot().setBackground(AppCompatResources.getDrawable(binding.getRoot().getContext(),R.drawable.group_list_bg_absent));
            }
        }
    }



    public void add(DataType dataType){
        array.add(dataType);

        Collections.sort(array, (dataType1, t1) -> {
            if(dataType1.type == DataType.TYPE_GRAPH){
                return 1;
            }

            if(dataType1.date > t1.date){
                return -1;
            }else if (dataType1.date < t1.date){
                return 1;
            }else {
                return 0;
            }
        });

        notifyItemInserted(array.size());
        notifyItemRangeChanged(0,array.size());
    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position).type;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    static class MainHolder extends RecyclerView.ViewHolder {

        View itemView;

        public MainHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    public static class DataType {

        public static final int TYPE_GRAPH = 1;
        public static final int TYPE_LIST = 2;

        int score1;
        int score2;
        ArrayList<String> team1;
        ArrayList<String> team2;
        String title;
        int type;
        long date;

        HashMap<String,Integer> colors;

        public DataType(int score1, int score2, ArrayList<String> team1, ArrayList<String> team2, String title, int type, long date, HashMap<String,Integer> colors) {
            this.score1 = score1;
            this.score2 = score2;
            this.team1 = team1;
            this.team2 = team2;
            this.title = title;
            this.type = type;
            this.date = date;
            this.colors = colors;
        }

        @Override
        public String toString() {
            return "com.example.aaaaaaaa_dodelat_makej.a.adapters.MainAdapter$DataType{" +
                    "score1=" + score1 +
                    ", score2=" + score2 +
                    ", team1=" + team1 +
                    ", team2=" + team2 +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", date=" + date +
                    ", colors=" + colors +
                    '}';
        }
    }
}
