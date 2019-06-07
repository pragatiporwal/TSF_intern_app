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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Education extends AppCompatActivity {

    RequestQueue queue;
    String Base_URL = "http://139.59.65.145:9090";
    EditText scollege,branch,scity,startyr,endyr;
    Button save;
    TextView certi;
    LinearLayout ll;
    ImageView ivcerti;
    int Pic_image=1;
    Uri imageuri;
    String dupid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);
        scollege=(EditText)findViewById(R.id.etcollege);
        scity=(EditText)findViewById(R.id.etcity);
        branch=(EditText)findViewById(R.id.etbranch);
        startyr=(EditText)findViewById(R.id.etstyr);
        endyr=(EditText)findViewById(R.id.etedyr);
        certi=(TextView)findViewById(R.id.certi);
        save=(Button)findViewById(R.id.btnsave);
        ll=(LinearLayout)findViewById(R.id.ll);

        Intent in=getIntent();
        final String id=in.getStringExtra("id");
        dupid=id;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               fetchdata(id);
            }
        });
        certi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivcerti=new ImageView(Education.this);
                ivcerti.setMaxWidth(10);
                ivcerti.setMaxHeight(10);
                //ivcerti.setImageResource(R.drawable.picas);
                ll.addView(ivcerti);
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
            ivcerti.setImageURI(imageuri);
            try {
                InputStream imageStream=getContentResolver().openInputStream(imageuri);
                Bitmap selectedImage= BitmapFactory.decodeStream(imageStream);
                String encodeImage=encodeimage(selectedImage);
                Toast.makeText(this, ""+imageuri, Toast.LENGTH_SHORT).show();
                RequestQueue queue;
                String Base_URL = "http://139.59.65.145:9090";

                queue= Volley.newRequestQueue(Education.this);
                String URL=Base_URL+"/user/educationdetail/certificate";
                Map<String,String> params=new HashMap<>();
                params.put("photo",encodeImage);
                params.put("uid","1000");
                JSONObject parameters=new JSONObject(params);
                JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String data =response.getString("status_message");
                            Toast.makeText(Education.this, ""+data, Toast.LENGTH_SHORT).show();
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
    private void fetchdata(String id) {
        queue= Volley.newRequestQueue(this);
        String URL=Base_URL+"/user/educationdetail/"+id;
        Map<String,String> params=new HashMap<>();
        String stcollege=scollege.getText().toString();
        String stcity=scity.getText().toString();
        String stbranch=branch.getText().toString();
        String stsrtyr=startyr.getText().toString();
        String stendyr=endyr.getText().toString();
        params.put("degree",stbranch);
        params.put("location",stcity);
        params.put("organisation",stcollege);
        params.put("start_year",stsrtyr);
        params.put("end_year",stendyr);
        JSONObject parameters=new JSONObject(params);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data =response.getJSONObject("data");
                    //Toast.makeText(Personal.this, ""+data, Toast.LENGTH_SHORT).show();
                    String degree=data.getString("degree");
                    String city=data.getString("location");
                    String college=data.getString("organisation");
                    String startYear=data.getString("start_year");
                    String endYear=data.getString("end_year");
                    branch.setText(degree);
                    scity.setText(city);
                    scollege.setText(college);
                    startyr.setText(startYear);
                    endyr.setText(endYear);
                    Toast.makeText(Education.this, "Inserted successfully" ,Toast.LENGTH_SHORT).show();
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
                    String degree=data.getString("degree");
                    String city=data.getString("location");
                    String college=data.getString("organisation");
                    String startYear=data.getString("start_year");
                    String endYear=data.getString("end_year");
                    branch.setText(degree);
                    scity.setText(city);
                    scollege.setText(college);
                    startyr.setText(startYear);
                    endyr.setText(endYear);
                    Toast.makeText(Education.this, "UPDATED successfully" ,Toast.LENGTH_SHORT).show();
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
