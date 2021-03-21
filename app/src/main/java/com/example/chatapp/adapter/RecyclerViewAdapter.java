package com.example.chatapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.models.User;
import com.example.chatapp.utils.OnChatClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    List<User> userList;
    OnChatClickListener onChatClickListener;

    public RecyclerViewAdapter(List<User> userList, OnChatClickListener onChatClickListener) {
        this.userList = userList;
        this.onChatClickListener = onChatClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row, parent, false);
        return new ViewHolder(view, onChatClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User currentUser = userList.get(position);
        Picasso.get()
                .load(currentUser.getProfilePicture())
                .placeholder(R.drawable.ic_user)
                .into(holder.userProfile);
        holder.username.setText(currentUser.getUsername());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView userProfile;
        TextView username;
        TextView lastMessage;
        OnChatClickListener onChatClickListener;

        public ViewHolder(@NonNull View itemView, OnChatClickListener onChatClickListener) {
            super(itemView);

            userProfile = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.chat_row_username);
            lastMessage = itemView.findViewById(R.id.chat_row_last_message);
            this.onChatClickListener = onChatClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            User currentUser = userList.get(getAdapterPosition());
            onChatClickListener.onChatViewClicked(currentUser);
        }
    }
}
