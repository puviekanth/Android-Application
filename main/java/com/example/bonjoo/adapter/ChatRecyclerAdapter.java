package com.example.bonjoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bonjoo.utilities.FirebaseUtil;
import com.example.bonjoo.ChatActivity;
import com.example.bonjoo.R;
import com.example.bonjoo.model.ChatMessageModel;
import com.example.bonjoo.utilities.FirebaseUtil;
import com.example.bonjoo.utilities.androidUtilities;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel, ChatRecyclerAdapter.ChatModelViewHolder> {

    Context context;
    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder ChatModelViewHolder, int i, @NonNull ChatMessageModel ChatMessageModel) {
        if(ChatMessageModel.getSenderID().equals(FirebaseUtil.currentUserID())){
            ChatModelViewHolder.reciever_layout.setVisibility(View.GONE);
            ChatModelViewHolder.sender_layout.setVisibility(View.VISIBLE);
            ChatModelViewHolder.sender_textview.setText(ChatMessageModel.getMessage());
        }else {
            ChatModelViewHolder.reciever_layout.setVisibility(View.VISIBLE);
            ChatModelViewHolder.sender_layout.setVisibility(View.GONE);
            ChatModelViewHolder.reciever_textview.setText(ChatMessageModel.getMessage());

        }

    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row,parent,false);
        return new ChatModelViewHolder(view);
    }

    class ChatModelViewHolder extends RecyclerView.ViewHolder{

        LinearLayout sender_layout, reciever_layout;
        TextView sender_textview,reciever_textview;


        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);
            sender_layout = itemView.findViewById(R.id.sender_chat_layout);
            reciever_layout = itemView.findViewById(R.id.reciever_chat_layout);
            sender_textview = itemView.findViewById(R.id.sender_chat_textview);
            reciever_textview = itemView.findViewById(R.id.recieveer_chat_textview);

        }
    }

}
