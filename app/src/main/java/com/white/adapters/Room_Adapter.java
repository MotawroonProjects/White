package com.white.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.white.R;

import com.white.databinding.LoadMoreRowBinding;
import com.white.databinding.UserSearchRowBinding;
import com.white.models.UserRoomModelData;
import com.white.ui.activity_room.ChatRoomActivity;

import java.util.List;

public class Room_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;

    private List<UserRoomModelData.UserRoomModel> userRoomModelList;
    private Context context;
    private ChatRoomActivity chatRoomActivity;

    public Room_Adapter(Context context, List<UserRoomModelData.UserRoomModel> userRoomModelList) {

        this.userRoomModelList = userRoomModelList;
        this.context = context;
        chatRoomActivity = (ChatRoomActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            UserSearchRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.user_search_row, parent, false);
            return new MyHolder(binding);
        } else {
            LoadMoreRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.load_more_row, parent, false);
            return new LoadMoreHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {

            UserRoomModelData.UserRoomModel userRoomModel = userRoomModelList.get(position);
            final MyHolder myHolder = (MyHolder) holder;
            myHolder.binding.setUserroommodel(userRoomModel);
            // myHolder.binding.tvDate.setText(Time_Ago.getTimeAgo(userRoomModel.getLast_message_date(),context));


            myHolder.itemView.setOnClickListener(v -> {
                UserRoomModelData.UserRoomModel userRoomModel1 = userRoomModelList.get(myHolder.getAdapterPosition());
                // fragment.setItemData(userRoomModel1,myHolder.getAdapterPosition());
                chatRoomActivity.setItemData(userRoomModel1, holder.getLayoutPosition());
            });
        } else {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.prgBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

            loadMoreHolder.binding.prgBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        return userRoomModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private UserSearchRowBinding binding;

        public MyHolder(UserSearchRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder {

        private LoadMoreRowBinding binding;

        public LoadMoreHolder(LoadMoreRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemViewType(int position) {
        UserRoomModelData.UserRoomModel userRoomModel = userRoomModelList.get(position);
        if (userRoomModel == null) {
            return ITEM_LOAD;
        } else {
            return ITEM_DATA;

        }


    }
}
