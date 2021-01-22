package com.samsung.itschool.mysurfaceexample;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class GameMap {
    int sizeTexture = 32;

    int mapArray[][];
    Bitmap textures[];

    public GameMap(int width, int height, Resources resources){
        Random random = new Random();
        mapArray = new int[height / 32][width / 32];
        for (int i = 0; i < mapArray.length; i++) {
            for (int j = 0; j < mapArray[i].length; j++) {
                if(j > mapArray[i].length/2 - 5 - random.nextInt(7) &&
                    j < mapArray[i].length/2 + 5 + random.nextInt(7))
                    mapArray[i][j] = 0;
                else
                    mapArray[i][j] = random.nextInt(3) + 1;
            }
        }

        textures = new Bitmap[4];
        textures[0] = BitmapFactory.decodeResource(resources, R.drawable.grass);
        textures[1] = BitmapFactory.decodeResource(resources, R.drawable.water);
        textures[2] = BitmapFactory.decodeResource(resources, R.drawable.rock);
        textures[3] = BitmapFactory.decodeResource(resources, R.drawable.lava);

    }

    public void draw(Canvas canvas){
        float x = 0, y = 0;
        Paint paint = new Paint();
        for (int i = 0; i < mapArray.length; i++) {
            for (int j = 0; j < mapArray[i].length; j++) {
                canvas.drawBitmap(textures[mapArray[i][j]], x, y, paint);
                x += sizeTexture;
            }
            y += sizeTexture;
            x = 0;
        }
        changeMap();
    }

    private void changeMap(){
        for (int i = mapArray.length - 2; i >= 0 ; i--) {
            for (int j = 0; j < mapArray[i].length; j++) {
                mapArray[i + 1][j] = mapArray[i][j];
            }
        }
        Random random = new Random();
        for (int j = 0; j < mapArray[0].length; j++) {
            if(j > mapArray[0].length/2 - 5 - random.nextInt(7) &&
                    j < mapArray[0].length/2 + 5 + random.nextInt(7))
                mapArray[0][j] = 0;
            else
                mapArray[0][j] = random.nextInt(3) + 1;
        }
    }

}
