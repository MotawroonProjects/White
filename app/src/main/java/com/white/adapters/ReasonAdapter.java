package com.white.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.white.R;
import com.white.databinding.ReasonRowBinding;
import com.white.databinding.SpicialRowBinding;
import com.white.models.ReasonModel;
import com.white.ui.activity_reservision_detials.ReservationDetialsActivity;

import java.util.List;

public class ReasonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ReasonModel.Data> list;
    private Context context;
    private LayoutInflater inflater;
    private ReservationDetialsActivity activity;
private int i=-1;
    public ReasonAdapter(List<ReasonModel.Data> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (ReservationDetialsActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ReasonRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.reason_row, parent, false);
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
                                                     activity.setreason(list.get(i).getReason());

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
        public ReasonRowBinding binding;

        public MyHolder(@NonNull ReasonRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
