package com.example.deosfriend.design;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;

import database.DBAdapter;
import database.IntervalDBAdapter;
import database.SessionDBAdapter;

/**
 * Created by L335A15 on 3/17/2015.
 */
public class Timer_Test extends ActionBarActivity {

    Button buttonStart, flag, back, timeButton, activeEn, activeNon, passiveEn, passiveNon, test;
    TextView timerTextView, tvTest, interval, child, status, id, session, passName, tvStartTime;
    CheckBox cbAdults, cbPeers, cbMaterials, cbNoneOther;

    DBAdapter myDB;
    SessionDBAdapter mySessionDB;
    IntervalDBAdapter myIntervalDB;

    private Toolbar toolbar;

    private int minutes; // will be widely used across 2-3 different
    private int seconds; // methods / functions converting time

    private long startTime = 0L; // default when timer starts
    private long updateTime = 0L; // stores time after pause begins
    private long pasTime = 0L; // stores time when paused
    private long timeLimit = 29 * 60 * (10 ^ 3);
    private long eightSecsLimit = 15 * 60 * (10 ^ 3);
    private long tenSecsLimit = 20 * 60 * (10 ^ 3);
    private long fithteenSecsLimit = 29 * 60 * (10 ^ 3);
    // 29*60*(10^3) = 15 secs
    // 15*60*(10^3) = 8 secs
    // 20*60*(10^3) = 10secs
    private long excessTime = 0L; // stores time when exceeded the timeLimit
    private long millisec = 0L;

    private int intervalCount = 0;
    private int flagCount = 0;
    private String engagement="";
    private String adults="Null", peers="Null", materials="Null", noneOther="Null";
    private String childName, childId;
    private long childID;
    int sessionNo;

    // runs without a timer by reposting this handler at the end of the runnable
    public int[] getTimeValues(long time) {
        int seconds = (int) (time / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return new int[]{minutes, seconds};
    }

    public long getNewTimeLimit(long time) {
        return time;
    }

    Vibrator alarm;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {

            long millisec = System.currentTimeMillis() - startTime;
            updateTime = pasTime + millisec;
            minutes = getTimeValues(updateTime)[0];
            seconds = getTimeValues(updateTime)[1];
            int countSeconds = seconds;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));
            tvTest.setBackgroundColor(Color.YELLOW);

            tvTest.setText("Observe");
            timerHandler.postDelayed(this, 2);

            // ===================== Countdown observe ========================
            int count2 = 0;
            int count3 = 8;
            int count4 = 15;
            if (count2 < 8) {
                count2++;
                count3 = count3 - seconds;
                tvTest.setText("Observe (" + (count3) + ")");
            }
            // ================================================================

            // Observe to snapshot (Currently snapshot)
            if (updateTime >= eightSecsLimit) {
                minutes = getTimeValues(timeLimit)[0];
                seconds = getTimeValues(timeLimit)[1];

                tvTest.setBackgroundColor(Color.RED);
                alarm.vibrate(500);
                tvTest.setText("Snapshot!");
            }

            // snapshot to record (Currently record)
            if (updateTime >= tenSecsLimit) {
                alarm.cancel();
                minutes = getTimeValues(timeLimit)[0];
                seconds = getTimeValues(timeLimit)[1];
                // count down record
                if (count2 < 5) {
                    count2++;
                    count4 = count4 - countSeconds;
                    tvTest.setText("Record (" + (count4) + ")");
                }
                tvTest.setBackgroundColor(Color.GREEN);
            }

