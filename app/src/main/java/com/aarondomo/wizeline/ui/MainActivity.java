package com.aarondomo.wizeline.ui;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aarondomo.wizeline.R;
import com.aarondomo.wizeline.ui.fragments.HomeFragment;
import com.aarondomo.wizeline.ui.fragments.LiveStandUpFragment;
import com.aarondomo.wizeline.ui.fragments.NewTeamFragment;
import com.aarondomo.wizeline.ui.fragments.RegisterUserFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TextToSpeech.OnInitListener, OnSpeakOut {

    private Toolbar toolbar;
    private FrameLayout fragmentContainer;
    private int fragmentContainerId;
    private FragmentManager fragmentManager;

    private TextToSpeech textToSpeech;

    public TextToSpeech getTextToSpeech() {
        return textToSpeech;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpNavigationDrawer();

        setUpFragmentContainter();

        displayNewFragment(new HomeFragment());

        //Text to speech
        textToSpeech = new TextToSpeech(getApplicationContext(), this);

    }


    private void setUpFragmentContainter(){
        fragmentContainerId = R.id.fragment_container;
        fragmentContainer = findViewById(fragmentContainerId);
        fragmentManager = getSupportFragmentManager();
    }

    private void setUpNavigationDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            displayNewFragment(new HomeFragment());
        } else if (id == R.id.nav_live_standup) {
            displayNewFragment(new LiveStandUpFragment());

//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_new_team) {
            displayNewFragment(new NewTeamFragment());
        } else if (id == R.id.nav_new_member) {
            displayNewFragment(new RegisterUserFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayNewFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentContainerId, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Locale locale = new Locale("es_419");
            int result = textToSpeech.setLanguage(locale);

            textToSpeech.setSpeechRate(1.2f);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported");
            }
            Log.d("TTS","Text to speech warm and ready!");
        } else {
            Log.e("TTS", "Initilization Failed");
        }
    }

    @Override
    public void onSpeak(String speech) {
        textToSpeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null, "");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
