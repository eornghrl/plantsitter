package com.example.zzamtiger.textview;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;


import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Boardcontent extends AppCompatActivity implements View.OnClickListener {

    String user_id = null;    //아이디 저장
    String title = null;
    String content = null;
    String strDate=null;
    Date date;
    Button btbcplus = null;
    Button btbcpback = null;
    EditText ettitle = null;
    EditText etcontent = null;
    String Plus = "http://teambutton.dothome.co.kr/Boardplus.php";    //설정 불러오기 php


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardcontent);

        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
        user_id = data.getString("id", "");   //저장되어있는 id가져오기

        btbcplus = (Button) findViewById(R.id.btbcplus);       //글 등록
        btbcplus.setOnClickListener(this);
        btbcpback = (Button) findViewById(R.id.btbcback);      //글 취소
        btbcpback.setOnClickListener(this);

        ettitle = (EditText) findViewById(R.id.ettitle);     //글 제목
        etcontent = (EditText) findViewById(R.id.etcontent); //글 내용
        etcontent.setHorizontallyScrolling(false);
    }

    public void gointent(String msg) {
        if (msg.equals("success")) {
            //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            Intent goboard = new Intent(this, Board.class);
            startActivity(goboard);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View arg0) {
        Intent intent = getIntent();
        switch (arg0.getId()) {
            case R.id.btbcback:         //뒤로
                Intent gomain = new Intent(this, Board.class);
                startActivity(gomain);
                break;
            case R.id.btbcplus:           //등록
                title = ettitle.getText().toString();
                if (title != null && title.length() == 0) {
                    Toast toast = Toast.makeText(this, "글제목은 최소 사항입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                content = etcontent.getText().toString();
                long now = System.currentTimeMillis();  //현재 시간 구하기
                date = new Date(now);                  //현재 시간 date형으로 변환

                SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
                Date date = new Date();
                strDate = dateFormat.format(date);

                new Plus().execute();
                break;

        }
    }

    public class Plus extends AsyncTask<Void, Void, String> {     //사용자가 선택한 조건에 따른 식물 추가

        @Override
        protected String doInBackground(Void... params) {

            try {
                URL obj = new URL(Plus);

                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                conn.setRequestMethod("POST"); //요청 방식을 설정 (default : GET)

                conn.setDoInput(true); //input을 사용하도록 설정 (default : true)
                conn.setDoOutput(true); //output을 사용하도록 설정 (default : false)

                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(
                        "&ID=" + user_id + "&title=" + title + "&content=" + content + "&date=" + strDate
                ); //요청 파라미터를 입력
                writer.flush();
                writer.close();
                os.close();


                conn.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); //캐릭터셋 설정

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (sb.length() > 0) {
                        sb.append("\n");
                    }
                    sb.append(line);
                }
                br.close();
                conn.disconnect();

                return sb.toString().trim();

            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }


        @Override
        protected void onPostExecute(String result) {
            gointent(result);

        }

    }
}
