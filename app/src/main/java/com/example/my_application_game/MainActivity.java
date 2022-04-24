package com.example.my_application_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HandshakeCompletedEvent;

public class MainActivity extends AppCompatActivity {

    //-------------------------------------temporary-------------------------------------------------//
//TIMER//
    private final int DELAY = 2000;
    private final int DELAY1 = 1000;
    private Timer timer;
    private int counter = 0;
    private MaterialTextView main_LBL_timer;
    private enum TIMER_STATUS{
        OFF,
        RUNNING,
        PAUSE
    }
    private TIMER_STATUS timerStatus = TIMER_STATUS.OFF;

//MATRIX//
    private ImageView[][] images_matrix;
//LIVE//
    private ImageView[] game_IMG_hearts;
    private int lives ;
//ARROWS//
    private ImageButton[] arrows;


//GAME MANAGER//
    private Game_Manager gameManager ;

//PLAYERS//
    private Player hunter;
    private Player aim;


/////////////////////////////image view target///////////////
    ImageView target ;

//DIRECTIONS//
    public static final int UP = 1;
    public static final int RIGHT = 2;
    public static final int DOWN = 3;
    public static final int LEFT = 4;

    public static int AIM_DIRECTION = -1;
    public static int HUNTER_DIRECTION = -1;

//START INDEX PLAYERS   //
    private int aim_start_index_x = 0;
    private int aim_start_index_Y = 1;
    private int hunter_start_index_x = 4;
    private int hunter_start_index_y = 1;

    private int target_start_index_x = 0;
    private int target_start_index_y = 0;

    //--------------------------------------------------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
    }


    //--------------------------------------------------------------------------------------//
    private void findViews() {

//TIMER//
        timer = new Timer();
        main_LBL_timer = findViewById(R.id.main_LBL_timer);
//GAME MANAGER//
        gameManager = new Game_Manager();

//MATRIX//
        images_matrix = new ImageView[][] {
                {findViewById(R.id.main_IMG_00)   ,findViewById(R.id.main_IMG_01)    ,findViewById(R.id.main_IMG_02)    },
                {findViewById(R.id.main_IMG_10)   ,findViewById(R.id.main_IMG_11)    ,findViewById(R.id.main_IMG_12)    },
                {findViewById(R.id.main_IMG_20)   ,findViewById(R.id.main_IMG_21)    ,findViewById(R.id.main_IMG_22)    },
                {findViewById(R.id.main_IMG_30)   ,findViewById(R.id.main_IMG_31)    ,findViewById(R.id.main_IMG_32)    },
                {findViewById(R.id.main_IMG_40)   ,findViewById(R.id.main_IMG_41)    ,findViewById(R.id.main_IMG_42)    }
        };
//LIVE//
        game_IMG_hearts = new ImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };
        lives = gameManager.getTotalGameLives();

//ARROWS BUTTON //
        arrows = new ImageButton[]{
                findViewById(R.id.main_BTN_up),
                findViewById(R.id.main_IMG_right),
                findViewById(R.id.main_BTN_down),
                findViewById(R.id.main_IMG_left),
        };
        buttonsPressAction();

//PLAYERS//
        aim = new Player(aim_start_index_x, aim_start_index_Y, AIM_DIRECTION);
        hunter = new Player(hunter_start_index_x, hunter_start_index_y, HUNTER_DIRECTION);

//GET PLAYERS INTO THE MATRIX//
        images_matrix[hunter.getX()][hunter.getY()].setImageResource(R.drawable.ic_hunter);
        images_matrix[hunter.getX()][hunter.getY()].setVisibility(View.VISIBLE);

        images_matrix[aim.getX()][aim.getY()].setImageResource(R.drawable.ic_aim);
        images_matrix[aim.getX()][aim.getY()].setVisibility(View.VISIBLE);

/////////////////////////////image view target///////////////

    }   //END FIND VIEWS//


    //--------------------------------------------------------------------------------------//

    //--- vibrate--//

    public void vibrate(int millisecond) {
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(millisecond, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(millisecond);
        }
    }

//RAN//

    public void startRunning(){
      aimSteps();
      hunterSteps();
      sameIndex();
    }



