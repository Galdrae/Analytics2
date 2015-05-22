package com.example.deosfriend.design;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import Controller.Interval;
import Controller.Session;
import database.DBAdapter;
import database.IntervalDBAdapter;
import database.SessionDBAdapter;
import database.NewGradingDB;

/**
 * Created by L335A15 on 3/17/2015.
 */
public class Timer_Test extends ActionBarActivity {

    Button buttonStart, flag, back, timeButton, activeEn, activeNon, passiveEn, passiveNon, test;
    TextView timerTextView, tvTest, interval, child, status, id, session, passName, tvStartTime;
    CheckBox cbAdults, cbPeers, cbMaterials, cbNoneOther, cbPhysicalPrompt;
    EditText tbPhysicalPrompt;

    DBAdapter myDB;
    SessionDBAdapter mySessionDB;
    IntervalDBAdapter myIntervalDB;
    NewGradingDB myNewGradingDB;

    Cursor lastRowID;

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

    private int intervalCount = 1;
    private int flagCount = 0;
    private String engagement = "", physicalPrompt = "";
    private String adults = "Null", peers = "Null", materials = "Null", noneOther = "Null";
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

            if (cbPhysicalPrompt.isChecked()) {
                physicalPrompt = "Checked";
                cbPhysicalPrompt.setChecked(true);
            } else {
                physicalPrompt = "null";
                cbPhysicalPrompt.setChecked(false);
            }

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

                            mySessionDB.updateInCompleteSession(childID, "SessionOnGoing", interval.getText().toString(), "Flag Count: " + flagCount, "Incomplete");

