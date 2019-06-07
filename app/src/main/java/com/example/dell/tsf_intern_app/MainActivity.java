package com.example.dell.tsf_intern_app;

import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText email,pass;
    Button btnlogin,btnsignup;
    RequestQueue queue;
    String Base_URL = "http://139.59.65.145:9090";
    ProgressDialog pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=(EditText)findViewById(R.id.etemail);
        pass=(EditText)findViewById(R.id.etpassword);
        btnlogin=(Button)findViewById(R.id.btnsubmit);
        btnsignup=(Button)findViewById(R.id.btnsignup);
        queue= Volley.newRequestQueue(this);
        pb=new ProgressDialog(this);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setMessage("Loading.....");
                pb.show();

                final String semail=email.getText().toString();
                final String spassword=pass.getText().toString();
                String URL=Base_URL+"/user/login";
                Map<String,String> params=new HashMap<>();
                params.put("email",semail);
                params.put("password",spassword);
                JSONObject parameters=new JSONObject(params);
                JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data =response.getJSONObject("data");
                            int id=data.getInt("id");
                            String recemail=data.getString("email");
                            Toast.makeText(MainActivity.this, "Login successfull", Toast.LENGTH_SHORT).show();
                            Intent in =new Intent(MainActivity.this,user_detail.class);
                            in.putExtra("id",id+"");
                            in.putExtra("email",recemail);
                            startActivity(in);
                            pb.cancel();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pb.cancel();
                    }
                });
                queue.add(request);
            }
        });
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in =new Intent(MainActivity.this,signup.class);
                startActivity(in);
            }
        });
    }
}