//  ACTION BY PRESS ARROW - LOGIC   //

    private void buttonsPressAction() {


        arrows[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AIM_DIRECTION = getRandomDirection();
                HUNTER_DIRECTION = UP;
                vibrate(200);
            }
        });
        arrows[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AIM_DIRECTION = getRandomDirection();
                HUNTER_DIRECTION = RIGHT;
                vibrate(200);
            }
        });
        arrows[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AIM_DIRECTION = getRandomDirection();
                HUNTER_DIRECTION = DOWN;
                vibrate(200);
            }
        });
        arrows[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AIM_DIRECTION = getRandomDirection();
                HUNTER_DIRECTION = LEFT;
                vibrate(200);
            }
        });


    }

    private int getRandomDirection() {
        AIM_DIRECTION = (int) (Math.random()*4);
        return AIM_DIRECTION;
    };

//SAME INDEX //
    int x;
    int y;
    private void sameIndex() {

        if(  (hunter.getX() == aim.getX()) && (hunter.getY()) == aim.getY()){
             x = aim.getX();
             y = aim.getY() ;

            images_matrix[hunter.getX()][hunter.getY()].setVisibility(View.INVISIBLE);
            images_matrix[aim.getX()][aim.getY()].setVisibility(View.INVISIBLE);

            images_matrix[x][y].setVisibility(View.INVISIBLE);
            images_matrix[x][y].setImageResource(R.drawable.ic_target);
            images_matrix[x][y].setVisibility(View.VISIBLE);



            if(gameManager.getLives() > 1 ){

                images_matrix[x][y].setImageResource(R.drawable.ic_target);
                gameManager.reduceLives();
                game_IMG_hearts[gameManager.getLives()].setVisibility(View.INVISIBLE);
                Toast.makeText(this,"EXCELLENT, TARGET HIT",Toast.LENGTH_LONG).show();
                //need to return the start point
                getToStartingIndex();
            }else {         //GAME OVER
                game_IMG_hearts[0].setVisibility(View.INVISIBLE);
                stopTimer();
                images_matrix[hunter.getX()][hunter.getY()].setVisibility(View.INVISIBLE);
                Toast.makeText(this,"Game Over",Toast.LENGTH_LONG).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2500);
            }
        }
    }

//BACK TO STARTING INDEX//
    Timer timer1;
    public void getToStartingIndex(){


        hunter.setX(hunter_start_index_x);
        hunter.setY(hunter_start_index_y);
        aim.setX(aim_start_index_x);
        aim.setY(aim_start_index_Y);

     Handler handler = new Handler();
     handler.postDelayed(new Runnable() {
         @Override
         public void run() {
             images_matrix[x][y].setVisibility(View.INVISIBLE);
         }
     },DELAY1);

        images_matrix[aim.getX()][aim.getY()].setImageResource(R.drawable.ic_aim);
        images_matrix[aim.getX()][aim.getY()].setVisibility(View.VISIBLE);

        images_matrix[hunter.getX()][hunter.getY()].setImageResource(R.drawable.ic_hunter);
        images_matrix[hunter.getX()][hunter.getY()].setVisibility(View.VISIBLE);


    }