                            if (flagCount < 9) { // resets current interval and add a flag count
                                flagCount = flagCount + 1;
                                flag.setText("Flag Count: " + flagCount);
                                startTime = System.currentTimeMillis();
                                timerHandler.removeCallbacks(timerRunnable);
                                timeButton.setText("Reset");
                                myIntervalDB.insertRow(interval.getText().toString(), engagement, physicalPrompt, adults, peers, materials, noneOther, childID, childName, sessionNo, "Flagged!");

                                // ===================== insert into interval (export)*** ========================
                                String childrenIDStr = Long.toString(childID);
                                String sessionNoStr = String.valueOf(sessionNo);
                                Interval newInterval = new Interval(childrenIDStr, interval.getText().toString(), engagement, physicalPrompt, adults, peers, materials, noneOther, childName, sessionNoStr, "Flagged!");
                                int status = myNewGradingDB.addPersonDataInterval(newInterval);
                                if (status == 1) {
//                                    Toast.makeText(getApplicationContext(), "-flag count less then 9- inserted successfully", Toast.LENGTH_LONG).show();
                                }
                                //

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

                                // ===================== update into child (export)*** ========================
                                String childrenIDStr = Long.toString(childID);
                                Toast.makeText(Timer_Test.this, childId, Toast.LENGTH_SHORT).show();
                                String statusUpdate = "Fail";
                                String updateChildArray[] = new String[]{statusUpdate};
                                updateChildRecord(childrenIDStr, updateChildArray);
                                Toast.makeText(Timer_Test.this, "Can", Toast.LENGTH_SHORT).show();
                                //

                                mySessionDB.updateInCompleteSession(childID, time, interval.getText().toString(), "Flag Count: 10", "Fail");

                                // ===================== update session (export)*** =============================
                                String noOfInterval = interval.getText().toString();
                                String endTimeStr = time;
                                String flagCount = "Flag Count: 10";
                                String statustr = "Fail";
                                String updateArray[] = new String[]{endTimeStr, noOfInterval, flagCount, statustr};
                                updateSessionRecord(childrenIDStr, updateArray);
                                //Toast.makeText(Timer_Test.this, "Update session -flag count 10- Successful!", Toast.LENGTH_SHORT).show();
                                //

                                // insert flagged row to interval table
                                myIntervalDB.insertRow(interval.getText().toString(), engagement, physicalPrompt, adults, peers, materials, noneOther, childID, childName, sessionNo, "Flagged!");
                                //  text changes
                                status.setText("Current status: Fail!");
                                flag.setText("Flag Count: " + 10);
                                buttonStart.setText("End");
                                timerTextView.setText(("End of Session"));
                                tvTest.setText("Flag count reaches 10. Session ended!");
                                buttonStart.setEnabled(false);
                                flag.setEnabled(false);

                                // ===================== insert into interval (export)*** ========================
                                String sessionNoStr = String.valueOf(sessionNo);
                                //Interval newInterval = new Interval(interval.getText().toString(), engagement, physicalPrompt, adults, peers, materials, noneOther, childrenIDStr, childName, sessionNoStr, "Flagged!");
                                Interval newInterval = new Interval(childrenIDStr, interval.getText().toString(), engagement, physicalPrompt, adults, peers, materials, noneOther, childName, sessionNoStr, "Flagged!");
                                int status = myNewGradingDB.addPersonDataInterval(newInterval);
                                if (status == 1) {
                                    // Toast.makeText(getApplicationContext(), "interval inserted successfully", Toast.LENGTH_LONG).show();
                                }
                                //

                            }
                        }
                    }
            );

            //  if reach 15s of the interval
            if (updateTime >= fithteenSecsLimit) {

                alarm.vibrate(500);

                int count = intervalCount = intervalCount + 1;
                // if reach maximum interval, stop timer
                if (count >= 6) {
                    timerHandler.removeCallbacks(timerRunnable);
                    // Current time code
                    long dateInMillis = System.currentTimeMillis();
                    String formatTime = "HH:mm:ss";
                    // convert time
                    final SimpleDateFormat timeString = new SimpleDateFormat(formatTime);
                    final String time = timeString.format(new Date(dateInMillis));
                    // child table codes
                    myDB.updateRow(childID, "Completed");

                    // ===================== update into child (export)*** ========================
                    String childrenIDStr = Long.toString(childID);
                    String statusUpdate = "Completed";
                    String updateChildArray[] = new String[]{statusUpdate};
                    updateChildRecord(childrenIDStr, updateChildArray);
                    //

                    // session update
                    mySessionDB.updateInCompleteSession(childID, time, "Interval: 5", flag.getText().toString(), "Completed");
                    // ===================== update into session (export)*** ========================
                    //String id_children = lastRowID.getString(0);
                    String noOfInterval = "Interval: 5";
                    String endTimeStr = time;
                    String flagCount = flag.getText().toString();
                    String statustr = "Completed";
                    String updateArray[] = new String[]{endTimeStr, noOfInterval, flagCount, statustr};
                    updateSessionRecord(childrenIDStr, updateArray);
                    // Toast.makeText(Timer_Test.this, "Update session -complete- status Successful!", Toast.LENGTH_SHORT).show();
                    //

                    // add last interval row
                    myIntervalDB.insertRow("Interval: 5", engagement, physicalPrompt, adults, peers, materials, noneOther, childID, childName, sessionNo, "Null");

                    // text changes
                    status.setText("Current status: Completed");
                    interval.setText("Interval: " + "5" + " x 15s");
                    timerTextView.setText(("End of Session"));
                    tvTest.setText("");
                    tvTest.setBackgroundColor(0);
                    buttonStart.setText("Grade");
                    flag.setEnabled(false);
                    // myIntervalDB.deleteAll();
                    // mySessionDB.deleteAll();

                    // ===================== insert into interval (export)*** ========================
                    String sessionNoStr = String.valueOf(sessionNo);
                    Interval newInterval = new Interval(childrenIDStr, "Interval: 5", engagement, physicalPrompt, adults, peers, materials, noneOther, childName, sessionNoStr, "Null");
                    int status = myNewGradingDB.addPersonDataInterval(newInterval);
                    if (status == 1) {
                        //  Toast.makeText(getApplicationContext(), "interval5- inserted successfully", Toast.LENGTH_LONG).show();
                    }
                    //

                } else { // if haven't reach maximun interval, continue and add interval count

                    mySessionDB.updateInCompleteSession(childID, "SessionOnGoing", interval.getText().toString(), flag.getText().toString(), "Incomplete");

                    // ===================== update into session (export)*** ========================
                    //String id_children = lastRowID.getString(0);
                    String childrenIDStr = Long.toString(childID);
                    String noOfInterval = interval.getText().toString();
                    String endTimeStr = "SessionOnGoing";
                    String flagCount = flag.getText().toString();
                    String statustr = "Incomplete";
                    String updateArray[] = new String[]{endTimeStr, noOfInterval, flagCount, statustr};
                    updateSessionRecord(childrenIDStr, updateArray);
                    //   Toast.makeText(Timer_Test.this, "Update session -incomplete- status Successful!", Toast.LENGTH_SHORT).show();

                    myIntervalDB.insertRow(interval.getText().toString(), engagement, physicalPrompt, adults, peers, materials, noneOther, childID, childName, sessionNo, "Null");

                    // ===================== insert into interval (export)*** ========================
                    String sessionNoStr = String.valueOf(sessionNo);
                    Interval newInterval = new Interval(childrenIDStr, interval.getText().toString(), engagement, physicalPrompt, adults, peers, materials, noneOther, childName, sessionNoStr, "Null");
                    int status = myNewGradingDB.addPersonDataInterval(newInterval);
                    if (status == 1) {
                        //    Toast.makeText(getApplicationContext(), "interval inserted successfully", Toast.LENGTH_LONG).show();
                    }

                    interval.setText("Interval: " + (count));
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
        myNewGradingDB = new NewGradingDB(getApplicationContext());

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
        session = (TextView) findViewById(R.id.tvSession);
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
        cbPhysicalPrompt = (CheckBox) findViewById(R.id.cbPhysicalPrompt);

        tbPhysicalPrompt = (EditText) findViewById(R.id.tbPhysicalPrompt);

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
        lastRowID = mySessionDB.getLastRow();

        // Child Table datas
        final String idRetrieve = cursor.getString(0);
        final String inspectorRetrieve = cursor.getString(6);
        final String venueRetrieve = cursor.getString(7);
        sessionNo = cursor.getInt(13);

        // set text
        status.setText("Current status: " + cursor.getString(12));
        child.setText("Currently observing: " + childName);
        session.setText("Session Number: " + sessionNo);

        // retrieve previous session details
        final Cursor sessionCursor = mySessionDB.getChildSession(childId);
        if (mySessionDB.checkExist(childId)) {

            flag.setText(sessionCursor.getString(9));

            String substring = "";
            String substring2 = "";

            if (sessionCursor.getString(8).length() == 11) {
                substring = sessionCursor.getString(8).substring(10, 11);
            }
            else if (sessionCursor.getString(8).length() == 12) {
                substring = sessionCursor.getString(8).substring(10, 12);
            }

            if (sessionCursor.getString(9).length() == 13) {
                substring2 = sessionCursor.getString(9).substring(12, 13);
            }
            else if (sessionCursor.getString(9).length() == 14) {
                substring2 = sessionCursor.getString(9).substring(12, 14);
            }

            int newInterval = 0;
            if(substring.length()>0) {
                 newInterval = (Integer.parseInt(substring) + 1);
            }

            if(substring2.length()>0) {
                flagCount = Integer.parseInt(substring2);
            }

            intervalCount = newInterval;
            interval.setText("Interval: " + String.valueOf(intervalCount));
            tvStartTime.setText("Start Time: " + sessionCursor.getString(6) + " | " + sessionCursor.getString(5));
          //  interval.setText(String.valueOf(newInterval));
        }
        // The start button
        timeButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) { // Stop and reset interval
                        Button timeButton = (Button) v;
                        if (timeButton.getText().equals("Not in use")) { // this code portion not in use anymore

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

                            // ===================== update into session (export)*** ========================
                            //String id_children = lastRowID.getString(0);
                            String childrenIDStr = Long.toString(childID);
                            String noOfInterval = interval.getText().toString();
                            String endTimeStr = time;
                            String flagCount = flag.getText().toString();
                            String statustr = "Incomplete";
                            String updateArray[] = new String[]{endTimeStr, noOfInterval, flagCount, statustr};
                            updateSessionRecord(childrenIDStr, updateArray);
                            //  Toast.makeText(Timer_Test.this, "Update session -incomplete- Successful!", Toast.LENGTH_SHORT).show();
                            //

                            // mySessionDB.updateRow(inID, "Completed", String.valueOf(sessionCount), "date");
                            status.setText("Current status: Paused");
                            timeButton.setText("Reset");
                            //tvStartTime.setText("  Session stopped at: " + time);
                            flag.setEnabled(false);

                        } else if (timeButton.getText().equals("Start")) {

                            alarm.vibrate(500);

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
                            // ===================== update into child (export)*** ========================
                            String childrenIDStr = Long.toString(childID);
                            String statusUpdate = "Incomplete";
                            String updateChildArray[] = new String[]{statusUpdate};
                            updateChildRecord(childrenIDStr, updateChildArray);

                            tvStartTime.setText("Start Time: " + time + " | " +  date);
                            if ( mySessionDB.checkExist(childId)) {
                                tvStartTime.setText("Start Time: " + sessionCursor.getString(6) + " | " + sessionCursor.getString(5));
                            }
                            else {
                                tvStartTime.setText("Start Time: " + time + " | " +  date);
                            }

                            // create session row
                            if (mySessionDB.checkExist(childId) && CheckIfExistSession(childId)) {
                                mySessionDB.updateInCompleteSession(cursor.getInt(0), time, interval.getText().toString(), flag.getText().toString(), "Incomplete");

                                // ===================== update into session (export)*** ========================
                                String noOfInterval = interval.getText().toString();
                                String endTimeStr = time;
                                String flagCount = flag.getText().toString();
                                String statustr = "Incomplete";
                                String updateArray[] = new String[]{endTimeStr, noOfInterval, flagCount, statustr};
                                updateSessionRecord(childrenIDStr, updateArray);
                                //    Toast.makeText(Timer_Test.this, "Update session -incomplete- status Successful!", Toast.LENGTH_SHORT).show();
                                //

                            } else {
                                mySessionDB.insertRow(idRetrieve, venueRetrieve, inspectorRetrieve, String.valueOf(sessionNo),
                                        date, time, "0", "Interval: 0", "Flag Count: 0", childName, "Incomplete");

                                // ===================== insert into session (export)*** ========================
                                Session newSession = new Session(venueRetrieve, idRetrieve, inspectorRetrieve, String.valueOf(sessionNo),
                                        date, time, "0", "0", "0", childName, "Incomplete");
                                int status = myNewGradingDB.addPersonDataSession(newSession);
                                if (status == 1) {
                                    //       Toast.makeText(getApplicationContext(), "new session inserted successfully", Toast.LENGTH_LONG).show();
                                }
                            }

                            status.setText("Current status: Observing");
                            session.setText("Session number: " + sessionNo);
                            interval.setText("Interval: " + intervalCount);
                          // timeButton.setVisibility(View.GONE);
                          // flag.setVisibility(View.VISIBLE);

                        } else if (timeButton.getText().equals("Reset")) {
                            // start timer, update status and get new status
                            startTime = System.currentTimeMillis();
                            timerHandler.postDelayed(timerRunnable, 0);
                         //   timeButton.setText("Pause");
                            status.setText("Current status: Observing");
                            flag.setEnabled(true);
                        } else if (timeButton.getText().equals("Grade")) {
                            Intent intent = new Intent(Timer_Test.this, Tabs.class);
                            intent.putExtra("Grade", "test");
                            intent.putExtra("childID", childId);
                            Timer_Test.this.startActivity(intent);
                        } else if (timeButton.getText().equals("Resume")) {
                            timerHandler.postDelayed(timerRunnable, 0);
                            timeButton.setText("Pause");
                        }
                    }
                });
        // Back button
        back.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Timer_Test.this, Tabs.class);
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

                        for (int i = 0; i < 3; i++) {
                            Toast.makeText(Timer_Test.this, message, Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        final Drawable d = getResources().getDrawable(R.drawable.button_selector);
        // The engagement buttons
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(activeEn)) {
                    activeEn.setBackgroundColor(getResources().getColor(R.color.buttonPressed));
                    engagement = activeEn.getText().toString();
                    activeNon.setBackgroundResource(R.drawable.button_selector);
                    passiveEn.setBackgroundResource(R.drawable.button_selector);
                    passiveNon.setBackgroundResource(R.drawable.button_selector);
                }
                if (v.equals(activeNon)) {
                    activeNon.setBackgroundColor(getResources().getColor(R.color.buttonPressed));
                    engagement = activeNon.getText().toString();
                    activeEn.setBackgroundResource(R.drawable.button_selector);
                    passiveEn.setBackgroundResource(R.drawable.button_selector);
                    passiveNon.setBackgroundResource(R.drawable.button_selector);
                }
                if (v.equals(passiveEn)) {
                    passiveEn.setBackgroundColor(getResources().getColor(R.color.buttonPressed));
                    engagement = passiveEn.getText().toString();
                    activeEn.setBackgroundResource(R.drawable.button_selector);
                    activeNon.setBackgroundResource(R.drawable.button_selector);
                    passiveNon.setBackgroundResource(R.drawable.button_selector);
                }
                if (v.equals(passiveNon)) {
                    passiveNon.setBackgroundColor(getResources().getColor(R.color.buttonPressed));
                    engagement = passiveNon.getText().toString();
                    activeEn.setBackgroundResource(R.drawable.button_selector);
                    passiveEn.setBackgroundResource(R.drawable.button_selector);
                    activeNon.setBackgroundResource(R.drawable.button_selector);
                }
            }
        };

        activeEn.setOnClickListener(listener);
        activeNon.setOnClickListener(listener);
        passiveEn.setOnClickListener(listener);
        passiveNon.setOnClickListener(listener);

    }

    public void onClick_cbPhysical(View v) {
        if (cbPhysicalPrompt.isChecked()) {
            physicalPrompt = "Checked";
            cbPhysicalPrompt.setChecked(true);
        } else {
            physicalPrompt = "null";
            cbPhysicalPrompt.setChecked(false);
        }
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

    public void openIntervalDB() {
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

//*************************************************************************************************************************************************************************
    // update record
//    public void updateChildRecord(String ChildrenID, String status){
//        SQLiteDatabase db = myNewGradingDB.getReadableDatabase();
//        ContentValues newValues = new ContentValues();
//        newValues.put("status", "test");
//        db.update("child", newValues ," id = '" + ChildrenID + "'", null);
//    }

    public void updateChildRecord(String ChildrenID, String updateChildArray[]) {
        SQLiteDatabase db = myNewGradingDB.getReadableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put("status", updateChildArray[0]);
        db.update("child", newValues, " id = '" + ChildrenID + "'", null);
    }

    public void updateSessionRecord(String ChildrenID, String updateSessionArray[]) {
        SQLiteDatabase db = myNewGradingDB.getReadableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put("endTime", updateSessionArray[0]);
        newValues.put("noInterval", updateSessionArray[1]);
        newValues.put("noFlags", updateSessionArray[2]);
        newValues.put("sessionStatus", updateSessionArray[3]);
        db.update("session", newValues, " child_Id = '" + ChildrenID + "'", null);
    }

    public boolean CheckIfExistSession(String childid) {
        SQLiteDatabase db = myNewGradingDB.getReadableDatabase();
        int count = -1;
        Cursor c = null;
        try {
            String query = "SELECT COUNT(*) FROM session WHERE child_Id = ?";
            c = db.rawQuery(query, new String[]{childid});
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            return count > 0;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
}
