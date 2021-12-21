package com.white.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.white.R;
import com.white.databinding.DiseaseRowBinding;
import com.white.models.DiseaseModel;
import com.white.ui.activity_edit_profile.EditprofileActivity;
import com.white.ui.activity_sign_up.SignUpActivity;

import java.util.List;

public class DiseasesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DiseaseModel> list;
    private Context context;
    private LayoutInflater inflater;
    private SignUpActivity activity;
    private EditprofileActivity editprofileActivity;

    public DiseasesAdapter(List<DiseaseModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        DiseaseRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.disease_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));



        myHolder.binding.imageClose.setOnClickListener(v -> {
            if(context instanceof SignUpActivity){
                activity=(SignUpActivity)context;
            activity.deleteSelectedDisease(myHolder.getAdapterPosition());
        }
        else if(context instanceof  EditprofileActivity){
                editprofileActivity=(EditprofileActivity) context;
                editprofileActivity.deleteSelectedDisease(myHolder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public DiseaseRowBinding binding;

        public MyHolder(@NonNull DiseaseRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
