package com.herwinlab.covideveryday.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.herwinlab.covideveryday.R;
import com.herwinlab.covideveryday.adapter.HospitalBedAdapter;
import com.herwinlab.covideveryday.adapter.NonCovidAdapter;
import com.herwinlab.covideveryday.api.ApiEndPoint;
import com.herwinlab.covideveryday.model.HospitalViewModel;
import com.herwinlab.covideveryday.model.NonCovidBedViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BedActivity extends AppCompatActivity {

    NonCovidAdapter nonCovidAdapter;
    HospitalBedAdapter hospitalBedAdapter;
    ShimmerFrameLayout shimmerFrameLayout;
    TabLayout tabLayout;
    TextView namaKota;
    String provid,kotaid;
    List<HospitalViewModel> dataHospital;
    List<NonCovidBedViewModel> dataNonCovid;
    RecyclerView recyclerViewBed;
    ImageView backEnd;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.rs_bed_activity);

        tabLayout = findViewById(R.id.tablayout);
        recyclerViewBed = findViewById(R.id.recyclerBed);
        Intent in = getIntent();
        provid = in.getStringExtra("provid");
        kotaid = in.getStringExtra("kotaid");
        namaKota = findViewById(R.id.bednama);
        shimmerFrameLayout = findViewById(R.id.shimmerbed);
        backEnd = findViewById(R.id.bed_Back);
        namaKota.setText(in.getStringExtra("nama"));
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerViewBed.setLayoutManager(lm);
        AndroidNetworking.initialize(getApplicationContext());
        getCovid("1");

        backEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        if(dataHospital != null){
                            recyclerViewBed.setAdapter(hospitalBedAdapter);
                        }
                        else{
                            getCovid("1");
                        }
                        break;
                    case 1:
                        if(dataNonCovid != null){
                            recyclerViewBed.setAdapter(nonCovidAdapter);
                        }
                        else{
                            getCovid("2");
                        }
                        break;
                }
                // Toast.makeText(bedActivity.this, String.valueOf(tab.getPosition()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void getCovid(String type){
        if(!shimmerFrameLayout.isShimmerStarted()){
            recyclerViewBed.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
        }
        AndroidNetworking.get(ApiEndPoint.url_rs+provid+"&cityid="+kotaid+"&type="+type)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray ja = response.getJSONArray("hospitals");
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            File f = new File(getFilesDir().getAbsolutePath()+"/io");
                            if(!f.exists()){
                                f.mkdir();
                                new SweetAlertDialog(BedActivity.this,SweetAlertDialog.NORMAL_TYPE)
                                        .setTitleText("Info App")
                                        .setContentText("Geser kotak kekiri untuk opsi lainnya")
                                        .show();
                            }
                            if(type.equals("1")) {
                                dataHospital = new ArrayList<>();
                                for (int i = 0; i < ja.length(); i++) {
                                    String id = ja.getJSONObject(i).getString("id");
                                    String name = ja.getJSONObject(i).getString("name");
                                    String address = ja.getJSONObject(i).getString("address");
                                    String phone = ja.getJSONObject(i).getString("phone");
                                    String queue = ja.getJSONObject(i).getString("queue");
                                    String bed_availability = ja.getJSONObject(i).getString("bed_availability");
                                    String info = ja.getJSONObject(i).getString("info");
                                    dataHospital.add(new HospitalViewModel(id, name, address, phone, queue, bed_availability, info));
                                }

                                hospitalBedAdapter = new HospitalBedAdapter(getApplicationContext(), dataHospital, BedActivity.this);
                                recyclerViewBed.setVisibility(View.VISIBLE);
                                if(dataHospital.size() >0) {
                                    recyclerViewBed.setAdapter(hospitalBedAdapter);
                                }
                                else{
                                    new SweetAlertDialog(BedActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Maaf data tidak ditemukan :(")
                                            .setContentText("Silahkan cari lokasi lain")
                                            .show();
                                }
                            }
                            else{
                                dataNonCovid = new ArrayList<>();
                                for(int i = 0;i<ja.length();i++){
                                    String id = ja.getJSONObject(i).getString("id");
                                    String name = ja.getJSONObject(i).getString("name");
                                    String address = ja.getJSONObject(i).getString("address");
                                    String phone = ja.getJSONObject(i).getString("phone");
                                    //List<bed_nonCovid> bed = new ArrayList<>();
                                    JSONArray jo = ja.getJSONObject(i).getJSONArray("available_beds");
                                    int jumlah = 0;
                                    String update = "";
                                    for(int j =0;j<jo.length();j++){
                                        try{
                                            update = jo.getJSONObject(j).getString("info");
                                            jumlah += Integer.parseInt(jo.getJSONObject(j).getString("available"));
                                        }catch(Exception e){
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    dataNonCovid.add(new NonCovidBedViewModel(id,name,address,phone,jumlah,update));
                                }
                                nonCovidAdapter = new NonCovidAdapter(getApplicationContext(),dataNonCovid,BedActivity.this);
                                recyclerViewBed.setVisibility(View.VISIBLE);
                                if(dataNonCovid.size() > 0) {
                                    recyclerViewBed.setAdapter(nonCovidAdapter);
                                }else{
                                    new SweetAlertDialog(BedActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Maaf data tidak ditemukan :(")
                                            .setContentText("Silahkan cari lokasi lain")
                                            .show();
                                }

                            }
                        } catch (JSONException e) {
                            Toast.makeText(BedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        new SweetAlertDialog(BedActivity.this,SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Opps error")
                                .setContentText(anError.getMessage())
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        finish();
                                    }
                                }).show();
                    }
                });

    }
}
