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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.google.firebase.messaging.FirebaseMessaging;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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

public class Mainmenu extends AppCompatActivity implements OnClickListener {
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    public static final int SEND_INFORMATION = 0;
    public static final int SEND_STOP = 1;
    Thread thread;



    TextView tx = null;  //샘플

    ImageView ivmaintemp;    //온도에 따른 이미지 뷰
    ImageView ivmaindust;    //미세먼지에 따른 이미지 뷰
    ImageView ivmainwater;    //수위에 따른 이미지 뷰

    TextView txtemp = null;  //전체 온도
    TextView txwaterlevel = null;  //전체 수위
    TextView txname1 = null;  //화분1 이름
    TextView txname2 = null;  //화분2 이름
    TextView txname3 = null;  //화분3 이름
    TextView txhumd1 = null;  //화분1 토양습도
    TextView txhumd2 = null;  //화분2 토양습도
    TextView txhumd3 = null;  //화분3 토양습도
    TextView txsunshine1 = null;  //화분1 일조량
    TextView txsunshine2 = null;  //화분2 일조량
    TextView txsunshine3 = null;  //화분3 일조량
    ImageButton btplus = null;     //화분추가
    ImageButton btlogout = null;   //로그아웃
    ImageButton btboard = null;   //게시판
    Button btop1 = null;      //화분1 상세설정
    Button btop2 = null;      //화분2 상세설정
    Button btop3 = null;      //화분3 상세설정

    ArrayList<ListItem> listItem = new ArrayList<ListItem>();
    ListItem Item;
    String user_id = null;
    int plantnum;
    String first = "http://teambutton.dothome.co.kr/first.php";  //화분들 현재상태 불러오기
    String Tokensave = "http://teambutton.dothome.co.kr/Token.php";  //화분들 현재상태 불러오기
    String urlpath1 = "http://teambutton.dothome.co.kr/Mainstate.php";  //화분들 현재상태 불러오기
    String dust = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureLIst?itemCode=PM10&dataGubun=HOUR&pageNo=1&numOfRows=10&ServiceKey=%2FhO6MEaWMef4ZjlgRNqODaKbH%2Fl5LwCPFEQriP%2FQrLwv3Jr3dHj0on1xIEM%2FNa1%2BAS%2F8TALZiP4rFH8sFMkzCA%3D%3D&_returnType=json";  //샘플

    String id = null;
    String pid = null;
    String pid1 = null;   //화분 상세페이지에 보낼 변수들
    String pid2 = null;
    String pid3 = null;
    String Area;    //화분 주인이 등록한 지역
    String token;   //FCM을 위한 토큰

    public class ListItem {
        private String[] mData;

        public ListItem(String[] data) {
            mData = data;
        }

        public ListItem(String Name, String Humd, String Sunshine, String Temp, String Waterlevel, String PID) {
            mData = new String[6];
            mData[0] = Name;
            mData[1] = Humd;
            mData[2] = Sunshine;
            mData[3] = Temp;
            mData[4] = Waterlevel;
            mData[5] = PID;
        }

        public String[] getData() {
            return mData;
        }

        public String getData(int index) {
            return mData[index];
        }

