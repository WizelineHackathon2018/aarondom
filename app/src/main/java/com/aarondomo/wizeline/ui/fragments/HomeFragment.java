package com.aarondomo.wizeline.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aarondomo.wizeline.R;
import com.aarondomo.wizeline.model.Minute;
import com.aarondomo.wizeline.ui.adapter.MinutesAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private MinutesAdapter minutesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclreview_minutes);

        minutesAdapter = new MinutesAdapter(getTestMinuteList());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(minutesAdapter);

        minutesAdapter.notifyDataSetChanged();

        return view;
    }

    private List<Minute> getTestMinuteList() {
        List<Minute> minuteList = new ArrayList<>();

        Minute minute1 = new Minute();

        minute1.setDate("date 1");
        minute1.setHour("hour 1");
        minute1.setTeam("team 1");

        Minute minute2 = new Minute();

        minute2.setDate("date 2");
        minute2.setHour("hour 2");
        minute2.setTeam("team 2");

        minuteList.add(minute1);
        minuteList.add(minute2);

        return minuteList;
    }
}
