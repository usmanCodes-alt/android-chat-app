package com.example.chatapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.ChatsActivity;
import com.example.chatapp.R;
import com.example.chatapp.adapter.RecyclerViewAdapter;
import com.example.chatapp.models.User;
import com.example.chatapp.utils.OnChatClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatsFragment extends Fragment implements OnChatClickListener {

    public static final String KEY_USERNAME = "username";
    public static final String KEY_IMAGE = "userImage";
    public static final String KEY_USER_ID = "userId";
    private List<User> userList = new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;

    public ChatsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        database = FirebaseDatabase.getInstance();
        adapter = new RecyclerViewAdapter(userList, this);
        recyclerView = view.findViewById(R.id.chat_recycler_view);
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    User currentUser = data.getValue(User.class);
                    String currentUserId = data.getKey();
                    assert currentUser != null;
                    currentUser.setUserId(data.getKey());
                    if (currentUser.getUserId().equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())) {
                        continue;
                    }
                    userList.add(currentUser);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onChatViewClicked(User clickedUser) {
        Intent intent = new Intent(getContext(), ChatsActivity.class);
        intent.putExtra(KEY_USERNAME, clickedUser.getUsername());
        intent.putExtra(KEY_IMAGE, clickedUser.getProfilePicture());
        intent.putExtra(KEY_USER_ID, clickedUser.getUserId());
        startActivity(intent);
    }
}