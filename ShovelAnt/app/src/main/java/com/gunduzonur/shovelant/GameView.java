package com.gunduzonur.shovelant;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import java.util.LinkedList;
import java.util.Random;

public class GameView extends SurfaceView{
    private final long TPS=30;
    private SurfaceHolder holder;
    private Paint paint;
    private Random random;
    private LinkedList<Sprite> fish;
    public GameView(Context context,AttributeSet attrs){
        super(context,attrs);
        holder=getHolder();
        paint=new Paint();
        random=new Random();
        fish=new LinkedList<>();
        holder.addCallback(new SurfaceHolderListener());
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        setOnTouchListener(new OnTouchListener());
    }
    public class SurfaceHolderListener implements SurfaceHolder.Callback,Runnable{
        int tick;
        @Override
        public void surfaceCreated(SurfaceHolder holder){
            Thread thread=new Thread(this);
            thread.start();
        }
        @Override
        public void surfaceChanged(SurfaceHolder holder,int format,int width,int height){}
        @Override
        public void surfaceDestroyed(SurfaceHolder holder){}
        @Override
        public void run(){
            tick=0;
            while(true){
                long time=System.currentTimeMillis();
                tick();
                time=System.currentTimeMillis()-time;
                if(time<1000/TPS){
                    try{
                        Thread.sleep(1000/TPS-time);
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        public void tick(){
            tick++;
            draw();
        }
        public Sprite collision(Sprite sprite){
            for(Sprite block:fish){
                if(block.isTouching(sprite)){
                    return block;
                }
            }
            return null;
        }
        public void draw(){
            Canvas canvas=holder.getSurface().lockHardwareCanvas();
            canvas.drawColor(Color.WHITE);
            canvas.drawText(Integer.toString(tick),10,50,paint);
            for(Sprite obstacle:fish){
                obstacle.tick(canvas);
            }
            holder.getSurface().unlockCanvasAndPost(canvas);
        }
    }
    public class OnTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view,MotionEvent event){
            return true;
        }
    }
}