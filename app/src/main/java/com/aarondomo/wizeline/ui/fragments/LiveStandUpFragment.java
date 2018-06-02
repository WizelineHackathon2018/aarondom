package com.aarondomo.wizeline.ui.fragments;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aarondomo.wizeline.R;

import java.io.IOException;
import java.util.UUID;

public class LiveStandUpFragment extends Fragment {

    private Button startStandUp;
    private TextView speaker;
    private TextView timer;

    private Button stop, record;

    private String outputFile;
    private MediaRecorder audioMediaRecorder = new MediaRecorder();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_live_standup, container, false);

        startStandUp = view.findViewById(R.id.button_start_standup);
        speaker = view.findViewById(R.id.textview_member_speaking);
        timer = view.findViewById(R.id.textview_timer);

        setUpStartStandUpButton();

        stop = (Button) view.findViewById(R.id.stop);
        record = (Button) view.findViewById(R.id.record);

        prepareAudio();

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

    private void prepareAudio(){
        stop.setEnabled(false);
        final Context applicationContext = getActivity().getApplicationContext();

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording_" + UUID.randomUUID() + ".3gp";
                audioMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                audioMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                audioMediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                audioMediaRecorder.setOutputFile(outputFile);

                try {
                    audioMediaRecorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                audioMediaRecorder.start();
                record.setEnabled(false);
                stop.setEnabled(true);

                Toast.makeText(applicationContext, "Recording started", Toast.LENGTH_SHORT).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioMediaRecorder.stop();
                audioMediaRecorder.reset();
                record.setEnabled(true);
                stop.setEnabled(false);
                Toast.makeText(applicationContext, "Audio Recorder stopped", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (audioMediaRecorder != null){
            audioMediaRecorder.release();
            audioMediaRecorder = null;
        }
    }
}