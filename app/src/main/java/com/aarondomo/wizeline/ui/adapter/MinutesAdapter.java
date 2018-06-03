package com.aarondomo.wizeline.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aarondomo.wizeline.R;
import com.aarondomo.wizeline.model.Minute;

import java.util.List;

public class MinutesAdapter extends RecyclerView.Adapter<MinutesAdapter.MinutesViewHolder>{

    private Context context;

    private List<Minute> minuteList;

    public MinutesAdapter(List<Minute> minuteList, Context context) {
        this.minuteList = minuteList;
        this.context = context;
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

        holder.linearLayout.removeAllViews();

        for (String key : minute.getUserUpdate().keySet() ) {
            String link = minute.getUserUpdate().get(key);

            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView userUpdate = new TextView(context);
            userUpdate.setLayoutParams(lparams);

            userUpdate.setText(Html.fromHtml("<a href=\""+ link + "\">" + key + "</a>"));
            userUpdate.setClickable(true);
            userUpdate.setMovementMethod (LinkMovementMethod.getInstance());

            holder.linearLayout.addView(userUpdate);

        }

    }

    @Override
    public int getItemCount() {
        return minuteList != null ? minuteList.size() : 0;
    }

    public class MinutesViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView hour;
        public TextView team;

        public LinearLayout linearLayout;

        public MinutesViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.textview_date);
            hour = (TextView) view.findViewById(R.id.textview_hour);
            team = (TextView) view.findViewById(R.id.textview_team);

            linearLayout = (LinearLayout)view.findViewById(R.id.linearlayout_updates);
        }
    }
}
