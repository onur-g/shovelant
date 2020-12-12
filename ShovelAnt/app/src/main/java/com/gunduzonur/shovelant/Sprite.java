package com.gunduzonur.shovelant;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
public class Sprite{
    private final int FRAMES=6;
    private Bitmap bmp;
    private Rect source;
    private Rect location;
    private double scale;
    private int row,width,height,frame,dx,dy,ds;
    public Sprite(Bitmap bmp,int sprites,int row,int width,int height,int x,int y,double scale){
        this.bmp=Bitmap.createScaledBitmap(bmp,FRAMES*width,sprites*height,false);
        this.row=row;
        this.width=width;
        this.height=height;
        this.scale=scale;
        frame=0;
        setLocation(x,y);
    }
    public void tick(Canvas canvas){
        source=new Rect(width*frame,height*row,width*(frame+1),height*(row+1));
        frame=(frame+1)%FRAMES;
        scale+=(double)(ds)/100;
        setLocation(location.centerX()+dx,location.centerY()+dy);
        canvas.drawBitmap(bmp,source,location,null);
    }
    public boolean isTouching(Sprite sprite){
        Rect hitBox=new Rect(location);
        int drawWidth=sprite.getLocation().width()-5; // -5 as an arbitrary buffer so this game isn't so gosh darn hard
        int drawHeight=sprite.getLocation().height()-5;
        hitBox.left-=drawWidth;
        hitBox.top-=drawHeight;
        hitBox.right+=drawWidth;
        hitBox.bottom+=drawHeight;
        return hitBox.contains(sprite.getLocation());
    }
    public void setDelta(int dx,int dy,int ds){
        this.dx=dx;
        this.dy=dy;
        this.ds=ds;
    }
    public void setLocation(int x,int y){
        int drawWidth=(int)(width*scale/2);
        int drawHeight=(int)(height*scale/2);
        location=new Rect(x-drawWidth,y-drawHeight,x+drawWidth,y+drawHeight);
    }
    public void setType(int row){
        this.row=row;
    }
    public void setScale(double scale){
        this.scale=scale;
    }
    public Rect getLocation(){
        return location;
    }
    public int getType(){
        return row;
    }
}