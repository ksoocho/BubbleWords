package com.cho.cks.bubblewords;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;

/**
 * Created by CKS on 2016-06-13.
 */
public class MyThread extends Thread {

    SurfaceHolder mHolder;
    Context mContext;
    QuestionWord questionWord;
    StageWord stageWord;

    int SuccessTot = 0;			// 득점 합계
    int FailTot = 0;				// 득점 합계

    int width, height;		// View의 폭과 높이
    Bitmap imgBack;
    Bitmap imgNext;
    Bitmap imgQuestion;
    Bitmap imgStage;
    Bitmap imgLanguage;
    Bitmap imgSound;
    Bitmap imgHelp;

    ArrayList<MyBubble> mBubble = new ArrayList<MyBubble>();  //  단어 풍선
    ArrayList<SmallBall> sBall = new ArrayList<SmallBall>();   //  작은 풍선
    ArrayList<WordMeaning> arrMeaning = new ArrayList<WordMeaning>(); // 단어의미

    Score totScore, failScore;

    boolean canRun = true;
    boolean isWait = false;

    private Speaker speaker;
    //-------------------------------------
    //  생성자
    //-------------------------------------
    public MyThread(SurfaceHolder holder, Context context) {

        mHolder = holder;	// SurfaceHolder 보존
        mContext = context;

        Display display = ((WindowManager) mContext.getSystemService (Context.WINDOW_SERVICE)).getDefaultDisplay();
        width = display.getWidth();		// View의 가로 폭
        height = display.getHeight() - 50;   // View의 세로 높이

        questionWord = new QuestionWord(mContext,width);
        stageWord = new StageWord(mContext,width);

        // -------------------------------------
        // Background
        // -------------------------------------
        imgBack = BitmapFactory.decodeResource(mContext.getResources(), com.cho.cks.bubblewords.R.drawable.sea);
        imgBack = Bitmap.createScaledBitmap(imgBack, width, height, false);

        // -------------------------------------
        // Word Language
        // -------------------------------------
        MakeLanguage();

        // -------------------------------------
        // Word Stage
        // -------------------------------------
        MakeStage();

        // -------------------------------------
        //잠수함
        // -------------------------------------
        imgNext = BitmapFactory.decodeResource(context.getResources(), com.cho.cks.bubblewords.R.drawable.submarine);

        int imgMainWidth = ((MyApp)mContext.getApplicationContext()).btnMainWidth;
        int imgMainHeight = ((MyApp)mContext.getApplicationContext()).btnMainHeight;

        imgNext = Bitmap.createScaledBitmap(imgNext, imgMainWidth, imgMainHeight, false);

        // -------------------------------------
        // Soune On / Off
        // -------------------------------------
        MakeSound();

        // -------------------------------------
        // Help
        // -------------------------------------
        imgHelp = BitmapFactory.decodeResource(mContext.getResources(), com.cho.cks.bubblewords.R.drawable.help);

        int imgHelpWidth = ((MyApp)mContext.getApplicationContext()).btnHelpWidth;
        int imgHelpHeight = ((MyApp)mContext.getApplicationContext()).btnHelpHeight;

        imgHelp = Bitmap.createScaledBitmap(imgHelp, imgHelpWidth, imgHelpHeight, false);

        // -------------------------------------
        // Question Word
        // -------------------------------------
        imgQuestion = BitmapFactory.decodeResource(mContext.getResources(), com.cho.cks.bubblewords.R.drawable.enter);
        imgQuestion = Bitmap.createScaledBitmap(imgQuestion, width -100 , 70, false);

        // -------------------------------------
        // 총점
        // -------------------------------------
        totScore = new Score(mContext,0,0,0);
        failScore = new Score(mContext,0,0,0);

        // -------------------------------------
        // TTS
        // -------------------------------------
        speaker = new Speaker(mContext);
        speaker.allow(true);
    }

    // -----------------------------
    // Thread 완전정지
    // -----------------------------
    public void StopThread()
    {
        canRun = false;
        synchronized (this){
            this.notify();
        }

        speaker.destroy();
    }

