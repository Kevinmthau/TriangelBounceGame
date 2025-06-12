package com.trianglebounce.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ball {
    public float x, y;
    private float vx, vy;
    private float radius = 15;
    private float gravity = 0.3f;
    private float bounce = 0.8f;
    
    public Ball(float x, float y) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
    }
    
    public void update() {
        vy += gravity;
        x += vx;
        y += vy;
        
        vx *= 0.99f;
    }
    
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(x, y, radius, paint);
    }
    
    public boolean isCollidingWith(Triangle triangle) {
        return triangle.contains(x, y) || isNearTriangleEdge(triangle);
    }
    
    private boolean isNearTriangleEdge(Triangle triangle) {
        float[] vertices = triangle.getVertices();
        
        for (int i = 0; i < 6; i += 2) {
            int nextIndex = (i + 2) % 6;
            float x1 = vertices[i];
            float y1 = vertices[i + 1];
            float x2 = vertices[nextIndex];
            float y2 = vertices[nextIndex + 1];
            
            float distance = distanceToLineSegment(x, y, x1, y1, x2, y2);
            if (distance <= radius) {
                return true;
            }
        }
        return false;
    }
    
    private float distanceToLineSegment(float px, float py, float x1, float y1, float x2, float y2) {
        float A = px - x1;
        float B = py - y1;
        float C = x2 - x1;
        float D = y2 - y1;
        
        float dot = A * C + B * D;
        float lenSq = C * C + D * D;
        float param = (lenSq != 0) ? dot / lenSq : -1;
        
        float xx, yy;
        
        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }
        
        float dx = px - xx;
        float dy = py - yy;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
    
    public void bounceOff(Triangle triangle) {
        float[] vertices = triangle.getVertices();
        float closestDistance = Float.MAX_VALUE;
        float normalX = 0, normalY = 0;
        
        for (int i = 0; i < 6; i += 2) {
            int nextIndex = (i + 2) % 6;
            float x1 = vertices[i];
            float y1 = vertices[i + 1];
            float x2 = vertices[nextIndex];
            float y2 = vertices[nextIndex + 1];
            
            float distance = distanceToLineSegment(x, y, x1, y1, x2, y2);
            if (distance < closestDistance) {
                closestDistance = distance;
                
                float edgeX = x2 - x1;
                float edgeY = y2 - y1;
                float length = (float) Math.sqrt(edgeX * edgeX + edgeY * edgeY);
                normalX = -edgeY / length;
                normalY = edgeX / length;
            }
        }
        
        float dotProduct = vx * normalX + vy * normalY;
        vx = vx - 2 * dotProduct * normalX;
        vy = vy - 2 * dotProduct * normalY;
        
        vx *= bounce;
        vy *= bounce;
        
        x += normalX * (radius - closestDistance + 1);
        y += normalY * (radius - closestDistance + 1);
    }
    
    public boolean isInBucket(Bucket bucket) {
        return x >= bucket.getX() && x <= bucket.getX() + bucket.getWidth() &&
               y >= bucket.getY() && y <= bucket.getY() + bucket.getHeight();
    }
}