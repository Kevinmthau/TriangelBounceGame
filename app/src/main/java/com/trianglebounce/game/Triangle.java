package com.trianglebounce.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class Triangle {
    private float x, y;
    private float size = 60;
    private Path trianglePath;
    
    public Triangle(float x, float y) {
        this.x = x;
        this.y = y;
        createTrianglePath();
    }
    
    private void createTrianglePath() {
        trianglePath = new Path();
        trianglePath.moveTo(x, y - size);
        trianglePath.lineTo(x - size, y + size);
        trianglePath.lineTo(x + size, y + size);
        trianglePath.close();
    }
    
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.RED);
        canvas.drawPath(trianglePath, paint);
    }
    
    public boolean contains(float px, float py) {
        float d1, d2, d3;
        boolean has_neg, has_pos;
        
        float x1 = x, y1 = y - size;
        float x2 = x - size, y2 = y + size;
        float x3 = x + size, y3 = y + size;
        
        d1 = sign(px, py, x1, y1, x2, y2);
        d2 = sign(px, py, x2, y2, x3, y3);
        d3 = sign(px, py, x3, y3, x1, y1);
        
        has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);
        
        return !(has_neg && has_pos);
    }
    
    private float sign(float p1x, float p1y, float p2x, float p2y, float p3x, float p3y) {
        return (p1x - p3x) * (p2y - p3y) - (p2x - p3x) * (p1y - p3y);
    }
    
    public float getX() { return x; }
    public float getY() { return y; }
    public float getSize() { return size; }
    
    public float[] getVertices() {
        return new float[]{
            x, y - size,
            x - size, y + size,
            x + size, y + size
        };
    }
}