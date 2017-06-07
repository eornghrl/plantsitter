package com.example.zzamtiger.textview;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class Joinmenu extends AppCompatActivity implements OnClickListener {
    Button btjoinok = null;
    Button btjoincancel = null;

    Spinner Spinner;
    private EditText editTextName;
    private EditText editTextPass;
    String user_area = "서울";

    String urlpath = "http://teambutton.dothome.co.kr/join.php";

    String user_id;
    String user_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Spinner = (Spinner) findViewById(R.id.joinarea);
        ArrayAdapter region_adapter = ArrayAdapter.createFromResource(this, R.array.region, android.R.layout.simple_spinner_item);
        Spinner.setAdapter(region_adapter);
        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_area = parent.getItemAtPosition(position).toString();
                if(user_area.equals("서울"))
                {
                    user_area="seoul";
                }
                else if(user_area.equals("부산"))
                {
                    user_area="busan";
                }
                else if(user_area.equals("대구"))
                {
                    user_area="daegu";
                }
                else if(user_area.equals("인천"))
                {
                    user_area="incheon";
                }
                else if(user_area.equals("광주"))
                {
                    user_area="gwangju";
                }
                else if(user_area.equals("대전"))
                {
                    user_area="daejeon";
                }
                else if(user_area.equals("울산"))
                {
                    user_area="ulsan";
                }
                else if(user_area.equals("경기"))
                {
                    user_area="gyeonggi";
                }
                else  if(user_area.equals("강원"))
                {
                    user_area="gangwon";
                }
                else if(user_area.equals("충북"))
                {
                    user_area="chungbuk";
                }
                else if(user_area.equals("충남"))
                {
                    user_area="chungnam";
                }
                else if(user_area.equals("전북"))
                {
                    user_area="jeonbuk";
                }
                else if(user_area.equals("전남"))
                {
                    user_area="jeonnam";
                }
                else if(user_area.equals("경북"))
                {
                    user_area="gyeongbuk";
                }
                else if(user_area.equals("경남"))
                {
                    user_area="gyeongnam";
                }
                else if(user_area.equals("제주"))
                {
                    user_area="jeju";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btjoincancel = (Button) findViewById(R.id.btjoincancel);
        btjoinok = (Button) findViewById(R.id.btjoinok);
        editTextName = (EditText) findViewById(R.id.joinname);
        editTextPass = (EditText) findViewById(R.id.joinpass);

        btjoincancel.setOnClickListener(this);
        btjoinok.setOnClickListener(this);

    }
    public void gointent(String msg){
        if (msg.equals("success")) {
            Intent itjoinok = new Intent(this, Loginmenu.class);
            startActivity(itjoinok);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btjoinok:
                user_id = editTextName.getText().toString();
                user_pass = editTextPass.getText().toString();
                new Join().execute();
                break;
            case R.id.btjoincancel:
                Intent itlogin = new Intent(this, Loginmenu.class);
                startActivity(itlogin);
                finish();
                break;
        }
    }
   public class Join extends AsyncTask<Void,Void,String>{       //사용자가 입력한 정보를 가지고 회원가입

        @Override
        protected String doInBackground(Void... params) {

            try {
                URL obj = new URL(urlpath);

                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
               conn.setRequestMethod("POST"); //요청 방식을 설정 (default : GET)

               conn.setDoInput(true); //input을 사용하도록 설정 (default : true)
                conn.setDoOutput(true); //output을 사용하도록 설정 (default : false)

                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);



                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(
                        "&user_id=" +user_id
                                + "&pw="+user_pass
                                + "&area="+user_area
                ); //요청 파라미터를 입력
                writer.flush();
                writer.close();
                os.close();


                conn.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); //캐릭터셋 설정

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    if(sb.length() > 0) {
                        sb.append("\n");
                    }
                    sb.append(line);
                }
                br.close();
                conn.disconnect();

                return sb.toString().trim();

            }catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }


        @Override
        protected void onPostExecute(String result){
            gointent(result);
        }
    }

}

