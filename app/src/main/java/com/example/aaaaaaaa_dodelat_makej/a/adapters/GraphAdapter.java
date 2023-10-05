package com.example.aaaaaaaa_dodelat_makej.a.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aaaaaaaa_dodelat_makej.a.graphs.BlockGraph;
import com.example.aaaaaaaa_dodelat_makej.databinding.DataItemBinding;
import com.example.aaaaaaaa_dodelat_makej.databinding.GraphItemBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class GraphAdapter extends RecyclerView.Adapter<GraphAdapter.GraphHolder> {

    ArrayList<ArrayList<BlockGraph.DataModel>> arr = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();

    @NonNull
    @Override
    public GraphHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GraphHolder(GraphItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GraphHolder holder, int position) {
        holder.binding.graph.add(arr.get(position));
        holder.binding.title.setText(titles.get(position));
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public void add(ArrayList<BlockGraph.DataModel> map,String title){
        arr.add(map);
        titles.add(title);
        notifyItemInserted(arr.size());
        notifyItemRangeChanged(0,arr.size());
    }

    class GraphHolder extends RecyclerView.ViewHolder {

        public GraphItemBinding binding;

        public GraphHolder(@NonNull GraphItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
