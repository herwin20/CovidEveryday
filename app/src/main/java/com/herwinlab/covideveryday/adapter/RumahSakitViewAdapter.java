package com.herwinlab.covideveryday.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.herwinlab.covideveryday.R;
import com.herwinlab.covideveryday.model.DataRumahSakitModel;

import java.util.List;

public class RumahSakitViewAdapter extends RecyclerView.Adapter<RumahSakitViewAdapter.ViewHolder> {
    private Context c;
    private List<DataRumahSakitModel> data;
    private int lastPosition = -1;
    private click cl;
    public RumahSakitViewAdapter(Context c, List data){
        this.c=c;
        this.data=data;
    }

    @NonNull
    @Override
    public RumahSakitViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.namaprov_per_item,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RumahSakitViewAdapter.ViewHolder holder, int position) {
        DataRumahSakitModel prop = data.get(position);
        holder.provinsi.setText(String.valueOf(position+1)+". "+prop.getNama());

        if(position %2 !=0){
            holder.card.setCardBackgroundColor(Color.parseColor("#000000"));
        }
        // setAnimation(holder.card,position);
    }

    private void setAnimation(View vt, int pos){
        if(pos > lastPosition){
            Animation anim = AnimationUtils.loadAnimation(c, android.R.anim.slide_in_left);
            vt.startAnimation(anim);
            lastPosition = pos;
        }
        else{
            Animation anim = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
            vt.startAnimation(anim);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView provinsi;
        private MaterialCardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            provinsi = itemView.findViewById(R.id.textProvinsi);
            card = itemView.findViewById(R.id.mycard);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(cl != null) cl.onItemClick(view,getAdapterPosition());
                }
            });
        }
    }
    public DataRumahSakitModel getItem(int id){
        return data.get(id);
    }
    public void setClick(click cl){
        this.cl=cl;
    }
    public interface click{
        void onItemClick(View v, int Position);
    }
}
