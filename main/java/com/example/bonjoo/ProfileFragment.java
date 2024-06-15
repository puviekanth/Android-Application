package com.example.bonjoo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bonjoo.model.UserModel;
import com.example.bonjoo.utilities.FirebaseUtil;
import com.example.bonjoo.utilities.androidUtilities;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.net.URI;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ProfileFragment extends Fragment {

    ImageView profilepic;
    EditText username;
    EditText phone_no;
    Button update;
    TextView logout;
    UserModel currentUser;
    ActivityResultLauncher<Intent> imagePicker;

    Uri selectedImageURI;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePicker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode()== Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null && data.getData()!=null){
                            selectedImageURI= data.getData();
                            androidUtilities.setProfilePic(getContext(),selectedImageURI,profilepic);
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profilepic = view.findViewById(R.id.profile_picture);
        username = view.findViewById(R.id.profile_username);
        phone_no = view.findViewById(R.id.phone_no);
        update = view.findViewById(R.id.btn_update_profile);
        logout = view.findViewById(R.id.btn_logout);

        //get user data and display it into the profile view
        getUserData();

        update.setOnClickListener(task->{
            updateButtonClick();
        });

        logout.setOnClickListener(task->{
            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    FirebaseUtil.logout();
                    Intent intent = new Intent(getContext(), splash_activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        });

        profilepic.setOnClickListener(task->{
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512).createIntent(
                    new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePicker.launch(intent);
                            return null;
                        }
                    }
            );
        });

        return view;
    }
    public void updateButtonClick(){
        String newusersName = username.getText().toString();
        if(newusersName.isEmpty() || newusersName.length()<3){
            username.setError("Length of Username should be at least 3 characters");
            return;
        }
        currentUser.setUsername(newusersName);
        if(selectedImageURI!=null){
            FirebaseUtil.getCurrentProfilePic().putFile(selectedImageURI).addOnCompleteListener(task->{
               updateToDB();
            });
        }else {
            updateToDB();
        }
    }

    public void updateToDB(){
        FirebaseUtil.currentUserDetails().set(currentUser).addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                androidUtilities.ShowToast(getContext(),"Updated Successfully");
            }else{
                androidUtilities.ShowToast(getContext(),"Update Failed");
            }
        });
    }

    public void getUserData() {

        FirebaseUtil.getCurrentProfilePic().getDownloadUrl().addOnCompleteListener(task->{
            if(task.isSuccessful()){
                Uri uri = task.getResult();
                androidUtilities.setProfilePic(getContext(),uri,profilepic);
            }
        });
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task->{
            currentUser = task.getResult().toObject(UserModel.class);
            assert currentUser != null;
            username.setText(currentUser.getUsername());
            phone_no.setText(currentUser.getPhone());
        });
    }
}