package com.example.bonjoo.utilities;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {

    public static boolean isLoggedIn(){ //checking whether user has already logged in or not
        //if logged in then the user will have an id
        return currentUserID() != null;
    }

    public static String currentUserID(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserID()); //read users collection to ge current user details
    }

    public static CollectionReference allUserCollectionReference(){ //create users collection
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static  DocumentReference getChatroomReference(String chatroomID){
        return FirebaseFirestore.getInstance().collection("chatroom").document(chatroomID);
    }


    public static String getChatRoomID(String userID1,String userID2){
        if(userID1.hashCode()<userID2.hashCode()){
            return userID1+"_"+userID2;
        }
        else{
            return userID2+"_"+userID1;
        }
    }

    public static  CollectionReference getChatroomMessageReference(String chatroomID){
        return getChatroomReference(chatroomID).collection("chats");
    }

    public static CollectionReference getAllChatroomReference(){
        return FirebaseFirestore.getInstance().collection("chatroom");
    }

    public static DocumentReference geOtherUserFromChatroom(List<String> userIDs){
        if(userIDs.get(0).equals(FirebaseUtil.currentUserID())){
            return allUserCollectionReference().document(userIDs.get(1));
        }else{
            return allUserCollectionReference().document(userIDs.get(0));
        }
    }

    public static String timesampToTimeString(Timestamp timestamp){
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }

    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }

    public static StorageReference getCurrentProfilePic(){
        return FirebaseStorage.getInstance().getReference().child("Profile_Pic").child(FirebaseUtil.currentUserID());
    }

    public static StorageReference getOtherProfilePic(String otherUserid){
        return FirebaseStorage.getInstance().getReference().child("Profile_Pic").child(otherUserid);
    }

}
