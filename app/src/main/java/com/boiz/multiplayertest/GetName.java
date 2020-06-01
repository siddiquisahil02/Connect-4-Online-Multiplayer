package com.boiz.multiplayertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetName extends AppCompatActivity {

    private EditText name;
    private Button conti;

    private String playerName = "";

    FirebaseDatabase database;
    DatabaseReference playerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.etNAME);
        conti = findViewById(R.id.btnCONTINUE);

        database = FirebaseDatabase.getInstance();

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        playerName = preferences.getString("playerName", "");

        if (!playerName.equals("")) {
            playerRef = database.getReference("players/" + playerName);
            addEventListener();
            playerRef.setValue("");
        }

        conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerName = name.getText().toString();
                name.setText("");
                if (!playerName.equals("")) {
                    conti.setText("LOGGING IN");
                    conti.setEnabled(false);
                    playerRef = database.getReference("players/" + playerName);
                    addEventListener();
                    playerRef.setValue("");
                }
            }
        });

    }

    private void addEventListener()
    {
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!playerName.equals(""))
                {
                    SharedPreferences preferences = getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("playerName",playerName);
                    editor.apply();

                    startActivity(new Intent(GetName.this, RoomMatch.class));
                    GetName.this.finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                conti.setText("LOG IN");
                conti.setEnabled(true);
                Toast.makeText(GetName.this, "ERROR...!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
