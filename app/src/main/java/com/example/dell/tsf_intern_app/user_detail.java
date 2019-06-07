package com.example.dell.tsf_intern_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class user_detail extends AppCompatActivity {
    TextView edu,prof,per;
    RequestQueue queue;
    String Base_URL = "http://139.59.65.145:9090";
    CircleImageView profilepic;
    int Pic_image=1;
    Uri imageuri;
    ImageView linkedin,github,google;
    TextView name,loc,link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        Intent in=getIntent();
        final String id=in.getStringExtra("id");
        final String email=in.getStringExtra("email");
        name=(TextView)findViewById(R.id.tvname);
        loc=(TextView)findViewById(R.id.tvloc);
        link=(TextView)findViewById(R.id.tvlink);
        edu=(TextView)findViewById(R.id.tvedu);
        prof=(TextView)findViewById(R.id.tvpro);
        per=(TextView)findViewById(R.id.tvper);
        profilepic=(CircleImageView)findViewById(R.id.pic);
        linkedin=(ImageView)findViewById(R.id.linkedin);
        github=(ImageView)findViewById(R.id.github);
        google=(ImageView)findViewById(R.id.google);

        fetchdata(id);
        fetchPhoto(id);
        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.linkedin.com"));
                startActivity(in);
            }
        });
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.github.com"));
                startActivity(in);
            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.google.com"));
                startActivity(in);
            }
        });

        edu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in =new Intent(user_detail.this,Education_main.class);
                in.putExtra("id",id);
                startActivity(in);

            }
        });
        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in =new Intent(user_detail.this,Professional_main.class);
                in.putExtra("id",id);
                startActivity(in);
                }
        });
        per.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(user_detail.this,Personal_main.class);
                in.putExtra("id",id);
                in.putExtra("email",email);
                startActivity(in);
            }
        });

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
                    String sname=data.getString("name");
                    String slinks=data.getString("links");
                    String slocation=data.getString("location");
                    name.setText(sname);
                    loc.setText(slocation);
                    link.setText(slinks);
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

    private void fetchPhoto(String id) {
        RequestQueue requestQueue;
        String Base_URL = "http://139.59.65.145:9090";
        String URL=Base_URL+"/user/personaldetail/profilepic/"+id;
        requestQueue=Volley.newRequestQueue(this.getApplicationContext());
        ImageRequest imageRequest=new ImageRequest(URL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                profilepic.setImageBitmap(response);
            }
        }, 0, 0, null,null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(user_detail.this, "Error found"+error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(imageRequest);
    }
}
