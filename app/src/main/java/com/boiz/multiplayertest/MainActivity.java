//package com.boiz.multiplayertest;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.GridView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity {
//
//    private static final int coloumn = 7;
//    private static final int row = 6;
//
//    private int[][] matrix = new int[row][coloumn];
//    private int in, player1points, player2points;
//
//    private int round = 0;
//
//    private Boolean player1 = true;
//    private Boolean flag = true;
//    private Boolean temp = true;
//
//    private TextView scoreP1;
//    private TextView scoreP2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //setContentView(R.layout.activity_first);
//        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        scoreP1 = findViewById(R.id.tvPlayer1);
//        scoreP2 = findViewById(R.id.tvPlayer2);
//        sho();
//    }
//
//    private void gridly()
//    {
//        GridView grid=(GridView)findViewById(R.id.grid);
//        grid.setNumColumns(coloumn);
//        List<Integer> matrixList=new ArrayList<>();
//        for (int i=0;i<row;i++)
//        {
//            for (int j=0;j<coloumn;j++)
//            {
//                matrixList.add(matrix[i][j]);
//            }
//        }
//        MatrixAdapter adapter=new MatrixAdapter(getApplicationContext(),matrixList);
//        grid.setAdapter(adapter);
//
//        if (temp)
//        {
//            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    int sel = position%7;
//                    update(sel);
//                }
//            });
//        }
//    }
//
//    private void update(int c)
//    {
//        System.out.println("SELECTED ROW -> " + c);
//        {
//            if(player1)
//            {
//                for(int i =5 ; i>=0 ; --i)
//                {
//                    if(matrix[i][c] == 0)
//                    {
//                        matrix[i][c] = 1;
//                        break;
//                    }
//                }
//            }
//            else
//            {
//                for(int i =5 ; i>=0 ; --i)
//                {
//                    if(matrix[i][c] == 0)
//                    {
//                        matrix[i][c] = 2;
//                        break;
//                    }
//                }
//            }
//            if(checkForwin())
//            {
//                if (player1) {
//                    player1wins();
//                }
//                else{
//                    player2wins();
//                }
//            }
//            else if (round == 42)
//            {
//                draw();
//            }
//            else
//            {
//                if(matrix[0][c]==0)
//                {
//                    player1 = !player1;
//                    round++;
//                    flag = true;
//                }
//                else
//                {
//                    if (flag)
//                    {
//                        player1 = !player1;
//                        round++;
//                    }
//                    flag=false;
//                }
//
//            }
//
//            sho();
//        }
//    }
//
//    private void player1wins()
//    {
//        temp = false;
//        player1points++;
//        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
//        updatepointsText();
//
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                resetBoard();
//            }
//        }, 2000);
//    }
//
//    private void player2wins()
//    {
//        temp = false;
//        player2points++;
//        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
//        updatepointsText();
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                resetBoard();
//            }
//        }, 2000);
//    }
//
//    private void updatepointsText() {
//        scoreP1.setText("Player 1 -> " + player1points);
//        scoreP2.setText("Player 2 -> " + player2points);
//    }
//
//    private boolean checkForwin()
//    {
//        //check for horizontal
//        for(int i=0;i<row;++i)
//        {
//            for (int j=0;j<coloumn - 3;++j)
//            {
//                if (matrix[i][j] == matrix[i][j+1] && matrix[i][j+1] == matrix[i][j+2] &&
//                        matrix[i][j+2] == matrix[i][j+3] && matrix[i][j+3] == matrix[i][j] && matrix[i][j] != 0)
//                {
//                    if (player1)
//                        matrix[i][j]=matrix[i][j+1]=matrix[i][j+2]=matrix[i][j+3]=5;
//                    else
//                        matrix[i][j]=matrix[i][j+1]=matrix[i][j+2]=matrix[i][j+3]=5;
//
//                    //gameEnd = true;
//
//                    return true;
//                }
//
//            }
//        }
//        //check for vertical
//        for(int j=0;j<coloumn;++j)
//        {
//            for (int i=0;i<row - 3;++i)
//            {
//                if (matrix[i][j] == matrix[i+1][j] && matrix[i+1][j] == matrix[i+2][j] &&
//                        matrix[i+2][j] == matrix[i+3][j] && matrix[i+3][j] == matrix[i][j] && matrix[i][j] != 0)
//                {
//                    if(player1)
//                        matrix[i][j]=matrix[i+1][j]=matrix[i+2][j]=matrix[i+3][j]=5;
//                    else
//                        matrix[i][j]=matrix[i+1][j]=matrix[i+2][j]=matrix[i+3][j]=6;
//
//                    //gameEnd = true;
//
//                    return true;
//                }
//
//            }
//        }
//        //check for +vely diagonl
//        for (int i =0 ; i<row-3 ; ++i)
//        {
//            for(int j=0 ; j<coloumn -3 ; ++j)
//            {
//                if (matrix[i][j] == matrix[i+1][j+1] && matrix[i+1][j+1] == matrix[i+2][j+2] &&
//                        matrix[i+2][j+2] == matrix[i+3][j+3] && matrix[i+3][j+3] == matrix[i][j] && matrix[i][j] != 0)
//                {
//                    if (player1)
//                        matrix[i][j]=matrix[i+1][j+1]=matrix[i+2][j+2]=matrix[i+3][j+3]=5;
//                    else
//                        matrix[i][j]=matrix[i+1][j+1]=matrix[i+2][j+2]=matrix[i+3][j+3]=6;
//
//                    //gameEnd = true;
//
//                    return true;
//                }
//            }
//        }
//        //check for -vely diagonl
//        for (int i =0 ; i<row-3 ; ++i)
//        {
//            for(int j=3 ; j<coloumn ; ++j)
//            {
//                if (matrix[i][j] == matrix[i+1][j-1] && matrix[i+1][j-1] == matrix[i+2][j-2] &&
//                        matrix[i+2][j-2] == matrix[i+3][j-3] && matrix[i+3][j-3] == matrix[i][j] && matrix[i][j] != 0)
//                {
//                    if (player1)
//                        matrix[i][j]=matrix[i+1][j-1]=matrix[i+2][j-2]=matrix[i+3][j-3]=5;
//                    else
//                        matrix[i][j]=matrix[i+1][j-1]=matrix[i+2][j-2]=matrix[i+3][j-3]=6;
//
//                    //gameEnd = true;
//
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private void draw() {
//        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
//        resetBoard();
//    }
//
//    private void resetBoard()
//    {
//        for(int i=0;i<row;++i)
//        {
//            for (int j=0;j<coloumn;++j)
//            {
//                matrix[i][j] = 0;
//            }
//        }
//        round = 0;
//        sho();
//        temp = true;
//    }
//
//    private void sho()
//    {
//        gridly();
//
//        for (int i = 0; i < 6; ++i) {
//            for (int j = 0; j < 7; ++j) {
//                System.out.print(matrix[i][j] + " ");
//            }
//            System.out.println("\n");
//        }
//    }
//}