    // -----------------------------
    // Thread 완전정지
    // -----------------------------
    public void PauseThread(boolean wait)
    {
        isWait = wait;
        synchronized (this){
            this.notify();
        }
    }
    //------------------------------------
    //   Touch가 비누방울 내부인지 조사
    //------------------------------------
     void CheckBubble( String strWord, String strMeaning) {

        // 화면에서 터치한 경우에 Bubble 생성하는 로직
        //for (MyBubble tmp :  mBubble) {
        //   if (Math.pow(tmp.x - x, 2) + Math.pow(tmp.y - y, 2) <= Math.pow(tmp.rad, 2)){
        //        tmp.dead = true;
        //        flag = true;
         //   }
        //}
         synchronized (mBubble) {
             mBubble.add(new MyBubble(mContext, width/2, height/2, width, height, strWord, strMeaning));
         }
    }

    //------------------------------------
    //   Destroy Bubble
    //------------------------------------
    void DestroyBubble(int x, int y) {

        boolean checkOK = false;
        boolean checkNG = false;

        for (MyBubble tmp :  mBubble) {
            if (Math.pow(tmp.x - x, 2) + Math.pow(tmp.y - y, 2) <= Math.pow(tmp.rad, 2)){

                String strQWord = questionWord.GetQuestionWord();
                String strMeaning = tmp.strMeaning;

                Log.d("DEBUG","Ball Word - "+tmp.strWord+":"+strQWord);

                // 단어 읽어주기
                boolean isSound = ((MyApp)mContext.getApplicationContext()).getIsSound();

                if (isSound) {
                    String lang = ((MyApp)mContext.getApplicationContext()).getWordLang();
                    speaker.speak(lang, tmp.strWord);
                }

                if (tmp.strWord.equals(strQWord)) {
                    checkOK = true;
                } else {
                    // 단어의미 보이기
                    arrMeaning.add(new WordMeaning(mContext, tmp.x, tmp.y, strMeaning));
                    Log.d("DEBUG","Word Meaning-"+strMeaning);
                    checkNG = true;
                }
            }
        }

        // 맞추면 모든 Bubble Destroy
        if ( checkOK == true){
            SuccessTot += 1;
            Log.d("DEBUG","Ball Word - check OK");
            for (MyBubble tmp :  mBubble) {
                tmp.dead = true;
            }
        }

        // 틀리면 ...
        if ( checkNG == true){
            FailTot += 1;
        }
    }

    //------------------------------------
    //   Clear All Bubble
    //------------------------------------
    void ClearBubble() {
        for (MyBubble tmp :  mBubble) {
            tmp.dead = true;
        }
    }
    //------------------------------------
    //   Make Bubble
    //------------------------------------
    void MakeBubble(int x, int y, String strWord, String strMeaning) {
            boolean flag = false;

        synchronized (mBubble) {
            mBubble.add(new MyBubble(mContext, x, y, width, height, strWord, strMeaning));
            //mBubble.add(new MyBubble(mContext, x, y, width, height));
        }
    }

    //------------------------------------
    //   Make Question
    //------------------------------------
    void MakeQuestion(String strWord, String strMeaning) {
        // Question Word
        questionWord.MakeQuestionWord(strWord, strMeaning, width);
        imgQuestion = questionWord.GetQuestionImage();
    }

    //------------------------------------
    //   Make Language
    //------------------------------------
    void MakeLanguage(){
        String wordLang = ((MyApp)mContext.getApplicationContext()).getWordLang();

        if (wordLang == "ENGLISH") {
            imgLanguage = BitmapFactory.decodeResource(mContext.getResources(), com.cho.cks.bubblewords.R.drawable.english);
        } else if (wordLang == "CHINESS"){
            imgLanguage = BitmapFactory.decodeResource(mContext.getResources(), com.cho.cks.bubblewords.R.drawable.china);
        } else if (wordLang == "JAPANESS"){
            imgLanguage = BitmapFactory.decodeResource(mContext.getResources(), com.cho.cks.bubblewords.R.drawable.japan);
        } else {
            imgLanguage = BitmapFactory.decodeResource(mContext.getResources(), com.cho.cks.bubblewords.R.drawable.english);
        }

        int imgWidth = ((MyApp)mContext.getApplicationContext()).btnLangWidth;
        int imgHeight = ((MyApp)mContext.getApplicationContext()).btnLangHeight;

        imgLanguage = Bitmap.createScaledBitmap(imgLanguage, imgWidth, imgHeight, false);
    }

