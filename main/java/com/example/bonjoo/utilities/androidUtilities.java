package com.example.bonjoo.utilities;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bonjoo.model.UserModel;
import com.example.bonjoo.model.UserModel;
import com.firebase.ui.auth.data.model.User;

public class androidUtilities {

    public static void ShowToast(Context context,String message){

        Toast.makeText(context,message,Toast.LENGTH_LONG).show();

    }

    public static void PassUserModalAsIntent(Intent intent, UserModel userModel){
        intent.putExtra("username", userModel.getUsername());
        intent.putExtra("phone",userModel.getPhone());
        intent.putExtra("userId", userModel.getUserID());
    }

    public static UserModel getUserModalFromIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setPhone(intent.getStringExtra("phone"));
        userModel.setUserID(intent.getStringExtra("userId"));
        return userModel;
    }

    public static void setProfilePic(Context context, Uri imageURI, ImageView imageView){
        Glide.with(context).load(imageURI).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

}
