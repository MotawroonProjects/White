package com.white.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.white.R;
import com.white.databinding.DrugRowBinding;
import com.white.models.DrugModel;
import com.white.ui.activity_home.fragments.Fragment_Medicine;

import java.util.ArrayList;
import java.util.List;

public class DrugsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DrugModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;
    private Fragment_Medicine fragment_medicine;

    public DrugsAdapter(List<DrugModel> list, Context context, Fragment_Medicine fragment_medicine) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;
        this.fragment_medicine = fragment_medicine;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DrugRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.drug_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        List<DrugModel.Drugs> drugsArrayList = new ArrayList<>();
        if (list.get(position).getReservations_drugs().size() > 2) {
            for (int i = 0; i < 2; i++) {
                drugsArrayList.add(list.get(position).getReservations_drugs().get(i));
            }
        } else {
            drugsArrayList.addAll(list.get(position).getReservations_drugs());
        }
        ChildDrugsAdapter childDrugsAdapter = new ChildDrugsAdapter(drugsArrayList, context);
        myHolder.binding.recview.setLayoutManager(new LinearLayoutManager(context));
        myHolder.binding.recview.setAdapter(childDrugsAdapter);
        myHolder.binding.showmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_medicine.setItemData(list.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private DrugRowBinding binding;

        public MyHolder(DrugRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
