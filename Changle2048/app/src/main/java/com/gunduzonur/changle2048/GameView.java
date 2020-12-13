package com.gunduzonur.changle2048;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

public class GameView extends GridLayout {
    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        start();
    }

    public void start() {
        setOnTouchListener(new View.OnTouchListener() {

            private float startX, startY;
            private float dx, dy;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        dx = event.getX() - startX;
                        dy = event.getY() - startY;

                        if(Math.abs(dx)>Math.abs(dy)) {
                            if(dx<-5) {
                                System.out.println("L threshold reached");
                            } else if (dx > 5) {
                                System.out.println("R threshold reached");
                            }
                        } else {
                            if (dy<-5) {
                                System.out.println("U threshold reached");
                            } else if(dy>5) {
                                System.out.println("D threshold reached");
                            }
                        }

                        break;
                }

                return true;
            }
        });
    }
}
