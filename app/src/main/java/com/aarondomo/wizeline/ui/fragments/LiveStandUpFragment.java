package com.aarondomo.wizeline.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aarondomo.wizeline.R;

public class LiveStandUpFragment extends Fragment {

    private Button startStandUp;
    private TextView speaker;
    private TextView timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_live_standup, container, false);

        startStandUp = view.findViewById(R.id.button_start_standup);
        speaker = view.findViewById(R.id.textview_member_speaking);
        timer = view.findViewById(R.id.textview_timer);

        setUpStartStandUpButton();

        return view;
    }

    private void setUpStartStandUpButton() {
        startStandUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speaker.setVisibility(View.VISIBLE);
                timer.setVisibility(View.VISIBLE);
            }
        });
    }

}
