package com.example.deosfriend.design;

import android.app.TabActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import database.NewGradingDB;
import database.SessionDBAdapter;

public class Tabs extends TabActivity {

    String childID, condition;
    Toolbar toolbar;
    SessionDBAdapter mySessionDB;
    NewGradingDB myNewGradingDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        myNewGradingDB = new NewGradingDB(getApplicationContext());
        openSessionDB();

        TabHost tabHost = getTabHost();

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        toolbar.setNavigationIcon(R.drawable.home);
        toolbar.setTitle("Child Details");
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Tabs.this, ListView_Database.class);
                        Tabs.this.startActivity(intent);
                    }
                }
        );

        childID = getIntent().getExtras().getString("childID");
        String childrenName = getIntent().getExtras().getString("childNAME");


//        TextView textTabDETAILS = new TextView(this);
//        textTabDETAILS.setText("Details");
//        textTabDETAILS.setTextSize(20);
//        textTabDETAILS.setTextColor(Color.BLACK);
//        textTabDETAILS.setTypeface(null, Typeface.BOLD);
//        textTabDETAILS.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        TabSpec Detailspec = tabHost.newTabSpec("Details");
        Detailspec.setIndicator("Details");
//        Detailspec.setIndicator(textTabDETAILS);
        Intent detailsIntent = new Intent(this, childDetails.class);
        detailsIntent.putExtra("idChild",childID );
        Detailspec.setContent(detailsIntent);


//        TextView textTabGRADE = new TextView(this);
//        textTabGRADE.setText("Grade");
//        textTabGRADE.setTextSize(20);
//        textTabGRADE.setTextColor(Color.BLACK);
//        textTabGRADE.setTypeface(null, Typeface.BOLD);
//        textTabGRADE.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        TabSpec Gradespec = tabHost.newTabSpec("Grade");
        Gradespec.setIndicator("Grade");
        Intent gradeIntent = new Intent(this, Grade.class);
        gradeIntent.putExtra("idChild",childID );
        Gradespec.setContent(gradeIntent);

//        TextView textTabRESULT = new TextView(this);
//        textTabRESULT.setText("Result");
//        textTabRESULT.setTextSize(20);
//        textTabRESULT.setTextColor(Color.BLACK);
//        textTabRESULT.setTypeface(null, Typeface.BOLD);
//        textTabRESULT.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        TabSpec Resultspec = tabHost.newTabSpec("Result");
        Resultspec.setIndicator("Result");
        Intent resultIntent = new Intent(this, Result.class);
        resultIntent.putExtra("idChild",childID );
        resultIntent.putExtra("childNAME",childrenName );
        Resultspec.setContent(resultIntent);


        // Adding all TabSpec to TabHost
        tabHost.addTab(Detailspec);
        //tabHost.addTab(Gradespec);


            tabHost.addTab(Gradespec);


        if ( CheckIfExist(childID)){
            tabHost.addTab(Resultspec);
        }

        if (getIntent().getExtras() != null) {
            condition = getIntent().getExtras().getString("Grade");
            if (condition != null) {
                if (condition.equals("test"))
                    tabHost.setCurrentTab(1);
            }
        }

        if (getIntent().getExtras() != null) {
            condition = getIntent().getExtras().getString("Result");
            if (condition != null) {
                if (condition.equals("test2"))
                    tabHost.setCurrentTab(2);
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    public void openSessionDB() {
        mySessionDB = new SessionDBAdapter(this);
        mySessionDB.open();
    }

    public void closeDB() {
        mySessionDB.close();
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

}