package com.samsung.itschool.mysurfaceexample;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.Random;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    Bitmap image, wall;
    Paint paint;
    float iX, iY, tX = 0, tY = 0, wallX, wallY;
    float dx = 0, dy = 0;
    Resources res;
    MyThread myThread;
    //контроль столкновений и размеров
    float hi, wi;//ширина и высота изображения
    float hs, ws;//ширина и высота области рисования
    boolean isFirstDraw = true;

    Rect wallRect, imageRect;

    public MySurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
        res = getResources();
        image = BitmapFactory.decodeResource(res, R.drawable.frankenshtain);
        wall = BitmapFactory.decodeResource(res, R.drawable.wall);
        hi = image.getHeight();
        wi = image.getWidth();
        iX = 100;
        iY = 100;
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(5);
        setAlpha(0);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        myThread = new MyThread(getHolder(), this);
        myThread.setRunning(true);
        myThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        myThread.setRunning(false);
        try {
            myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(isFirstDraw){
            hs = canvas.getHeight();
            ws = canvas.getWidth();
            wallX = ws / 2;
            Random random = new Random();
            wallY = random.nextInt((int)(hs - wall.getHeight() - 5));
            wallRect = new Rect((int)wallX, (int)wallY, (int)(wallX + wall.getWidth()),
                    (int)(wallY + wall.getHeight()));
            isFirstDraw = false;
        }
        canvas.drawBitmap(image, iX, iY, paint);
        canvas.drawBitmap(wall, wallX, wallY, paint);
        //canvas.drawLine(iX, iY, tX, tY, paint);
        //if(tX != 0)
            //delta();
        imageRect = new Rect((int)iX, (int)iY, (int) (iX + wi), (int)(iY + hi));
        if(imageRect.intersect(wallRect)){
            dy = 0;
            dx = 0;
        }

        iX += dx;
        iY += dy;
        checkScreen();
    }

    private void checkScreen(){
        if(iY + hi >= hs || iY <= 0)
            dy = -dy;
        if(iX + wi >= ws || iX <= 0)
            dx = -dx;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            tX = event.getX();
            tY = event.getY();
            delta();
        }
        return true;
    }
    //расчет смещения картинки по x и y
    void delta(){
        double ro = Math.sqrt(Math.pow(tX-iX, 2)+Math.pow(tY-iY, 2));
        double k = 15;
        dx = (float) (k * (tX - iX)/ro);
        dy = (float) (k * (tY - iY)/ro);
    }
}