    //------------------------------------
    //   Sound
    //------------------------------------
    void MakeSound(){
        boolean isSound = ((MyApp)mContext.getApplicationContext()).getIsSound();

        if (isSound) {
            imgSound = BitmapFactory.decodeResource(mContext.getResources(), com.cho.cks.bubblewords.R.drawable.soundon);
        } else {
            imgSound = BitmapFactory.decodeResource(mContext.getResources(), com.cho.cks.bubblewords.R.drawable.soundoff);
        }

        int imgWidth = ((MyApp)mContext.getApplicationContext()).btnSoundWidth;
        int imgHeight = ((MyApp)mContext.getApplicationContext()).btnSoundHeight;

        imgSound = Bitmap.createScaledBitmap(imgSound, imgWidth, imgHeight, false);

    }

    //------------------------------------
    //   Make Stage
    //------------------------------------
    void MakeStage(){
        stageWord.MakeStage();
        imgStage = stageWord.GetStageImage();
    }

    int CheckBubbleRunning()
    {
        int checkCount = 0;

        for (MyBubble tmp : mBubble) {
            checkCount++;
        }

        return checkCount;
    }
    //-------------------------------------
    //  작은  비눗방울 만들기
    //-------------------------------------
    private void MakeSmallBall(int x, int y) {
        Random rnd = new Random();

        int count = rnd.nextInt(9) + 7;   // 7~15개

        for (int i = 1; i <= count; i++) {
            int ang = rnd.nextInt(360);
            sBall.add(new SmallBall(mContext, x, y, ang, width, height));
        }
    }

    //----------------------------------
    //  비누방울 이동
    //----------------------------------
    private void MoveBubble() {

        // 큰 비눗방울 이동
        for (int i = mBubble.size() - 1; i >= 0; i--) {
            mBubble.get(i).MoveBubble();
            if (mBubble.get(i).dead == true) {
                MakeSmallBall(mBubble.get(i).x, mBubble.get(i).y);    // 작은 방울
                mBubble.remove(i);
            }
        }

        // 작은 비눗방울 이동
        for (int i = sBall.size() - 1; i >= 0; i--) {
            sBall.get(i).MoveBall();
            if (sBall.get(i).dead == true) {
                sBall.remove(i);
            }
        }

        // 단어의미 이동
        for (int i = arrMeaning.size() - 1; i >= 0; i--) {
            if (arrMeaning.get(i).Move() == false){
                arrMeaning.remove(i);
            }
        }
    }

