package com.gunduzonur.shovelant;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import java.util.LinkedList;
import java.util.Random;

public class GameView extends SurfaceView{
    private final long TPS=15;
    private final int WIDTH=10;
    private final int HEIGHT=20;
    private SharedPreferences sharedPreferences;
    private SurfaceHolder holder;
    private Paint paint;
    private Random random;
    private Bitmap mapBmp,gameOverBmp;
    private Bitmap[] fishBmp,towerBmp;
    private LinkedList<Fish> fishList;
    private LinkedList<Tower> towerList;
    private int[][] map;
    private int[] route;
    private boolean[] routeDir;
    private int tick,bradCoin,selectTower;
    public GameView(Context context,AttributeSet attrs){
        super(context,attrs);
        sharedPreferences=context.getSharedPreferences("selectTower",0);
        holder=getHolder();
        paint=new Paint();
        mapBmp=BitmapFactory.decodeResource(getResources(),R.drawable.map);
        gameOverBmp=BitmapFactory.decodeResource(getResources(),R.drawable.gameover);
        fishBmp=new Bitmap[7];
        fishBmp[0]=BitmapFactory.decodeResource(getResources(),R.drawable.blob);
        fishBmp[1]=BitmapFactory.decodeResource(getResources(),R.drawable.puffer);
        fishBmp[2]=BitmapFactory.decodeResource(getResources(),R.drawable.jelly);
        fishBmp[3]=BitmapFactory.decodeResource(getResources(),R.drawable.clown);
        fishBmp[4]=BitmapFactory.decodeResource(getResources(),R.drawable.narwhal);
        fishBmp[5]=BitmapFactory.decodeResource(getResources(),R.drawable.cat);
        fishBmp[6]=BitmapFactory.decodeResource(getResources(),R.drawable.butter);
        towerBmp=new Bitmap[4];
        towerBmp[0]=BitmapFactory.decodeResource(getResources(),R.drawable.fisherman);
        towerBmp[1]=BitmapFactory.decodeResource(getResources(),R.drawable.laser);
        towerBmp[2]=BitmapFactory.decodeResource(getResources(),R.drawable.tesla);
        towerBmp[3]=BitmapFactory.decodeResource(getResources(),R.drawable.sniper);
        random=new Random();
        fishList=new LinkedList<>();
        towerList=new LinkedList<>();
        tick=0;
        bradCoin=50;
        holder.addCallback(new SurfaceHolderListener());
        paint.setTextSize(50);
        paint.setStrokeWidth(10);
        setOnTouchListener(new OnTouchListener());
    }
    public class SurfaceHolderListener implements SurfaceHolder.Callback,Runnable{
        Thread thread;
        boolean draw;
        @Override
        public void surfaceCreated(SurfaceHolder holder){
            thread=new Thread(this);
            draw=true;
            thread.start();
            buildMap();
        }
        @Override
        public void surfaceChanged(SurfaceHolder holder,int format,int width,int height){}
        @Override
        public void surfaceDestroyed(SurfaceHolder holder){
            draw=false;
        }
        @Override
        public void run(){
            while(draw){
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
            bradCoin+=tick%5==0?1:0;
            LinkedList<Point> attacks=attack(tick);
            selectTower=sharedPreferences.getInt("selectTower",-1);
            if(tick%15==0){
                generateFish(tick);
            }
            draw(attacks);
        }
        public void generateFish(int tick){
            if(tick%900==0){
                Fish butter=new Fish(fishBmp[6],6,route,routeDir,getWidth()/WIDTH,getHeight()/HEIGHT);
                butter.setLocation(0,getHeight()/HEIGHT*9+getHeight()/HEIGHT/2);
                fishList.add(butter);
            }
            else{
                Fish fish=null;
                int difIndex=tick<2700?tick/300:9;
                double[][] difficulty=new double[10][6];
                difficulty[0]=new double[]{.05,0,0,0,0,0};
                difficulty[1]=new double[]{.1,.05,0,0,0,0};
                difficulty[2]=new double[]{.1,.05,.05,0,0,0};
                difficulty[3]=new double[]{.1,.05,.05,.1,0,0};
                difficulty[4]=new double[]{.1,.05,.05,.05,.2,0};
                difficulty[5]=new double[]{.1,.1,.05,.05,.1,0};
                difficulty[6]=new double[]{.1,.05,.1,.05,.07,.05};
                difficulty[7]=new double[]{.07,.1,.1,.1,.1,.07};
                difficulty[8]=new double[]{.05,.1,.1,.1,.1,.05};
                difficulty[9]=new double[]{.05,.05,.1,.1,.1,.1};
                double rand=random.nextDouble();
                for(int i=0;i<difficulty[difIndex].length;i++){
                    if(rand<difficulty[difIndex][i]){
                        fish=new Fish(fishBmp[i],i,route,routeDir,getWidth()/WIDTH,getHeight()/HEIGHT);
                        break;
                    }
                    rand-=difficulty[difIndex][i];
                }
                if(fish!=null){
                    fish.setLocation(0,getHeight()/HEIGHT*9+getHeight()/HEIGHT/2);
                    fishList.add(fish);
                }
            }
        }
        public void draw(LinkedList<Point> attacks){
            Canvas canvas=holder.getSurface().lockHardwareCanvas();
            canvas.drawBitmap(mapBmp,new Rect(0,0,mapBmp.getWidth(),mapBmp.getHeight()),new Rect(0,0,getWidth(),getHeight()),null);
            paint.setColor(Color.BLACK);
            canvas.drawText(Integer.toString(bradCoin)+"₿",10,50,paint);
            for(Fish fish:fishList){
                if(!fish.tick(canvas)){
                    draw=false;
                }
            }
            for(Tower tower:towerList){
                tower.tick(canvas);
            }
            paint.setColor(Color.RED);
            for(int i=0;i<attacks.size();i+=2){
                canvas.drawLine(attacks.get(i).x,attacks.get(i).y,attacks.get(i+1).x,attacks.get(i+1).y,paint);
            }
            if(!draw){
                canvas.drawBitmap(mapBmp,new Rect(0,0,mapBmp.getWidth(),mapBmp.getHeight()),new Rect(0,0,getWidth(),getHeight()),null);
                canvas.drawBitmap(gameOverBmp,new Rect(0,0,gameOverBmp.getWidth(),gameOverBmp.getHeight()),new Rect(0,0,getWidth(),getHeight()),null);
            }
            holder.getSurface().unlockCanvasAndPost(canvas);
        }
        public LinkedList<Point> attack(int tick){
            LinkedList<Point> attacks=new LinkedList<>();
            for(Tower tower:towerList){
                for(int i=0;i<fishList.size();i++){
                    if(getDistance(tower,fishList.get(i))<=tower.getRange()&&tick%tower.getReload()==0){
                        int damage=tower.getAttack();
                        if(damage!=0){
                            attacks.add(new Point(tower.getLocation().centerX(),tower.getLocation().centerY()));
                            attacks.add(new Point(fishList.get(i).getLocation().centerX(),fishList.get(i).getLocation().centerY()));
                            int value=fishList.get(i).attack(damage,tower.getPenetration());
                            bradCoin+=value;
                            if(value>0){
                                fishList.remove(i--);
                            }
                        }
                        if(i>0&&fishList.get(i).getDistance()>fishList.get(i-1).getDistance()){
                            Fish temp=fishList.get(i);
                            fishList.set(i,fishList.get(i-1));
                            fishList.set(i-1,temp);
                        }
                    }
                }
            }
            return attacks;
        }
        public double getDistance(Tower tower,Fish fish){
            int towerX=tower.getLocation().centerX();
            int towerY=tower.getLocation().centerY();
            int fishX=fish.getLocation().centerX();
            int fishY=fish.getLocation().centerY();
            return Math.sqrt(Math.pow(towerX-fishX,2)*WIDTH/getWidth()+Math.pow(towerY-fishY,2)*HEIGHT/getHeight())/10;
        }
        private void buildMap(){
            map=new int[WIDTH][HEIGHT];
            for(int row=0;row<WIDTH;row++){
                for(int col=0;col<HEIGHT;col++){
                    map[row][col]=0;
                }
            }
            int[] waterCols=new int[]{0,1,2,3,3,3,3,3,2,1,1,1,1,1,1,2,3,4,5,6,7,8,8,8,7,6,5,5,5,5,5,6,7,8,8,8,8,8,8,8,8,8,8,7,6,5,4,4,4,4,5,6,6,6,5,4,3,2,1,1,1,1,1,1,1}; // Advanced coding strategy
            int[] waterRows=new int[]{9,9,9,9,10,11,12,13,13,13,14,15,16,17,18,18,18,18,18,18,18,18,17,16,16,16,16,15,14,13,12,12,12,12,11,10,9,8,7,6,5,4,3,2,1,1,1,1,1,2,3,4,4,4,5,6,6,6,6,6,6,5,4,3,2,1,0};
            route=new int[]{3,13,1,18,8,16,5,12,8,1,4,4,6,6,1,0};
            routeDir=new boolean[]{true,false,true,true,false,false,false,true,false,false,true,true,true,false,false,false};
            for(int i=0;i<route.length;i++){
                if(i%2==0){
                    route[i]=getWidth()/WIDTH*route[i]+getWidth()/WIDTH/2;
                }
                else{
                    route[i]=getHeight()/HEIGHT*route[i]+getHeight()/HEIGHT/2;
                }
            }
            for(int i=0;i<waterCols.length;i++){
                map[waterCols[i]][waterRows[i]]=1;
            }
        }
    }
    public class OnTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view,MotionEvent event){
            if(event.getAction()==MotionEvent.ACTION_DOWN){
                int tile=map[(int)(event.getX()*WIDTH/getWidth())][(int)(event.getY()*HEIGHT/getHeight())];
                if(tile==0){
                    switch(selectTower){
                        case 0:
                            if(bradCoin>=75){
                                map[(int)(event.getX()*WIDTH/getWidth())][(int)(event.getY()*HEIGHT/getHeight())]=2;
                                towerList.add(new Tower(towerBmp[0],0,(int)(event.getX()*WIDTH/getWidth()),(int)(event.getY()*HEIGHT/getHeight()),getWidth()/WIDTH,getHeight()/HEIGHT));
                                bradCoin-=25;
                            }
                            break;
                        case 1:
                            if(bradCoin>=100){
                                map[(int)(event.getX()*WIDTH/getWidth())][(int)(event.getY()*HEIGHT/getHeight())]=3;
                                towerList.add(new Tower(towerBmp[1],1,(int)(event.getX()*WIDTH/getWidth()),(int)(event.getY()*HEIGHT/getHeight()),getWidth()/WIDTH,getHeight()/HEIGHT));
                                bradCoin-=50;
                            }
                            break;
                        case 2:
                            if(bradCoin>=125){
                                map[(int)(event.getX()*WIDTH/getWidth())][(int)(event.getY()*HEIGHT/getHeight())]=4;
                                towerList.add(new Tower(towerBmp[2],2,(int)(event.getX()*WIDTH/getWidth()),(int)(event.getY()*HEIGHT/getHeight()),getWidth()/WIDTH,getHeight()/HEIGHT));
                                bradCoin-=75;
                            }
                            break;
                        case 3:
                            if(bradCoin>=125){
                                map[(int)(event.getX()*WIDTH/getWidth())][(int)(event.getY()*HEIGHT/getHeight())]=5;
                                towerList.add(new Tower(towerBmp[3],3,(int)(event.getX()*WIDTH/getWidth()),(int)(event.getY()*HEIGHT/getHeight()),getWidth()/WIDTH,getHeight()/HEIGHT));
                                bradCoin-=50;
                            }
                            break;
                    }
                }
                else if(tile>1){
                    for(Tower tower:towerList){
                        if(tower.getCol()==(int)(event.getX()*WIDTH/getWidth())&&tower.getRow()==(int)(event.getY()*HEIGHT/getHeight())){
                            switch(tile){
                                case 2:
                                    if(bradCoin>=100){ // Don't touch this section of code it looks like DNA
                                        tower.upgrade();
                                        bradCoin-=100;
                                        tile+=4;
                                    }
                                    break;
                                case 3:
                                    if(bradCoin>=125){
                                        tower.upgrade();
                                        bradCoin-=125;
                                        tile+=4;
                                    }
                                    break;
                                case 4:
                                case 5:
                                case 6:
                                    if(bradCoin>=150){
                                        tower.upgrade();
                                        bradCoin-=150;
                                        tile+=4;
                                    }
                                    break;
                                case 7:
                                case 9:
                                    if(bradCoin>=175){
                                        tower.upgrade();
                                        bradCoin-=175;
                                        tile+=4;
                                    }
                                    break;
                                case 8:
                                    if(bradCoin>=200){
                                        tower.upgrade();
                                        bradCoin-=200;
                                        tile+=4;
                                    }
                                    break;
                            }
                            map[(int)(event.getX()*WIDTH/getWidth())][(int)(event.getY()*HEIGHT/getHeight())]=tile;
                        }
                    }
                }
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt("selectTower",-1);
                editor.apply();
            }
            return true;
        }
    }
}