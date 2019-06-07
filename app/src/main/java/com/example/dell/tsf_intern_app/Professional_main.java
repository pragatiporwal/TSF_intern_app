package com.example.dell.tsf_intern_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class Professional_main extends AppCompatActivity {

    RequestQueue queue;
    String Base_URL="http://139.59.65.145:9090";
    TextView org,desig,start,end;
    ImageView edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_main);
        Intent in=getIntent();
        final String id=in.getStringExtra("id");
        org=(TextView) findViewById(R.id.tvorg);
        desig=(TextView) findViewById(R.id.tvdesig);
        start=(TextView) findViewById(R.id.tvstdate);
        end=(TextView) findViewById(R.id.tvenddate);
        edit=(ImageView)findViewById(R.id.ivedit);
        fetchdata(id);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Professional_main.this,Professional.class);
                in.putExtra("id",id);
                startActivity(in);
            }
        });
    }

    private void fetchdata(String id) {
        queue= Volley.newRequestQueue(this);
        String URL=Base_URL+"/user/professionaldetail/"+id;
        Map<String,String> params=new HashMap<>();
        JSONObject parameters=new JSONObject(params);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data =response.getJSONObject("data");
                    String sorg=data.getString("organisation");
                    String sdesig=data.getString("designation");
                    String sstart=data.getString("start_date");
                    String send=data.getString("end_date");
                    org.setText("Organisation : "+sorg);
                    desig.setText("Designation : "+sdesig);
                    start.setText("Start Date : "+sstart);
                    end.setText("End Date : "+send);
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
