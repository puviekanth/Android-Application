package com.example.bonjoo.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class Chatroom {

    String chatroomID;
    List<String> userIDs;
    Timestamp lastMessageTime;
    String lastMessageSenderID;
    String lastMessage;

    public Chatroom() {
    }

    public Chatroom(String chatroomID, List<String> userIDs, Timestamp lastMessageTime, String lastMessageSenderID) {
        this.chatroomID = chatroomID;
        this.userIDs = userIDs;
        this.lastMessageTime = lastMessageTime;
        this.lastMessageSenderID = lastMessageSenderID;

    }


    public String getChatroomID() {
        return chatroomID;
    }

    public void setChatroomID(String chatroomID) {
        this.chatroomID = chatroomID;
    }

    public List<String> getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(List<String> userIDs) {
        this.userIDs = userIDs;
    }

    public Timestamp getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Timestamp lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getLastMessageSenderID() {
        return lastMessageSenderID;
    }

    public void setLastMessageSenderID(String lastMessageSenderID) {
        this.lastMessageSenderID = lastMessageSenderID;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
