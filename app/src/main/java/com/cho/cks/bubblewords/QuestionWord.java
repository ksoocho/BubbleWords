package com.cho.cks.bubblewords;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;

/**
 * Created by CKS on 2016-06-19.
 */
public class QuestionWord {
    private Bitmap bitmapQuestion;
    private String questionWord;
    private Context mContext;

    public QuestionWord(Context context, int view_width)
    {
        mContext = context;

        bitmapQuestion = BitmapFactory.decodeResource(mContext.getResources(), com.cho.cks.bubblewords.R.drawable.enter);
        bitmapQuestion = Bitmap.createScaledBitmap(bitmapQuestion, view_width -100 , 70, false);
    }

    public void MakeQuestionWord( String strWord, String strMeaning, int view_width) {

        questionWord = strWord;

        Paint paint = new Paint();
        paint.setTextSize(16 * mContext.getResources().getDisplayMetrics().density);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.LEFT);
        //paint.setFakeBoldText(true);

        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(strMeaning) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);

        Bitmap image = BitmapFactory.decodeResource(mContext.getResources(), com.cho.cks.bubblewords.R.drawable.enter);

        int imgWidth = ((MyApp)mContext.getApplicationContext()).btnQuestionWidth;
        int imgHeight = ((MyApp)mContext.getApplicationContext()).btnQuestionHeight;

        image = Bitmap.createScaledBitmap(image, view_width - imgWidth , imgHeight, false);

        Canvas canvas = new Canvas(image);
        canvas.drawText(strMeaning, 15, canvas.getHeight()/2+5 , paint);

        bitmapQuestion = image;
    }

    public String GetQuestionWord()
    {
        return this.questionWord;
    }

    public Bitmap GetQuestionImage()
    {
        return this.bitmapQuestion;
    }
}
