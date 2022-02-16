package com.herwinlab.covideveryday.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.herwinlab.covideveryday.R;
import com.herwinlab.covideveryday.model.DetailCovidModel;

import java.util.List;

public class DetailViewAdapter extends RecyclerView.Adapter<DetailViewAdapter.ViewHolder> {
    private Context c;
    private List<DetailCovidModel> data;

    public DetailViewAdapter(Context c, List<DetailCovidModel> data) {
        this.c = c;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.detail_per_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nama.setText(data.get(position).getTitle());
        holder.antrian.setText("Antrian\n"+ data.get(position).getQueue());
        holder.tersedia.setText("Tersedia\n"+data.get(position).getBed_empty() );
        holder.status.setText("Jumlah\n"+data.get(position).getBed_available());
        holder.update.setText(data.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nama,antrian,tersedia,status,update;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama =itemView.findViewById(R.id.detailTitle);
            antrian = itemView.findViewById(R.id.detailAntrian);
            tersedia = itemView.findViewById(R.id.detailTersedia);
            status = itemView.findViewById(R.id.detailStatus);
            update = itemView.findViewById(R.id.detailUpdate);
        }
    }
}
