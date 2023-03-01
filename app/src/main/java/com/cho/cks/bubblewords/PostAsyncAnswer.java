package com.cho.cks.bubblewords;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.HashMap;
import java.util.Random;

public class PostAsyncAnswer extends AsyncTask<String, String, JSONArray>  {

    JSONParser jsonParser = new JSONParser();

    private ProgressDialog pDialog;
    private Context mContext;

    private static final String LOGIN_URL = "http://cksoonew.cafe24.com/cks_star_words/ajax/ajaxUserTestAuto.php";

    public PostAsyncAnswer(Context context){
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Attempting to get word...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected JSONArray doInBackground(String... args) {

        try {

            HashMap<String, String> params = new HashMap<>();
            params.put("wordlevel", args[0]);
            params.put("wordcnt", args[1]);

            Log.d("request", "starting");

            JSONArray json = jsonParser.makeHttpRequestArr(LOGIN_URL, "POST", params);

            if (json != null) {
                Log.d("JSON result", json.toString());
                return json;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(JSONArray json) {

        int word_id = 0;
        String word_letter = "";
        String word_meaning = "";

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }

        if (json != null) {
            Log.d("JSON parameter", json.toString());
            //Toast.makeText(mContext, "Success - Get Word", Toast.LENGTH_LONG).show();

            try {

                SharedPreferences pref = mContext.getSharedPreferences("pref", mContext.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                editor.putInt("word_count", json.length());

                Random rnd = new Random();

                // Word Bubble 수를 계산하는 로직
                // Main Button을 눌렀을때 생성되는 수와 동일해야 함.
                int questionCount = ((MyApp)mContext.getApplicationContext()).getQuestionNo();

                int qestionNo = rnd.nextInt( questionCount );

                for (int i=0; i<json.length(); i++) {

                    JSONObject obj = json.getJSONObject(i);

                    Log.d("LOG", "JSONArray index : " + obj);

                    word_id = obj.getInt("wordid");
                    word_letter = obj.getString("wordletter");
                    word_meaning = obj.getString("wordmeaning");

                    editor.putInt("word_id"+i, word_id);
                    editor.putString("word_letter"+i, word_letter);
                    editor.putString("word_meaning"+i, word_meaning);

                    if ( i == qestionNo )
                    {
                        editor.putInt("qword_id", word_id);
                        editor.putString("qword_letter", word_letter);
                        editor.putString("qword_meaning", word_meaning);
                    }

                }

                editor.commit();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d("Success!", word_letter);
    }
}