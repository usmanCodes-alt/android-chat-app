package com.example.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.models.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<Message> messageArrayList;
    Context context;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChatAdapter(ArrayList<Message> messageArrayList, Context context) {
        this.messageArrayList = messageArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.conversation_sender, parent, false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.conversation_receiver, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message currentMessage = messageArrayList.get(position);
        if (holder.getClass() == SenderViewHolder.class) {
            ((SenderViewHolder)holder).messageSent.setText(currentMessage.getText());
            //((SenderViewHolder)holder).messageTime.setText(String.valueOf(currentMessage.getTimestamp()));
        }
        else {
            ((ReceiverViewHolder)holder).messageReceived.setText(currentMessage.getText());
            //((ReceiverViewHolder)holder).messageTime.setText(String.valueOf(currentMessage.getTimestamp()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message currentMessage = messageArrayList.get(position);
        if (currentMessage.getId().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER_VIEW_TYPE;
        }
        else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView messageReceived;
        TextView messageTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            messageReceived = itemView.findViewById(R.id.receiver_text);
            messageTime = itemView.findViewById(R.id.receiver_time);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView messageSent;
        TextView messageTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            messageSent = itemView.findViewById(R.id.sender_message);
            messageTime = itemView.findViewById(R.id.sender_time);
        }
    }
}