        public void setData(String[] data) {
            mData = data;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btlogout = (ImageButton) findViewById(R.id.btlogout);
        btplus = (ImageButton) findViewById(R.id.btplus);
        btboard = (ImageButton) findViewById(R.id.btboard);
        btop1 = (Button) findViewById(R.id.btop1);
        btop2 = (Button) findViewById(R.id.btop2);
        btop3 = (Button) findViewById(R.id.btop3);

        tx = (TextView) findViewById(R.id.textView17);

        txtemp = (TextView) findViewById(R.id.txtemp);
        txwaterlevel = (TextView) findViewById(R.id.txwaterlevel);

        txname1 = (TextView) findViewById(R.id.txname1);
        txname2 = (TextView) findViewById(R.id.txname2);
        txname3 = (TextView) findViewById(R.id.txname3);
        txhumd1 = (TextView) findViewById(R.id.txhumd1);
        txhumd2 = (TextView) findViewById(R.id.txhumd2);
        txhumd3 = (TextView) findViewById(R.id.txhumd3);
        txsunshine1 = (TextView) findViewById(R.id.txsunshine1);
        txsunshine2 = (TextView) findViewById(R.id.txsunshine2);
        txsunshine3 = (TextView) findViewById(R.id.txsunshine3);

        ivmaintemp=(ImageView)findViewById(R.id.ivmaintemp);
        ivmaindust=(ImageView)findViewById(R.id.ivmaindust);
        ivmainwater=(ImageView)findViewById(R.id.ivmainwater);

        btlogout.setOnClickListener(this);
        btplus.setOnClickListener(this);
        btboard.setOnClickListener(this);
        btop1.setOnClickListener(this);
        btop2.setOnClickListener(this);
        btop3.setOnClickListener(this);



        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
        String id = data.getString("id", "");   //저장되어있는 id가져오기
        user_id = id; // php로 보낼 변수에 저장



        FirebaseInstanceId.getInstance().getToken();        //토큰 얻기
        token = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM_Token", token);
        FirebaseMessaging.getInstance().subscribeToTopic("notice");

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        new first().execute();       //거주지역 저장
        new Token().execute();       //토큰 등록
        new Mainstate().execute();       //메인메뉴 화분 상태 갱신
        new Duststate().execute();      //거주지역에 따른 미세먼지 측정
        thread=new Thread();
        thread.start();

    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_INFORMATION:
                    break;

                case SEND_STOP:
                    thread.stopThread();
                    break;

                default:
                    break;
            }
        }
    };

    class Thread extends java.lang.Thread {
        boolean stopped = false;
        int i = 0;

        public Thread() {
            stopped = false;
        }

        public void stopThread() {
            stopped = true;
        }

        @Override
        public void run() {
            super.run();
            while (stopped == false) {
                i++;
                try {
                    new Mainstate().execute();      //메인화면 갱신
                    new Duststate().execute();      //미세먼지 측정 갱신
                    sleep(1800000);       //30분마다 메인화면과 미세먼지 갱신
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class first extends AsyncTask<Void, Void, String> {     //맨처음 사용자의 거주지역정보를 받아오기 위함
        StringBuilder jsonHtml = new StringBuilder();

        @Override
        protected String doInBackground(Void... params) {

            try {
                URL obj = new URL(first);

                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                if (conn != null) {      // 연결되었음 코드가 리턴되면.
                    conn.setRequestMethod("POST"); //요청 방식을 설정 (default : GET)
                    conn.setDoInput(true); //input을 사용하도록 설정 (default : true)
                    conn.setDoOutput(true); //output을 사용하도록 설정 (default : false)

                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(
                            "&user_id=" + user_id
                    ); //요청 파라미터를 입력
                    writer.flush();
                    writer.close();
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

            try {

                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    Area = jo.getString("AREA");                    //사용자의 거주지역을 저장
                }
                //tx.setText(Area);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class Token extends AsyncTask<Void, Void, String> {     //토큰 등록
        StringBuilder jsonHtml = new StringBuilder();

        @Override
        protected String doInBackground(Void... params) {

            try {
                URL obj = new URL(Tokensave);

                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                if (conn != null) {      // 연결되었음 코드가 리턴되면.
                    conn.setRequestMethod("POST"); //요청 방식을 설정 (default : GET)
                    conn.setDoInput(true); //input을 사용하도록 설정 (default : true)
                    conn.setDoOutput(true); //output을 사용하도록 설정 (default : false)

                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(
                            "&user_id=" + user_id + "&token=" + token
                    ); //요청 파라미터를 입력
                    writer.flush();
                    writer.close();
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
        }
    }

    public class Mainstate extends AsyncTask<Void, Void, String> {     //메인메뉴 화분 상태 갱신을 위해서 php 이용
        StringBuilder jsonHtml = new StringBuilder();
        double dtemp;
        @Override
        protected String doInBackground(Void... params) {

            try {
                URL obj = new URL(urlpath1);

                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                if (conn != null) {      // 연결되었음 코드가 리턴되면.
                    conn.setRequestMethod("POST"); //요청 방식을 설정 (default : GET)
                    conn.setDoInput(true); //input을 사용하도록 설정 (default : true)
                    conn.setDoOutput(true); //output을 사용하도록 설정 (default : false)

                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(
                            "&user_id=" + user_id
                    ); //요청 파라미터를 입력
                    writer.flush();
                    writer.close();
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
            String Temp;        //화분 온도
            String Waterlevel;        //화분 수위
            String Name;        //화분 이름
            String Humd;        //화분 습도
            String Sunshine;    //화분 일조량
            String PID;    //화분 고유 번호


            String a;
            try {

                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    Name = jo.getString("Name");
                    Humd = jo.getString("Humd");
                    Sunshine = jo.getString("Sunshine");
                    Temp = jo.getString("Temp");
                    Waterlevel = jo.getString("Waterlevel");
                    PID = jo.getString("PID");
                    listItem.add(new ListItem(Name, Humd, Sunshine, Temp, Waterlevel, PID));
                }
                plantnum = ja.length();       //현재 화분 갯수 알아내기
                SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);    //id값 저장
                SharedPreferences.Editor editor = data.edit();
                editor.putInt("plantnum", plantnum);        //화분 갯수 저장
                editor.commit();        //저장

                a = String.valueOf(plantnum);   //화분갯수 확인용
                //tx.setText(a);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (plantnum == 0)     //화분이 하나도 없을 경우
            {
                txname1.setText("화분을");
                txhumd1.setText("추가해");
                txsunshine1.setText("주세요");
                txname2.setText("화분을");
                txhumd2.setText("추가해");
                txsunshine2.setText("주세요");
                txname3.setText("화분을");
                txhumd3.setText("추가해");
                txsunshine3.setText("주세요");

                btop1.setVisibility(View.INVISIBLE);
                btop2.setVisibility(View.INVISIBLE);
                btop3.setVisibility(View.INVISIBLE);
            } else if (plantnum == 1)         //화분 1개일떄의 설정
            {

                Name = listItem.get(0).getData(0);
                txname1.setText(Name);
                Humd = listItem.get(0).getData(1);
                txhumd1.setText(Humd);
                Sunshine = listItem.get(0).getData(2);
                txsunshine1.setText(Sunshine);

                Temp = listItem.get(0).getData(3);
                dtemp=Double.valueOf(Temp).doubleValue();
                if(dtemp>25)
                {
                    ivmaintemp.setImageResource(R.drawable.hightemp);
                }
                else if(dtemp<=25 && dtemp>15)
                {
                    ivmaintemp.setImageResource(R.drawable.midtemp);
                }
                else if(dtemp<=15)
                {
                    ivmaintemp.setImageResource(R.drawable.lowtemp);
                }
                txtemp.setText("온도:" + Temp + "℃");

                Waterlevel = listItem.get(0).getData(4);
                if(Waterlevel.equals("sang"))
                {
                    ivmainwater.setImageResource(R.drawable.highwater);
                    txwaterlevel.setText("수위:상");
                }
                else if(Waterlevel.equals("jung"))
                {
                    ivmainwater.setImageResource(R.drawable.midwater);
                    txwaterlevel.setText("수위:중");
                }
                else if(Waterlevel.equals("ha"))
                {
                    ivmainwater.setImageResource(R.drawable.lowwater);
                    txwaterlevel.setText("수위:하");
                }
                pid1 = listItem.get(0).getData(5);

                txname2.setText("화분을");
                txhumd2.setText("추가해");
                txsunshine2.setText("주세요");
                txname3.setText("화분을");
                txhumd3.setText("추가해");
                txsunshine3.setText("주세요");


                btop2.setVisibility(View.INVISIBLE);
                btop3.setVisibility(View.INVISIBLE);

            } else if (plantnum == 2)   //화분 2개일떄의 설정
            {

                Name = listItem.get(0).getData(0);
                txname1.setText(Name);
                Humd = listItem.get(0).getData(1);
                txhumd1.setText(Humd);
                Sunshine = listItem.get(0).getData(2);
                txsunshine1.setText(Sunshine);


                Temp = listItem.get(0).getData(3);
                dtemp=Double.valueOf(Temp).doubleValue();
                if(dtemp>25)
                {
                    ivmaintemp.setImageResource(R.drawable.hightemp);
                }
                else if(dtemp<=25 && dtemp>15)
                {
                    ivmaintemp.setImageResource(R.drawable.midtemp);
                }
                else if(dtemp<=15)
                {
                    ivmaintemp.setImageResource(R.drawable.lowtemp);
                }
                txtemp.setText("온도:" + Temp + "℃");


                Waterlevel = listItem.get(0).getData(4);
                if(Waterlevel.equals("sang"))
                {
                    ivmainwater.setImageResource(R.drawable.highwater);
                    txwaterlevel.setText("수위:상");
                }
                else if(Waterlevel.equals("jung"))
                {
                    ivmainwater.setImageResource(R.drawable.midwater);
                    txwaterlevel.setText("수위:중");
                }
                else if(Waterlevel.equals("ha"))
                {
                    ivmainwater.setImageResource(R.drawable.lowwater);
                    txwaterlevel.setText("수위:하");
                }

                Name = listItem.get(1).getData(0);
                txname2.setText(Name);
                Humd = listItem.get(1).getData(1);
                txhumd2.setText(Humd);
                Sunshine = listItem.get(1).getData(2);
                txsunshine2.setText(Sunshine);
                pid1 = listItem.get(0).getData(5);
                pid2 = listItem.get(1).getData(5);

                txname3.setText("화분을");
                txhumd3.setText("추가해");
                txsunshine3.setText("주세요");

                btop3.setVisibility(View.INVISIBLE);
            } else if (plantnum == 3)       //화분 3개일떄의 설정
            {

                Name = listItem.get(0).getData(0);
                txname1.setText(Name);
                Humd = listItem.get(0).getData(1);
                txhumd1.setText(Humd);
                Sunshine = listItem.get(0).getData(2);
                txsunshine1.setText(Sunshine);

                Temp = listItem.get(0).getData(3);
                dtemp=Double.valueOf(Temp).doubleValue();
                if(dtemp>25)
                {
                    ivmaintemp.setImageResource(R.drawable.hightemp);
                }
                else if(dtemp<=25 && dtemp>15)
                {
                    ivmaintemp.setImageResource(R.drawable.midtemp);
                }
                else if(dtemp<=15)
                {
                    ivmaintemp.setImageResource(R.drawable.lowtemp);
                }
                txtemp.setText("온도:" + Temp + "℃");

                Waterlevel = listItem.get(0).getData(4);
                if(Waterlevel.equals("sang"))
                {
                    ivmainwater.setImageResource(R.drawable.highwater);
                    txwaterlevel.setText("수위:상");
                }
                else if(Waterlevel.equals("jung"))
                {
                    ivmainwater.setImageResource(R.drawable.midwater);
                    txwaterlevel.setText("수위:중");
                }
                else if(Waterlevel.equals("ha"))
                {
                    ivmainwater.setImageResource(R.drawable.lowwater);
                    txwaterlevel.setText("수위:하");
                }


                Name = listItem.get(1).getData(0);
                txname2.setText(Name);
                Humd = listItem.get(1).getData(1);
                txhumd2.setText(Humd);
                Sunshine = listItem.get(1).getData(2);
                txsunshine2.setText(Sunshine);

                Name = listItem.get(2).getData(0);
                txname3.setText(Name);
                Humd = listItem.get(2).getData(1);
                txhumd3.setText(Humd);
                Sunshine = listItem.get(2).getData(2);
                txsunshine3.setText(Sunshine);

                pid1 = listItem.get(0).getData(5);
                pid2 = listItem.get(1).getData(5);
                pid3 = listItem.get(2).getData(5);
            }

        }
    }
    public class Duststate extends AsyncTask<Void, Void, String> {     //거주지역에 따른 미세먼지 측정
        StringBuilder jsonHtml = new StringBuilder();

        @Override
        protected String doInBackground(Void... params) {

            try {
                URL obj = new URL(dust);

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
            String dust=null;
            int intdust;
            try {

                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("list");

                root = ja.getJSONObject(0); // 가장 최신 결과만을 얻음
                dust = root.getString(Area);    //사용자의 거주지역에 따른 정보 흭득
                intdust= Integer.parseInt(dust);      //string to int
                if(intdust>=0 && intdust<31)                //값에 따른 미세먼지 상태 출력
                {
                    tx.setText("미세먼지:좋음("+Area+")");
                    ivmaindust.setImageResource(R.drawable.sunny);
                }
                else if(intdust>=31 && intdust<81)
                {
                    tx.setText("미세먼지:보통("+Area+")");
                    ivmaindust.setImageResource(R.drawable.sunny);
                }
                else if(intdust>=81 && intdust<150)
                {
                    tx.setText("미세먼지:나쁨("+Area+")");
                    ivmaindust.setImageResource(R.drawable.dust);
                }
                else
                {
                    tx.setText("미세먼지:매우나쁨("+Area+")");
                    ivmaindust.setImageResource(R.drawable.dust);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onClick(View arg0){
        Intent intent=getIntent();
        switch(arg0.getId()){
            case R.id.btlogout:         //로그아웃
                handler.sendEmptyMessage(SEND_STOP);
                Intent itlogout=new Intent(this,Loginmenu.class);
                startActivity(itlogout);
                break;
            case R.id.btplus:           //화분추가
                handler.sendEmptyMessage(SEND_STOP);
                SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
                int ppid = data.getInt("plantnum",0);   //저장되어있는 화분 갯수가져오기
                if(ppid==3)
                {
                    Toast toast = Toast.makeText(this, "더이상 화분을 추가하실수 없습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                Intent itplus=new Intent(this,plusActivity.class);
                startActivity(itplus);
                break;
            case R.id.btboard:           //화분추가
                handler.sendEmptyMessage(SEND_STOP);
                Intent board=new Intent(this,Board.class);
                startActivity(board);
                break;
            case R.id.btop1:            //화분1 상세 페이지
                handler.sendEmptyMessage(SEND_STOP);
                Intent itop1=new Intent(this,Detail.class);
                data = getSharedPreferences("data", MODE_PRIVATE);
                id = data.getString("id", "");   //저장되어있는 id가져오기
                user_id=id; // php로 보낼 변수에 저장
                itop1.putExtra("pid",pid1);  //화분1의 설정 정보들을 읽기위한 구별값 전달
                startActivity(itop1);
                break;
            case R.id.btop2:            //화분2 상세 페이지
                handler.sendEmptyMessage(SEND_STOP);
                Intent itop2=new Intent(this,Detail.class);
                data = getSharedPreferences("data", MODE_PRIVATE);
                id = data.getString("id", "");   //저장되어있는 id가져오기
                user_id=id; // php로 보낼 변수에 저장
                itop2.putExtra("pid",pid2);  //화분1의 설정 정보들을 읽기위한 구별값 전달
                startActivity(itop2);
                break;
            case R.id.btop3:            //화분3 상세 페이지
                handler.sendEmptyMessage(SEND_STOP);
                Intent itop3=new Intent(this,Detail.class);
                data = getSharedPreferences("data", MODE_PRIVATE);
                id = data.getString("id", "");   //저장되어있는 id가져오기
                user_id=id; // php로 보낼 변수에 저장
                itop3.putExtra("pid",pid3);  //화분1의 설정 정보들을 읽기위한 구별값 전달
                startActivity(itop3);
                break;
        }
    }

    @Override
    public void onBackPressed() {           //백버튼 2번으로 종료시키기
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            super.onBackPressed();
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

}
