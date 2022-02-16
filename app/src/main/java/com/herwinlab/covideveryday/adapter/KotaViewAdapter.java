package com.herwinlab.covideveryday.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.herwinlab.covideveryday.R;
import com.herwinlab.covideveryday.model.DataRumahSakitModel;

import java.util.List;

public class KotaViewAdapter extends RecyclerView.Adapter<KotaViewAdapter.ViewHolder> {
    private List<DataRumahSakitModel> data;
    private Context c;
    private click cl;
    public KotaViewAdapter(Context c, List data){
        this.c=c;
        this.data=data;
    }
    @NonNull
    @Override
    public KotaViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.namaprov_per_item,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull KotaViewAdapter.ViewHolder holder, int position) {
        DataRumahSakitModel prop = data.get(position);
        holder.text.setText(String.valueOf(position+1)+". "+prop.getNama());

        if(position %2 !=0){
            holder.card.setCardBackgroundColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        MaterialCardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.textProvinsi);
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
    public void setClick(KotaViewAdapter.click cl){
        this.cl=cl;
    }
    public interface click{
        void onItemClick(View v, int Position);
    }
}
