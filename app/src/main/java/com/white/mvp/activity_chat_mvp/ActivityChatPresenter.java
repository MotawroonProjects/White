package com.white.mvp.activity_chat_mvp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.white.R;
import com.white.models.ChatUserModel;
import com.white.models.MessageDataModel;
import com.white.models.MessageModel;
import com.white.models.UserModel;
import com.white.preferences.Preferences;
import com.white.remote.Api;
import com.white.share.Common;
import com.white.tags.Tags;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityChatPresenter {
    private UserModel userModel;
    private Preferences preferences;
    private ChatActivityView view;
    private Context context;

    public ActivityChatPresenter(ChatActivityView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void backPress() {

        view.onFinished();


    }


    public void getChatMessages(ChatUserModel chatUserModel) {
        try {
            view.onProgressShow();
            Api.getService(Tags.base_url)
                    .getRoomMessages(chatUserModel.getRoom_id(), "on", 20, 1)
                    .enqueue(new Callback<MessageModel>() {
                        @Override
                        public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                            view.onProgressHide();
                            if (response.isSuccessful() && response.body() != null) {
                                //  chatUserModel = new ChatUserModel(response.body().getRoom().getOther_user_name(), response.body().getRoom().getOther_user_avatar(), response.body().getRoom().getSecond_user_id()+"", response.body().getRoom().getId());
                                view.ondataload(response.body());

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                 //   e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    view.onFailed(context.getResources().getString(R.string.server_error));
                                    // Toast.makeText(com.motmaen_client.activities_fragments.chat_activity.ChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    view.onFailed(context.getResources().getString(R.string.failed));
                                    // Toast.makeText(com.motmaen_client.activities_fragments.chat_activity.ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageModel> call, Throwable t) {
                            try {
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {

                                        view.onFailed(context.getResources().getString(R.string.something));
                                        //  Toast.makeText(com.motmaen_client.activities_fragments.chat_activity.ChatActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        view.onFailed(t.getMessage());
                                        //  Toast.makeText(com.motmaen_client.activities_fragments.chat_activity.ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    public void loadMore(int next_page, ChatUserModel chatUserModel) {
        try {
            Log.e(";lll",next_page+"");
           // view.onloadmore();
            Api.getService(Tags.base_url)
                    .getRoomMessages(chatUserModel.getRoom_id(), "on", 20, next_page)
                    .enqueue(new Callback<MessageModel>() {
                        @Override
                        public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                         //   view.onfinishloadmore();

                            if (response.isSuccessful() && response.body() != null&&response.body().getData()!=null) {
                                view.ondataloadmore(response.body());


                            } else {

                                if (response.code() == 500) {
                                    view.onFailed(context.getResources().getString(R.string.server_error));
                                    // Toast.makeText(com.motmaen_client.activities_fragments.chat_activity.ChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    view.onFailed(context.getResources().getString(R.string.failed));

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (Exception e) {
//                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageModel> call, Throwable t) {
                            try {
                              //  view.onfinishloadmore();
                                view.onremove();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {

                                        view.onFailed(context.getResources().getString(R.string.something));
                                        //  Toast.makeText(com.motmaen_client.activities_fragments.chat_activity.ChatActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        view.onFailed(t.getMessage());
                                        //  Toast.makeText(com.motmaen_client.activities_fragments.chat_activity.ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }


    public void sendmessageimage(UserModel userModel, ChatUserModel chatUserModel, Uri url) {
        view.onLoad();

        RequestBody user_part = Common.getRequestBodyText(userModel.getData().getId() + "");
        RequestBody reciver_part = Common.getRequestBodyText(chatUserModel.getId() + "");
        RequestBody type_part = Common.getRequestBodyText("image");
        RequestBody room_part = Common.getRequestBodyText(chatUserModel.getRoom_id() + "");


        MultipartBody.Part image_part = Common.getMultiPart(context, Uri.parse(url.toString()), "image");

        Api.getService(Tags.base_url)
                .sendmessagewithimage(user_part, reciver_part, type_part, room_part, image_part)
                .enqueue(new Callback<MessageDataModel>() {
                    @Override
                    public void onResponse(Call<MessageDataModel> call, Response<MessageDataModel> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            //listener.onSuccess(response.body());

                            view.ondataload(response.body());
                        } else {
                            Log.e("codeimage", response.code() + "_");
                            try {

                                view.onFailed(context.getResources().getString(R.string.failed));
                                // Toast.makeText(com.motmaen_client.activities_fragments.chat_activity.ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                Log.e("respons", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // listener.onFailed(response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageDataModel> call, Throwable t) {
                        try {
                            view.onFinishload();
                            view.onFailed(context.getString(R.string.something));
                            //  Toast.makeText(com.motmaen_client.activities_fragments.chat_activity.ChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                        }
                    }
                });
    }


    public void sendmessagetext(String message, UserModel userModel, ChatUserModel chatUserModel) {
        view.onLoad();

        try {
            Api.getService(Tags.base_url)
                    .sendmessagetext(userModel.getData().getId() + "", chatUserModel.getId(), "message", chatUserModel.getRoom_id() + "", message).enqueue(new Callback<MessageDataModel>() {
                @Override
                public void onResponse(Call<MessageDataModel> call, Response<MessageDataModel> response) {

                    view.onFinishload();
                    // dialog.dismiss();
                    if (response.isSuccessful()) {

                        Log.e("llll", response.toString());

                        view.ondataload(response.body());
                    } else {
                        try {
                            view.onFailed(context.getResources().getString(R.string.failed));
                            //  Toast.makeText(com.motmaen_client.activities_fragments.chat_activity.ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.toString() + " " + response.code() + "" + response.message() + "" + response.errorBody() + response.raw() + response.body() + response.headers() + " " + response.errorBody().toString());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<MessageDataModel> call, Throwable t) {
                    view.onFinishload();
                    try {
                        view.onFailed(context.getResources().getString(R.string.failed));

                        // Toast.makeText(com.motmaen_client.activities_fragments.chat_activity.ChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            view.onFinishload();
            //  dialog.dismiss();
            Log.e("error", e.getMessage().toString());
        }
    }


}
