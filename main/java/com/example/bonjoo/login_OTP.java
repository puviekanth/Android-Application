package com.example.bonjoo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bonjoo.utilities.androidUtilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class login_OTP extends AppCompatActivity {

    String phoneNumber;

    EditText OTP;

    TextView resend_OTP;

    Button btnNext;
    Long timeoutSeconds = 60L;
    String verificationCode;

    PhoneAuthProvider.ForceResendingToken resendingToken;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_otp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        OTP = findViewById(R.id.login_otp);

        resend_OTP = findViewById(R.id.txt_resendOTP);
        btnNext = findViewById(R.id.btn_OTPlogin_next);
        phoneNumber = Objects.requireNonNull(getIntent().getExtras()).getString("phone");




        sendOTP(phoneNumber,false);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enterredOTP = OTP.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,enterredOTP);
                signIn(credential);
                setInProgress(true);
            }
        });

        resend_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP(phoneNumber,true);
            }
        });
    }

    void sendOTP(String phoneNumber,boolean isResend){
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder = new PhoneAuthOptions.Builder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signIn(phoneAuthCredential);
                        setInProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        androidUtilities.ShowToast(getApplicationContext(),"OTP verfication failed");
                        setInProgress(false);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationCode = s;
                        resendingToken = forceResendingToken;
                        androidUtilities.ShowToast(getApplicationContext(),"OTP sent successfully");
                        setInProgress(false);
                    }
                });

        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }
        else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }

    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            btnNext.setVisibility(View.GONE);
        }
        else{
            btnNext.setVisibility(View.VISIBLE);
        }
    }

    void signIn(PhoneAuthCredential phoneAuthCredential){
        //login and go to the username activity
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    Intent intent = new Intent(login_OTP.this, login_username.class);
                    intent.putExtra("phone",phoneNumber);
                    startActivity(intent);
                }
                else {
                    androidUtilities.ShowToast(getApplicationContext(),"OTP verification failed");
                }
            }
        });

    }

    void startResendTimer(){
        resend_OTP.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                resend_OTP.setText("Resend OTP in "+timeoutSeconds+" seconds");
                if(timeoutSeconds<=0){
                    timeoutSeconds=60L;
                    resend_OTP.setText("Resend OTP ");
                    timer.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resend_OTP.setEnabled(true);
                        }
                    });
                }
            }
        },0,1000);
    }
}