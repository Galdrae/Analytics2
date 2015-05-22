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
import java.text.SimpleDateFormat;
import java.util.Date;

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

    SessionDBAdapter mySessionDB;
    EditText childName, childGender, childPriDi, childSecDi, childRemarks, childInspector, childVenue, childActivity, childNoAdults, childNoChildren;
    TextView lbSessionId, lbChildId, lbSessionNo, lbTotalInterval, lbTotalFlags, lbDateTaken, lbTimeStarted, lbTimeEnded, lbSessionStatus, name;
    Button start, Export;
    String childName4, childID;
    Button btnSubmit;
    Toolbar toolbar;
    private static final String TAG = "Grade";
    File file = null;
    String array[] = new String[]{"child_Id", "name", "qns1", "qns2", "qns3", "qns4", "qns5", "id"};
    DBAdapter myDB;
    gradingDB gradeDB;
    NewGradingDB myNewGradingDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_details);

        childName = (EditText) findViewById(R.id.tbName);
        Export = (Button) findViewById(R.id.btn_Export);
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
        myNewGradingDB = new NewGradingDB(getApplicationContext());

        openDB();
        openSessionDB();

        childID = getIntent().getExtras().getString("idChild");
        Long inID = Long.parseLong(childID);

        Cursor cursor = myDB.getRow(inID);
        Cursor sessionCursor = mySessionDB.getChildSession(childID);

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

        if ( mySessionDB.checkExist(childID))
        if ( sessionCursor.getString(11).equals("Completed") || sessionCursor.getString(11).equals("Fail") ) {
            start.setVisibility(View.GONE);
            Export.setVisibility(View.VISIBLE);
        }

        start.setText("Proceed");
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

    public void OnClick_Export(View v) {
        ExportDatabaseCSVTask task = new ExportDatabaseCSVTask();
        task.execute();
    }

    private class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(childDetails.this);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage("Exporting database...");
            this.dialog.show();

        }

        protected Boolean doInBackground(final String... args) {

            File dbFile = getDatabasePath("newgrading.db");
            Log.v(TAG, "Db path is: " + dbFile);  //get the path of db

            File exportDir = new File(Environment.getExternalStorageDirectory(), "");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            // date and time code
            long dateInMillis = System.currentTimeMillis();
            String formatDate = "dd-MM-yyyy";
            String formatTime = "HH-mm-ss";
            // convert date to format
            final SimpleDateFormat dateString = new SimpleDateFormat(formatDate);
            // convert time
            final SimpleDateFormat timeString = new SimpleDateFormat(formatTime);
            final String date = dateString.format(new Date(dateInMillis));
            final String time = timeString.format(new Date(dateInMillis));

            file = new File(exportDir, "" + childName4 + " (D)" + date + " (T)" + time + " Details.csv");
            try {

                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));


                // sqlite core query
                SQLiteDatabase db = myNewGradingDB.getReadableDatabase();
                //Cursor curCSV=mydb.rawQuery("select * from " + TableName_ans,null);

                Cursor curCSV3 = db.rawQuery("SELECT * FROM child WHERE id = ?", new String[]{childID});
                csvWrite.writeNext("Name", "Gender", "SessionNo", "Inspector", "Remarks", "Primary Diagnosis", "Secondary Diagnosis", "Activity", "No of adults", "No of children", "Session Status", "Venue");
                while (curCSV3.moveToNext()) {
                    String arrStr[] = {
                            curCSV3.getString(3), curCSV3.getString(4),
                            curCSV3.getString(11),curCSV3.getString(7),
                            curCSV3.getString(10),curCSV3.getString(0),
                            curCSV3.getString(1), curCSV3.getString(2),
                            curCSV3.getString(8), curCSV3.getString(9),
                            curCSV3.getString(6), curCSV3.getString(5)};
                    csvWrite.writeNext(arrStr);
                }

                csvWrite.writeNext();

                Cursor curCSV4 = db.rawQuery("SELECT * FROM interval WHERE child_id = ?", new String[]{childID});
                csvWrite.writeNext("Name", "Child_id", "SessionNo", "Flag", "engagement", "Interval", "Adult", "Peer", "Material", "None Other", "Physical");
                while (curCSV4.moveToNext()) {
                    String arrStr[] = {
                            curCSV4.getString(1), curCSV4.getString(2),
                            curCSV4.getString(5), curCSV4.getString(4),
                            curCSV4.getString(3), curCSV4.getString(6),
                            curCSV4.getString(0), curCSV4.getString(9),
                            curCSV4.getString(7), curCSV4.getString(8),
                            curCSV4.getString(10)};
                    csvWrite.writeNext(arrStr);
                }

                csvWrite.writeNext();

                Cursor curCSV5 = db.rawQuery("SELECT * FROM session WHERE child_Id = ?", new String[]{childID});
                csvWrite.writeNext("Name", "Child_id", "Venue", "Inspector", "Session No", "Session Status", "Date", "Start Time", "End Time", "No of flag", "No of interval ");
                while (curCSV5.moveToNext()) {
                    String arrStr[] = {
                            curCSV5.getString(8), curCSV5.getString(1),
                            curCSV5.getString(0), curCSV5.getString(7),
                            curCSV5.getString(9), curCSV5.getString(10),
                            curCSV5.getString(2), curCSV5.getString(4),
                            curCSV5.getString(3), curCSV5.getString(5),
                            curCSV5.getString(6)};
                    csvWrite.writeNext(arrStr);
                }

                csvWrite.writeNext();

                Cursor curCSV = db.rawQuery("SELECT * FROM grade_child WHERE child_Id = ?", new String[]{childID});
                csvWrite.writeNext("Name", "Child_id", "Qns1", "Qns2", "Qns3", "Qns4", "Qns5");
                while (curCSV.moveToNext()) {
                    String arrStr[] = {
                            curCSV.getString(1), curCSV.getString(0),
                            curCSV.getString(3),
                            curCSV.getString(4), curCSV.getString(5),
                            curCSV.getString(6),curCSV.getString(7)};
                    csvWrite.writeNext(arrStr);
                }

                csvWrite.writeNext();

                csvWrite.close();
                return true;
            } catch (IOException e) {
                Log.e("Grade", e.getMessage(), e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (success) {
                Toast.makeText(childDetails.this, "Export successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(childDetails.this, "Export failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}