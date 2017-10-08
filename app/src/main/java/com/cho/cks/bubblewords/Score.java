package com.cho.cks.bubblewords;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by CKS on 2016-06-21.
 */
public class Score {
    public int x, y, sw, sh;
    public Bitmap imgScore;

    private Bitmap fonts[] = new Bitmap[10]; // 글꼴 이미지 저장
    private int loop = 0;

    public Score (Context context, int _x, int _y, int _score){

        x = _x;
        y = _y;

        for ( int i = 0; i <= 9; i++){
            fonts[i] = BitmapFactory.decodeResource(context.getResources(), com.cho.cks.bubblewords.R.drawable.f0 + i);
        }
        MakeScore(_score);
    }

    public void MakeScore(int _score){
        String score = ""+_score;
        int L = score.length();

        imgScore = Bitmap.createBitmap(fonts[0].getWidth()*L, fonts[0].getHeight(), fonts[0].getConfig());

        Canvas canvas = new Canvas();
        canvas.setBitmap(imgScore);

        int w = fonts[0].getWidth();

        for ( int i = 0; i < L; i++){
            int n = (int) score.charAt(i) - 48;
            canvas.drawBitmap(fonts[n], w * i, 0, null);
        }

        sw = imgScore.getWidth() / 2;
        sh = imgScore.getHeight() / 2;
    }
}
