package com.example.aaaaaaaa_dodelat_makej;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aaaaaaaa_dodelat_makej.databinding.ListItemBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {

    private static final int TYPE_LIST = 1;
    private static final int TYPE_GRAPH = 2;

    private ArrayList<Game> arrayList = new ArrayList<>();

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListHolder(ListItemBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
        holder.binding.date.setText(new SimpleDateFormat("dd.MM.yyyy").format(new Date(arrayList.get(position).date)).toString());
        holder.binding.type.setText(arrayList.get(position).data.title);
        holder.binding.team1Data.setText(arrayList.get(position).data.score1+"");
        holder.binding.team2Data.setText(arrayList.get(position).data.score2+"");

        for(String name:arrayList.get(position).team1){
            TextView view = new TextView(holder.binding.getRoot().getContext());
            view.setText(name);
            holder.binding.team1Names.addView(view);
        }
        for(String name:arrayList.get(position).team2){
            TextView view = new TextView(holder.binding.getRoot().getContext());
            view.setText(name);
            holder.binding.team2Names.addView(view);
        }
    }

    public void add(Game g){
        arrayList.add(g);
        notifyItemInserted(arrayList.size());
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    static class ListHolder extends RecyclerView.ViewHolder {

        ListItemBinding binding;

        public ListHolder(@NonNull ListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
//    static class GraphHolder extends RecyclerView.ViewHolder {
//
//    }
}
