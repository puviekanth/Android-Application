package com.example.bonjoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bonjoo.model.UserModel;
import com.example.bonjoo.utilities.FirebaseUtil;
import com.example.bonjoo.ChatActivity;
import com.example.bonjoo.R;
import com.example.bonjoo.model.UserModel;
import com.example.bonjoo.utilities.FirebaseUtil;
import com.example.bonjoo.utilities.androidUtilities;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Firebase;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserModelViewHolder> {

    Context context;
    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder userModelViewHolder, int i, @NonNull UserModel userModel) {
        userModelViewHolder.usernameText.setText(userModel.getUsername());
        userModelViewHolder.phoneNumberText.setText(userModel.getPhone());
        if(userModel.getUserID().equals(FirebaseUtil.currentUserID())){
            userModelViewHolder.usernameText.setText(userModel.getUsername()+" (Me)");  //differentiatiiting other users from own number
        }

        FirebaseUtil.getOtherProfilePic(userModel.getUserID()).getDownloadUrl().addOnCompleteListener(t->{
            if(t.isSuccessful()){
                Uri uri = t.getResult();
                androidUtilities.setProfilePic(context,uri,userModelViewHolder.profilePic);
            }
        });

        userModelViewHolder.itemView.setOnClickListener(v->{
            //navigate to an item view once you have clicked a search option
            Intent intent = new Intent(context, ChatActivity.class);
            androidUtilities.PassUserModalAsIntent(intent,userModel);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        });

    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row,parent,false);
        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder{

        TextView usernameText;
        TextView phoneNumberText;
        ImageView profilePic;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.username_text);
            phoneNumberText = itemView.findViewById(R.id.phone_text);
            profilePic = itemView.findViewById(R.id.profile_pic);
        }
    }

}
