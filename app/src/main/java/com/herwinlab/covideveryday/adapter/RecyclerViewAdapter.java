package com.herwinlab.covideveryday.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.herwinlab.covideveryday.R;
import com.herwinlab.covideveryday.model.WorldData;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecycleViewHolder> {

    private ArrayList<WorldData> countryDataArrayList;
    private final Context mContext;
    LayoutInflater layoutInflater;

    public RecyclerViewAdapter(ArrayList<WorldData> recycleDataArrayList, Context mContext) {
        this.countryDataArrayList = recycleDataArrayList;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_per_item, parent, false);
        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecycleViewHolder holder, int position) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");

        // Set Data to textView from our modal class.
        WorldData modal = countryDataArrayList.get(position);
        holder.caseToday.setText(formatter.format(modal.getCases()));
        holder.recoveredToday.setText(formatter.format(modal.getRecovered()));
        holder.deathsToday.setText(formatter.format(modal.getDeaths()));
        holder.Population.setText(formatter.format(modal.getPopulation()));
        holder.Critical.setText(modal.getactivePerOneMillion() + " %");
        holder.countryToday.setText(modal.getCountry());
        holder.addCase.setText(formatter.format(modal.getTodayCases()));
        holder.addRecov.setText(formatter.format(modal.getTodayRecovered()));
        holder.addDeath.setText(formatter.format(modal.getTodayDeaths()));
        holder.testTracing.setText(formatter.format(modal.getTests()));
        // Image
        Picasso.get().load(modal.getCountryInfo().getFlag()).into(holder.countryFlag);
        //Glide.with(holder.itemView).load(modal.getCountryInfo().getFlag()).into(holder.countryFlag);

        //Convert Unix Time To Format Std
        Date datetime = new Date(modal.getUpdated());
        String datetimeFor = new SimpleDateFormat("dd-MM-yy hh:mm:ss").format(datetime);
        holder.lastUpdated.setText(datetimeFor);
    }

    @Override
    public int getItemCount() {
        return countryDataArrayList.size();
    }

    public void setFilter(ArrayList<WorldData> filter) {
        countryDataArrayList = new ArrayList<>();
        countryDataArrayList.addAll(filter);
        notifyDataSetChanged();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder {
        // Creating variable for ours views
        private final TextView caseToday;
        private final TextView recoveredToday;
        private final TextView deathsToday;
        private final TextView countryToday;
        private final TextView Population;
        private final TextView Critical;
        private final TextView lastUpdated;
        private final TextView addCase;
        private final TextView addRecov;
        private final TextView addDeath;
        private final TextView testTracing;
        private final ImageView countryFlag;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initializing our views
            caseToday = itemView.findViewById(R.id.casesRV);
            recoveredToday =  itemView.findViewById(R.id.recoveredRV);
            deathsToday = itemView.findViewById(R.id.deathsRV);
            countryToday = itemView.findViewById(R.id.countryRV);
            countryFlag =  itemView.findViewById(R.id.flagRV);
            Population = itemView.findViewById(R.id.populationRV);
            Critical = itemView.findViewById(R.id.critpopulRV);
            lastUpdated = itemView.findViewById(R.id.timeRV);
            addCase = itemView.findViewById(R.id.add_caseRV);
            addRecov = itemView.findViewById(R.id.add_recovRV);
            addDeath = itemView.findViewById(R.id.add_deathRV);
            testTracing = itemView.findViewById(R.id.testRV);
        }
    }
}

