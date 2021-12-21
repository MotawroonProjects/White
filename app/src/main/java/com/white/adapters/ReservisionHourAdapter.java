package com.white.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.white.R;
import com.white.databinding.HourRowBinding;
import com.white.models.SingleReservisionTimeModel;
import com.white.ui.activity_reservation.ReservationActivity;

import java.util.List;

public class ReservisionHourAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SingleReservisionTimeModel> list;
    private Context context;
    private LayoutInflater inflater;
    private int i = 0;

    public ReservisionHourAdapter(List<SingleReservisionTimeModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        HourRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.hour_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(view -> {
            i = position;
            notifyDataSetChanged();
        });
        if (i == position) {
            myHolder.binding.tvchoose.setTextColor(context.getResources().getColor(R.color.color1));
            if (context instanceof ReservationActivity) {
                ReservationActivity clinicReservationActivity = (ReservationActivity) context;
                clinicReservationActivity.getchild(position);
                i = position;
            }
        } else {
            myHolder.binding.tvchoose.setTextColor(context.getResources().getColor(R.color.black));

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private HourRowBinding binding;

        public MyHolder(HourRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
