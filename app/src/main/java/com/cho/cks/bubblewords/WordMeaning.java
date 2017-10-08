package com.cho.cks.bubblewords;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by CKS on 2016-06-25.
 */
public class WordMeaning {

    public int x, y ; //좌표
    public Paint paint;
    public String strMeaning;

    private int loop = 0;
    private int color = Color.WHITE;

    // 생성자
    public WordMeaning( Context context, int _x, int _y, String _strMeaning) {
        x = _x;
        y = _y;
        strMeaning = _strMeaning;

        loop = 0;
        paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(16 * context.getResources().getDisplayMetrics().density);
        //paint.setFakeBoldText(true);
        paint.setAntiAlias(true);

        Move();
    }

    public boolean Move() {
        y -= 4;

        if ( y < -20 ) return false;

        loop++;

        if (loop%4 == 0) {
            color = ( Color.WHITE + Color.YELLOW) - color;
            paint.setColor(color);
        }

        return true;

    }
}
