package com.aarondomo.wizeline.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aarondomo.wizeline.R;
import com.aarondomo.wizeline.model.Minute;

import java.util.List;

public class MinutesAdapter extends RecyclerView.Adapter<MinutesAdapter.MinutesViewHolder>{

    private List<Minute> minuteList;

    public MinutesAdapter(List<Minute> minuteList) {
        this.minuteList = minuteList;
    }

    @Override
    public MinutesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row_minute, parent, false);

        return new MinutesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MinutesViewHolder holder, int position) {
        Minute minute = minuteList.get(position);
        holder.date.setText(minute.getDate());
        holder.hour.setText(minute.getHour());
        holder.team.setText(minute.getTeam());
    }

    @Override
    public int getItemCount() {
        return minuteList != null ? minuteList.size() : 0;
    }

    public class MinutesViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView hour;
        public TextView team;

        public MinutesViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.textview_date);
            hour = (TextView) view.findViewById(R.id.textview_hour);
            team = (TextView) view.findViewById(R.id.textview_team);
        }
    }
}
