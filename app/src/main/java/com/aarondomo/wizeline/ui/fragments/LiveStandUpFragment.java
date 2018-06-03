package com.aarondomo.wizeline.ui.fragments;

import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aarondomo.wizeline.R;
import com.aarondomo.wizeline.ui.OnSpeakOut;
import com.aarondomo.wizeline.utils.ScrumCoordinator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LiveStandUpFragment extends Fragment {

    private Button startStandUp;
    private TextView speaker;

    private TextView timer;

    private Button stop;

    private MediaRecorder audioMediaRecorder = new MediaRecorder();

    private ScrumCoordinator scrumCoordinator;

    private OnSpeakOut textToSpeech;

    private CountDownTimer countDownTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_live_standup, container, false);

        startStandUp = view.findViewById(R.id.button_start_standup);
        speaker = view.findViewById(R.id.textview_member_speaking);
        timer = view.findViewById(R.id.textview_timer);

        //Audio Recording
        stop = (Button) view.findViewById(R.id.stop);

        //Meeting Logic
        scrumCoordinator = new ScrumCoordinator(getMembers());

        setUpButtonListeners();

        //TextToSpeech
        textToSpeech = (OnSpeakOut)getActivity();

        return view;
    }

    private List<String> getMembers(){
        List<String> users = new ArrayList<>();

        users.add("Juan");
        users.add("Pedro");
        users.add("Paco");

        return users;
    }

    private void setUpButtonListeners() {
        stop.setEnabled(false);

        startStandUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speaker.setVisibility(View.VISIBLE);
                timer.setVisibility(View.VISIBLE);
                nextParticipant();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudioRecord();
                nextParticipant();
            }
        });
    }


    private String startAudioRecord(String user){
        String audiofile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + user + "_" + UUID.randomUUID() + ".3gp";
        audioMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        audioMediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        audioMediaRecorder.setOutputFile(audiofile);

        try {
            audioMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        audioMediaRecorder.start();
        stop.setEnabled(true);

        Toast.makeText(getContext(), "Recording started", Toast.LENGTH_SHORT).show();

        return audiofile;
    }

    private void stopAudioRecord() {
        audioMediaRecorder.stop();
        audioMediaRecorder.reset();
        stop.setEnabled(false);
        Toast.makeText(getContext(), "Audio Recorder stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (audioMediaRecorder != null){
            audioMediaRecorder.release();
            audioMediaRecorder = null;
        }

    }



    private void nextParticipant() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        if (scrumCoordinator.hasNext()) {
            String next = scrumCoordinator.getNextToSpeak();
            speakOut("Tu turno, " + next);
            speaker.setText(next);
            startAudioRecord(next);
            startTimer();
        }
    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                long remainingTime = millisUntilFinished / 1000;
                if (remainingTime < 7) {
                    timer.setBackgroundColor(Color.YELLOW);
                } else {
                    timer.setBackgroundColor(Color.GREEN);
                }
                timer.setText("" + remainingTime);
            }
            public void onFinish() {
                timer.setBackgroundColor(Color.RED);
                timer.setText("Hey!, que tal un POST stand up");
            }
        }.start();
    }


    private void speakOut(final String speech){
        textToSpeech.onSpeak(speech);
    }


}
