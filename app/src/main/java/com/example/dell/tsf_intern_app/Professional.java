package com.example.dell.tsf_intern_app;

import android.content.Intent;
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

public class Professional extends AppCompatActivity {
    RequestQueue queue;
    String Base_URL="http://139.59.65.145:9090";
    EditText org,desig,start,end;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional);
        Intent in=getIntent();
        final String id=in.getStringExtra("id");
        org=(EditText)findViewById(R.id.etorg);
        desig=(EditText)findViewById(R.id.etdesig);
        start=(EditText)findViewById(R.id.etstdate);
        end=(EditText)findViewById(R.id.etenddate);
        submit=(Button)findViewById(R.id.btnsubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchdata(id);
            }
        });

    }

    private void fetchdata(String id) {
        queue= Volley.newRequestQueue(this);
        String URL=Base_URL+"/user/professionaldetail/"+id;
        Map<String,String> params=new HashMap<>();
        String sorg=org.getText().toString();
        String sdesig=desig.getText().toString();
        String sstart=start.getText().toString();
        String send=end.getText().toString();
        params.put("organisation",sorg);
        params.put("designation",sdesig);
        params.put("start_date",sstart);
        params.put("end_date",send);
        JSONObject parameters=new JSONObject(params);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data =response.getJSONObject("data");
                    //Toast.makeText(Personal.this, ""+data, Toast.LENGTH_SHORT).show();
                    String sorg=data.getString("organisation");
                    String sdesig=data.getString("designation");
                    String sstart=data.getString("start_date");
                    String send=data.getString("end_date");
                    org.setText(sorg);
                    desig.setText(sdesig);
                    start.setText(sstart);
                    end.setText(send);
                    Toast.makeText(Professional.this, "Inserted successfully" ,Toast.LENGTH_SHORT).show();
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
                    String sorg=data.getString("organisation");
                    String sdesig=data.getString("designation");
                    String sstart=data.getString("start_date");
                    String send=data.getString("end_date");
                    org.setText(sorg);
                    desig.setText(sdesig);
                    start.setText(sstart);
                    end.setText(send);
                    Toast.makeText(Professional.this, "UPDATED successfully" ,Toast.LENGTH_SHORT).show();
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
