package com.herwinlab.covideveryday.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.herwinlab.covideveryday.R;
import com.herwinlab.covideveryday.fragment.DetailActivity;
import com.herwinlab.covideveryday.model.HospitalViewModel;

import java.util.List;

public class HospitalBedAdapter extends RecyclerView.Adapter<HospitalBedAdapter.ViewHolder> {

        private List<HospitalViewModel> data;
        private Context c,ca;

        public HospitalBedAdapter(Context c,List data, Context ca){
            this.c=c;
            this.data=data;
            this.ca=ca;
        }
        @NonNull
        @Override
        public HospitalBedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(c).inflate(R.layout.bed_per_item,parent,false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull HospitalBedAdapter.ViewHolder holder, int position) {
            holder.name.setText(String.valueOf(position+1)+". "+data.get(position).getName());
            holder.update.setText(data.get(position).getInfo());
            holder.address.setText(data.get(position).getAddress());
            holder.queue.setText("Antrian\n"+data.get(position).getQueue());
            holder.bed_availability.setText("Tersedia\n"+data.get(position).getBed_availability());
            if(Integer.parseInt(data.get(position).getBed_availability()) > 0){
                holder.status.setText("Status\nAda");
                holder.status.setTextColor(Color.parseColor("#0B8807"));
                holder.status.setBackground(c.getDrawable(R.drawable.back_availinfo));
            }
            else{
                holder.status.setText("Status\nPenuh");
                holder.status.setTextColor(Color.RED);
                holder.status.setBackground(c.getDrawable(R.drawable.back_statusinfo));
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
        class ViewHolder extends RecyclerView.ViewHolder{
            Button call,map,detail;
            TextView name,address,queue,bed_availability,status,update;
            LinearLayout lin;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.rs_name);
                address = itemView.findViewById(R.id.jalanrs_name);
                queue = itemView.findViewById(R.id.bedWaiting);
                bed_availability = itemView.findViewById(R.id.bedAvail);
                status = itemView.findViewById(R.id.bedFull);
                //lin = itemView.findViewById(R.id.linItemBed);
                call = itemView.findViewById(R.id.bedcall);
                map = itemView.findViewById(R.id.bedmaps);
                detail = itemView.findViewById(R.id.beddeatil);
                update = itemView.findViewById(R.id.updateData);
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_DIAL);
                        i.setData(Uri.parse("tel:"+data.get(getAdapterPosition()).getPhone()));
                        ca.startActivity(i);
                    }
                });

                map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri guri = Uri.parse("geo:0,0?q="+data.get(getAdapterPosition()).getName()+", "+data.get(getAdapterPosition()).getAddress());
                        Intent i = new Intent(Intent.ACTION_VIEW,guri);
                        i.setPackage("com.google.android.apps.maps");
                        ca.startActivity(i);
                    }
                });
                detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ca, DetailActivity.class);
                        i.putExtra("id",data.get(getAdapterPosition()).getId());
                        i.putExtra("type","1");
                        i.putExtra("judul",data.get(getAdapterPosition()).getName());
                        ca.startActivity(i);
                    }
                });
            }
        }
}
