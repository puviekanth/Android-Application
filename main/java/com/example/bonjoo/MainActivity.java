package com.example.bonjoo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bonjoo.SearchActivity;
import com.example.bonjoo.utilities.FirebaseUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigation;
    ImageButton searchBtn;
    ChatFragment chatFragment;
    ProfileFragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        chatFragment = new ChatFragment();
        profileFragment = new ProfileFragment();
        bottomNavigation = findViewById(R.id.bottom_navigation);
        searchBtn = findViewById(R.id.search_btn);

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {  //when a menu in bottom navigation is clicked
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.chat ){   //check whether chat option is clicked
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fame_layout,chatFragment).commit(); //replace the main frame layout with chat fragment
                }
                if(menuItem.getItemId() == R.id.pofile ){   //check whether chat option is clicked
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fame_layout,profileFragment).commit(); //replace the main frame layout with chat fragment
                }
                return true;
            }
        });

        bottomNavigation.setSelectedItemId(R.id.chat); //by default chat option selected

        searchBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        getToken();

    }

    void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task->{
            if(task.isSuccessful()){
                String token = task.getResult();
                FirebaseUtil.currentUserDetails().update("fcmToken",token);
            }
        });
    }
}