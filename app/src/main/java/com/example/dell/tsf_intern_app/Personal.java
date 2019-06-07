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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Personal extends AppCompatActivity {

    RequestQueue queue;
    String Base_URL="http://139.59.65.145:9090";
    EditText name,uid,mobile,location,links,skills;
    Button btnsubmit;
    CircleImageView profilepic;
    int Pic_image=1;
    Uri imageuri;
    String dupid="1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        name=(EditText)findViewById(R.id.etname);
        uid=(EditText)findViewById(R.id.etuid);
        mobile=(EditText)findViewById(R.id.etno);
        location=(EditText)findViewById(R.id.etloc);
        links=(EditText)findViewById(R.id.etlinks);
        skills=(EditText)findViewById(R.id.etskills);
        btnsubmit=(Button)findViewById(R.id.btnsubmit);
        profilepic=(CircleImageView)findViewById(R.id.pic);
        Intent in=getIntent();
        final String id=in.getStringExtra("id");
        final String email=in.getStringExtra("email");
        //Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
        uid.setText(id);
        dupid=id;
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchdata(id,email);
            }
        });
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallary=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallary,Pic_image);
            }
        });
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Pic_image && resultCode==RESULT_OK)
        {
            imageuri=data.getData();
            profilepic.setImageURI(imageuri);
            try {
                InputStream imageStream=getContentResolver().openInputStream(imageuri);
                Bitmap selectedImage= BitmapFactory.decodeStream(imageStream);
                String encodeImage=encodeimage(selectedImage);
                System.out.println(encodeImage);
                Toast.makeText(this, ""+imageuri, Toast.LENGTH_SHORT).show();
                RequestQueue queue;
                String Base_URL = "http://139.59.65.145:9090";

                queue= Volley.newRequestQueue(Personal.this);
                String URL=Base_URL+"/user/personaldetail/pp/post";
                Map<String,String> params=new HashMap<>();
                params.put("photo",encodeImage);
                params.put("uid",dupid);
                JSONObject parameters=new JSONObject(params);
                JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String data =response.getString("status_message");
                            Toast.makeText(Personal.this, ""+data, Toast.LENGTH_SHORT).show();
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            }
    }

    private String encodeimage(Bitmap bm) {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b=baos.toByteArray();
        String encImage= Base64.encodeToString(b,Base64.DEFAULT);
        return encImage;
    }
    private void fetchdata(String id,String email) {
        queue= Volley.newRequestQueue(this);
        String URL=Base_URL+"/user/personaldetail/"+id;
        Map<String,String> params=new HashMap<>();
        String sname=name.getText().toString();
        String smobile=mobile.getText().toString();
        String slocation=location.getText().toString();
        String slinks=links.getText().toString();;
        String sskills=skills.getText().toString();
        params.put("name",sname);
        params.put("mobile_no",smobile);
        params.put("location",slocation);
        params.put("links",slinks);
        params.put("skills",sskills);
        params.put("email",email);
        JSONObject parameters=new JSONObject(params);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data =response.getJSONObject("data");
                    //Toast.makeText(Personal.this, ""+data, Toast.LENGTH_SHORT).show();
                    String sskills=data.getString("skills");
                    String smobile=data.getString("mobile_no");
                    String sname=data.getString("name");
                    String slinks=data.getString("links");
                    String slocation=data.getString("location");
                    int suid=data.getInt("uid");
                    name.setText(sname);
                    uid.setText(String.valueOf(suid));
                    mobile.setText(smobile);
                    location.setText(slocation);
                    links.setText(slinks);
                    skills.setText(sskills);
                    Toast.makeText(Personal.this, "Inserted successfully" ,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        JsonObjectRequest request1=new JsonObjectRequest(Request.Method.PUT, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Toast.makeText(Personal.this, "in", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject data =response.getJSONObject("data");
                    //Toast.makeText(Personal.this, ""+data, Toast.LENGTH_SHORT).show();
                    String sskills=data.getString("skills");
                    String smobile=data.getString("mobile_no");
                    String sname=data.getString("name");
                    String slinks=data.getString("links");
                    String slocation=data.getString("location");
                    int suid=data.getInt("uid");
                    name.setText(sname);
                    uid.setText(String.valueOf(suid));
                    mobile.setText(smobile);
                    location.setText(slocation);
                    links.setText(slinks);
                    skills.setText(sskills);
                    Toast.makeText(Personal.this, "UPDATED successfully" ,Toast.LENGTH_SHORT).show();
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
        queue.add(request1);
    }


}
