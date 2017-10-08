package com.cho.cks.bubblewords;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.Toast;
import android.content.SharedPreferences;

public class MainActivity extends Activity {

    MyView myView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.cho.cks.bubblewords.R.layout.activity_main);

        // -----------------------------
        // 언어 / 단어장 초기화
        // -----------------------------
        ((MyApp)this.getApplicationContext()).setWordLang("ENGLISH");
        ((MyApp)this.getApplicationContext()).setDifficult(1);
        ((MyApp)this.getApplicationContext()).setWordStage();
        ((MyApp)this.getApplicationContext()).setIsChangeLang(true);

        myView = (MyView) findViewById(com.cho.cks.bubblewords.R.id.myView);
    }

    public void onInit(int status) {

    }

    @Override
    protected void onPause() {
        super.onPause();
       myView.PauseProcess();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myView.ResumeProcess();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myView.StopProcess();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // -----------------------------------
        // Option Menu 추가하기
        // -----------------------------------
        menu.add(0,1,0,"Login");
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // -----------------------------------
        // Option Menu 선택시 처리 로직
        // -----------------------------------
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);

        switch(item.getItemId()) {
            case 1:

                // ----------------------------------------
                // 로그인은 처음에 한번 접속시 일련번호로 자동생성되도록 하고
                // 수시로 로그인이 필요없도록 앱정보에 로그인정보를 저장하도록 함
                // 로그인정보를 이용한 로직은 아직 개발되지 않음.
                // ----------------------------------------
                int user_id = pref.getInt("user_id", 0);

                if ( user_id > 0  ) {
                    Toast.makeText(MainActivity.this, "Success - Get User(Preferences)", Toast.LENGTH_LONG).show();
                } else {
                    String username = "autouser";
                    String password = "12345678";
                    new PostAsyncLogin(MainActivity.this).execute(username, password);
                }
                break;
        }

        return true;
    }

} // Activity