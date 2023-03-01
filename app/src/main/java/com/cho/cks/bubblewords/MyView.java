package com.cho.cks.bubblewords;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.*;
import android.view.*;
import android.view.SurfaceHolder.Callback;
import android.content.Intent;

/**
 * Created by CKS on 2016-06-12.
 */
public class MyView extends SurfaceView implements Callback {

    static MyThread mThread;
    static SurfaceHolder mHolder;
    static Context mContext;

    static int view_width, view_height;					// View

    //-------------------------------------
    //  생성자
    //-------------------------------------
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        mHolder = holder;  // holder와 context 보존
        mContext = context;
        mThread = new MyThread(holder, context);  // Thread 생성

        Display display = ((WindowManager) mContext.getSystemService (Context.WINDOW_SERVICE)).getDefaultDisplay();
        view_width = display.getWidth();
        view_height = display.getHeight();

        setFocusable(true);
    }

    //-------------------------------------
    //  SurfaceView가 생성될 때 실행되는 부분
    //-------------------------------------
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mThread.start();
        } catch (Exception e) {
            RestartProcess();
        }
    }

    //-------------------------------------
    //  SurfaceView가 바뀔 때 실행되는 부분
    //-------------------------------------
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int format, int width, int height) {

    }

    //-------------------------------------
    //  SurfaceView가 해제될 때 실행되는 부분
    //-------------------------------------
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        StopProcess();
    }

    // Stop
    public void StopProcess()
    {
        mThread.StopThread();
    }

    // Pause
    public void PauseProcess()
    {
        mThread.PauseThread(true);
    }

    // Resume
    public void ResumeProcess()
    {
        mThread.PauseThread(false);
    }

    // Restart
    public void RestartProcess()
    {
        mThread.StopThread();

        mThread = null;
        mThread = new MyThread(mHolder,mContext);
        mThread.start();
    }
    //------------------------------------
    //      Timer Handler
    //------------------------------------
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            invalidate();		// View를 다시 그림
            mHandler.sendEmptyMessageDelayed(0, 10);
        }
    }; // Handler

    //------------------------------------
    //      onTouch Event
    //------------------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        SharedPreferences pref =  mContext.getSharedPreferences("pref",  mContext.MODE_PRIVATE);

        if (event.getAction() == MotionEvent.ACTION_DOWN){

            int x = (int) event.getX();
            int y = (int) event.getY();

            // *****************************************************************************
            // Click Main Area
            // *****************************************************************************
            if ( x >= 0
                    && y >= 0
                    && x <= view_width
                    && y <= (view_height - 300)
                    )
            {
                mThread.DestroyBubble(x, y);	// 비누방울 제거

                boolean refreshFlag = ((MyApp)mContext.getApplicationContext()).getRefreshFlag();

                if ( refreshFlag ) {
                    generateWordQuestion(pref);
                }

                ((MyApp)mContext.getApplicationContext()).setRefreshFlag(false);
            }

            // *****************************************************************************
            // Click Language
            // *****************************************************************************
            int imgLangWidth = ((MyApp)mContext.getApplicationContext()).btnLangWidth;
            int imgLangHeight = ((MyApp)mContext.getApplicationContext()).btnLangHeight;
            int imgLangXPos = ((MyApp)mContext.getApplicationContext()).btnLangXPos;
            int imgLangYPos = ((MyApp)mContext.getApplicationContext()).btnLangYPos;

            if ( x >= imgLangXPos
                    && y >= (view_height - imgLangYPos)
                    && x <= imgLangXPos + imgLangWidth
                    && y <= (view_height - imgLangYPos) +imgLangHeight
                    )
            {
                String wordLang = ((MyApp)mContext.getApplicationContext()).getWordLang();

                if (wordLang == "ENGLISH"){
                    ((MyApp)mContext.getApplicationContext()).setWordLang("CHINESS");
                } else if (wordLang == "CHINESS") {
                    ((MyApp)mContext.getApplicationContext()).setWordLang("JAPANESS");
                } else if (wordLang == "JAPANESS") {
                    ((MyApp)mContext.getApplicationContext()).setWordLang("ENGLISH");
                }

                // 난이도 및 스테이지
                ((MyApp)mContext.getApplicationContext()).setDifficult(1);
                ((MyApp)mContext.getApplicationContext()).setWordStage();

                // Language 변경여부
                ((MyApp)mContext.getApplicationContext()).setIsChangeLang(true);

                mThread.MakeLanguage();
                mThread.MakeStage();

            }

            // *****************************************************************************
            // Click Stage
            // *****************************************************************************
            int imgStageWidth = ((MyApp)mContext.getApplicationContext()).btnStageWidth;
            int imgStageHeight = ((MyApp)mContext.getApplicationContext()).btnStageHeight;
            int imgStageXPos = ((MyApp)mContext.getApplicationContext()).btnStageXPos;
            int imgStageYPos = ((MyApp)mContext.getApplicationContext()).btnStageYPos;

            if ( x >= imgStageXPos
                    && y >= (view_height - imgStageYPos)
                    && x <= imgStageXPos + imgStageWidth
                    && y <= (view_height - imgStageYPos) +imgStageHeight
                    )
            {

                int difficult = ((MyApp)mContext.getApplicationContext()).getDifficult();
                String wordLang = ((MyApp)mContext.getApplicationContext()).getWordLang();

                if (wordLang == "ENGLISH"){
                    if (difficult == 3) {
                        difficult = 1;
                    } else {
                        difficult++;
                    }
                } else if (wordLang == "CHINESS") {
                    if (difficult == 3) {
                        difficult = 1;
                    } else {
                        difficult++;
                    }
                } else if (wordLang == "JAPANESS") {
                    if (difficult == 3) {
                        difficult = 1;
                    } else {
                        difficult++;
                    }
                }

                // 난이도 및 스테이지
                ((MyApp)mContext.getApplicationContext()).setDifficult(difficult);
                ((MyApp)mContext.getApplicationContext()).setWordStage();

                // Language 변경여부
                ((MyApp)mContext.getApplicationContext()).setIsChangeLang(true);

                mThread.MakeStage();
            }
            // *****************************************************************************
            // 잠수함
            // *****************************************************************************
            int imgMainWidth = ((MyApp)mContext.getApplicationContext()).btnMainWidth;
            int imgMainHeight = ((MyApp)mContext.getApplicationContext()).btnMainHeight;
            int imgMainXPos = ((MyApp)mContext.getApplicationContext()).btnMainXPos;
            int imgMainYPos = ((MyApp)mContext.getApplicationContext()).btnMainYPos;

            if ( x >= (view_width/2-imgMainXPos)
                  && y >= (view_height - imgMainYPos)
                  && x <= (view_width/2-imgMainXPos) + imgMainWidth
                  && y <= (view_height - imgMainYPos) + imgMainHeight
            )
            {
                // Login 되지 않으면

                // 실행중에는 새로운 문제가 실행되지 않도록 함
                if (mThread.CheckBubbleRunning() == 0 ) {

                    generateWordQuestion(pref);

                } else {

                    mThread.ClearBubble();

                }

            }

            // *****************************************************************************
            // Sound
            // *****************************************************************************
            int imgSoundWidth = ((MyApp)mContext.getApplicationContext()).btnSoundWidth;
            int imgSoundHeight = ((MyApp)mContext.getApplicationContext()).btnSoundHeight;
            int imgSoundXPos = ((MyApp)mContext.getApplicationContext()).btnSoundXPos;
            int imgSoundYPos = ((MyApp)mContext.getApplicationContext()).btnSoundYPos;

            if ( x >= (view_width - imgSoundXPos)
                    && y >= (view_height - imgSoundYPos)
                    && x <= (view_width - imgSoundXPos) + imgSoundWidth
                    && y <= (view_height - imgSoundYPos) + imgSoundHeight
                    )
            {
                boolean isSound = ((MyApp)mContext.getApplicationContext()).getIsSound();

                if ( isSound ){
                    ((MyApp)mContext.getApplicationContext()).setIsSound(false);
                } else {
                    ((MyApp)mContext.getApplicationContext()).setIsSound(true);
                }

                mThread.MakeSound();
            }

            // *****************************************************************************
            // Help
            // *****************************************************************************
            int imgHelpWidth = ((MyApp)mContext.getApplicationContext()).btnHelpWidth;
            int imgHelpHeight = ((MyApp)mContext.getApplicationContext()).btnHelpHeight;
            int imgHelpXPos = ((MyApp)mContext.getApplicationContext()).btnHelpXPos;
            int imgHelpYPos = ((MyApp)mContext.getApplicationContext()).btnHelpYPos;

            if ( x >= (view_width - imgHelpXPos)
                    && y >= (view_height - imgHelpYPos)
                    && x <= (view_width - imgHelpXPos) + imgHelpWidth
                    && y <= (view_height - imgHelpYPos) + imgHelpHeight
                    )
            {
                Intent intent = new Intent(mContext, Help.class);
                mContext.startActivity(intent);

            }
        }
        return true;
    } // onTouchEvent

    public boolean makeWordBubble(String strWord, String strMeaning)
    {
        mThread.CheckBubble(strWord, strMeaning);		// 비누방울 조사

        return true;
    }

    public void generateWordQuestion(SharedPreferences pref) {
        // ---------------------------------
        // JSON에서 문제 가져오기
        // ---------------------------------
        // 실제로 보여지는 문제는 이미 가져다 놓은 문제임. Async
        String word_level = ((MyApp)mContext.getApplicationContext()).getWordStage();

        int questionCount = ((MyApp)mContext.getApplicationContext()).getQuestionNo();

        String strQuestionNo = String.valueOf( questionCount );

        new PostAsyncWord(mContext).execute(word_level, strQuestionNo);

        // ---------------------------------
        //문제 만들기
        // ---------------------------------
        String strWord, strMeaning;

        // Language 변경여부 Check
        boolean isChangeLang = ((MyApp)mContext.getApplicationContext()).getIsChangeLang();

        if (isChangeLang == false)
        {

            // 문제 만들기
            strWord = pref.getString("qword_letter", "");
            strMeaning = pref.getString("qword_meaning", "");

            makeWordQuestion(strWord, strMeaning);

            // Word Bubble 만들기
            int word_count = pref.getInt("word_count", 0);

            for (int i = 0; i < word_count; i++) {
                strWord = pref.getString("word_letter" + i, "");
                strMeaning = pref.getString("word_meaning" + i, "");

                makeWordBubble(strWord, strMeaning);

                SystemClock.sleep(1000);
            }

        } else {

            // Language 변경여부 Clear
            ((MyApp)mContext.getApplicationContext()).setIsChangeLang(false);
        }
    }

    public boolean makeWordQuestion(String strWord, String strMeaning)
    {
        mThread.MakeQuestion(strWord, strMeaning);		// 문제

        return true;
    }

    //----------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.exit(0);
        return true;
    }

}
