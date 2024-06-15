package com.example.bonjoo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bonjoo.adapter.InboxChatAdapter;
import com.example.bonjoo.adapter.SearchUserRecyclerAdapter;
import com.example.bonjoo.model.Chatroom;
import com.example.bonjoo.model.UserModel;
import com.example.bonjoo.utilities.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class ChatFragment extends Fragment {

    RecyclerView recyclerView;
    InboxChatAdapter adapter;


    public ChatFragment(){
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.inbox_recycler_view);
        setupRecyclerView();
        return view;
}

    private void setupRecyclerView() {
        Query query = FirebaseUtil.getAllChatroomReference()
                .whereArrayContains("userIDs",FirebaseUtil.currentUserID())
                .orderBy("lastMessageTime", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Chatroom> options = new FirestoreRecyclerOptions.Builder<Chatroom>().setQuery(query, Chatroom.class).build();

        adapter = new InboxChatAdapter(options,getContext()); //get a reference of your adapter and viewholder class
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));  //set its layout
        recyclerView.setAdapter(adapter); //set the adapter to the recycler layout
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
}
