package com.white.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.white.R;
import com.white.databinding.EmergencyDoctorRowBinding;
import com.white.models.SingleDoctorModel;
import com.white.ui.activity_emergency.EmergencyActivity;

import java.util.List;

public class EmergencyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SingleDoctorModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;

    public EmergencyAdapter(List<SingleDoctorModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        EmergencyDoctorRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.emergency_doctor_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(view -> {
            if(context instanceof EmergencyActivity){
                EmergencyActivity activity=(EmergencyActivity)context;
            SingleDoctorModel doctorModel = null;
            doctorModel = list.get(myHolder.getAdapterPosition());
            activity.setItemData(doctorModel,myHolder.binding,myHolder.getAdapterPosition());
        }});
        if (list.get(position).getOnline_time_status().equals("on")||list.get(position).getOnline_time().equals("0")){
            myHolder.binding.imgAvilable.setColorFilter(ContextCompat.getColor(context,R.color.green));
            // myHolder.binding.txtAvilable.setHintTextColor(R.color.green);
        }else {
            myHolder.binding.imgAvilable.setColorFilter(ContextCompat.getColor(context,R.color.gray5));

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private EmergencyDoctorRowBinding binding;

        public MyHolder(EmergencyDoctorRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }




}
