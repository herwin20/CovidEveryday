package com.herwinlab.covideveryday.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.herwinlab.covideveryday.R;
import com.herwinlab.covideveryday.adapter.DetailViewAdapter;
import com.herwinlab.covideveryday.api.ApiEndPoint;
import com.herwinlab.covideveryday.model.DetailCovidModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailActivity extends AppCompatActivity {

    RecyclerView recyclerViewDetail;
    ShimmerFrameLayout shimmerFrameLayout;
    TextView judul, type;
    List<DetailCovidModel> data;
    DetailViewAdapter detailViewAdapter;
    String id,types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        judul = findViewById(R.id.detailNama);
        type = findViewById(R.id.detailType);
        recyclerViewDetail = findViewById(R.id.detailRecycler);
        shimmerFrameLayout = findViewById(R.id.detailShimmer);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        types = intent.getStringExtra("type");
        judul.setText(intent.getStringExtra("judul"));
        LinearLayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewDetail.setLayoutManager(lm);
        if(types.equals("1")){
            //type covid
            type.setText("Type Covid-19");
        }
        else{
            type.setText("Type Non Covid-19");
        }
        getData();
    }

    public void getData(){
        if(!shimmerFrameLayout.isShimmerStarted()){
            shimmerFrameLayout.startShimmer();
            shimmerFrameLayout.setVisibility(View.VISIBLE);
        }
        AndroidNetworking.get(ApiEndPoint.url_detail)
                .addPathParameter("id",id)
                .addPathParameter("type",types)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jo = response.getJSONObject("data");
                            JSONArray ja = jo.getJSONArray("bedDetail");
                            data = new ArrayList<>();
                            for(int i=0;i<ja.length();i++){
                                String time = ja.getJSONObject(i).getString("time");

                                data.add(new DetailCovidModel(time,  ja.getJSONObject(i).getJSONObject("stats").getString("title"),
                                        ja.getJSONObject(i).getJSONObject("stats").getString("bed_available"),
                                        ja.getJSONObject(i).getJSONObject("stats").getString("bed_empty"),
                                        ja.getJSONObject(i).getJSONObject("stats").getString("queue")));
                            }
                            //  Toast.makeText(detailActivity.this, String.valueOf(data.size()), Toast.LENGTH_SHORT).show();
                            detailViewAdapter = new DetailViewAdapter(getApplicationContext(),data);
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            recyclerViewDetail.setVisibility(View.VISIBLE);
                            recyclerViewDetail.setAdapter(detailViewAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            new SweetAlertDialog(DetailActivity.this,SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(e.getMessage())
                                    .show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if(anError.getMessage().contains("dress")){
                            new SweetAlertDialog(DetailActivity.this,SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Opps!")
                                    .setContentText("Koneksi error, Coba ulangi lagi")
                                    .show();
                        }
                    }
                });
    }
}
