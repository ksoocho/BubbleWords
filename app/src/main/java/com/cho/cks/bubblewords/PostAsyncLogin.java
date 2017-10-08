package com.cho.cks.bubblewords;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by CKS on 2016-06-14.
 */
public class PostAsyncLogin extends AsyncTask<String, String, JSONObject> {
    JSONParser jsonParser = new JSONParser();

    private ProgressDialog pDialog;
    private Context mContext;

    private static final String LOGIN_URL = "http://cksoonew.cafe24.com/cks_star_words/ajax/ajaxWordAutoLogin.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public PostAsyncLogin(Context context){
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Attempting login...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String... args) {

        try {

            HashMap<String, String> params = new HashMap<>();
            params.put("usercode", args[0]);
            params.put("password", args[1]);

            Log.d("request", "starting");

            JSONObject json = jsonParser.makeHttpRequestObj(LOGIN_URL, "POST", params);

            if (json != null) {
                Log.d("JSON result", json.toString());
                return json;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(JSONObject json) {

        int user_id = 0;
        String user_mode = "";
        String return_code = "";

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }

        if (json != null) {
            Toast.makeText(mContext, "Success - Get User(Database)", Toast.LENGTH_LONG).show();

            try {

                SharedPreferences pref = mContext.getSharedPreferences("pref", mContext.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                user_id = json.getInt("user_id");
                user_mode  = json.getString("user_mode");
                return_code = json.getString("return_code");

                editor.putInt("user_id", user_id);
                editor.commit();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (return_code == "S") {
            Log.d("Success!", user_mode);
        }else{
            Log.d("Failure", user_mode);
        }
    }
}
