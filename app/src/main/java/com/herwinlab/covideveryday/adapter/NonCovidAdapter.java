package com.herwinlab.covideveryday.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.herwinlab.covideveryday.R;
import com.herwinlab.covideveryday.fragment.DetailActivity;
import com.herwinlab.covideveryday.model.NonCovidBedViewModel;

import java.util.List;

public class NonCovidAdapter extends RecyclerView.Adapter<NonCovidAdapter.ViewHolder> {
    private List<NonCovidBedViewModel> data;
    private Context c, ca;

    public NonCovidAdapter(Context c, List data, Context ca) {
        this.c = c;
        this.ca = ca;
        this.data = data;
    }

    @NonNull
    @Override
    public NonCovidAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.noncovid_per_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NonCovidAdapter.ViewHolder holder, int position) {
        holder.nama.setText(String.valueOf(position + 1) + ". " + data.get(position).getName());
        holder.jalan.setText(data.get(position).getAddress());
        holder.update.setText(data.get(position).getUpdate());
        holder.tersedia.setText("Tersedia \n" + String.valueOf(data.get(position).getJumlah()));
        if (data.get(position).getJumlah() > 0) {
            holder.status.setText("Status\nAda");
            holder.status.setTextColor(Color.parseColor("#0B8807"));
            holder.status.setBackground(c.getDrawable(R.drawable.back_availinfo));
        } else {
            holder.status.setText("Status\nPenuh");
            holder.status.setTextColor(Color.RED);
            holder.status.setBackground(c.getDrawable(R.drawable.back_statusinfo));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, jalan, tersedia, status, update;
        Button call, map, detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nonrs_name);
            jalan = itemView.findViewById(R.id.nonjalanrs_name);
            tersedia = itemView.findViewById(R.id.nonbedAvail);
            status = itemView.findViewById(R.id.nonbedFull);
            update = itemView.findViewById(R.id.nonupdateData);

            call = itemView.findViewById(R.id.nonbedcall);
            map = itemView.findViewById(R.id.nonbedmaps);
            detail = itemView.findViewById(R.id.nonbeddeatil);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_DIAL);
                    i.setData(Uri.parse("tel:" + data.get(getAdapterPosition()).getPhone()));
                    ca.startActivity(i);
                }
            });

            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri guri = Uri.parse("geo:0,0?q=" + data.get(getAdapterPosition()).getName() + ", " + data.get(getAdapterPosition()).getAddress());
                    Intent i = new Intent(Intent.ACTION_VIEW, guri);
                    i.setPackage("com.google.android.apps.maps");
                    ca.startActivity(i);
                }
            });
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ca, DetailActivity.class);
                    i.putExtra("id", data.get(getAdapterPosition()).getId());
                    i.putExtra("type", "2");
                    i.putExtra("judul", data.get(getAdapterPosition()).getName());
                    ca.startActivity(i);
                }
            });
        }
    }
}
