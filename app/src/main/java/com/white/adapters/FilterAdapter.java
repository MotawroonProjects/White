package com.white.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.white.R;
import com.white.databinding.DiseaseRowBinding;
import com.white.databinding.FilterRowBinding;
import com.white.models.FilterModel;
import com.white.ui.activity_doctor.DoctorActivity;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FilterModel> list;
    private Context context;
    private LayoutInflater inflater;
    private DoctorActivity activity;

    public FilterAdapter(List<FilterModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (DoctorActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        FilterRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.filter_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.imageDelete.setOnClickListener(view -> {
            FilterModel model = list.get(holder.getAdapterPosition());
            activity.deleteFilter(model,holder.getAdapterPosition());
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public FilterRowBinding binding;

        public MyHolder(@NonNull FilterRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
