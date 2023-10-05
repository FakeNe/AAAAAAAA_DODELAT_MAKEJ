package com.example.aaaaaaaa_dodelat_makej.a.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aaaaaaaa_dodelat_makej.R;
import com.example.aaaaaaaa_dodelat_makej.databinding.DataItemBinding;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataHolder> {

    private ArrayList<String> arrayList;

    public DataAdapter(ArrayList<String> s){
        arrayList = s;
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DataHolder(DataItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, int position) {
        holder.binding.title.setText(arrayList.get(position));
        if(position == getItemCount()-1){
            holder.binding.team2Val.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }
//TODO: DIALOG
//        holder.binding.close.setOnClickListener(view -> remove(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void add(String s){
        arrayList.add(s);
        notifyItemInserted(arrayList.size());
        notifyItemRangeChanged(0,arrayList.size());
    }
    public void remove(int p){
        arrayList.remove(p);
        notifyItemRemoved(p);
        notifyItemRangeChanged(0,arrayList.size());
    }

    public ArrayList<String> getArray(){
        return arrayList;
    }

    public static class DataHolder extends RecyclerView.ViewHolder{

        public DataItemBinding binding;

        public DataHolder(@NonNull DataItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
