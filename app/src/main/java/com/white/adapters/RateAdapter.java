package com.white.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.white.R;
import com.white.databinding.RateRowBinding;
import com.white.models.SingleDoctorModel;

import java.util.List;

public class RateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SingleDoctorModel.Rates> list;
    private Context context;
    private LayoutInflater inflater;

    public RateAdapter(List<SingleDoctorModel.Rates> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RateRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.rate_row, parent, false);
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
        private RateRowBinding binding;

        public MyHolder(RateRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }




}
