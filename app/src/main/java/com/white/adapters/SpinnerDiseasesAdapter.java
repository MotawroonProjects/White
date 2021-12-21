package com.white.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.white.R;
import com.white.databinding.SpinnerRowBinding;
import com.white.models.DiseaseModel;

import java.util.List;

public class SpinnerDiseasesAdapter extends BaseAdapter {
    private List<DiseaseModel> list;
    private Context context;
    private LayoutInflater inflater;

    public SpinnerDiseasesAdapter(List<DiseaseModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") SpinnerRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.spinner_row,parent,false);
        String title = list.get(position).getTitle();
        binding.setTitle(title);
        return binding.getRoot();
    }
}
