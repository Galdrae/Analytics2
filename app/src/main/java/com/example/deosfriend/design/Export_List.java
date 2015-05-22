package com.example.deosfriend.design;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.deosfriend.design.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import au.com.bytecode.opencsv.CSVWriter;
import database.DBAdapter;
import database.IntervalDBAdapter;
import database.NewGradingDB;
import database.SessionDBAdapter;

public class Export_List extends ActionBarActivity {

    private static final String TAG="Export_List";
    private Toolbar toolbar;
    File file=null;
    Button export;
    ImageButton FAB;
    IntervalDBAdapter myIntervalDb;
    SessionDBAdapter mySessionDb;
    DBAdapter myDB;
    NewGradingDB myNewGradingDB;
    int ChildCompleted, ChildUncompleted, ChildFailed, ChildNotObserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export__list);
        myNewGradingDB = new NewGradingDB(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        export = (Button) findViewById(R.id.btn_Export);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        openDB();
        openIntervalDB();
        openSessionDB();

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,(DrawerLayout)findViewById(R.id.drawer_layout), toolbar);

        populateListViewFromDB();
    }

    private void populateListViewFromDB(){
        Cursor cursor = myDB.getAllRows();
        startManagingCursor(cursor);

        String[] fromFieldNames = new String[]
                {DBAdapter.KEY_CHILDNAME, DBAdapter.KEY_INSPECTOR, DBAdapter.KEY_IMAGE, DBAdapter.KEY_STATUS};
        int[] toViewIDs = new int[]
                {R.id.tvName_Db, R.id.tvAge_Db, R.id.lv_Image, R.id.tvStatus};

        SimpleCursorAdapter myCursorAdapter =
                new SimpleCursorAdapter(
                        this,
                        R.layout.activity_export__row,
                        cursor,
                        fromFieldNames, // DB column names
                        toViewIDs       // View IDS to put info in
                );

        // Set adapter for list view
        ListView myList = (ListView) findViewById(R.id.listView_db);
        myList.setAdapter(myCursorAdapter);
    }

    public void onClick_Export (View v){

        ChildCompleted = myDB.CountComplete("Completed");
        ChildUncompleted = myDB.CountIncomplete("Incomplete");
        ChildFailed = myDB.CountFail("Fail");
        ChildNotObserve = myDB.CountNotObserve("Not observed");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Export_List.this);
        alertDialogBuilder.setTitle("Alert");
        alertDialogBuilder.setMessage("Total no. of Not observe: " + ChildNotObserve+ "\n" +
                "Total no. of Incomplete: " + ChildUncompleted + "\n" +
                "Total no. of Complete: " + ChildCompleted+ "\n" +
                "Total no. of Failed: " + ChildFailed +
                "\nData will be backup and clear. Do you want to continue? ");

        alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                exportAll();
            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
                populateListViewFromDB();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void exportAll(){
        ExportDatabaseCSVTask task=new ExportDatabaseCSVTask();
        task.execute();
    }

    private void exportDB() {

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

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "com.example.deosfriend.apptest"
                        + "//databases//" + "newgrading.db";
                String backupDBPath = "backup " + " (D)" + date + " (T)" + time +".db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getApplicationContext(), "Backup Successful!",
                        Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Backup Failed!", Toast.LENGTH_SHORT)
                    .show();

        }
    }

    private class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(Export_List.this);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage("Exporting database...");
            this.dialog.show();

        }
        protected Boolean doInBackground(final String... args){

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

            File dbFile=getDatabasePath("newgrading.db");
            Log.v(TAG, "Db path is: " + dbFile);  //get the path of db

            File exportDir = new File(Environment.getExternalStorageDirectory(), "");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            file = new File(exportDir, "ChildObservation" + " (D)" + date + " (T)" + time +".csv");
            try {

                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                SQLiteDatabase db = myNewGradingDB.getReadableDatabase();

                Cursor curCSV3 = db.rawQuery("SELECT * FROM child ", null);
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

                Cursor curCSV4 = db.rawQuery("SELECT * FROM interval ", null);
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

                Cursor curCSV5 = db.rawQuery("SELECT * FROM session ", null);
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

                Cursor curCSV = db.rawQuery("SELECT * FROM grade_child", null);
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
            }
            catch (IOException e){
                Log.e("ListView_Database", e.getMessage(), e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success)	{

            if (this.dialog.isShowing()){
                this.dialog.dismiss();
            }
            if (success){
                Toast.makeText(Export_List.this, "Export successful!", Toast.LENGTH_SHORT).show();
                exportDB();

                truncateTables();
                myDB.truncateYKChildTables();
                myIntervalDb.truncateYKIntervalTables();;
                mySessionDb.truncateYKSessionTables();


                Intent myIntent = new Intent(Export_List.this, ListView_Database.class);
                Export_List.this.startActivity(myIntent);
            }
            else {
                Toast.makeText(Export_List.this, "Export failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //___________

    private void openDB(){
        myDB = new DBAdapter(this);
        myDB.open();
    }

    private void openIntervalDB(){
        myIntervalDb = new IntervalDBAdapter(this);
        myIntervalDb.open();
    }

    private void openSessionDB(){
        mySessionDb = new SessionDBAdapter(this);
        mySessionDb .open();
    }

    private void truncateTables(){
        SQLiteDatabase db = myNewGradingDB.getReadableDatabase();
        db.execSQL("delete from sqlite_sequence where name='child'" );
        db.execSQL("delete from child;");

        db.execSQL("delete from sqlite_sequence where name='grade_child'" );
        db.execSQL("delete from grade_child;");

        db.execSQL("delete from sqlite_sequence where name='interval'" );
        db.execSQL("delete from interval;");

        db.execSQL("delete from sqlite_sequence where name='session'" );
        db.execSQL("delete from session;");
    }
}
