package com.cho.cks.bubblewords;

import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

/**
 * Created by CKS on 2016-06-12.
 */
public class Speaker implements OnInitListener {

    private TextToSpeech tts;
    private boolean ready = false;
    private boolean allowed = false;
    private String wordLang;

    public Speaker(Context context){

        wordLang = ((MyApp)context.getApplicationContext()).getWordLang();
        tts= new TextToSpeech(context, this);
    }

    public boolean isAllowed(){
        return allowed;
    }

    public void allow(boolean allowed){
        this.allowed = allowed;
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            // Change this to match your locale
            if (wordLang == "ENGLISH"){
                tts.setLanguage(Locale.US);
            } else if  (wordLang == "CHINESS") {
                tts.setLanguage(Locale.SIMPLIFIED_CHINESE);
            } else if  (wordLang == "JAPANESS") {
                tts.setLanguage(Locale.JAPAN);
            }
            ready = true;
        }else{
            ready = false;
        }
    }

    public void speak(String text){

        // Speak only if the TTS is ready
        // and the user has allowed speech

        if(ready && allowed) {
            HashMap<String, String> hash = new HashMap<String,String>();
            hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                    String.valueOf(AudioManager.STREAM_NOTIFICATION));
            tts.speak(text, TextToSpeech.QUEUE_ADD, hash);
        }
    }

    public void speak(String lang, String text){

        // Speak only if the TTS is ready
        // and the user has allowed speech
        if (lang == "ENGLISH"){
            tts.setLanguage(Locale.US);
        } else if  (lang == "CHINESS") {
            tts.setLanguage(Locale.SIMPLIFIED_CHINESE);
        } else if  (lang == "JAPANESS") {
            tts.setLanguage(Locale.JAPAN);
        }

        if(ready && allowed) {
            HashMap<String, String> hash = new HashMap<String,String>();
            hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                    String.valueOf(AudioManager.STREAM_MUSIC));
            tts.speak(text, TextToSpeech.QUEUE_ADD, hash);
        }
    }

    public void pause(int duration){
        tts.playSilence(duration, TextToSpeech.QUEUE_ADD, null);
    }

    // Free up resources
    public void destroy(){
        tts.shutdown();
    }
}
