package com.white.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.white.R;
import com.white.databinding.ChildDrugRowBinding;
import com.white.models.DrugModel;

import java.util.List;

public class ChildDrugsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DrugModel.Drugs> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;

    public ChildDrugsAdapter(List<DrugModel.Drugs> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ChildDrugRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.child_drug_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private ChildDrugRowBinding binding;

        public MyHolder(ChildDrugRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }




}
