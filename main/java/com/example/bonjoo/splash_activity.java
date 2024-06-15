package com.example.bonjoo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bonjoo.model.UserModel;
import com.example.bonjoo.utilities.FirebaseUtil;
import com.example.bonjoo.utilities.FirebaseUtil;
import com.example.bonjoo.utilities.androidUtilities;

public class splash_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

       if( FirebaseUtil.isLoggedIn() &&   getIntent().getExtras()!=null){ //if from notification
           String userID = getIntent().getExtras().getString("userID");
           FirebaseUtil.allUserCollectionReference().document("userID").get().addOnCompleteListener(task->{
              if(task.isSuccessful()){
                  UserModel userModel = task.getResult().toObject(UserModel.class);

                  Intent mainIntent = new Intent(this, MainActivity.class);
                  mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                  startActivity(mainIntent);

                  Intent intent = new Intent(this, ChatActivity.class);
                  assert userModel != null;
                  androidUtilities.PassUserModalAsIntent(intent,userModel);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  startActivity(intent);
                  finish();
              }
           });

       }else{
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   if(FirebaseUtil.isLoggedIn()){
                       Intent intent = new Intent(splash_activity.this, MainActivity.class);
                       startActivity(intent);
                   }else{
                       Intent intent = new Intent(splash_activity.this, com.example.bonjoo.phone_number_login.class);
                       startActivity(intent);
                   }

                   finish();
               }
           },1000);
       }

    }
}