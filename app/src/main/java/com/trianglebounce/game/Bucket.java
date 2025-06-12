package com.trianglebounce.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Bucket {
    private float x, y;
    private float width = 100;
    private float height = 50;
    private RectF bucketRect;
    
    public Bucket(float x, float y) {
        this.x = x;
        this.y = y;
        bucketRect = new RectF(x, y, x + width, y + height);
    }
    
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        
        canvas.drawLine(x, y, x, y + height, paint);
        canvas.drawLine(x, y + height, x + width, y + height, paint);
        canvas.drawLine(x + width, y + height, x + width, y, paint);
        
        paint.setStyle(Paint.Style.FILL);
    }
    
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
}