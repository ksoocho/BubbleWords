package com.cho.cks.bubblewords;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

/**
 * Created by CKS on 2016-06-14.
 */
public class SmallBall {
    public int x, y, rad;			// 좌표, 반지름
    public  boolean dead = false; 	// 터뜨림 여부
    public Bitmap imgBall;			// 비트맵 이미지

    private int width, height;		// View의 크기
    private int cx, cy;				// 원의 중심점
    private int cr;					// 원의 반지름
    private double r;				// 이동 각도 (radian)
    private int speed;				// 이동 속도
    private int num;				// 이미지 번호
    private int life;				// 생명 주기

    //-------------------------------------
    //  생성자
    //-------------------------------------
    public SmallBall(Context context, int _x, int _y, int ang, int _width, int _height) {
        cx = _x;		// 중심점
        cy = _y;
        width = _width;
        height = _height;
        r = ang * Math.PI / 180;		// 각도 radian

        Random rnd = new Random();
        speed = rnd.nextInt(5) + 2;		// 속도     : 2~6
        rad = rnd.nextInt(10) + 5;		// 반지름   : 5~14
        num = rnd.nextInt(6);			// 이미지  : 0~5
        life = rnd.nextInt(31) + 20;	// 20~50

        imgBall = BitmapFactory.decodeResource(context.getResources(), com.cho.cks.bubblewords.R.drawable.b0 + num);
        imgBall = Bitmap.createScaledBitmap(imgBall, rad * 2, rad * 2, false);
        cr = 10;						// 원의 초기 반지름
        MoveBall();
    }

    //-------------------------------------
    //  MoveBall
    //-------------------------------------
    public void MoveBall() {
        life--;
        cr += speed;
        x = (int) (cx + Math.cos(r) * cr);
        y = (int) (cy - Math.sin(r) * cr);
        if (x < -rad || x > width + rad ||
                y < -rad || y > height + rad || life <= 0)
            dead = true;
    }
}
