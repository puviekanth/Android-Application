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

import com.example.bonjoo.ChatActivity;
import com.example.bonjoo.R;
import com.example.bonjoo.model.*;
import com.example.bonjoo.model.UserModel;
import com.example.bonjoo.utilities.FirebaseUtil;
import com.example.bonjoo.utilities.androidUtilities;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class InboxChatAdapter extends FirestoreRecyclerAdapter<Chatroom, InboxChatAdapter.ChatroomViewHolder> {

    Context context;
    public InboxChatAdapter(@NonNull FirestoreRecyclerOptions<Chatroom> options, Context context) {
        super(options);
        this.context = context;
    }



    @NonNull
    @Override
    public ChatroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inbox_chat_recycler_row,parent,false);
        return new ChatroomViewHolder(view);
    }



    @Override
    protected void onBindViewHolder(@NonNull ChatroomViewHolder chatroomViewHolder, int i, @NonNull Chatroom chatroom) {
        FirebaseUtil.geOtherUserFromChatroom(chatroom.getUserIDs()).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        boolean lastMessageSentByYou =chatroom.getLastMessageSenderID().equals(FirebaseUtil.currentUserID());
                        UserModel otherUser = task.getResult().toObject(UserModel.class);

                        FirebaseUtil.getOtherProfilePic(otherUser.getUserID()).getDownloadUrl().addOnCompleteListener(t->{
                            if(t.isSuccessful()){
                                Uri uri = t.getResult();
                                androidUtilities.setProfilePic(context,uri,chatroomViewHolder.profilePic);
                            }
                        });


                        chatroomViewHolder.usernameText.setText(otherUser.getUsername());
                        if(lastMessageSentByYou){
                            chatroomViewHolder.lastMessageText.setText("You: "+chatroom.getLastMessage());
                        }else{
                            chatroomViewHolder.lastMessageText.setText(chatroom.getLastMessage());
                        }

                        chatroomViewHolder.itemView.setOnClickListener(v->{
                            //navigate to an item view once you have clicked a search option
                            Intent intent = new Intent(context, ChatActivity.class);
                            androidUtilities.PassUserModalAsIntent(intent,otherUser);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        });

                        chatroomViewHolder.lastMessageTimeText.setText(FirebaseUtil.timesampToTimeString(chatroom.getLastMessageTime()));
                    }
                });

    }

    class ChatroomViewHolder extends RecyclerView.ViewHolder{

        TextView usernameText;
        TextView lastMessageText;
        TextView lastMessageTimeText;
        ImageView profilePic;

        public ChatroomViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.username_text);
            lastMessageText = itemView.findViewById(R.id.last_message_text);
            lastMessageTimeText = itemView.findViewById(R.id.last_message_time_text);
            profilePic = itemView.findViewById(R.id.profile_pic);
        }
    }

}
