package com.boiz.multiplayertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomMatch extends AppCompatActivity {

    private ListView listView;
    private Button create;

    private List<String> roomsList;

    String playerName = "";
    String roomName = "";

    FirebaseDatabase database;
    DatabaseReference roomRef;
    DatabaseReference roomsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        listView = findViewById(R.id.listVIEW);
        create = findViewById(R.id.btnCREATE);

        database = FirebaseDatabase.getInstance();

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        playerName = preferences.getString("playerName" , "");
        roomName = playerName;

        roomsList = new ArrayList<>();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.setText("CREATING ROOM");
                create.setEnabled(false);
                roomName = playerName;
                roomRef = database.getReference("rooms/" + roomName + "/player1");
                addEventLisntener();
                roomRef.setValue(playerName);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                roomName = roomsList.get(position);
                roomRef = database.getReference("rooms/" + roomName + "/player2");
                addEventLisntener();
                roomRef.setValue(playerName);
            }
        });

        addRoomsEventLisntener();
    }

    private void addEventLisntener()
    {
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                create.setText("CREATE ROOM");
                create.setEnabled(true);
                Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
                intent.putExtra("roomName",roomName);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                RoomMatch.this.finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                create.setText("CREATE ROOM");
                create.setEnabled(true);
                Toast.makeText(RoomMatch.this, "Error..!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRoomsEventLisntener()
    {
        roomsRef = database.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomsList.clear();
                Iterable<DataSnapshot> rooms = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : rooms)
                {
                    roomsList.add(snapshot.getKey());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(RoomMatch.this, android.R.layout.simple_list_item_1, roomsList);

                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
