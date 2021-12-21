package com.white.preferences;

import android.content.Context;
import android.content.SharedPreferences;


import com.white.models.ChatUserModel;
import com.white.models.DrugTimeModel;
import com.white.models.UserModel;
import com.white.models.UserSettingsModel;
import com.white.tags.Tags;
import com.google.gson.Gson;

public class Preferences {

    private static Preferences instance = null;

    private Preferences() {
    }

    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
        }
        return instance;
    }

    public void create_update_user_settings(Context context, UserSettingsModel model) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String data = new Gson().toJson(model);
        editor.putString("settings", data);
        editor.apply();


    }

    public UserSettingsModel getUserSettings(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("settings_pref", Context.MODE_PRIVATE);
        UserSettingsModel model = new Gson().fromJson(preferences.getString("settings",""),UserSettingsModel.class);
        return model;

    }


   public void create_update_userdata(Context context, UserModel userModel) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = gson.toJson(userModel);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_data", user_data);
        editor.apply();
        create_update_session(context, Tags.session_login);

    }

  public UserModel getUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = preferences.getString("user_data", "");
        UserModel userModel = gson.fromJson(user_data, UserModel.class);
        return userModel;
    }
    public void create_update_session(Context context, String session) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("state", session);
        editor.apply();


    }


    public void create_room_id(Context context, String room_id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("room", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("room_id", room_id);
        editor.apply();


    }

    public String getRoomId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("room", Context.MODE_PRIVATE);
        String room_id = preferences.getString("room_id", Tags.session_logout);
        return room_id;
    }

    public String getSession(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        String session = preferences.getString("state", Tags.session_logout);
        return session;
    }

    public void clear(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.apply();
        SharedPreferences preferences2 = context.getSharedPreferences("room", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit2 = preferences2.edit();
        edit2.clear();
        edit2.apply();

        SharedPreferences preferences3 = context.getSharedPreferences("settings_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit3 = preferences3.edit();
        edit3.clear();
        edit3.apply();
        create_update_session(context, Tags.session_logout);
    }

    public void create_update_ChatUserData(Context context , ChatUserModel chatUserModel)
    {
        SharedPreferences preferences = context.getSharedPreferences("chatUserPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String userDataGson = gson.toJson(chatUserModel);
        editor.putString("chat_user_data",userDataGson);
        editor.apply();
    }

    public ChatUserModel getChatUserData(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("chatUserPref",Context.MODE_PRIVATE);
        String userDataGson = preferences.getString("chat_user_data","");
        return new Gson().fromJson(userDataGson, ChatUserModel.class);
    }

    public void clearChatUserData(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("chatUserPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
    public void create_update_time(Context context , DrugTimeModel model)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("drugtime", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String cart_data = gson.toJson(model);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("drugtime_data", cart_data);
        editor.apply();

    }

    public DrugTimeModel getTimeData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("drugtime", Context.MODE_PRIVATE);
        String json_data = sharedPreferences.getString("drugtime_data","");
        Gson gson = new Gson();
        DrugTimeModel model = gson.fromJson(json_data,DrugTimeModel.class);
        return model;
    }

    public void clearTime(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("drugtime", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.apply();
    }


}
