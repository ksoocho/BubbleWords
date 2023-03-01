package com.cho.cks.bubblewords;

import android.content.*;
import android.graphics.*;

import java.util.*;

/**
 * Created by CKS on 2016-06-12.
 */
public class MyBubble {

    public int x, y, rad;            // 위치, 반지름
    public Bitmap imgBbl;            // 비누방울 비트맵 이미지
    public boolean dead = false;    // 터질 것인지 여부
    public String strWord;
    public String strMeaning;

    private int count = 0;            // 벽과의 충돌 회수
    private int sx, sy;                // 이동 방향과 속도
    private int width, height;        // View의 크기

    //----------------------------------
    //  생성자(Constructor)
    //----------------------------------
    public MyBubble(Context context, int _x, int _y, int _width, int _height) {
        x = _x;                // 파라메터 저장
        y = _y;
        width = _width;        // View의 크기
        height = _height;

        Random rnd = new Random();
        rad = 40; //rnd.nextInt(31) + 10;			  // 10~40 : 반지름
        int k = rnd.nextInt(2) == 0 ? -1 : 1;        // -1, 1
        sx = (rnd.nextInt(4) + 1) * k;                // ± 2~5 : 속도
        sy = (rnd.nextInt(4) + 1) * k;                // ± 2~5

        // 비트맵 이미지를 위에서 설정한 반지름 2배 크기로 만든다
        imgBbl = BitmapFactory.decodeResource(context.getResources(), com.cho.cks.bubblewords.R.drawable.bubble);
        imgBbl = Bitmap.createScaledBitmap(imgBbl, rad * 2, rad * 2, false);
        MoveBubble();        // 비누방울 이동
    }

    public MyBubble(Context context, int _x, int _y, int _width, int _height, String _strWord, String _strMeaning) {
        x = _x;                // 파라메터 저장
        y = _y;
        width = _width;        // View의 크기
        height = _height - 300;

        strWord = _strWord;
        strMeaning = _strMeaning;

        Random rnd = new Random();


        int k = rnd.nextInt(2) == 0 ? -1 : 1;        // -1, 1
        sx = (rnd.nextInt(4) + 1) * k;                // ± 2~5 : 속도
        sy = (rnd.nextInt(4) + 1) * k;                // ± 2~5

        // 비트맵 이미지를 위에서 설정한 반지름 2배 크기로 만든다
        imgBbl = textAsBitmap(context, _strWord);

        rad = imgBbl.getWidth()/2;

        MoveBubble();        // 비누방울 이동
    }

    //----------------------------------
    //  비누방울 이동
    //----------------------------------
    public void MoveBubble() {
        x += sx;    // 이동
        y += sy;

        if (x <= rad || x >= width - rad) {     // 좌우의 벽
            sx = -sx;                           // 반대 방향으로 반사
            count++;                            // 벽과 부딪친 횟수
        }

        if (y <= rad || y >= height - rad) {
            sy = -sy;
            count++;
        }

        // 벽과 10번 이상 충돌이면 터뜨림
        //if (count >= 10) dead = true;
    }

    public Bitmap textAsBitmap(Context context, String text) {

        Paint paint = new Paint();
        paint.setTextSize(16  * context.getResources().getDisplayMetrics().density);
        paint.setColor(Color.YELLOW);
        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.LEFT);

        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);

        // 단어 풍선의 기본크기
        // 너무 작으면 선택하기 힘들다
        if (width < 100) width = 100;

        Bitmap image = BitmapFactory.decodeResource(context.getResources(), com.cho.cks.bubblewords.R.drawable.bubble_1);
        image = Bitmap.createScaledBitmap(image, width+10, width+10, false);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 5, canvas.getHeight()/2+5 , paint);
        return image;
    }
}
