package com.cho.cks.bubblewords;

import android.app.*;

/**
 * Created by CKS on 2016-06-23.
 */
public class MyApp extends Application {

    // ------------------------------------
    // 기본문제수
    // 문제수 = 기본문제수 + 난이도
    // ------------------------------------
    public static int questionNo = 3;

    // ------------------------------------
    // 언어별 기본 단어장
    // ------------------------------------
    public static String stageEnglish1 = "A0002";
    public static String stageEnglish2 = "B0003";
    public static String stageEnglish3 = "C0003";

    public static String stageChiness1 = "E0002";

    public static String stageJapaness1 =  "J0001";

    // ------------------------------------
    // 화면 Layout
    // 해상도에 따라 어떻게 조절하나???
    // ------------------------------------
    public static int btnLangWidth = 80;
    public static int btnLangHeight = 80;
    public static int btnLangXPos = 30;   // left  기준
    public static int btnLangYPos = 200;

    public static int btnStageWidth = 90;
    public static int btnStageHeight = 90;
    public static int btnStageXPos = 140; // left  기준
    public static int btnStageYPos = 200;

    public static int btnMainWidth = 200;
    public static int btnMainHeight = 200;
    public static int btnMainXPos = 100;  // Ceneter 기준
    public static int btnMainYPos = 300;

    public static int btnSoundWidth = 80;
    public static int btnSoundHeight = 80;
    public static int btnSoundXPos = 230;
    public static int btnSoundYPos = 200;

    public static int btnHelpWidth = 80;
    public static int btnHelpHeight = 80;
    public static int btnHelpXPos = 130;
    public static int btnHelpYPos = 200;

    public static int btnQuestionWidth = 100;
    public static int btnQuestionHeight = 70;
    public static int btnQuestionXPos = 50;
    public static int btnQuestionYPos = 390;


    // ------------------------------------
    // Global Setting
    // ------------------------------------
    private int difficult;
    private String word_lang;
    private String word_stage;
    private boolean isSound;
    private boolean isChangeLang;


    // --------------------
    // 단어 풍선 수
    // --------------------
    public int getQuestionNo() {
        return questionNo + difficult;
    }

    // --------------------
    // Difficult (난이도)
    // --------------------
    public int getDifficult() {
        return difficult;
    }
    public void setDifficult(int _diff){
        difficult = _diff;
    }

    public String getDifficultDescr() {

        String str = "";

        if (difficult == 1) {
            str = "초급";
        } else if (difficult == 2) {
            str = "중급";
        } else {
            str = "고급";
        }

        return str;
    }

    // --------------------
    // Language (언어)
    // --------------------
    public String getWordLang() {
        return word_lang;
    }
    public void setWordLang(String _lang){
        word_lang = _lang;
    }

    // --------------------
    // 단어장
    // --------------------
    public String getWordStage() {
        return word_stage;
    }
    public void setWordStage(String _stage){
        word_stage = _stage;
    }

    // 난이도에 따른 단어장 선택
    public void setWordStage(){

        String stage = "";

        if (word_lang == "ENGLISH"){

            if (difficult == 1) {
                stage = stageEnglish1;
            } else if (difficult == 2) {
                stage = stageEnglish2;
            } else {
                stage = stageEnglish3;
            }

        } else if (word_lang == "CHINESS") {
            stage = stageChiness1;
        } else if (word_lang == "JAPANESS") {
            stage = stageJapaness1;
        }

        word_stage = stage;
    }

    // --------------------
    // Sound
    // --------------------
    public boolean getIsSound(){
        return isSound;
    }
    public void setIsSound(boolean _sound) {
        isSound = _sound;
    }

    // --------------------
    // Change Language
    // --------------------
    public boolean getIsChangeLang(){
        return isChangeLang;
    }
    public void setIsChangeLang(boolean _changeLang) {
        isChangeLang = _changeLang;
    }

}
