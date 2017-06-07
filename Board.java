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
import java.util.ArrayList;
import java.util.Date;

public class Board extends AppCompatActivity implements View.OnClickListener {

    ListView listView;
    MyListAdapter myListAdapter;
    ArrayList<list_item> list_itemArrayList;
    Button back = null;      //화분1 상세설정
    Button plus = null;      //화분2 상세설정
    String user_id=null;    //아이디 저장
    String Boardload = "http://teambutton.dothome.co.kr/Boardload.php";    //게시판 불러오기


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        back = (Button) findViewById(R.id.btboardback);
        plus = (Button) findViewById(R.id.btboardplus);

        back.setOnClickListener(this);
        plus.setOnClickListener(this);

        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
        user_id = data.getString("id", "");   //저장되어있는 id가져오기

        listView = (ListView) findViewById(R.id.my_listview);
        list_itemArrayList = new ArrayList<list_item>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Board.this ,list_itemArrayList.get(position).getNickname(),Toast.LENGTH_LONG).show();
            }
        });

        new Boardstate().execute();
    }

    public void onClick(View arg0){
        Intent intent=getIntent();
        switch(arg0.getId()){
            case R.id.btboardback:         //뒤로
                Intent gomain=new Intent(this,Mainmenu.class);
                startActivity(gomain);
                break;
            case R.id.btboardplus:           //글쓰기
                Intent gocontent=new Intent(this,Boardcontent.class);
                startActivity(gocontent);
                break;

        }
    }

    public class Boardstate extends AsyncTask<Void, Void, String> {     //게시판 갱신을 위해서 php 이용
        StringBuilder jsonHtml = new StringBuilder();

        @Override
        protected String doInBackground(Void... params) {

            try {
                URL obj = new URL(Boardload);

                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                if (conn != null) {      // 연결되었음 코드가 리턴되면.
                    conn.setRequestMethod("POST"); //요청 방식을 설정 (default : GET)
                    conn.setDoInput(true); //input을 사용하도록 설정 (default : true)
                    conn.setDoOutput(true); //output을 사용하도록 설정 (default : false)

                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);

                    OutputStream os = conn.getOutputStream();
                    os.close();
                    conn.connect();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for (; ; ) {
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if (line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
            return jsonHtml.toString();
        }

        @Override
        protected void onPostExecute(String str) {       //json 가지고 작업하기
            String ID;        //작성자 id
            String Title;        //글제목
            String Content;        //글 내용
            String Date;        //작성 시간
            Date date;
            String a;
            try {

                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    ID = jo.getString("ID");
                    Title = jo.getString("Title");
                    Content = jo.getString("Content");
                    Date = jo.getString("Date");
                    list_itemArrayList.add(new list_item("https://cdn-images-1.medium.com/fit/c/36/36/0*HgJ2Psmia7PjQsp9.jpg", ID, Title, Date, Content));
                }
                myListAdapter = new MyListAdapter(Board.this, list_itemArrayList);
                listView.setAdapter(myListAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