    //-------------------------------------
    //  Canvas에 그리기
    //-------------------------------------
    public void run() {
        Canvas canvas = null; 					// canvas를 만든다

        while (canRun) {

            canvas = mHolder.lockCanvas();		// canvas를 잠그고 버퍼 할당

            try {
                synchronized (mHolder) {		// 동기화 유지
                    MoveBubble();

                    // --------------------------------------
                    // Background Image
                    // --------------------------------------
                    if(imgBack != null){
                        canvas.drawBitmap(imgBack, 0, 0, null);
                    } else {
                        Log.d("DEBUG","Background is null");
                    }

                    // --------------------------------------
                    // Language Image
                    // --------------------------------------
                    int imgLangXPos = ((MyApp)mContext.getApplicationContext()).btnLangXPos;
                    int imgLangYPos = ((MyApp)mContext.getApplicationContext()).btnLangYPos;

                    if(imgLanguage != null){
                        canvas.drawBitmap(imgLanguage, imgLangXPos, height - imgLangYPos, null);
                    } else {
                        Log.d("DEBUG","Language is null");
                    }

                    // --------------------------------------
                    // Word Stage Image
                    // --------------------------------------
                    int imgStageXPos = ((MyApp)mContext.getApplicationContext()).btnStageXPos;
                    int imgStageYPos = ((MyApp)mContext.getApplicationContext()).btnStageYPos;

                    if(imgStage != null){
                        canvas.drawBitmap(imgStage, imgStageXPos, height - imgStageYPos, null);
                    } else {
                        Log.d("DEBUG","Grade is null");
                    }

                    // --------------------------------------
                    // Yellow Submarine Image
                    // --------------------------------------
                    int imgMainXPos = ((MyApp)mContext.getApplicationContext()).btnMainXPos;
                    int imgMainYPos = ((MyApp)mContext.getApplicationContext()).btnMainYPos;

                    if(imgNext != null){
                        canvas.drawBitmap(imgNext, width/2-imgMainXPos, height - imgMainYPos, null);
                    } else {
                        Log.d("DEBUG","Yellow Submarine is null");
                    }

                    // --------------------------------------
                    // Word Stage Image
                    // --------------------------------------
                    int imgSoundXPos = ((MyApp)mContext.getApplicationContext()).btnSoundXPos;
                    int imgSoundYPos = ((MyApp)mContext.getApplicationContext()).btnSoundYPos;

                    if(imgSound != null){
                        canvas.drawBitmap(imgSound, width - imgSoundXPos, height - imgSoundYPos, null);
                    } else {
                        Log.d("DEBUG","Sound is null");
                    }

                    // --------------------------------------
                    // Help Image
                    // --------------------------------------
                    int imgHelpXPos = ((MyApp)mContext.getApplicationContext()).btnHelpXPos;
                    int imgHelpYPos = ((MyApp)mContext.getApplicationContext()).btnHelpYPos;

                    if(imgHelp != null){
                        canvas.drawBitmap(imgHelp, width - imgHelpXPos, height - imgHelpYPos, null);
                    } else {
                        Log.d("DEBUG","Help is null");
                    }

                    // --------------------------------------
                    // Question Image
                    // --------------------------------------
                    int imgQuestionXPos = ((MyApp)mContext.getApplicationContext()).btnQuestionXPos;
                    int imgQuestionYPos = ((MyApp)mContext.getApplicationContext()).btnQuestionYPos;

                    if(imgQuestion != null){
                        canvas.drawBitmap(imgQuestion, imgQuestionXPos, height - imgQuestionYPos, null);
                    } else {
                        Log.d("DEBUG","Question is null");
                    }

                    // --------------------------------------
                    // Word Bubble
                    // --------------------------------------
                    //for (MyBubble tmp : mBubble) {
                    //    Log.d("DEBUG",tmp.strWord+" / "+ tmp.x +"-"+ tmp.rad +" / "+tmp.y+ "-"+ tmp.rad);
                    //    canvas.drawBitmap(tmp.imgBbl, tmp.x - tmp.rad,  tmp.y - tmp.rad, null);
                    //}
                    synchronized (mBubble) {
                        Iterator<MyBubble> it = mBubble.iterator();
                        while (it.hasNext()) {
                            MyBubble tmp = (MyBubble) it.next();
                            //Log.d("DEBUG",tmp.strWord+" / "+ tmp.x +"-"+ tmp.rad +" / "+tmp.y+ "-"+ tmp.rad);
                            canvas.drawBitmap(tmp.imgBbl, tmp.x - tmp.rad, tmp.y - tmp.rad, null);
                        }
                    }

                    // --------------------------------------
                    // 작은비눗방울
                    // --------------------------------------
                    for (SmallBall tmp : sBall) {
                        canvas.drawBitmap(tmp.imgBall, tmp.x - tmp.rad,  tmp.y - tmp.rad, null);
                    }

                    // --------------------------------------
                    // 단어의미 ( Text )
                    // --------------------------------------
                    for ( WordMeaning  tmp : arrMeaning) {
                        //Log.d("DEBUG",tmp.strMeaning+" / "+ tmp.x +" / "+tmp.y);
                        int xPos, yPos;

                        if (tmp.x - 20 < 20 ) {
                            xPos = 20;
                        } else {
                            xPos = tmp.x - 20;
                        }

                        if (tmp.y - 20 < 20 ) {
                            yPos = 20;
                        } else {
                            yPos = tmp.y - 20;
                        }

                        canvas.drawText(tmp.strMeaning, xPos, yPos, tmp.paint);
                    }

                    // --------------------------------------
                    // 총점 Display
                    // --------------------------------------
                    totScore.MakeScore(SuccessTot);
                    canvas.drawBitmap(totScore.imgScore, 150,10, null);

                    failScore.MakeScore(FailTot);
                    canvas.drawBitmap(failScore.imgScore, width - 150,10, null);

                }
            } finally {							// 버퍼 작업이 끝나면
                if (canvas != null)				// 버퍼의 내용을 View에 전송
                {
                    mHolder.unlockCanvasAndPost(canvas);
                }
            }

            //Thread 일시정지
            synchronized (this)
            {
                if (isWait){
                    try {
                        wait();
                    } catch (Exception e) {
                        //nothing
                    }
                }
            } //sync
        } // while
    } // run
}
