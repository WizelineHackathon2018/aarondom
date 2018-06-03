package com.aarondomo.wizeline.ui.fragments;

import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aarondomo.wizeline.R;
import com.aarondomo.wizeline.model.Minute;
import com.aarondomo.wizeline.ui.OnSpeakOut;
import com.aarondomo.wizeline.utils.ScrumCoordinator;
import com.aarondomo.wizeline.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LiveStandUpFragment extends Fragment {

    private Button startStandUp;
    private TextView speaker;

    private TextView timer;
    private TextView audioStatus;

    private Button stop;
    private Button saveMinuteButton;

    private MediaRecorder audioMediaRecorder = new MediaRecorder();
    private String pathForAudio;
    private String audiofile;

    private ScrumCoordinator scrumCoordinator;

    private OnSpeakOut textToSpeech;

    private CountDownTimer countDownTimer;

    private FirebaseDatabase database;
    private DatabaseReference minuteDbReference;

    private StorageReference storageReference;

    private Date today = new Date();

    private Map<String, String> audioMap = new HashMap<>();
    private Minute minute = new Minute();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDB();

    }

    private void initDB(){
        //Cloud Database

        database = FirebaseDatabase.getInstance();
        minuteDbReference = database.getReference();

        //Cloud Storage
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_live_standup, container, false);

        startStandUp = view.findViewById(R.id.button_start_standup);
        speaker = view.findViewById(R.id.textview_member_speaking);
        timer = view.findViewById(R.id.textview_timer);
        audioStatus = view.findViewById(R.id.textview_audio_status);
        saveMinuteButton = view.findViewById(R.id.save_minute);


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
        saveMinuteButton.setEnabled(false);

        startStandUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speaker.setVisibility(View.VISIBLE);
                timer.setVisibility(View.VISIBLE);
                stop.setVisibility(View.VISIBLE);
                nextParticipant();
                startStandUp.setVisibility(View.GONE);

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudioRecord();
                nextParticipant();
            }
        });

        saveMinuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMinute();
                saveMinuteButton.setEnabled(false);
                saveMinuteButton.setVisibility(View.GONE);
            }
        });
    }


    private String startAudioRecord(String user){
        pathForAudio = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        audiofile = pathForAudio
                + user
                + "_"
                + Utils.getFormattedDate(today)
                + "_"
                + UUID.randomUUID().toString().substring(1,8)
                + ".3gp";
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

        //Toast.makeText(getContext(), "Recording started", Toast.LENGTH_SHORT).show();
        audioStatus.setText("Recording started");

        return audiofile;
    }

    private void stopAudioRecord() {
        audioMediaRecorder.stop();
        audioMediaRecorder.reset();
        stop.setEnabled(false);
        //Toast.makeText(getContext(), "Audio Recorder stopped", Toast.LENGTH_SHORT).show();
        audioStatus.setText("Audio Recorder stopped");
        uploadFile();
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
        } else {
            stop.setVisibility(View.GONE);
            saveMinuteButton.setVisibility(View.VISIBLE);
            speakOut("Ok que comience el post");
            saveMinuteButton.setEnabled(true);
        }
    }

    private void saveMinute() {

        minute.setDate(Utils.getFormattedDate(today));
        minute.setHour(Utils.getFormattedHour(today));
        minute.setUserUpdate(audioMap);
        minute.setTeam("Super Team");

        minuteDbReference.child("team_minutes").push().setValue(minute);
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
                timer.setText("Hey!, POST stand up");
                timer.setTextColor(Color.WHITE);
            }
        }.start();
    }


    private void speakOut(final String speech){
        textToSpeech.onSpeak(speech);
    }

    private void uploadFile(){
        Uri file = Uri.fromFile(new File(audiofile));
        String filename = audiofile.replace(pathForAudio, "");
        final String[] teamMember = filename.split("_");
        StorageReference audioMinutes = storageReference.child("minutes/" + "team/" + filename);

        audioMinutes.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        if (teamMember[0].equals("post")) {
                            minute.setPost(downloadUrl.toString());
                        } else {
                            audioMap.put(teamMember[0], downloadUrl.toString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                });
    }


}
