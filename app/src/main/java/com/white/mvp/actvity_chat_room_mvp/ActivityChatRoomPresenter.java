package com.white.mvp.actvity_chat_room_mvp;

import android.content.Context;
import android.util.Log;

import com.white.R;
import com.white.models.UserModel;
import com.white.models.UserRoomModelData;
import com.white.preferences.Preferences;
import com.white.remote.Api;
import com.white.tags.Tags;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityChatRoomPresenter {
    private UserModel userModel;
    private Preferences preferences;
    private ChatRoomActivityView view;
    private Context context;

    public ActivityChatRoomPresenter(ChatRoomActivityView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void backPress() {

        view.onFinished();


    }


    public void getRooms(UserModel userModel) {

        view.onProgressShow();
        try {
            Api.getService(Tags.base_url)
                    .getRooms(userModel.getData().getId(),"patient", "off")
                    .enqueue(new Callback<UserRoomModelData>() {
                        @Override
                        public void onResponse(Call<UserRoomModelData> call, Response<UserRoomModelData> response) {
                            view.onProgressHide();
                           // Log.e("kdkdkdk", response.body().getRooms().size() + "");
                            if (response.isSuccessful() && response.body() != null && response.body().getData()!= null) {
                                view.ondata(response.body());
                            } else {
                                if (response.code() == 500) {
                                    //   Toast.makeText(com.ghiar.activities_fragments.activity_room.ChatRoomActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    view.onFailed(context.getResources().getString(R.string.server_error));

                                } else {
                                    view.onFailed(context.getResources().getString(R.string.failed));

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserRoomModelData> call, Throwable t) {
                            try {
                                view.onProgressHide();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        view.onFailed(context.getResources().getString(R.string.something));
                                    } else {
                                        view.onFailed(t.getMessage());
                                    }
                                }

                            } catch (Exception e) {

                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("eeee", e.getMessage() + "__");
            view.onProgressHide();
        }
    }


//    private void loadMore(int page)
//    {
//        try {
//
//            Api.getService(Tags.base_url)
//                    .getRooms(userModel.getId(),page)
//                    .enqueue(new Callback<UserRoomModelData>() {
//                        @Override
//                        public void onResponse(Call<UserRoomModelData> call, Response<UserRoomModelData> response) {
//                            userRoomModelList.remove(userRoomModelList.size()-1);
//                            room_adapter.notifyItemRemoved(userRoomModelList.size()-1);
//                            isLoading = false;
//
//                            if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
//                            {
//                                userRoomModelList.addAll(response.body().getData());
//                                room_adapter.notifyDataSetChanged();
//                                current_page = response.body().getCurrent_page();
//                            }else
//                            {
//                                if (response.code() == 500) {
//                                    Toast.makeText(ChatRoomActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//
//
//                                }else
//                                {
//                                    Toast.makeText(ChatRoomActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//
//                                    try {
//
//                                        Log.e("error",response.code()+"_"+response.errorBody().string());
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<UserRoomModelData> call, Throwable t) {
//                            try {
//                                if (userRoomModelList.get(userRoomModelList.size()-1)==null)
//                                {
//                                    userRoomModelList.remove(userRoomModelList.size()-1);
//                                    room_adapter.notifyItemRemoved(userRoomModelList.size()-1);
//                                    isLoading = false;
//                                }
//
//
//                                if (t.getMessage()!=null)
//                                {
//                                    Log.e("error",t.getMessage());
//                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
//                                    {
//                                        Toast.makeText(ChatRoomActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
//                                    }else
//                                    {
//                                        Toast.makeText(ChatRoomActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                            }catch (Exception e){}
//                        }
//                    });
//        }catch (Exception e){
//            binding.progBar.setVisibility(View.GONE);
//        }
//    }


}