            // record to new interval
            // Flag button - Adds a flag count until max of 10 count
            flag.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            if (flagCount < 9) { // resets current interval and add a flag count
                                flagCount = flagCount + 1;
                                flag.setText("Flag Count: " + flagCount);
                                startTime = System.currentTimeMillis();
                                timerHandler.postDelayed(timerRunnable, 0);
                                myIntervalDB.insertRow(interval.getText().toString(), engagement, adults, peers, materials, noneOther, childID, childName, sessionNo, "Flagged!");
                            } else if (flagCount >= 9) { // if flag count reaches 10, terminate session

                                timerHandler.removeCallbacks(timerRunnable);
                                // Current time code
                                long dateInMillis = System.currentTimeMillis();
                                String formatTime = "HH:mm:ss";
                                // convert time
                                final SimpleDateFormat timeString = new SimpleDateFormat(formatTime);
                                final String time = timeString.format(new Date(dateInMillis));
                                // update status to failed!
                                Cursor lastRowID = mySessionDB.getLastRow();
                                myDB.updateRow(childID, "Fail");
                                mySessionDB.updateInCompleteSession(lastRowID.getInt(0), time, interval.getText().toString(), "Flag Count: 10", "Fail");
                                // insert flagged row to interval table
                                myIntervalDB.insertRow(interval.getText().toString(), engagement, adults, peers, materials, noneOther, childID, childName, sessionNo, "Flagged!");
                                //  text changes
                                status.setText("Current status: Fail!");
                                flag.setText("Flag Count: " + 10);
                                buttonStart.setText("End");
                                timerTextView.setText(("End of Session"));
                                tvTest.setText("Flag count reaches 10. Session ended!");
                                buttonStart.setEnabled(false);
                                flag.setEnabled(false);

                            }
                        }
                    }
            );

            //  if reach 15s of the interval
            if (updateTime >= fithteenSecsLimit) {

                Cursor lastRowID = mySessionDB.getLastRow();

                int count = intervalCount = intervalCount + 1;
                // if reach maximum interval, stop timer
                if (count >= 5) {
                    timerHandler.removeCallbacks(timerRunnable);
                    // Current time code
                    long dateInMillis = System.currentTimeMillis();
                    String formatTime = "HH:mm:ss";
                    // convert time
                    final SimpleDateFormat timeString = new SimpleDateFormat(formatTime);
                    final String time = timeString.format(new Date(dateInMillis));
                    // child table codes
                    myDB.updateRow(childID, "Completed");
                    // session update
                    mySessionDB.updateInCompleteSession(lastRowID.getInt(0), time, "Interval: 5", flag.getText().toString(), "Completed");
                    // add last interval row
                    myIntervalDB.insertRow("Interval: 5", engagement, adults, peers, materials, noneOther, childID, childName, sessionNo, "Null");
                    // text changes
                    status.setText("Current status: Completed");
                    interval.setText("Interval: " + (count) + " x 15s");
                    timerTextView.setText(("End of Session"));
                    tvTest.setText("");
                    tvTest.setBackgroundColor(0);
                    buttonStart.setText("End");
                    buttonStart.setEnabled(false);
                    flag.setEnabled(false);

                } else { // if haven't reach maximun interval, continue and add interval count

                    // The checkbox buttons
                    if (cbMaterials.isChecked())
                        materials = "Yes";
                    else
                        materials = "Null";
                    if (cbPeers.isChecked())
                        peers = "Yes";
                    else
                        peers = "Null";
                    if (cbAdults.isChecked())
                        adults = "Yes";
                    else
                        adults = "Null";
                    if (cbNoneOther.isChecked())
                        noneOther = "Yes";
                    else
                        noneOther = "Null";

                    interval.setText("Interval: " + (count));
                    mySessionDB.updateInCompleteSession(lastRowID.getInt(0), "SessionOnGoing", interval.getText().toString(), flag.getText().toString(), "Incomplete");
                    myIntervalDB.insertRow(interval.getText().toString(), engagement, adults, peers, materials, noneOther, childID, childName, sessionNo, "Null");

                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);

                    millisec = System.currentTimeMillis() - startTime;
                    updateTime = pasTime + millisec;
                    minutes = getTimeValues(updateTime)[0];
                    seconds = getTimeValues(updateTime)[1];

                    timerTextView.setText(String.format("%d:%02d", minutes, seconds));
                    tvTest.setText("");

                    alarm.vibrate(500);
                }
            }
        }
    };

    /*   ===================================End of timer code=====================================


        =========================================================================================*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_test);

        openDB();
        openSessionDB();
        openIntervalDB();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.home);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Timer_Test.this, ListView_Database.class);
                        Timer_Test.this.startActivity(intent);
                    }
                }
        );

        //=========================Initialize===================================

        timerTextView = (TextView) findViewById(R.id.timerView);
        tvTest = (TextView) findViewById(R.id.tvCapture);
        interval = (TextView) findViewById(R.id.tvIntervalCount);
        child = (TextView) findViewById(R.id.tvCurrentChild);
        status = (TextView) findViewById(R.id.tvStatus);
        id = (TextView) findViewById(R.id.tvId);
        session = (TextView) findViewById(R.id.tvSession);
        passName = (TextView) findViewById(R.id.tvPassChildName);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);

        buttonStart = (Button) findViewById(R.id.btnStart);
        timeButton = (Button) findViewById(R.id.btnStart);
        back = (Button) findViewById(R.id.btnBack);
        flag = (Button) findViewById(R.id.btnFlag);
        activeEn = (Button) findViewById(R.id.btnAE);
        activeNon = (Button) findViewById(R.id.btnANE);
        passiveEn = (Button) findViewById(R.id.btnPE);
        passiveNon = (Button) findViewById(R.id.btnPNE);

        cbAdults = (CheckBox) findViewById(R.id.cbAdults);
        cbPeers = (CheckBox) findViewById(R.id.cbPeers);
        cbMaterials = (CheckBox) findViewById(R.id.cbMaterials);
        cbNoneOther = (CheckBox) findViewById(R.id.cbNoneOther);

        flag.setEnabled(false);
        alarm = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //========================================================================

        timeButton.setText("Start");

        // Extract variable pass from previous activity
        childId = getIntent().getExtras().getString("childID");
        childName = getIntent().getExtras().getString("childName");

        childID = Long.parseLong(childId);
        final Long inID = Long.parseLong(childId);
        final Cursor cursor = myDB.getRow(inID);
        final Cursor cursor2 = mySessionDB.getRow(inID);

        // Child Table datas
        final String idRetrieve = cursor.getString(0);
        sessionNo = cursor.getInt(13);
        final String inspectorRetrieve = cursor.getString(6);
        final String venueRetrieve = cursor.getString(7);

        // set text
        id.setText(childId);
        passName.setText(childName);
        status.setText("Current status: " + cursor.getString(12));
        child.setText("Currently observing: " + childName);
        session.setText("Session Number: " + sessionNo);

        // The start button
        timeButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) { // Stop and reset interval
                        Button timeButton = (Button) v;
                        if (timeButton.getText().equals("Pause")) {

                            // date and time code
                            long dateInMillis = System.currentTimeMillis();
                            String formatDate = "dd-MM-yyyy";
                            String formatTime = "HH:mm:ss";
                            // convert date to format
                            final SimpleDateFormat dateString = new SimpleDateFormat(formatDate);
                            // convert time
                            final SimpleDateFormat timeString = new SimpleDateFormat(formatTime);
                            final String date = dateString.format(new Date(dateInMillis));
                            final String time = timeString.format(new Date(dateInMillis));

                            timerHandler.removeCallbacks(timerRunnable);
                            // update child table status

                            // update session table row
                            Cursor cursor = mySessionDB.getLastRow();
                            mySessionDB.updateInCompleteSession(cursor.getInt(0), time, interval.getText().toString(), flag.getText().toString(), "Incomplete");
                            // mySessionDB.updateRow(inID, "Completed", String.valueOf(sessionCount), "date");
                            status.setText("  Current status: Paused");
                            timeButton.setText("Reset");
                            //tvStartTime.setText("  Session stopped at: " + time);
                            flag.setEnabled(false);

                        } else if (timeButton.getText().equals("Start")) {

                            // date and time code
                            long dateInMillis = System.currentTimeMillis();
                            String formatDate = "dd-MM-yyyy";
                            String formatTime = "HH:mm:ss";
                            // convert date to format
                            final SimpleDateFormat dateString = new SimpleDateFormat(formatDate);
                            // convert time
                            final SimpleDateFormat timeString = new SimpleDateFormat(formatTime);
                            final String date = dateString.format(new Date(dateInMillis));
                            final String time = timeString.format(new Date(dateInMillis));

                            // start timer, create session
                            alarm.vibrate(500);
                            startTime = System.currentTimeMillis();
                            timerHandler.postDelayed(timerRunnable, 0);
                            flag.setEnabled(true);
                            // update child table status
                            myDB.updateSessionNo(inID, "Incomplete");
                            // create session row
                            mySessionDB.insertRow(idRetrieve, venueRetrieve, inspectorRetrieve, String.valueOf(sessionNo),
                                    date, time, "0", "0", "0", childName, "Incomplete");
                            status.setText("Current status: Observing");
                            timeButton.setText("Pause");
                            tvStartTime.setText("Start Time: " + time);
                            session.setText("Session number: " + sessionNo);

                        } else if (timeButton.getText().equals("Reset")) {
                            // start timer, update status and get new status
                            startTime = System.currentTimeMillis();
                            timerHandler.postDelayed(timerRunnable, 0);
                            timeButton.setText("Pause");
                            status.setText("Current status: Observing");
                            flag.setEnabled(true);
                        }
                    }
                });
        // Back button
        back.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Timer_Test.this, childDetails.class);
                        intent.putExtra("childID", childId);
                        Timer_Test.this.startActivity(intent);
                    }
                }
        );

        test = (Button) findViewById(R.id.btnTest);
        test.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        String message = "";
                        Cursor cursor = mySessionDB.getLastRow();

                                message += "ID: " + cursor.getInt(0)
                                        + "\n Child ID: " + cursor.getString(1)
                                        + "\n Session Number: " + cursor.getString(4)
                                        + "\n Date: " + cursor.getString(7)
                                        + "\n Start Time: " + cursor.getString(8)
                                        + "\n End Time: " + cursor.getString(9)
                                        + "\n No. of Intervals: " + cursor.getString(10)
                                        + "\n No. of flags: " + cursor.getString(11)
                                        + "\n Child name: " + cursor.getString(12)
                                        + "\n Session Status: " + cursor.getString(13)
                                        + "\n";

                        for (int i=0; i < 3; i++) {
                            Toast.makeText(Timer_Test.this, message, Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );


        // The engagement buttons
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(activeEn)) {
                    activeEn.setBackgroundColor(getResources().getColor(R.color.buttonPressed));
                    engagement = activeEn.getText().toString();
                    activeNon.setBackgroundColor(getResources().getColor(R.color.buttonNotPressed));
                    passiveEn.setBackgroundColor(getResources().getColor(R.color.buttonNotPressed));
                    passiveNon.setBackgroundColor(getResources().getColor(R.color.buttonNotPressed));
                }
                if (v.equals(activeNon)) {
                    activeNon.setBackgroundColor(getResources().getColor(R.color.buttonPressed));
                    engagement = activeNon.getText().toString();
                    activeEn.setBackgroundColor(getResources().getColor(R.color.buttonNotPressed));
                    passiveEn.setBackgroundColor(getResources().getColor(R.color.buttonNotPressed));
                    passiveNon.setBackgroundColor(getResources().getColor(R.color.buttonNotPressed));
                }
                if (v.equals(passiveEn)) {
                    passiveEn.setBackgroundColor(getResources().getColor(R.color.buttonPressed));
                    engagement = passiveEn.getText().toString();
                    activeEn.setBackgroundColor(getResources().getColor(R.color.buttonNotPressed));
                    activeNon.setBackgroundColor(getResources().getColor(R.color.buttonNotPressed));
                    passiveNon.setBackgroundColor(getResources().getColor(R.color.buttonNotPressed));
                }
                if (v.equals(passiveNon)) {
                    passiveNon.setBackgroundColor(getResources().getColor(R.color.buttonPressed));
                    engagement = passiveNon.getText().toString();
                    activeEn.setBackgroundColor(getResources().getColor(R.color.buttonNotPressed));
                    passiveEn.setBackgroundColor(getResources().getColor(R.color.buttonNotPressed));
                    activeNon.setBackgroundColor(getResources().getColor(R.color.buttonNotPressed));
                }
            }
        };

        activeEn.setOnClickListener(listener);
        activeNon.setOnClickListener(listener);
        passiveEn.setOnClickListener(listener);
        passiveNon.setOnClickListener(listener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        myDB = new DBAdapter(this);
        myDB.open();

    }

    public void openSessionDB() {
        mySessionDB = new SessionDBAdapter(this);
        mySessionDB.open();
    }

    public void openIntervalDB(){
        myIntervalDB = new IntervalDBAdapter(this);
        myIntervalDB.open();
    }

    public void closeDB() {
        myDB.close();
        mySessionDB.close();
        myIntervalDB.close();
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        Button b = (Button) findViewById(R.id.btnStart);
        b.setText("start");
    }

}
