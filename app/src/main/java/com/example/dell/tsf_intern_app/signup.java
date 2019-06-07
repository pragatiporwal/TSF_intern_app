package com.example.dell.tsf_intern_app;

import android.app.VoiceInteractor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class signup extends AppCompatActivity {

    EditText email,password;
    Button submit;
    RequestQueue queue;
    String BaseURL="http://139.59.65.145:9090";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email=(EditText)findViewById(R.id.etemail);
        password=(EditText)findViewById(R.id.etpass);
        submit=(Button)findViewById(R.id.btnsubmit);
        queue= Volley.newRequestQueue(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String semail=email.getText().toString();
                String spassword=password.getText().toString();
                if(!semail.isEmpty() && !spassword.isEmpty()) {
                    String URL = BaseURL + "/user/signup";
                    Map<String, String> param = new HashMap<>();
                    param.put("email", semail);
                    param.put("password", spassword);
                    JSONObject parameters=new JSONObject(param);
                    JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject data=response.getJSONObject("data");
                                int id=data.getInt("id");
                                String email=data.getString("email");
                                Toast.makeText(signup.this, "Create Account Successfully ", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    queue.add(request);
                }
            }
        });
    }
}
