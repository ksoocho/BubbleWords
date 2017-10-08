package com.cho.cks.bubblewords;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by CKS on 2016-06-26.
 */

public class Help extends Activity {

    private Button finishActivityButton;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.cho.cks.bubblewords.R.layout.help);

        // English
        ImageView ivEnglish = (ImageView)findViewById(com.cho.cks.bubblewords.R.id.imgEnglish);
        Bitmap imgEnglish = BitmapFactory.decodeResource(getResources(), com.cho.cks.bubblewords.R.drawable.english);
        Bitmap resizeEnglish = Bitmap.createScaledBitmap(imgEnglish, 100, 100, true);
        ivEnglish.setImageBitmap(resizeEnglish);
        ivEnglish.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // 레이아웃 크기에 이미지를 맞춘다

        // Chiness
        ImageView ivChiness = (ImageView)findViewById(com.cho.cks.bubblewords.R.id.imgChiness);
        Bitmap imgChiness = BitmapFactory.decodeResource(getResources(), com.cho.cks.bubblewords.R.drawable.china);
        Bitmap resizeChiness = Bitmap.createScaledBitmap(imgChiness, 100, 100, true);
        ivChiness.setImageBitmap(resizeChiness);
        ivChiness.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // 레이아웃 크기에 이미지를 맞춘다

        // Japaness
        ImageView ivJapaness = (ImageView)findViewById(com.cho.cks.bubblewords.R.id.imgJapaness);
        Bitmap imgJapaness = BitmapFactory.decodeResource(getResources(), com.cho.cks.bubblewords.R.drawable.japan);
        Bitmap resizeJapaness = Bitmap.createScaledBitmap(imgJapaness, 100, 100, true);
        ivJapaness.setImageBitmap(resizeJapaness);
        ivJapaness.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // 레이아웃 크기에 이미지를 맞춘다

        // Stage
        ImageView ivStage = (ImageView)findViewById(com.cho.cks.bubblewords.R.id.imgStage);
        Bitmap imgStage = BitmapFactory.decodeResource(getResources(), com.cho.cks.bubblewords.R.drawable.button);
        Bitmap resizeStage = Bitmap.createScaledBitmap(imgStage, 100, 100, true);
        ivStage.setImageBitmap(resizeStage);
        ivStage.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // 레이아웃 크기에 이미지를 맞춘다

        // Yellow Submarine
        ImageView ivMarine = (ImageView)findViewById(com.cho.cks.bubblewords.R.id.imgMarine);
        Bitmap imgMarine = BitmapFactory.decodeResource(getResources(), com.cho.cks.bubblewords.R.drawable.submarine);
        Bitmap resizeMarine = Bitmap.createScaledBitmap(imgMarine, 100, 100, true);
        ivMarine.setImageBitmap(resizeMarine);
        ivMarine.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // 레이아웃 크기에 이미지를 맞춘다

        // Sound
        ImageView ivSound = (ImageView)findViewById(com.cho.cks.bubblewords.R.id.imgSound);
        Bitmap imgSound = BitmapFactory.decodeResource(getResources(), com.cho.cks.bubblewords.R.drawable.soundon);
        Bitmap resizeSound = Bitmap.createScaledBitmap(imgSound, 100, 100, true);
        ivSound.setImageBitmap(resizeSound);
        ivSound.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // 레이아웃 크기에 이미지를 맞춘다

        finishActivityButton = (Button)findViewById(com.cho.cks.bubblewords.R.id.finishActivity);

        finishActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
