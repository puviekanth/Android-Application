package com.example.bonjoo;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bonjoo.adapter.ChatRecyclerAdapter;
import com.example.bonjoo.adapter.SearchUserRecyclerAdapter;
import com.example.bonjoo.model.ChatMessageModel;
import com.example.bonjoo.model.Chatroom;
import com.example.bonjoo.model.UserModel;
import com.example.bonjoo.utilities.FirebaseUtil;
import com.example.bonjoo.utilities.androidUtilities;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {
    ChatRecyclerAdapter adapter;

    Chatroom chatroomModel;

    UserModel otherUser;
    String chatroomID;
    TextView other_username;
    EditText input_message;
    ImageButton send_button;
    ImageView profile_pic;
    ImageButton back_button;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        otherUser = androidUtilities.getUserModalFromIntent(getIntent());  //getting details of searched user to show it on the chat screen
        chatroomID = FirebaseUtil.getChatRoomID(FirebaseUtil.currentUserID(),otherUser.getUserID()); //get the chatroom ID for person using the app and the other user
        other_username = findViewById(R.id.other_username);
        input_message = findViewById(R.id.input_message);
        back_button = findViewById(R.id.back_btn);
        send_button = findViewById(R.id.send_btn);
        profile_pic = findViewById(R.id.profile_pic);
        recyclerView = findViewById(R.id.recycler_view);


        back_button.setOnClickListener(v->{
            onBackPressed();
        });

        other_username.setText(otherUser.getUsername());

        send_button.setOnClickListener(v->{
            String message = input_message.getText().toString().trim();
            if(message.isEmpty()){
                return;
            }
            sendMessageToUser(message);
        });


        getOrCreateChatroom();
        setUpChatsInRecyclerView();

        FirebaseUtil.getOtherProfilePic(otherUser.getUserID()).getDownloadUrl().addOnCompleteListener(t->{
            if(t.isSuccessful()){
                Uri uri = t.getResult();
                androidUtilities.setProfilePic(this,uri,profile_pic);
            }
        });


    }

    private void setUpChatsInRecyclerView() {
        Query query = FirebaseUtil.getChatroomMessageReference(chatroomID).orderBy("timestamp", Query.Direction.DESCENDING); //get msgs from chatroom DB and sort them according to time
        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>().setQuery(query, ChatMessageModel.class).build();

        adapter = new ChatRecyclerAdapter(options,getApplicationContext());//get a reference of your adapter which has  viewholder class
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);  //set its layout
        recyclerView.setAdapter(adapter); //set the adapter to the recycler layout
        adapter.startListening();
        //set scrolling for the chat recycler view
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });

    }

    void getOrCreateChatroom(){  //get the chats from DB and store it in the chatroom model class
        FirebaseUtil.getChatroomReference(chatroomID).get().addOnCompleteListener(task->{
            if(task.isSuccessful()){
                chatroomModel = task.getResult().toObject(Chatroom.class);
                if(chatroomModel == null){  //if the returned chatroom is null, when its nothing, new convo then create a chatroom
                    chatroomModel = new Chatroom(chatroomID, Arrays.asList(FirebaseUtil.currentUserID(),otherUser.getUserID()), Timestamp.now(),"");
                    FirebaseUtil.getChatroomReference(chatroomID).set(chatroomModel); //set the chatroom collection in DB with chats
                }
            }
        });

    }

    void sendMessageToUser(String message){

        chatroomModel.setLastMessageTime(Timestamp.now());
        chatroomModel.setLastMessageSenderID(FirebaseUtil.currentUserID()); //update the time and last sender in the chatroom
        chatroomModel.setLastMessage(message);
        FirebaseUtil.getChatroomReference(chatroomID).set(chatroomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message,FirebaseUtil.currentUserID(),Timestamp.now()); //create message
        FirebaseUtil.getChatroomMessageReference(chatroomID).add(chatMessageModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    input_message.setText("");
                }
            }
        });

    }
}