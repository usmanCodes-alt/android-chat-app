package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.adapter.ChatAdapter;
import com.example.chatapp.models.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.chatapp.fragments.ChatsFragment.KEY_IMAGE;
import static com.example.chatapp.fragments.ChatsFragment.KEY_USERNAME;
import static com.example.chatapp.fragments.ChatsFragment.KEY_USER_ID;

public class ChatsActivity extends AppCompatActivity {

    private CircleImageView receiverProfilePicture;
    private TextView receiverUsername;
    private TextView userMessage;
    private ImageView backToChatsView;
    private ImageView sendButton;
    private ArrayList<Message> messageArrayList;
    private ChatAdapter chatAdapter;
    private RecyclerView recyclerView;
    private String senderRoom;
    private String receiverRoom;
    private TextView senderMessageTime;
    private TextView receiverMessageTime;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        receiverProfilePicture = findViewById(R.id.profile_image);
        receiverUsername = findViewById(R.id.username);
        backToChatsView = findViewById(R.id.back_to_contacts);
        sendButton = findViewById(R.id.send_message);
        userMessage = findViewById(R.id.user_message);
        senderMessageTime = findViewById(R.id.sender_time);
        receiverMessageTime = findViewById(R.id.receiver_time);
        messageArrayList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageArrayList, this);
        recyclerView = findViewById(R.id.conversation_recycler_view);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        String senderId = firebaseAuth.getUid();
        String receiverId = getIntent().getStringExtra(KEY_USER_ID);
        String username = getIntent().getStringExtra(KEY_USERNAME);
        String profilePicturePath = getIntent().getStringExtra(KEY_IMAGE);

        senderRoom = senderId + receiverId;
        receiverRoom = receiverId + senderId;

        Picasso.get()
                .load(profilePicturePath)
                .placeholder(R.drawable.ic_user)
                .into(receiverProfilePicture);
        receiverUsername.setText(username);

        backToChatsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        database.getReference()
                .child("Chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageArrayList.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            Message model = dataSnapshot.getValue(Message.class);
                            messageArrayList.add(model);
                        }
                        chatAdapter.notifyDataSetChanged();
                        int itemCount = chatAdapter.getItemCount() == 0? 0 : chatAdapter.getItemCount() - 1;
                        recyclerView.post(() -> recyclerView.smoothScrollToPosition(itemCount));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(userMessage.getText().toString())) {
                    return;
                }
                /*Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh-mm");
                String dateTime = simpleDateFormat.format(calendar.getTime());*/
                String message = userMessage.getText().toString().trim();
                Message messageModel = new Message(senderId, message);
                messageModel.setTimestamp(new Date().getTime());
                userMessage.setText("");
                database.getReference()     // database
                        .child("Chats")     // new chats node
                        .child(senderRoom)  // conversation between sender and receiver (contains sender messages)
                        .push()             // pushes a new node at runtime
                        .setValue(messageModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                database.getReference()
                                        .child("Chats")
                                        .child(receiverRoom)
                                        .push()
                                        .setValue(messageModel);
                            }
                        });
            }
        });
    }
}