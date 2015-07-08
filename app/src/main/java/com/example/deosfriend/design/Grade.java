package com.example.deosfriend.design;

import android.app.Activity;
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
import java.lang.reflect.GenericArrayType;

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


public class Grade extends Activity {

    String array[] = new String[] {"child_Id", "name", "qns1", "qns2", "qns3", "qns4", "qns5", "id"};
    DBAdapter myDB;
    gradingDB gradeDB;
    NewGradingDB myNewGradingDB;
    SessionDBAdapter mySessionDB;
    Toolbar toolbar;
    String childName1;
    EditText childName, childGender, childPriDi, childSecDi, childRemarks, childInspector, childVenue, childActivity, childNoAdults, childNoChildren;
    TextView lbSessionId, lbChildId, lbSessionNo, lbTotalInterval, lbTotalFlags, lbDateTaken, lbTimeStarted, lbTimeEnded, lbSessionStatus;
    Button start;
    String childID;
    TextView RSQ11, RSQ22, RSQ33, RSQ44, RSQ55, name, Childid;
    Button btnSubmit, ExportBTN;
    private RatingBar RSQ1, RSQ2, RSQ3, RSQ4, RSQ5;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade);

        myNewGradingDB = new NewGradingDB(getApplicationContext());

//        // ================ Action bar codes ================
//        toolbar = (Toolbar) findViewById(R.id.app_bar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        toolbar.setNavigationIcon(R.drawable.home);
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(Grade.this, ListView_Database.class);
//                        Grade.this.startActivity(intent);
//                    }
//                }
//        );
//        // ===================================================

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
        name = (TextView) findViewById(R.id.tbChildName);
        Childid = (TextView) findViewById(R.id.tbQuestionIDCreate);
      //  ExportBTN = (Button) findViewById(R.id.btnExport);
        RSQ1 = (RatingBar) findViewById(R.id.Qns1_RS);
        RSQ2 = (RatingBar) findViewById(R.id.Qns2_RS);
        RSQ3 = (RatingBar) findViewById(R.id.Qns3_RS);
        RSQ4 = (RatingBar) findViewById(R.id.Qns4_RS);
        RSQ5 = (RatingBar) findViewById(R.id.Qns5_RS);
        btnSubmit = (Button) findViewById(R.id.button2);
        RSQ11 = (TextView) findViewById(R.id.textView6);
        RSQ22 = (TextView) findViewById(R.id.textView7);
        RSQ33 = (TextView) findViewById(R.id.textView8);
        RSQ44 = (TextView) findViewById(R.id.textView9);
        RSQ55 = (TextView) findViewById(R.id.textView10);
        start = (Button) findViewById(R.id.btnObserve);

        openDB();
        openGradeDB();
        openSessionDB();

        childID = getIntent().getExtras().getString("idChild");
        Long inID = Long.parseLong(childID);

        Cursor cursor = myDB.getRow(inID);
        name.setText(cursor.getString(1));
        childName1 = cursor.getString(1);

        RSQ1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                RSQ11.setText(String.valueOf(rating));
            }
        });
        RSQ2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                RSQ22.setText(String.valueOf(rating));
            }
        });
        RSQ3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                RSQ33.setText(String.valueOf(rating));
            }
        });
        RSQ4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                RSQ44.setText(String.valueOf(rating));
            }
        });
        RSQ5.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                RSQ55.setText(String.valueOf(rating));
            }
        });

        Childid.setText(childID);
        String ChildrenID = childID;
        if (CheckIfExist(childID)) {
            Childid.setText(childID );

            Cursor c = getRow1(ChildrenID, array);
            RSQ11.setText(c.getString(2));
            RSQ22.setText(c.getString(3));
            RSQ33.setText(c.getString(4));
            RSQ44.setText(c.getString(5));
            RSQ55.setText(c.getString(6));

            RSQ1.setRating(Float.parseFloat(c.getString(2)));
            RSQ2.setRating(Float.parseFloat(c.getString(3)));
            RSQ3.setRating(Float.parseFloat(c.getString(4)));
            RSQ4.setRating(Float.parseFloat(c.getString(5)));
            RSQ5.setRating(Float.parseFloat(c.getString(6)));
        }
        else{

        }

    }

    public void OnClick_Submit(View v) {
        //intent
        String ChildrenName = getIntent().getExtras().getString("Name");
        String ChildrenID = childID;
        //int intChildrenID = Integer.parseInt(ChildrenID);
        if (CheckIfExist(ChildrenID)) {
            String qns1 = RSQ11.getText().toString();
            String qns2 = RSQ22.getText().toString();
            String qns3 = RSQ33.getText().toString();
            String qns4 = RSQ44.getText().toString();
            String qns5 = RSQ55.getText().toString();

            String updateArray[]= new String[]{qns1, qns2, qns3, qns4, qns5};
            updateRecord(ChildrenID, updateArray);
            Toast.makeText(Grade.this, "Update Successful!", Toast.LENGTH_SHORT).show();

            if (CheckIfExist(ChildrenID)) {
                String ChildName = name.getText().toString();
                Cursor c = getRow1(ChildrenID, array);
                RSQ11.setText(c.getString(2));
                RSQ22.setText(c.getString(3));
                RSQ33.setText(c.getString(4));
                RSQ44.setText(c.getString(5));
                RSQ55.setText(c.getString(6));

                RSQ1.setRating(Float.parseFloat(c.getString(2)));
                RSQ2.setRating(Float.parseFloat(c.getString(3)));
                RSQ3.setRating(Float.parseFloat(c.getString(4)));
                RSQ4.setRating(Float.parseFloat(c.getString(5)));
                RSQ5.setRating(Float.parseFloat(c.getString(6)));
            }
        }
        else{
            String Child_id = childID;
            String nameChild = name.getText().toString();
            String qns1 = RSQ11.getText().toString();
            String qns2 = RSQ22.getText().toString();
            String qns3 = RSQ33.getText().toString();
            String qns4 = RSQ44.getText().toString();
            String qns5 = RSQ55.getText().toString();

            if (qns1.isEmpty()||qns2.isEmpty()||qns3.isEmpty()||qns4.isEmpty()||qns5.isEmpty())
                Toast.makeText(getApplicationContext(), "Please fill in all the fields", Toast.LENGTH_LONG).show();
            else{

                float FRQ1 = Float.parseFloat(qns1);
                float FRQ2 = Float.parseFloat(qns2);
                float FRQ3 = Float.parseFloat(qns3);
                float FRQ4 = Float.parseFloat(qns4);
                float FRQ5 = Float.parseFloat(qns5);

                Grade_Child newGrade = new Grade_Child(nameChild, Child_id ,FRQ1, FRQ2, FRQ3, FRQ4, FRQ5);
                int status = myNewGradingDB.addPersonData(newGrade);
                if (status == 1){
                    Toast.makeText(getApplicationContext(), "Data inserted successfully", Toast.LENGTH_LONG).show();
                }
            }
        }

        Intent intent = new Intent(Grade.this, Tabs.class);
        intent.putExtra("Result", "test2");
        intent.putExtra("childID", childID);
        intent.putExtra("childNAME", childName1);
        Grade.this.startActivity(intent);
    }

    // method to check if child's id exist in DB
    public boolean CheckIfExist(String childid){
        SQLiteDatabase db = myNewGradingDB.getReadableDatabase();
        int count = -1;
        Cursor c = null;
        try {
            String query = "SELECT COUNT(*) FROM grade_child WHERE child_Id = ?";
            c = db.rawQuery(query, new String[] {childid});
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            return count > 0;
        }
        finally {
            if (c != null) {
                c.close();
            }
        }
    }

    // retrieve all keys from child's id in database
    public Cursor getRow1(String ChildrenID, String ALL_KEYS[]) {
        SQLiteDatabase db = myNewGradingDB.getReadableDatabase();
        String where = "child_Id = '" + ChildrenID + "'";
        Cursor c = 	db.query(true, "grade_child", ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // update record
    public void updateRecord(String ChildrenID, String updateArray[]){
        SQLiteDatabase db = myNewGradingDB.getReadableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put("qns1", updateArray[0]);
        newValues.put("qns2", updateArray[1]);
        newValues.put("qns3", updateArray[2]);
        newValues.put("qns4", updateArray[3]);
        newValues.put("qns5", updateArray[4]);
        db.update("grade_child", newValues ," child_Id = '" + ChildrenID + "'", null);
    }


    private void openGradeDB() {
        gradeDB = new gradingDB(this);
        gradeDB.open();
    }

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
        Intent myIntent = new Intent(Grade.this, ListView_Database.class);
        Grade.this.startActivity(myIntent);
    }
}
