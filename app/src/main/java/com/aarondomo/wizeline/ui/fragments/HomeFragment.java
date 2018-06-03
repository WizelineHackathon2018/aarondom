package com.aarondomo.wizeline.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aarondomo.wizeline.R;
import com.aarondomo.wizeline.model.Minute;
import com.aarondomo.wizeline.ui.adapter.MinutesAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private MinutesAdapter minutesAdapter;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("team_minutes");

    private List<Minute> minuteList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclreview_minutes);

        readMinutesFromDb();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        minutesAdapter = new MinutesAdapter(minuteList, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(minutesAdapter);

        return view;
    }

    private void readMinutesFromDb() {
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Minute minute = postSnapshot.getValue(Minute.class);
                    minuteList.add(minute);
                }
                minutesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("mytag", "Failed to read value.", error.toException());
            }
        });
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
