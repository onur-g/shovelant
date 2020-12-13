package com.gunduzonur.shovelant;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Tower{
    private Bitmap bmp;
    private Rect source,location;
    private int type,col,row,attack,armor,range,reload;
    private boolean alreadyAttacked,multiAttack;
    public Tower(Bitmap bmp,int type,int col,int row,int width,int height){
        this.bmp=bmp;
        this.type=type;
        this.col=col;
        this.row=row;
        source=new Rect(0,0,bmp.getWidth()/2,bmp.getHeight()/2);
        location=new Rect(col*width-20,row*height-20,(col+1)*width+20,(row+1)*height+20);
        alreadyAttacked=false;
        setStats(type);
    }
    public void tick(Canvas canvas){
        alreadyAttacked=false;
        canvas.drawBitmap(bmp,source,location,null);
    }
    public void setStats(int type){
        switch(type){
            case 0:
                attack=5;
                armor=0;
                range=3;
                reload=8;
                multiAttack=false;
                break;
            case 1:
                attack=2;
                armor=1;
                range=3;
                reload=2;
                multiAttack=false;
                break;
            case 2:
                attack=5;
                armor=5;
                range=2;
                reload=15;
                multiAttack=true;
                break;
            case 3:
                attack=25;
                armor=10;
                range=6;
                reload=30;
                multiAttack=false;
                break;
            case 4:
                attack=7;
                armor=0;
                range=3;
                reload=8;
                multiAttack=false;
                break;
            case 5:
                attack=3;
                armor=1;
                range=3;
                reload=2;
                multiAttack=false;
                break;
            case 6:
                attack=7;
                armor=5;
                range=2;
                reload=10;
                multiAttack=true;
                break;
            case 7:
                attack=25;
                armor=10;
                range=6;
                reload=22;
                multiAttack=false;
                break;
            case 8:
                attack=10;
                armor=3;
                range=3;
                reload=8;
                multiAttack=false;
                break;
            case 9:
                attack=4;
                armor=2;
                range=4;
                reload=2;
                multiAttack=false;
                break;
            case 10:
                attack=9;
                armor=5;
                range=3;
                reload=10;
                multiAttack=true;
                break;
            case 11:
                attack=35;
                armor=15;
                range=6;
                reload=22;
                multiAttack=false;
        }
    }
    public void upgrade(){
        if(type/4==0){
            source=new Rect(bmp.getWidth()/2,0,bmp.getWidth(),bmp.getHeight()/2);
        }
        else{
            source=new Rect(0,bmp.getHeight()/2,bmp.getWidth()/2,bmp.getHeight());
        }
        type+=4;
        setStats(type);
    }
    public int getCol(){
        return col;
    }
    public int getRow(){
        return row;
    }
    public Rect getLocation(){
        return location;
    }
    public int getAttack(){
        if(multiAttack||!alreadyAttacked){
            alreadyAttacked=true;
            return attack;
        }
        return 0;
    }
    public int getPenetration(){
        return armor;
    }
    public int getRange(){
        return range;
    }
    public int getReload(){
        return reload;
    }
}