package com.example.zzamtiger.textview;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

import android.content.SharedPreferences;

public class Loginmenu extends AppCompatActivity implements OnClickListener {
    Button btlogin=null;
    Button btjoin=null;
    private EditText editTextName;
    private EditText editTextPass;
    String user_area = "서울";

    String urlpath = "http://teambutton.dothome.co.kr/login.php";

    String user_id;
    String user_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btjoin=(Button)findViewById(R.id.btjoin);
        btlogin=(Button)findViewById(R.id.btlogin);
        editTextName = (EditText) findViewById(R.id.editname);
        editTextPass = (EditText) findViewById(R.id.editpass);

        btjoin.setOnClickListener(this);
        btlogin.setOnClickListener(this);
    }
    public void gointent(String msg){
        if (msg.equals("success")) {
            Intent itlogin = new Intent(this, Mainmenu.class);
            startActivity(itlogin);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
   public void onClick(View arg0){
        switch(arg0.getId()){
            case R.id.btjoin:
                Intent itjoin=new Intent(this,Joinmenu.class);
                startActivity(itjoin);
                finish();
                break;
            case R.id.btlogin:
                user_id = editTextName.getText().toString();
                user_pass = editTextPass.getText().toString();

                SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);    //id값 저장
                SharedPreferences.Editor editor = data.edit();
                editor.putString("id", user_id);
                editor.commit();        //저장

                new Login().execute();
                break;
        }
    }
    public class Login extends AsyncTask<Void,Void,String>{      //ID와 password 검사

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
                        "&user_id=" +user_id + "&pw="+user_pass
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
