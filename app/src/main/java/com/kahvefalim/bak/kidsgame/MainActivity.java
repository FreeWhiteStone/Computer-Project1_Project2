package com.kahvefalim.bak.kidsgame;

import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.DragEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;



public class MainActivity extends Activity   {
    ImageView boy;
    ImageView star;
    Animation translateRightUp;
    Animation translateRight;
    Animation translateOne;
    Button exit;
    Button replay;
    Button new_game;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button left;
    Button right;
    Button up;
    Button down;
    Button repeat;
    Button play;
    Button conditional;

    StartDraggingLsntr myStartDraggingLsnr;
    EndDraggingLsntr myEndDraggingLsntr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        exit=(Button)findViewById(R.id.button_exit);
        conditional=(Button)findViewById(R.id.conditional);
        play=(Button)findViewById(R.id.play);
        repeat=(Button)findViewById(R.id.repeat);
        down=(Button)findViewById(R.id.down);
        up=(Button)findViewById(R.id.up);
        right=(Button)findViewById(R.id.right);
        star=(ImageView)findViewById(R.id.one_star);
        boy=(ImageView)findViewById(R.id.one_boy);
        right=(Button)findViewById(R.id.right);
        left=(Button)findViewById(R.id.left);
        button4=(Button)findViewById(R.id.button4);
        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);
        new_game=(Button)findViewById(R.id.button_new_game);
        replay=(Button)findViewById(R.id.button_replay);
        // start/end drag listeners from class
        myStartDraggingLsnr=new StartDraggingLsntr();
        myEndDraggingLsntr=new EndDraggingLsntr();

        left.setOnLongClickListener(myStartDraggingLsnr);
        right.setOnLongClickListener(myStartDraggingLsnr);
        up.setOnLongClickListener(myStartDraggingLsnr);
        down.setOnLongClickListener(myStartDraggingLsnr);
        repeat.setOnLongClickListener(myStartDraggingLsnr);
        conditional.setOnLongClickListener(myStartDraggingLsnr);

        button1.setOnDragListener(myEndDraggingLsntr);
        button2.setOnDragListener(myEndDraggingLsntr);
        button3.setOnDragListener(myEndDraggingLsntr);
        button4.setOnDragListener(myEndDraggingLsntr);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Boş kutucuklara cisimleri koyarak yıldızları toplayın").setTitle("Seviye 1");
        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        // Static bounce
        final Animation bounce = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
        translateRightUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate_right_up);
        translateRight = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate_right);
        translateOne = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate_one);
        boy.startAnimation(bounce);
        star.startAnimation(bounce);

        final MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.level_1);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        // play button
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable btn1 = button1.getBackground();
                Drawable btn2 = button2.getBackground();
                Drawable btn3 = button3.getBackground();
                Drawable btnRight = right.getBackground();
                Drawable btnUp = up.getBackground();
                if (btn1 == btnRight && btn2 == btnUp && btn3 == btnRight) {
                    boy.startAnimation(translateOne);
                }
                else if (btn1 == btnRight && btn2 == btnUp) {
                    boy.startAnimation(translateRightUp);
                    Toast.makeText(MainActivity.this, "Neredeyse!", Toast.LENGTH_LONG).show();
                }
                else if (btn1 == btnRight) {
                    boy.startAnimation(translateRight);
                    Toast.makeText(MainActivity.this, "İki hareket daha!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Geçersiz Giriş", Toast.LENGTH_LONG).show();
                }
            }
        });

        translateOne.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Bir sonsaki seviye!").setTitle("Tebrikler!");
                builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mediaPlayer.stop();
                        Intent levelTwo = new Intent(MainActivity.this, Main2Activity.class);
                        levelTwo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(levelTwo);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        // exit button
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

        // replay button
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                mediaPlayer.stop();
                finish();
                startActivity(intent);
            }
        });

        // new game button
        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                finish();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private class EndDraggingLsntr implements View.OnDragListener{
        @Override
        public boolean onDrag(View view, DragEvent event) {
            if (event.getAction()==DragEvent.ACTION_DROP)
            {
                view.setBackground( ((Button) event.getLocalState()).getBackground());
                view.setContentDescription( ((Button) event.getLocalState()).getContentDescription());
            }

            return true;
        }
    }

    private class StartDraggingLsntr implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View view) {
            WithDraggingShadow shadow = new WithDraggingShadow(view);
            ClipData data=ClipData.newPlainText("","");
            view.startDrag( data, shadow, view, 0);
            return false;
        }
    }

    private class WithDraggingShadow extends View.DragShadowBuilder{
        public WithDraggingShadow(View view){
            super(view);

        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            super.onDrawShadow(canvas);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);
        }
    }
}
