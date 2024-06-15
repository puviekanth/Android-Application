package com.example.bonjoo;

import android.app.DownloadManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bonjoo.adapter.SearchUserRecyclerAdapter;
import com.example.bonjoo.model.UserModel;
import com.example.bonjoo.utilities.FirebaseUtil;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.Query;

import java.util.Queue;

public class SearchActivity extends AppCompatActivity {

    EditText searchInput;
    ImageButton backButon;
    ImageButton searchButton;
    RecyclerView recyclerView;
    SearchUserRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchInput = findViewById(R.id.search_username_input);
        searchButton = findViewById(R.id.search_user_btn);
        backButon = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.search_user_recycler_view);

        searchInput.requestFocus();

        backButon.setOnClickListener(v->{
            onBackPressed();
        });

        searchButton.setOnClickListener(v->{
            String searchTerm = searchInput.getText().toString();
            if(searchTerm.isEmpty() || searchTerm.length()<3){
                searchInput.setError("Invalid Username");
                return;
            }
            setUpSearchRecyclerView(searchTerm); //if username valid, to retrieve info from DB and show in recycler view
        });

    }

    public  void setUpSearchRecyclerView(String value){

        Query query = FirebaseUtil.allUserCollectionReference().whereGreaterThanOrEqualTo("username",value).whereLessThanOrEqualTo("username",value+'\uf8ff'); //search query based on username
        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>().setQuery(query, UserModel.class).build();

        adapter = new SearchUserRecyclerAdapter(options,getApplicationContext()); //get a reference of your adapter and viewholder class
        recyclerView.setLayoutManager(new LinearLayoutManager(this));  //set its layout
        recyclerView.setAdapter(adapter); //set the adapter to the recycler layout
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.startListening();
        }
    }
}