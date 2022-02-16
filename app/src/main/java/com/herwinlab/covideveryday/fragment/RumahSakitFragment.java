package com.herwinlab.covideveryday.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.herwinlab.covideveryday.R;
import com.herwinlab.covideveryday.adapter.KotaViewAdapter;
import com.herwinlab.covideveryday.adapter.RumahSakitViewAdapter;
import com.herwinlab.covideveryday.api.ApiEndPoint;
import com.herwinlab.covideveryday.model.DataRumahSakitModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RumahSakitFragment extends Fragment {

    Message msg;
    List<DataRumahSakitModel> provinsi,kota,provclone,kotaclone;
    //TextView tapp;
    public RecyclerView recyclerProvinsi;
    RumahSakitViewAdapter rumahSakitViewAdapter;
    //public NestedScrollView scrollView;
    public ConstraintLayout constraintLayout;
    KotaViewAdapter kotaViewAdapter;
    public ShimmerFrameLayout shimmerFrameLayout;
    public SearchView searchView;
    public Button buttonFilter;
    Boolean kotas;
    String g,provid;

    public RumahSakitFragment () {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rs_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        constraintLayout = view.findViewById(R.id.cons);
        //tapp = findViewById(R.id.tapp);
        shimmerFrameLayout = view.findViewById(R.id.shimmer1);
        buttonFilter = view.findViewById(R.id.button_provinsi);
        //scroll = findViewById(R.id.scroll);
        searchView = view.findViewById(R.id.sv);
        //searchView.setInputType(InputType.TS);
        recyclerProvinsi = view.findViewById(R.id.recycler_Provinsi);
        msg = new Message(getActivity());
        //tapp.setPaintFlags(tapp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerProvinsi.setLayoutManager(lm);
        getProvince();
        provclone = new ArrayList<>();
        kotaclone = new ArrayList<>();

        buttonFilter.setOnClickListener(view1 -> {
            buttonFilter.setVisibility(View.GONE);
            recyclerProvinsi.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    kotas=false;
                    searchView.setQuery("",false);
                    searchView.setQueryHint("Filter provinsi");
                    searchView.clearFocus();
                    constraintLayout.requestFocus();
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recyclerProvinsi.setVisibility(View.VISIBLE);
                    recyclerProvinsi.setAdapter(rumahSakitViewAdapter);
                }
            },3000);

        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals(null) || newText.equals("")){
                    if(kotas){
                        recyclerProvinsi.setAdapter(kotaViewAdapter);
                    }
                    else{
                        recyclerProvinsi.setAdapter(rumahSakitViewAdapter);
                    }
                }
                else{
                    if(kotas){
                        kotaclone.clear();
                        int ta=0;
                        for(DataRumahSakitModel yu : kota){
                            if(yu.getNama().contains(newText)){
                                g=newText;
                                kotaclone.add(kota.get(ta));
                            }
                            ta++;
                        }

                        KotaViewAdapter kotadc = new KotaViewAdapter(getActivity(),kotaclone);
                        recyclerProvinsi.setAdapter(kotadc);
                        kotadc.setClick((v, Position) -> {
                            Intent i = new Intent(getActivity(), BedActivity.class);
                            i.putExtra("provid",provid);
                            i.putExtra("kotaid",kotaclone.get(Position).getId());
                            i.putExtra("nama",kotaclone.get(Position).getNama());
                            startActivity(i);
                            //     Toast.makeText(MainActivity.this, String.valueOf(provid)+" "+kotaclone.get(Position).getId(), Toast.LENGTH_SHORT).show();
                        });

                    }
                    else{
                        provclone.clear();
                        int ta=0;
                        for(DataRumahSakitModel yu : provinsi){
                            if(yu.getNama().contains(newText)){
                                g=newText;
                                provclone.add(provinsi.get(ta));
                            }
                            ta++;
                        }
                        RumahSakitViewAdapter padc = new RumahSakitViewAdapter(getActivity(),provclone);
                        recyclerProvinsi.setAdapter(padc);
                        padc.setClick(new RumahSakitViewAdapter.click() {
                            @Override
                            public void onItemClick(View v, int Position) {
                                recyclerProvinsi.setVisibility(View.GONE);
                                buttonFilter.setVisibility(View.VISIBLE);
                                buttonFilter.setText(provclone.get(Position).getNama());
                                //scroll.post((Runnable) () -> scroll.smoothScrollTo(0,0));

                                provid = provclone.get(Position).getId();
                                getKota(provid);
                                // Toast.makeText(getApplicationContext(),provid,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                return false;
            }
        });
    }

    public void getProvince()
    {
        kotas = false;
        searchView.setQueryHint("Filter provinsi");
        if(!shimmerFrameLayout.isShimmerStarted()){
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
        }
        AndroidNetworking.get(ApiEndPoint.url_Provinsi)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            provinsi = new ArrayList<>();
                            JSONArray ja = response.getJSONArray("provinces");
                            for(int i=0;i<ja.length();i++){
                                provinsi.add(new DataRumahSakitModel(ja.getJSONObject(i).getString("id"),ja.getJSONObject(i).getString("name")));
                            }
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            recyclerProvinsi.setVisibility(View.VISIBLE);
                            rumahSakitViewAdapter = new RumahSakitViewAdapter(getActivity(),provinsi);
                            recyclerProvinsi.setAdapter(rumahSakitViewAdapter);
                            rumahSakitViewAdapter.setClick(new RumahSakitViewAdapter.click() {
                                @Override
                                public void onItemClick(View v, int Position) {
                                    recyclerProvinsi.setVisibility(View.GONE);
                                    buttonFilter.setVisibility(View.VISIBLE);
                                    buttonFilter.setText(provinsi.get(Position).getNama());
                                    //scroll.post((Runnable) () -> scroll.smoothScrollTo(0,0));
                                    provid = provinsi.get(Position).getId();
                                    getKota(provid);
                                    // Toast.makeText(getApplicationContext(),provid,Toast.LENGTH_LONG).show();
                                }
                            });
                            // Toast.makeText(MainActivity.this, String.valueOf(provinsi.size()), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            msg.error("Opps error",e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        if(anError.getMessage().contains("dress")){

                            Toast.makeText(getActivity(), "selesai, status tidak ada koneksi", Toast.LENGTH_LONG).show();
                            getActivity().finish();
                        }
                    }
                });
    }

    public void getKota(String id) {
        kotas = true;
        searchView.setQuery("", false);
        searchView.setQueryHint("Filter Kota / Kabupaten");
        searchView.clearFocus();
        constraintLayout.requestFocus();
        if (!shimmerFrameLayout.isShimmerStarted()) {
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
        }
        AndroidNetworking.get(ApiEndPoint.url_Kota + id)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            kota = new ArrayList<>();
                            JSONArray ja = response.getJSONArray("cities");
                            for (int i = 0; i < ja.length(); i++) {
                                kota.add(new DataRumahSakitModel(ja.getJSONObject(i).getString("id"), ja.getJSONObject(i).getString("name")));
                            }
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            kotaViewAdapter = new KotaViewAdapter(getActivity(), kota);
                            recyclerProvinsi.setVisibility(View.VISIBLE);
                            recyclerProvinsi.setAdapter(kotaViewAdapter);
                            kotaViewAdapter.setClick(new KotaViewAdapter.click() {
                                @Override
                                public void onItemClick(View v, int Position) {
                                        Intent i = new Intent(getActivity(),BedActivity.class);
                                        i.putExtra("provid",provid);
                                        i.putExtra("kotaid",kota.get(Position).getId());
                                        i.putExtra("nama",kota.get(Position).getNama());
                                        startActivity(i);
                                    // Toast.makeText(MainActivity.this, String.valueOf(provid)+" "+kota.get(Position).getId(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } catch (JSONException e) {
                            msg.error("Error", e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Koneksi error", Toast.LENGTH_SHORT).show();
                        buttonFilter.setVisibility(View.GONE);
                        recyclerProvinsi.setAdapter(rumahSakitViewAdapter);
                        recyclerProvinsi.setVisibility(View.VISIBLE);
                    }
                });
    }
}
