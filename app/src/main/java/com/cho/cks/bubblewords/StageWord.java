package com.cho.cks.bubblewords;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by CKS on 2016-06-23.
 */
public class StageWord {
    private Bitmap bitmapStage;
    private Context mContext;


    public StageWord(Context context, int view_width)
    {
        mContext = context;

        bitmapStage = BitmapFactory.decodeResource(mContext.getResources(), com.cho.cks.bubblewords.R.drawable.button);
        bitmapStage = Bitmap.createScaledBitmap(bitmapStage, 100 , 100, false);
    }

    void MakeStage(){

        int difficult = ((MyApp)mContext.getApplicationContext()).getDifficult();

        // 난이도를 가지고 오지 못하는 경우 - 영어 / 초급
        if(difficult == 0) {

            ((MyApp)mContext.getApplicationContext()).setWordLang("ENGLISH");
            ((MyApp)mContext.getApplicationContext()).setDifficult(1);
            ((MyApp)mContext.getApplicationContext()).setWordStage();
            ((MyApp)mContext.getApplicationContext()).setIsChangeLang(true);

        }

        // 난이도 설명
        String str = ((MyApp)mContext.getApplicationContext()).getDifficultDescr();

        Paint paint = new Paint();
        paint.setTextSize(16 * mContext.getResources().getDisplayMetrics().density);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setFakeBoldText(true);

        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(str) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);

        Bitmap image = BitmapFactory.decodeResource(mContext.getResources(), com.cho.cks.bubblewords.R.drawable.button);

        int imgWidth = ((MyApp)mContext.getApplicationContext()).btnStageWidth;
        int imgHeight = ((MyApp)mContext.getApplicationContext()).btnStageHeight;

        image = Bitmap.createScaledBitmap(image, imgWidth, imgHeight, false);

        Canvas canvas = new Canvas(image);
        canvas.drawText(str, canvas.getWidth()/2, canvas.getHeight()/2+7 , paint);

        bitmapStage = image;
    }

    public Bitmap GetStageImage()
    {
        return this.bitmapStage;
    }
}
