package com.example.deosfriend.design;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import Controller.Child;
import Controller.Grade_Child;
import Controller.Interval;
import Controller.Message;
import Controller.Session;
import au.com.bytecode.opencsv.CSVWriter;
import database.DBAdapter;
import database.NewGradingDB;
import database.SessionDBAdapter;
import database.gradingDB;

/**
 * Created by Deo's Friend on 3/15/2015.
 */
public class childDetails extends ActionBarActivity {
    DBAdapter myDB;
    SessionDBAdapter mySessionDB;

    EditText childName, childGender, childPriDi, childSecDi, childRemarks, childInspector, childVenue, childActivity, childNoAdults, childNoChildren;
    TextView lbSessionId, lbChildId, lbSessionNo, lbTotalInterval, lbTotalFlags, lbDateTaken, lbTimeStarted, lbTimeEnded, lbSessionStatus, name;
    Button start;
    String childName4, childID;
    Button btnSubmit;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_details);

        childName = (EditText) findViewById(R.id.tbName);
        childGender = (EditText) findViewById(R.id.tbGender);
        childPriDi = (EditText) findViewById(R.id.tbPriDi);
        childSecDi = (EditText) findViewById(R.id.tbSecDi);
        childRemarks = (EditText) findViewById(R.id.tbRemarks);
        childInspector = (EditText) findViewById(R.id.tbInspector);
        childVenue = (EditText) findViewById(R.id.tbVenue);
        childActivity = (EditText) findViewById(R.id.tbActivity);
        childNoAdults = (EditText) findViewById(R.id.tbNoAdults);
        childNoChildren = (EditText) findViewById(R.id.tbNoChildren);

        lbSessionId = (TextView) findViewById(R.id.lbSessionId);
        lbChildId = (TextView) findViewById(R.id.lbChildId);
        lbSessionNo = (TextView) findViewById(R.id.lbSessionId);
        lbTotalInterval = (TextView) findViewById(R.id.lbTotalInterval);
        lbTotalFlags = (TextView) findViewById(R.id.lbTotalFlags);
        lbDateTaken = (TextView) findViewById(R.id.lbDate);
        lbTimeStarted = (TextView) findViewById(R.id.lbStart);
        lbTimeEnded = (TextView) findViewById(R.id.lbEnd);
        lbSessionStatus = (TextView) findViewById(R.id.lbStatus);

        start = (Button) findViewById(R.id.btnObserve);

        openDB();
        openSessionDB();

        childID = getIntent().getExtras().getString("idChild");
        Long inID = Long.parseLong(childID);

        Cursor cursor = myDB.getRow(inID);

        childName.setText("Name:     " + cursor.getString(1));
        childGender.setText("Gender:   " + cursor.getString(2));
        childPriDi.setText(cursor.getString(3));
        childSecDi.setText(cursor.getString(4));
        childRemarks.setText(cursor.getString(5));
        childInspector.setText(cursor.getString(6));
        childVenue.setText(cursor.getString(7));
        childActivity.setText(cursor.getString(8));
        childNoAdults.setText("No. of Adults:     " + cursor.getString(9));
        childNoChildren.setText("No. of Children:  " + cursor.getString(10));

        childName4 = cursor.getString(1);

        start.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent myIntent = new Intent(childDetails.this, Timer_Test.class);
                        myIntent.putExtra("childID", childID);
                        myIntent.putExtra("childName", childName4);
                        childDetails.this.startActivity(myIntent);
                    }
                }
        );
        btnSubmit = (Button) findViewById(R.id.button2);
        name = (TextView) findViewById(R.id.tbChildName);
    }
    // ============ Database =================
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        myDB = new DBAdapter(this);
        myDB.open();
    }

    private void openSessionDB() {
        mySessionDB = new SessionDBAdapter(this);
        mySessionDB.open();
    }

    public void closeDB() {
        myDB.close();
        mySessionDB.close();
    }

    public void onClick_Back(View view) {
        Intent myIntent = new Intent(childDetails.this, ListView_Database.class);
        childDetails.this.startActivity(myIntent);
    }
}