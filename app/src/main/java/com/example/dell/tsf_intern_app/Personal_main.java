package com.example.dell.tsf_intern_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Personal_main extends AppCompatActivity {
    RequestQueue queue;
    String Base_URL="http://139.59.65.145:9090";
    TextView name,uid,mobile,location,links,skill;
    ImageView edit;
    CircleImageView pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_main);
        name=(TextView) findViewById(R.id.tvname);
        uid=(TextView)findViewById(R.id.tvuid);
        mobile=(TextView) findViewById(R.id.tvno);
        location=(TextView)findViewById(R.id.tvloc);
        links=(TextView)findViewById(R.id.tvlinks);
        skill=(TextView)findViewById(R.id.tvskills);
        edit=(ImageView)findViewById(R.id.ivedit);
        pic= (CircleImageView)findViewById(R.id.pic);
        Intent in=getIntent();
        final String id=in.getStringExtra("id");
        final String email=in.getStringExtra("email");
        fetchPhoto(id);
        fetchdata(id);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Personal_main.this,Personal.class);
                in.putExtra("id",id);
                in.putExtra("email",email);
                startActivity(in);
            }
        });
    }

    private void fetchPhoto(String id) {
        RequestQueue requestQueue;
        String Base_URL = "http://139.59.65.145:9090";
        String URL=Base_URL+"/user/personaldetail/profilepic/"+id;
        requestQueue=Volley.newRequestQueue(this.getApplicationContext());
        ImageRequest imageRequest=new ImageRequest(URL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                pic.setImageBitmap(response);
            }
        }, 0, 0, null,null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Personal_main.this, "Error found"+error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(imageRequest);
    }


    private void fetchdata(String id) {
        queue= Volley.newRequestQueue(this);
        String URL=Base_URL+"/user/personaldetail/"+id;
        Map<String,String> params=new HashMap<>();
        JSONObject parameters=new JSONObject(params);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data =response.getJSONObject("data");
                    String sskills=data.getString("skills");
                    String smobile=data.getString("mobile_no");
                    String sname=data.getString("name");
                    String slinks=data.getString("links");
                    String slocation=data.getString("location");
                    int suid=data.getInt("uid");
                    name.setText("Name : "+sname);
                    uid.setText("User Id : "+String.valueOf(suid));
                    mobile.setText("Mobile : "+smobile);
                    location.setText("Location : "+slocation);
                    links.setText("Link : "+slinks);
                    skill.setText("Skill : "+sskills);
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
