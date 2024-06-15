package com.example.bonjoo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bonjoo.R;
import com.hbb20.CountryCodePicker;

public class phone_number_login extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText phoneNumber;
    Button sendOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phone_number_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        countryCodePicker = findViewById(R.id.login_countrycode);
        phoneNumber = findViewById(R.id.login_phonenumber);
        sendOTP = findViewById(R.id.btn_sendOTP);

        countryCodePicker.registerCarrierNumberEditText(phoneNumber);
        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!countryCodePicker.isValidFullNumber()){
                    phoneNumber.setError("Phone number invalid");
                    return;
                }
                Intent intent = new Intent(phone_number_login.this, login_OTP.class);
                intent.putExtra("phone",countryCodePicker.getFullNumberWithPlus());
                startActivity(intent);

            }
        });


    }
}