//--------------------------------------steps------------------------------------------------//
    public void hunterSteps(){
        if (lives == 0)
        images_matrix[hunter.getX()][hunter.getY()].setVisibility(View.INVISIBLE);
        switch (HUNTER_DIRECTION){

            case UP:
                images_matrix[hunter.getX()][hunter.getY()].setVisibility(View.INVISIBLE);
                if(hunter.getX() == 0)
                    hunter.setX(gameManager.getHeight()-1);
                else
                    hunter.setX(hunter.getX()-1);
                images_matrix[hunter.getX()][hunter.getY()].setImageResource(R.drawable.ic_hunter);
                images_matrix[hunter.getX()][hunter.getY()].setVisibility(View.VISIBLE);
                break;
            case RIGHT:
                images_matrix[hunter.getX()][hunter.getY()].setVisibility(View.INVISIBLE);
                if(hunter.getY() == (gameManager.getWidth() - 1))
                    hunter.setY(0);
                else
                    hunter.setY(hunter.getY()+1);
                images_matrix[hunter.getX()][hunter.getY()].setImageResource(R.drawable.ic_hunter);
                images_matrix[hunter.getX()][hunter.getY()].setVisibility(View.VISIBLE);
                break;
            case DOWN:
                images_matrix[hunter.getX()][hunter.getY()].setVisibility(View.INVISIBLE);
                if(hunter.getX() == (gameManager.getHeight() - 1))
                    hunter.setX(0);
                else
                    hunter.setX(hunter.getX()+1);
                images_matrix[hunter.getX()][hunter.getY()].setImageResource(R.drawable.ic_hunter);
                images_matrix[hunter.getX()][hunter.getY()].setVisibility(View.VISIBLE);
                break;
            case LEFT:
                images_matrix[hunter.getX()][hunter.getY()].setVisibility(View.INVISIBLE);
                if(hunter.getY() == 0)
                    hunter.setY(gameManager.getWidth() -1);
                else
                    hunter.setY(hunter.getY()-1);
                images_matrix[hunter.getX()][hunter.getY()].setImageResource(R.drawable.ic_hunter);
                images_matrix[hunter.getX()][hunter.getY()].setVisibility(View.VISIBLE);
                break;


        }

    }

    public void aimSteps(){
        if(lives == 0)
            images_matrix[aim.getX()][aim.getY()].setVisibility(View.INVISIBLE);
        switch (AIM_DIRECTION){
            case UP:
                images_matrix[aim.getX()][aim.getY()].setVisibility(View.INVISIBLE);
                if(aim.getX() == 0)
                    aim.setX(gameManager.getHeight()-1);
                else
                    aim.setX(aim.getX()-1);
                images_matrix[aim.getX()][aim.getY()].setImageResource(R.drawable.ic_aim);
                images_matrix[aim.getX()][aim.getY()].setVisibility(View.VISIBLE);
                break;
            case RIGHT:
                images_matrix[aim.getX()][aim.getY()].setVisibility(View.INVISIBLE);
                if(aim.getX() == (gameManager.getHeight() - 1))
                    aim.setX(0);
                else
                    aim.setX(aim.getX()+1);
                images_matrix[aim.getX()][aim.getY()].setImageResource(R.drawable.ic_aim);
                images_matrix[aim.getX()][aim.getY()].setVisibility(View.VISIBLE);
                break;
            case DOWN:
                images_matrix[aim.getX()][aim.getY()].setVisibility(View.INVISIBLE);
                if(aim.getY() == (gameManager.getWidth() - 1))
                    aim.setY(0);
                else
                    aim.setY(aim.getY()+1);
                images_matrix[aim.getX()][aim.getY()].setImageResource(R.drawable.ic_aim);
                images_matrix[aim.getX()][aim.getY()].setVisibility(View.VISIBLE);
                break;
            case LEFT:
                images_matrix[aim.getX()][aim.getY()].setVisibility(View.INVISIBLE);
                if(aim.getY() == 0)
                    aim.setY(gameManager.getWidth() - 1);
                else
                    aim.setY(aim.getY()-1);
                images_matrix[aim.getX()][aim.getY()].setImageResource(R.drawable.ic_aim);
                images_matrix[aim.getX()][aim.getY()].setVisibility(View.VISIBLE);
                break;






        }


    }




//--------------------------------------------------------------------------------------//
//TIMER FUNCTIONS//
    @Override
    protected void onStart() {
        super.onStart();
        if(timerStatus == TIMER_STATUS.OFF){
            startTimer();
        } else if(timerStatus == TIMER_STATUS.RUNNING ){
            stopTimer();
        }else{
            startTimer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(timerStatus == TIMER_STATUS.RUNNING){
            stopTimer();
            timerStatus = TIMER_STATUS.PAUSE;
        }
    }

    private void stopTimer() {
        timerStatus = TIMER_STATUS.OFF;
        timer.cancel();
    }

    private void startTimer () {
        timerStatus = TIMER_STATUS.RUNNING;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tick();
                        startRunning();
                    }
                });

            }
        }, 0, DELAY);
    }
    ;
    private void tick () {
        ++counter;
        main_LBL_timer.setText("" + counter);
    }




}