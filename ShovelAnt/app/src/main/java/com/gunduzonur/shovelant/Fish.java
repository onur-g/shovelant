package com.gunduzonur.shovelant;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Fish{
    private Bitmap bmp;
    private Rect source,location;
    private int[] route;
    private boolean[] routeDir;
    private int direction,step,dx,dy,width,height,distance,health,speed,armor,value;
    public Fish(Bitmap bmp,int type,int[] route,boolean[] routeDir,int width,int height){
        this.bmp=bmp;
        this.route=route;
        this.routeDir=routeDir;
        this.width=width;
        this.height=height;
        direction=0;
        step=0;
        distance=0;
        switch(type){
            case 0:
                health=100;
                speed=6;
                armor=0;
                value=5;
                break;
            case 1:
                health=200;
                speed=4;
                armor=5;
                value=10;
                break;
            case 2:
                health=170;
                speed=8;
                armor=0;
                value=5;
                break;
            case 3:
                health=200;
                speed=10;
                armor=1;
                value=8;
                break;
            case 4:
                health=600;
                speed=8;
                armor=3;
                value=25;
                break;
            case 5:
                health=1000;
                speed=6;
                armor=7;
                value=30;
                break;
            default:
                health=2000;
                speed=2;
                armor=10;
                value=100;
        }
        dx=speed;
        dy=0;
    }
    public boolean tick(Canvas canvas){
        if(!processDirection(route,routeDir)){
            return false;
        }
        setLocation(location.centerX()+dx,location.centerY()+dy);
        canvas.drawBitmap(bmp,source,location,null);
        distance++;
        return true;
    }
    public int attack(int damage,int penetration){
        health-=damage+(penetration-armor)*damage*.05;
        if(health<=0){
            return value;
        }
        return 0;
    }
    public boolean processDirection(int[] route,boolean[] routeDir){
        try{
            if(step%2==0&&(location.centerX()>route[step]&&dx>0||location.centerX()<route[step]&&dx<0)){
                dx=0;
                dy=routeDir[step]?speed:-speed;
                direction=routeDir[step]?1:3;
                step++;
            }
            else if(step%2==1&&(location.centerY()>route[step]&&dy>0||location.centerY()<route[step]&&dy<0)){
                dx=routeDir[step]?speed:-speed;
                dy=0;
                direction=routeDir[step]?0:2;
                step++;
            }
            switch(direction){
                case 0:
                    source=new Rect(0,bmp.getHeight()/2,bmp.getWidth()/2,bmp.getHeight());
                    break;
                case 1:
                    source=new Rect(0,0,bmp.getWidth()/2,bmp.getHeight()/2);
                    break;
                case 2:
                    source=new Rect(bmp.getWidth()/2,bmp.getHeight()/2,bmp.getWidth(),bmp.getHeight());
                    break;
                default:
                    source=new Rect(bmp.getWidth()/2,0,bmp.getWidth(),bmp.getHeight()/2);
            }
        }
        catch(IndexOutOfBoundsException e){
            return false;
        }
        return true;
    }
    public void setLocation(int x,int y){
        location=new Rect(x-width/2-20,y-height/2-20,x+width/2+20,y+height/2+20);
    }
    public Rect getLocation(){
        return location;
    }
    public int getDistance(){
        return distance;
    }
}