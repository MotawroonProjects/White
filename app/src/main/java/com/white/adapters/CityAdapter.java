package com.white.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.white.R;
import com.white.databinding.CityRowBinding;
import com.white.models.CityModel;
import com.white.ui.activity_doctor.DoctorActivity;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CityModel> list;
    private Context context;
    private LayoutInflater inflater;
    private DoctorActivity activity;
private int i=-1;
    public CityAdapter(List<CityModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (DoctorActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CityRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.city_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

myHolder.itemView.setOnClickListener(new
                                             View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View view) {
                                                     i=position;
                                                     activity.setcity(list.get(i).getId());

                                                     notifyDataSetChanged();
                                                 }
                                             });
if(i==position){
    myHolder.binding.imageDelete.setChecked(true);
}
else {
    myHolder.binding.imageDelete.setChecked(false);

}


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public CityRowBinding binding;

        public MyHolder(@NonNull CityRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
