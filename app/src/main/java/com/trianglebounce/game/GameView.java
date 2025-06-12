package com.trianglebounce.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {
    private Thread gameThread = null;
    private SurfaceHolder surfaceHolder;
    private volatile boolean playing;
    private boolean paused = true;
    private Canvas canvas;
    private Paint paint;
    
    private int screenX, screenY;
    private List<Triangle> triangles;
    private Ball ball;
    private Bucket bucket;
    private boolean gameWon = false;
    private boolean ballDropped = false;
    
    public GameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        paint = new Paint();
        triangles = new ArrayList<>();
    }
    
    @Override
    public void run() {
        while (playing) {
            if (!paused) {
                update();
                draw();
            }
        }
    }
    
    private void update() {
        if (ball != null && ballDropped) {
            ball.update();
            
            synchronized (triangles) {
                for (Triangle triangle : triangles) {
                    if (ball.isCollidingWith(triangle)) {
                        ball.bounceOff(triangle);
                    }
                }
            }
            
            if (ball.y > screenY) {
                resetGame();
            }
            
            if (bucket != null && ball.isInBucket(bucket)) {
                gameWon = true;
            }
        }
    }
    
    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            
            paint.setColor(Color.WHITE);
            paint.setTextSize(32);
            if (triangles.size() < 4) {
                canvas.drawText("Tap to place triangles (" + triangles.size() + "/4)", 20, screenY - 40, paint);
            } else if (!ballDropped) {
                canvas.drawText("Tap to drop ball", 20, screenY - 40, paint);
            }
            
            synchronized (triangles) {
                for (Triangle triangle : triangles) {
                    triangle.draw(canvas, paint);
                }
            }
            
            if (bucket != null) {
                bucket.draw(canvas, paint);
            }
            
            if (ball != null) {
                ball.draw(canvas, paint);
            }
            
            if (gameWon) {
                paint.setColor(Color.GREEN);
                paint.setTextSize(64);
                canvas.drawText("YOU WIN!", screenX/2 - 120, screenY/2, paint);
            }
            
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
    
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                paused = false;
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                
                if (triangles.size() < 4) {
                    synchronized (triangles) {
                        triangles.add(new Triangle(x, y));
                        if (triangles.size() == 4) {
                            bucket = new Bucket(screenX/2 - 50, screenY - 80);
                        }
                    }
                } else if (!ballDropped) {
                    ball = new Ball(screenX/2, 80);
                    ballDropped = true;
                }
                break;
        }
        return true;
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenX = w;
        screenY = h;
    }
    
    private void resetGame() {
        synchronized (triangles) {
            triangles.clear();
        }
        ball = null;
        bucket = null;
        gameWon = false;
        ballDropped = false;
    }
}