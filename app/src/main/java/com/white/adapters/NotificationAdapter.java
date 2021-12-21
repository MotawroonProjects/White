package com.white.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.white.R;
import com.white.databinding.NotificationRowBinding;
import com.white.models.NotificationModel;
import com.white.models.UserModel;
import com.white.preferences.Preferences;
import com.white.ui.notification_activity.NotificationActivity;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationAdapterVH> {

    private List<NotificationModel.Data> notificationList;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    Preferences preferences;
    UserModel userModel;


    public NotificationAdapter(Context context) {
        this.context = context;
    }

    public NotificationAdapter(List<NotificationModel.Data> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public NotificationAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.notification_row, parent, false);
        return new NotificationAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapterVH holder, int position) {
        holder.binding.setModel(notificationList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof NotificationActivity){
                    NotificationActivity notificationActivity=(NotificationActivity) context;
                    notificationActivity.delete(holder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class NotificationAdapterVH extends RecyclerView.ViewHolder {
        public NotificationRowBinding binding;

        public NotificationAdapterVH(@NonNull NotificationRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
