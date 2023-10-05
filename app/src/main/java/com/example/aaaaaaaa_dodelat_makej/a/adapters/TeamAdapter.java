package com.example.aaaaaaaa_dodelat_makej.a.adapters;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aaaaaaaa_dodelat_makej.R;
import com.example.aaaaaaaa_dodelat_makej.databinding.TeamItemBinding;

import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamHolder> {

    private ArrayList<String> arrayList;
    private boolean b = false;

    public TeamAdapter(){
        arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public TeamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TeamHolder(TeamItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TeamHolder holder, int position) {
        holder.binding.name.setText(arrayList.get(position));
        holder.binding.remove.setOnClickListener(view -> {
            arrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(0,arrayList.size());
        });
        if(b) {
            holder.binding.getRoot().getBackground().setColorFilter(holder.binding.getRoot().getResources().getColor(R.color.lose, holder.binding.getRoot().getContext().getTheme()), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void add(String s){
        arrayList.add(s);
        notifyItemInserted(arrayList.size());
        notifyItemRangeChanged(0,arrayList.size());
    }

    public ArrayList<String> getArray(){
        return arrayList;
    }

    public void team2(boolean b){
        this.b = b;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class TeamHolder extends RecyclerView.ViewHolder {

        public TeamItemBinding binding;

        public TeamHolder(@NonNull TeamItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
