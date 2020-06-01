package com.boiz.multiplayertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main3Activity extends AppCompatActivity{

    private static final int coloumn = 7;
    private static final int row = 6;

    private int[][] matrix = new int[row][coloumn];
    private int in, player1points, player2points;

    private int round = 0;

    private Boolean player1 = true;
    private Boolean flag = true;
    private Boolean enable ;

    private TextView scoreP1;
    private TextView scoreP2;

    private String playerName = "";
    private String roomName = "";
    private String role = "";
    private String message = "";

    FirebaseDatabase database;
    DatabaseReference messageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        scoreP1 = findViewById(R.id.tvPlayer1);
        scoreP2 = findViewById(R.id.tvPlayer2);

        enable=true;

        gridly();

        database = FirebaseDatabase.getInstance();

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        playerName = preferences.getString("playerName" , "");

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            roomName = extras.getString("roomName");
            assert roomName != null;
            if (roomName.equals(playerName))
            {
                role = "host";
            }
            else
            {
                role = "guest";
            }
        }

        messageRef = database.getReference("rooms/" + roomName + "/message");
        message = role;
        messageRef.setValue(message);
        addRoomEventListener();
    }

    private void  addRoomEventListener()
    {
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    if (role.equals("host")) {
                        if (Objects.requireNonNull(dataSnapshot.getValue(String.class)).contains("guest:")) {
                            enable=true;
                            Toast.makeText(Main3Activity.this, "" + Objects.requireNonNull(dataSnapshot.getValue(String.class)), Toast.LENGTH_SHORT).show();
                            update(Integer.parseInt(Objects.requireNonNull(dataSnapshot.getValue(String.class)).replace("guest:","")));
                        }
                    } else {
                        if (Objects.requireNonNull(dataSnapshot.getValue(String.class)).contains("host:")) {
                            enable=true;
                            Toast.makeText(Main3Activity.this, "" + Objects.requireNonNull(dataSnapshot.getValue(String.class)), Toast.LENGTH_SHORT).show();
                            update(Integer.parseInt(Objects.requireNonNull(dataSnapshot.getValue(String.class)).replace("host:","")));
                        }
                    }
                }
                else
                {
                    //Toast.makeText(Main3Activity.this, "GAME ENDED", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                    finish();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                messageRef.setValue(message);
            }
        });
    }

    private void gridly()
    {
        GridView grid=(GridView)findViewById(R.id.grid);
        grid.setNumColumns(coloumn);
        List<Integer> matrixList=new ArrayList<>();
        for (int i=0;i<row;i++)
        {
            for (int j=0;j<coloumn;j++)
            {
                matrixList.add(matrix[i][j]);
            }
        }
        MatrixAdapter adapter=new MatrixAdapter(getApplicationContext(),matrixList);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (enable)
                {
                    int sel = position%7;
                    update(sel);
                    message = role + ":" + sel;
                    messageRef.setValue(message);
                }
                enable = false;
            }
        });
    }

    private void update(int c)
    {
        {
            if(player1)
            {
                for(int i =5 ; i>=0 ; --i)
                {
                    if(matrix[i][c] == 0)
                    {
                        matrix[i][c] = 1;
                        break;
                    }
                }
            }
            else
            {
                for(int i =5 ; i>=0 ; --i)
                {
                    if(matrix[i][c] == 0)
                    {
                        matrix[i][c] = 2;
                        break;
                    }
                }
            }

            if(checkForwin())
            {
                if (player1) {
                    player1wins();
                }
                else{
                    player2wins();
                }
            }
            else if (round == 42)
            {
                draw();
            }
            else
            {
                if(matrix[0][c]==0)
                {
                    player1 = !player1;
                    round++;
                    flag = true;
                }
                else
                {
                    if (flag)
                    {
                        player1 = !player1;
                        round++;
                    }
                    flag=false;
                }

            }
            gridly();
        }
    }

    private void player1wins()
    {
        player1points++;
        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
        updatepointsText();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
            }
        }, 2000);
    }

    private void player2wins()
    {
        player2points++;
        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
        updatepointsText();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
            }
        }, 2000);
    }

    private void updatepointsText() {
        scoreP1.setText("Player 1 -> " + player1points);
        scoreP2.setText("Player 2 -> " + player2points);
    }

    private boolean checkForwin()
    {
        //check for horizontal
        for(int i=0;i<row;++i)
        {
            for (int j=0;j<coloumn - 3;++j)
            {
                if (matrix[i][j] == matrix[i][j+1] && matrix[i][j+1] == matrix[i][j+2] &&
                        matrix[i][j+2] == matrix[i][j+3] && matrix[i][j+3] == matrix[i][j] && matrix[i][j] != 0)
                {
                    if (player1)
                        matrix[i][j]=matrix[i][j+1]=matrix[i][j+2]=matrix[i][j+3]=5;
                    else
                        matrix[i][j]=matrix[i][j+1]=matrix[i][j+2]=matrix[i][j+3]=6;

                    return true;
                }

            }
        }
        //check for vertical
        for(int j=0;j<coloumn;++j)
        {
            for (int i=0;i<row - 3;++i)
            {
                if (matrix[i][j] == matrix[i+1][j] && matrix[i+1][j] == matrix[i+2][j] &&
                        matrix[i+2][j] == matrix[i+3][j] && matrix[i+3][j] == matrix[i][j] && matrix[i][j] != 0)
                {
                    if(player1)
                        matrix[i][j]=matrix[i+1][j]=matrix[i+2][j]=matrix[i+3][j]=5;
                    else
                        matrix[i][j]=matrix[i+1][j]=matrix[i+2][j]=matrix[i+3][j]=6;

                    return true;
                }

            }
        }
        //check for +vely diagonl
        for (int i =0 ; i<row-3 ; ++i)
        {
            for(int j=0 ; j<coloumn -3 ; ++j)
            {
                if (matrix[i][j] == matrix[i+1][j+1] && matrix[i+1][j+1] == matrix[i+2][j+2] &&
                        matrix[i+2][j+2] == matrix[i+3][j+3] && matrix[i+3][j+3] == matrix[i][j] && matrix[i][j] != 0)
                {
                    if (player1)
                        matrix[i][j]=matrix[i+1][j+1]=matrix[i+2][j+2]=matrix[i+3][j+3]=5;
                    else
                        matrix[i][j]=matrix[i+1][j+1]=matrix[i+2][j+2]=matrix[i+3][j+3]=6;

                    return true;
                }
            }
        }
        //check for -vely diagonl
        for (int i =0 ; i<row-3 ; ++i)
        {
            for(int j=3 ; j<coloumn ; ++j)
            {
                if (matrix[i][j] == matrix[i+1][j-1] && matrix[i+1][j-1] == matrix[i+2][j-2] &&
                        matrix[i+2][j-2] == matrix[i+3][j-3] && matrix[i+3][j-3] == matrix[i][j] && matrix[i][j] != 0)
                {
                    if (player1)
                        matrix[i][j]=matrix[i+1][j-1]=matrix[i+2][j-2]=matrix[i+3][j-3]=5;
                    else
                        matrix[i][j]=matrix[i+1][j-1]=matrix[i+2][j-2]=matrix[i+3][j-3]=6;

                    return true;
                }
            }
        }
        return false;
    }

    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void resetBoard()
    {
        for(int i=0;i<row;++i)
        {
            for (int j=0;j<coloumn;++j)
            {
                matrix[i][j] = 0;
            }
        }
        message = role;
        round = 0;
        player1=true;
        gridly();
    }

    @Override
    public void onBackPressed() {
        database.getReference().removeValue();
        finishAffinity();
        finish();
        super.onBackPressed();
    }
}
