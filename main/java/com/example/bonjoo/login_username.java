package com.example.bonjoo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bonjoo.model.UserModel;
import com.example.bonjoo.utilities.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class login_username extends AppCompatActivity {


    EditText username;
    Button start;
    String phonenumber;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_username);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        username = findViewById(R.id.login_username);
        start = findViewById(R.id.btn_login_start);

        phonenumber = getIntent().getExtras().getString("phone");
        getUsername(); //get Username from DB
        start.setOnClickListener(v -> {
            setUsername(); // once start button clicked, then the username is set to a variable and if its is new(get method giving null) then add it into the DS
        });
    }

    void setUsername(){

        String usersName = username.getText().toString();
        if(usersName.isEmpty() || usersName.length()<3){
            username.setError("Length of Username should be at least 3 characters");
            return;
        }
        setInProgress(true);
        if(userModel!=null){
            userModel.setUsername(usersName);
        }
        else{
            userModel = new UserModel(usersName,phonenumber, Timestamp.now(),FirebaseUtil.currentUserID());
        }
        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {   //add the details to the DB
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(login_username.this,MainActivity.class); //once completed logging
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // clear all login task and create new task for main Activity
                startActivity(intent);
            }
        });

    }

    void getUsername(){
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override   //retrieving the current user details from the DB and assigning it as a task and on Complete
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()){ //if retrieving successful then
                    userModel = task.getResult().toObject(UserModel.class); //convert the returned DB user details into UserModel class object
                    if(userModel!=null){
                        username.setText(userModel.getUsername()); // assigning the retrieved data to values
                    }
                }
            }
        });
    }


    void setInProgress(boolean inProgress){
        if(inProgress){
            start.setVisibility(View.GONE);
        }
        else{
            start.setVisibility(View.VISIBLE);
        }
    }
}